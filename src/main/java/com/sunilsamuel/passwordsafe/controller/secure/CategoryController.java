/**
 * CategoryController.java (Sep 17, 2014 - 11:30:10 PM)
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

package com.sunilsamuel.passwordsafe.controller.secure;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sunilsamuel.passwordsafe.model.Category;
import com.sunilsamuel.passwordsafe.repo.CategoryRepository;
import com.sunilsamuel.passwordsafe.repo.EntryRepository;

@Controller
@RequestMapping(value = "/secure/category/")
public class CategoryController {
	@Autowired
	private CategoryRepository catRepo;

	@Autowired
	private EntryRepository entryRepo;

	/**
	 * Create a default category if this is the first time the user is running
	 * this application. This table can be updated by the user.
	 */
	@PostConstruct
	public void initializeTable() {
		if (catRepo.count() == 0) {
			Category cat = new Category();
			cat.setDescription("My first category");
			cat.setTitle("First Category");
			cat.setId(1L);
			cat.setNotes("This is a category that is created by default and should be updated");
			catRepo.save(cat);
		}
	}

	/**
	 * Get the forms for creating a new category
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "new-category")
	public String addCategoryForm(@RequestParam(required = false, name = "parentId") Long parentId, Model model) {
		Category category = new Category();
		Category parent = null;

		/**
		 * Check if this new category should belong to a parent category. That
		 * is, this category is a 'sub-category'.
		 */
		if (parentId != null) {
			parent = catRepo.findOne(parentId);
			if (parent != null) {
				category.setParentId(parent.getId());
			}
		}
		model.addAttribute("parent", parent);

		model.addAttribute("category", category);
		return "include/subpages/category-form";
	}

	/**
	 * Create the new category given the user input.
	 * 
	 * @param category
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, value = "new-category")
	public String addCategory(@ModelAttribute("category") Category category, Model model) {
		category.setTitle(category.getTitle().trim());
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		// We do not want duplicate names in the same parent, but could have
		// duplicate names in other categories.
		List<Category> categories = catRepo.findByTitleAndParentId(category.getTitle(), category.getParentId());
		if (categories != null && categories.size() > 0) {
			model.addAttribute("msg",
					"Category with title '" + category.getTitle() + "' already exists within the parent category.  "
							+ "Please use another title or select edit link.");
			return "error";
		}
		Category upCat = catRepo.save(category);
		model.addAttribute("success", true);
		model.addAttribute("data", createJson(upCat, false));
		model.addAttribute("msg", "Successfully added category with title '" + upCat.getTitle() + "'");

		return "success";
	}

	/**
	 * This method is used to get all of the children for a given category with
	 * id. If the id is null, then we are getting the first level for the root.
	 * <br>
	 * If 'use' is set, then instead of giving the children of the category with
	 * the given id, give itself.
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "get-all-level")
	public String getLevelCategories(@RequestParam(required = false, name = "id") Long id,
			@RequestParam(required = false, name = "use") String use, Model model) {

		List<Category> categories;
		if (use != null && id != null) {
			Category category = catRepo.findOne(id);
			categories = new ArrayList<Category>();
			categories.add(category);
			model.addAttribute("level", (category.getParentId() == null ? "root" : "sub"));
		} else {
			Sort sortOrder = new Sort(new Order(Direction.ASC, "title").ignoreCase());

			categories = catRepo.findByParentId(id, sortOrder);
			model.addAttribute("level", (id == null ? "root" : "sub"));
		}
		model.addAttribute("categories", categories);
		return "include/subpages/all-category";
	}

	@RequestMapping(method = RequestMethod.GET, value = "get-by-id")
	public String getCategoryById(@RequestParam(name = "id") Long id,
			@RequestParam(name = "type", required = false) String type, Model model) {
		model.addAttribute("category", catRepo.findOne(id));
		model.addAttribute("update", true);
		if ("view".equalsIgnoreCase(type)) {
			return "include/subpages/category-view";
		}
		return "include/subpages/category-form";
	}

	@RequestMapping(method = RequestMethod.POST, value = "update-category")
	public String updateCategory(@ModelAttribute("category") Category category, Model model) {
		category.setTitle(category.getTitle().trim());
		category.setUpdated(new Date());
		Category upCat = catRepo.save(category);
		model.addAttribute("success", true);
		model.addAttribute("data", createJson(upCat, true));
		model.addAttribute("msg", "Successfully updated category with title '" + upCat.getTitle() + "'");

		return "success";
	}

	@RequestMapping(method = RequestMethod.GET, value = "child-exists", produces = { MediaType.APPLICATION_JSON_VALUE })
	public String checkChildExistsForParent(@RequestParam(name = "id") Long id, Model model) {
		Long count = catRepo.countByParentId(id);
		if (count > 0) {
			model.addAttribute("msg", count + (count > 1 ? " sub-folders exist." : " sub-folder exists.")
					+ "  Please remove all of the sub folders, prior to deleting this node.");
			return "error";
		}
		count = entryRepo.countByParentCategoryId(id);
		if (count > 0) {
			model.addAttribute("msg", "There are entries for this category.  Please remove them first");
			return "error";
		}
		catRepo.delete(id);
		model.addAttribute("msg", "Category deleted successfully.");
		return "success";
	}

	private String createJson(Category category, boolean editing) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"title\":\"").append(category.getTitle()).append("\",").append("\"id\":\"")
				.append(category.getId() != null ? category.getId() : "").append("\",").append("\"parentId\":\"")
				.append(category.getParentId() != null ? category.getParentId() : "").append("\"");
		if (editing) {
			json.append(",\"editing\":\"true\"");
		}
		json.append("}");
		return json.toString();
	}
}
