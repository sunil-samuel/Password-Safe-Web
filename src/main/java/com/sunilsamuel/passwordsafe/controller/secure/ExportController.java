/**
 * ImportExportController.java (Sep 17, 2014 - 11:30:10 PM)
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
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sunilsamuel.passwordsafe.format.ExportFile;
import com.sunilsamuel.passwordsafe.format.ExportPasswordSafeFile;
import com.sunilsamuel.passwordsafe.format.ExportPdfFile;
import com.sunilsamuel.passwordsafe.format.ExportXlsxFile;
import com.sunilsamuel.passwordsafe.repo.CategoryRepository;
import com.sunilsamuel.passwordsafe.repo.EntryRepository;

@Controller
@RequestMapping(value = "/secure/export/")
public class ExportController {
	@Autowired
	private EntryRepository entryRepo;
	@Autowired
	private CategoryRepository catRepo;
	/**
	 * Value = actual name in the getter, so it will be get<Key>. Key = what is
	 * printed out.
	 */
	private final Map<String, String> entryFields = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("Id", "id");
			put("Root Self Id", "rootSelfId");
			put("Root Element", "rootElement");
			put("Parent Category Id", "parentCategoryId");
			put("Root Element", "rootElement");
			put("Title", "title");
			put("Description", "description");
			put("Username", "username");
			put("Password", "password");
			put("URL", "url");
			put("Username", "username");
			put("Notes", "notes");
			put("Created On", "created");
			put("Updated On", "updated");
			put("Username", "username");
			put("Expires", "expires");
		}
	};
	private final Map<String, String> categoryFields = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("Id", "id");
			put("Parent Id", "ParentId");
			put("Title", "title");
			put("Description", "description");
			put("Notes", "notes");
		}
	};

	@RequestMapping(method = RequestMethod.GET, value = "exportData")
	public String exportDataForm(Model model) {
		return "include/subpages/export";
	}

	@RequestMapping(method = RequestMethod.GET, value = "exportDataToFile")
	public void exportData(@RequestParam(name = "format") String format,
			@RequestParam(name = "password") String password, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		ExportFile exportFile = null;

		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String fileName = "Password-Safe-" + now.format(formatter);
		if ("xlsx".equals(format)) {
			exportFile = new ExportXlsxFile();
			response.setHeader("Content-Disposition", exportFile.getContentDisposition(fileName + ".xlsx"));
		} else if ("pdf".equals(format)) {
			exportFile = new ExportPdfFile();
			response.setHeader("Content-Disposition", exportFile.getContentDisposition(fileName + ".pdf"));
		} else if ("safe".equals(format)) {
			exportFile = new ExportPasswordSafeFile();
			response.setHeader("Content-Disposition", exportFile.getContentDisposition(fileName + ".safe"));
		}
		exportFile.setFields(categoryFields, entryFields);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("categories", catRepo.findAll());
		data.put("entries", entryRepo.findAll());
		data.put("password", password);
		exportFile.process(data);
		response.setContentType(exportFile.getContentType());
		exportFile.writeTo(response.getOutputStream());
		response.getOutputStream().flush();
		response.getOutputStream().close();
		exportFile.close();
	}
}
