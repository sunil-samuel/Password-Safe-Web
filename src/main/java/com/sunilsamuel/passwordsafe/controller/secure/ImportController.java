package com.sunilsamuel.passwordsafe.controller.secure;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.sunilsamuel.passwordsafe.format.ExportPasswordSafeFile;
import com.sunilsamuel.passwordsafe.model.Category;
import com.sunilsamuel.passwordsafe.model.Entry;
import com.sunilsamuel.passwordsafe.repo.CategoryRepository;
import com.sunilsamuel.passwordsafe.repo.EntryRepository;

@SuppressWarnings("unchecked")
@Controller
@RequestMapping(value = "/secure/import/")
public class ImportController {
	@Autowired
	private EntryRepository entryRepo;
	@Autowired
	private CategoryRepository catRepo;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String getFormForUpload(Model model) {
		return "include/subpages/upload";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(@RequestParam(value = "password") String password, MultipartHttpServletRequest request,
			HttpServletResponse response, Model model) {

		try {
			MultipartFile mFile = request.getFile(request.getFileNames().next());
			Map<String, Object> data = new ExportPasswordSafeFile().readFrom(mFile.getInputStream(), password);

			if (data == null) {
				model.addAttribute("msg", "Could not read file, please validate the password");
				return "error";
			}
			List<Category> categories = (List<Category>) data.get("categories");
			System.out.println("CATEGORIES [" + categories + "]\n\n");
			// catRepo.save(categories);
			Map<Long, Long> categoriesMapping = saveCategories(categories);

			List<Entry> entries = (List<Entry>) data.get("entries");
			System.out.println("ENTRIES [" + entries + "]\n\n");
			saveEntries(entries, categoriesMapping);
			// entryRepo.save(entries);
			model.addAttribute("msg", "Successfully imported the Password Safe Data");
			return "success";
		} catch (IOException e) {
			e.printStackTrace();
			model.addAttribute("msg", "Could not read file, please validate the password");
			return "error";
		}
	}

	/**
	 * When importing the existing set of categories, the id's cannot be reused.
	 * Instead, we must use the id that is auto generated. We also must ensure
	 * that the parentId for the category is also updated with the new id.
	 * 
	 * @param categories
	 *            List of categories
	 */
	private Map<Long, Long> saveCategories(List<Category> categories) {
		Map<Long, Long> ids = new HashMap<Long, Long>();
		for (Category category : categories) {
			Long oldParentId = category.getParentId();
			Long newParentId = null;
			if (oldParentId != null) {
				newParentId = ids.get(oldParentId);
				if (newParentId == null) {
					logger.error("Old parent id {} was not found", oldParentId);
				}
			}
			category.setParentId(newParentId);
			Category newCategory = catRepo.save(category);
			logger.info("Saved category with old id {} title {} new id {}", category.getId(), category.getTitle(),
					newCategory.getId());
			ids.put(category.getId(), newCategory.getId());
		}
		return ids;
	}

	private void saveEntries(List<Entry> entries, Map<Long, Long> mapping) {
		for (Entry entry : entries) {
			Long catId = entry.getParentCategoryId();
			Long newCatId = null;
			if (catId != null) {
				newCatId = mapping.get(catId);
				if (newCatId == null) {
					logger.error("Could not find new cateogry id for old category id {}", catId);
				}
			}
			entry.setParentCategoryId(newCatId);
			entryRepo.save(entry);
			logger.info("Saved entry with title {}", entry.getTitle());
		}
	}
}
