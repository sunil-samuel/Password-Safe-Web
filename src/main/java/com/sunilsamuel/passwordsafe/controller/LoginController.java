/**
 * LoginController.java (Sep 17, 2014 - 11:30:10 PM)
 *
 * Sunil Samuel CONFIDENTIAL
 *
 *  [2017] Sunil Samuel
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Sunil Samuel. The intellectual and technical
 * concepts contained herein are proprietary to Sunil Samuel
 * and may be covered by U.S. and Foreign Patents, patents in
 * process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written permission
 * is obtained from Sunil Samuel.
 */

package com.sunilsamuel.passwordsafe.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sunilsamuel.passwordsafe.model.Member;
import com.sunilsamuel.passwordsafe.repo.EncryptPassword;
import com.sunilsamuel.passwordsafe.repo.MemberRepository;

@Controller
@RequestMapping(value = "/")
public class LoginController {

	@Autowired
	MemberRepository memberRepo;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(method = RequestMethod.GET)
	public String displayRootPage(Model model, HttpSession session, Locale locale) {
		String language = locale.getLanguage();
		if (language == null) {
			language = "en";
		}
		model.addAttribute("language", language);
		model.addAttribute("url", "/WEB-INF/views/include/subpages/login-form.jsp");
		if (session.getAttribute("loggedinUserInfo") != null) {
			model.addAttribute("url", "/WEB-INF/views/secure/list.jsp");
		}
		return "index";
	}

	@RequestMapping(value = "languageRoller", method = RequestMethod.GET)
	public String languageRoller() {
		/**
		 * every time the language is changed, we need to refresh everything.
		 * TODO: keep the person logged in.
		 */
		return "timedout";
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(@RequestParam("userid") String user, @RequestParam("pwd") String password, HttpSession session,
			Model model) {
		if (session.getAttribute("loggedinUserInfo") != null) {
			return "secure/list";
		}
		List<Member> member = memberRepo.findByEmailIgnoreCaseAndPassword(user, EncryptPassword.encrypt(password));
		if (member == null || member.size() <= 0) {
			model.addAttribute("status", "error");
			model.addAttribute("msg", "Userid and password do not match");
			return "error";
		}
		session.setAttribute("loggedinUserInfo", member.get(0));
		return "secure/list";
	}

	@RequestMapping(value = "secure/logout", method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.invalidate();
		return "timedout";
	}

	@RequestMapping(value = "timedout", method = RequestMethod.GET)
	public String timedout(HttpSession session) {
		session.invalidate();
		return "timedout";
	}

	@RequestMapping(value = "js", method = RequestMethod.GET)
	public String getJavascript() {
		return "include/directive/js";
	}
}
