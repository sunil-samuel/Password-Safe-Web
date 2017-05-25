/**
 * EntryController.java (Sep 17, 2014 - 11:30:10 PM)
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

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sunilsamuel.passwordsafe.model.Category;
import com.sunilsamuel.passwordsafe.model.Entry;
import com.sunilsamuel.passwordsafe.repo.CategoryRepository;
import com.sunilsamuel.passwordsafe.repo.EntryRepository;

/**
 * This is the controller for managing the entry for a category.
 * 
 * @author Sunil Samuel (sgs@sunilsamuel.com)
 *
 */
@Controller
@RequestMapping(value = "/secure/entry/")
public class EntryController {

	@Autowired
	private EntryRepository entryRepo;
	@Autowired
	private CategoryRepository catRepo;

	/**
	 * Create a default entry if this is the first time the user is running this
	 * application. This table can be updated by the user.
	 */
	@PostConstruct
	public void initializeTable() {
		if (entryRepo.count() == 0) {
			Entry entry = new Entry();
			entry.setDescription("My first entry");
			entry.setNotes("This is an entry that is created by default and should be updated");
			entry.setParentCategoryId(1L);
			entry.setTitle("My first entry");
			entryRepo.save(entry);
		}
	}

	/**
	 * Return the form for creating new entry.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "new-entry")
	public String getEntryForm(@RequestParam(name = "catId") Long catId, Model model) {
		Category category = catRepo.findOne(catId);
		Entry entry = new Entry();
		entry.setParentCategoryId(category.getId());
		model.addAttribute("entry", entry);
		model.addAttribute("category", category);

		return "include/subpages/entry-form";
	}

	@RequestMapping(method = RequestMethod.POST, value = "new-entry")
	public String addNewEntry(@ModelAttribute("entry") Entry entry, Model model) {
		entry.setRootElement(true);
		if (entry.getId() == null) {
			entry.setCreated(new Date());
			entry.setUpdated(new Date());
		} else {
			entry.setUpdated(new Date());
		}
		Entry upEntry = entryRepo.save(entry);
		model.addAttribute("msg", "Successfully added entry with title '" + upEntry.getTitle() + "'");
		return "success";
	}

	@RequestMapping(method = RequestMethod.GET, value = "get-entry-for-edit")
	public String getEntryForEdit(@RequestParam(name = "id") Long id, Model model) {
		Entry entry = entryRepo.findOne(id);
		model.addAttribute("legend", "Edit Entry");
		model.addAttribute("entry", entry);

		return "include/subpages/entry-form";
	}

	@RequestMapping(method = RequestMethod.GET, value = "get-entry-for-view")
	public String getEntryForView(@RequestParam(name = "id") Long id, Model model) {
		Entry entry = entryRepo.findOne(id);
		model.addAttribute("legend", "View Entry");
		model.addAttribute("entry", entry);

		return "include/subpages/entry-view";
	}

	@RequestMapping(method = RequestMethod.GET, value = "get-all-entries")
	public String getEntriesForCategory(@RequestParam(name = "id") Long catId, Model model) {
		List<Entry> entries = entryRepo.findByParentCategoryIdOrderByTitleAsc(catId);
		model.addAttribute("entries", entries);
		return "include/subpages/all-entry";
	}

	@RequestMapping(method = RequestMethod.GET, value = "search-entries")
	public String searchEntries(@RequestParam(name = "search") String search, Model model) {
		// search = "%" + search + "%";
		List<Entry> entries = entryRepo
				.findByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContainingOrNotesIgnoreCaseContainingOrderByTitleAsc(
						search, search, search);
		model.addAttribute("entries", entries);
		model.addAttribute("search", search);
		return "include/subpages/all-entry";
	}

	@RequestMapping(method = RequestMethod.POST, value = "remove-entry")
	public String removeEntryById(@RequestParam(name = "id") Long id, Model model) {
		Entry entry = entryRepo.findOne(id);
		entryRepo.delete(id);
		model.addAttribute("msg", "Removed the entry with title '" + entry.getTitle() + "'");
		return "success";
	}

}
