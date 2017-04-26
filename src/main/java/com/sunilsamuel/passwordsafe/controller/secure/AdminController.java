package com.sunilsamuel.passwordsafe.controller.secure;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sunilsamuel.passwordsafe.model.Member;
import com.sunilsamuel.passwordsafe.model.Role;
import com.sunilsamuel.passwordsafe.repo.EncryptPassword;
import com.sunilsamuel.passwordsafe.repo.MemberRepository;
import com.sunilsamuel.passwordsafe.validator.MemberValidator;

@Controller
@RequestMapping(value = "/secure/admin/")
public class AdminController {
	@Autowired
	MemberRepository memberRepo;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@PostConstruct
	public void createFirstAdminUser() {
		// memberRepo.deleteAll();
		List<Member> members = memberRepo.findByRole(Role.Admin);
		if (members.size() <= 0) {
			Member member = new Member();
			member.setFirstName("First Admin");
			member.setLastName("User");
			member.setEmail("admin@passwordsafeweb.com");
			member.setPassword(EncryptPassword.encrypt("mypassword"));
			member.setPhoneNumber("2027099320");
			member.setRole(Role.Admin);

			memberRepo.save(member);
			logger.info("Inserted first user given empty database");
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/priv/new-user")
	public String getNewUser(HttpSession session, Model model) {
		model.addAttribute("member", new Member());
		model.addAttribute("roles", Role.values());
		return "include/subpages/my-account";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/priv/new-user")
	public String postNewUser(@ModelAttribute("member") Member member, BindingResult bindingResult, Model model) {
		MemberValidator val = new MemberValidator();
		val.validateFull(member, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("msg", "Errors with input");
			return "error";
		}
		member.setPassword(EncryptPassword.encrypt(member.getPassword()));
		try {
			memberRepo.save(member);
			model.addAttribute("success", true);
			model.addAttribute("msg", "Successfully added new user");
			return "success";

		} catch (DataIntegrityViolationException e) {
			model.addAttribute("success", false);
			model.addAttribute("msg", "There is an error in the data.  Possibly duplicate email.");
			return "error";
		} catch (Exception e) {
			model.addAttribute("success", false);
			model.addAttribute("msg", "There are errors with updating this user");
			return "error";

		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/priv/users-list")
	public String getUsersList(Model model) {
		model.addAttribute("members", memberRepo.findAllByOrderByLastNameAsc());
		return "include/subpages/edit-users";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/priv/edit-user")
	public String getEditUser(@RequestParam("id") Long id, @RequestParam("email") String email, Model model) {

		model.addAttribute("member", memberRepo.findOneByIdAndEmail(id, email));
		model.addAttribute("roles", Role.values());
		return "include/subpages/my-account";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/priv/edit-user")
	public String postEditUser(@ModelAttribute("member") Member member, BindingResult bindingResult, Model model) {

		MemberValidator val = new MemberValidator();
		val.validate(member, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("msg", "Errors with input");
			return "error";
		}
		Member origMember = memberRepo.getOne(member.getId());
		if (member.getPassword() != null && member.getPassword().length() > 0) {
			origMember.setPassword(EncryptPassword.encrypt(member.getPassword()));
		}
		origMember.setEmail(member.getEmail());
		origMember.setFirstName(member.getFirstName());
		origMember.setLastName(member.getLastName());
		origMember.setPhoneNumber(member.getPhoneNumber());
		origMember.setRole(member.getRole());

		try {
			memberRepo.save(origMember);
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("success", false);
			model.addAttribute("msg", "There is an error in the data.  Possibly duplicate email.");
			return "error";
		}
		model.addAttribute("success", true);
		model.addAttribute("msg", "Successfully updated your information");
		return "success";
	}

	/**
	 * This deletes the user given the id. Prior to deleting this user, make
	 * sure that this is not the last 'admin' user. If it is, then we cannot
	 * allow them to delete this user. We will provide an appropriate error.
	 * 
	 * @param id
	 * @param email
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/priv/delete-user")
	public String postDeleteUser(@RequestParam("id") Long id, @RequestParam("email") String email, Model model) {

		Member member = memberRepo.findOne(id);
		/**
		 * If this is an admin, then check to see if there are other admin
		 * users.
		 */
		if (member.getRole() == Role.Admin) {
			Long adminUsers = memberRepo.countByRole(Role.Admin);
			if (adminUsers <= 1) {
				model.addAttribute("success", false);
				model.addAttribute("msg",
						"This user is the last 'admin'.  Please create another one before removing this user.");
				return "error";
			}
		}
		memberRepo.delete(id);
		model.addAttribute("success", true);
		model.addAttribute("msg", "Successfully deleted user [" + email + "]");
		return "success";
	}

	@RequestMapping(method = RequestMethod.GET, value = "my-account")
	public String getMyAccount(HttpSession session, Model model) {
		Member thisUser = (Member) session.getAttribute("loggedinUserInfo");
		if (thisUser == null) {
			logger.warn("User was null from session when getting 'my account'");
			return "index";
		}
		model.addAttribute("member", thisUser);
		/**
		 * If the user does not have 'Admin' role, then just provide the current
		 * role for this user.
		 */
		if (thisUser.getRole() == Role.Admin) {
			model.addAttribute("roles", Role.values());
		} else {
			Role[] role = new Role[1];
			role[0] = thisUser.getRole();
			model.addAttribute("roles", role);
		}
		return "include/subpages/my-account";
	}

	@RequestMapping(method = RequestMethod.POST, value = "my-account")
	public String postMyAccount(@ModelAttribute("member") Member member, HttpSession session,
			BindingResult bindingResult, Model model) {
		Validator val = new MemberValidator();
		val.validate(member, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("msg", "Errors with input");
			return "error";
		}

		Member thisUser = (Member) session.getAttribute("loggedinUserInfo");
		if (thisUser == null) {
			logger.warn("User was null from session when posting 'my account'");
			return "index";
		}
		/**
		 * If the user is not 'Admin', then we will keep the original role.
		 */
		if (thisUser.getRole() != Role.Admin) {
			member.setRole(thisUser.getRole());
		}
		member.setId(thisUser.getId());
		/**
		 * If the new password is empty, then use the old other. Otherwise, use
		 * the new password but first encrypt it.
		 */
		if (member.getPassword() == null || member.getPassword().trim().length() == 0) {
			member.setPassword(thisUser.getPassword());
		} else {
			member.setPassword(EncryptPassword.encrypt(member.getPassword()));
		}
		try {
			memberRepo.save(member);
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("success", false);
			model.addAttribute("msg", "There is an error in the data.  Possibly duplicate email.");
			return "error";
		}
		session.setAttribute("loggedinUserInfo", member);
		model.addAttribute("success", true);
		model.addAttribute("msg", "Successfully updated your information");
		return "success";
	}

}
