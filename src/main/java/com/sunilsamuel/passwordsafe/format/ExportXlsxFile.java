/**
 * ExportXlsxFile.java (Sep 17, 2014 - 11:30:10 PM)
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

package com.sunilsamuel.passwordsafe.format;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.crypt.HashAlgorithm;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.sunilsamuel.passwordsafe.model.Category;
import com.sunilsamuel.passwordsafe.model.Entry;

/**
 * Create an xlsx file given a list of Category and Entries. This class will
 * create two sheets within the same Excel worksheet and provide a mechanism for
 * writing the files to a stream so that it can be sent back to the caller.
 * 
 * @author Sunil Samuel (sgs@sunilsamuel.com)
 *
 */
@SuppressWarnings("unchecked")
public class ExportXlsxFile extends ExportFile {
	private Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
	private XSSFWorkbook workbook;

	public ExportXlsxFile() {
		super();
	}

	/**
	 * Return the xlsx content type.
	 * 
	 * @return
	 */
	@Override
	public String getContentType() {
		return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	}

	@Override
	public void process(Map<String, Object> data) {
		workbook = new XSSFWorkbook();
		workbook.setWorkbookPassword((String) data.get("password"), HashAlgorithm.sha256);
		this.data = data;
		createHeaderStyle();
		createDataStyles();
		processCategories();
		processEntries();
		autoSizeColumns(workbook);
	}

	@Override
	public void writeTo(ServletOutputStream stream) {
		try {
			workbook.write(stream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void close() {
		try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processCategories() {
		int startRowNumber = 0;
		Sheet sheet = workbook.createSheet("Categories");
		Row header = sheet.createRow(startRowNumber++);
		int cellNumber = 0;
		for (String key : categoryFields.keySet()) {
			header.createCell(cellNumber++).setCellValue(key);
		}
		header.setRowStyle(styles.get("header"));

		for (Category category : (List<Category>) data.get("categories")) {
			Row categoryRow = sheet.createRow(startRowNumber++);
			cellNumber = 0;
			for (String key : categoryFields.keySet()) {
				categoryRow.createCell(cellNumber++)
						.setCellValue(getValueFromObject(categoryFields.get(key), category, Category.class));
			}
			categoryRow.setRowStyle((startRowNumber % 2 == 0 ? styles.get("even") : styles.get("odd")));
		}
	}

	private void processEntries() {
		int startRowNumber = 0;
		Sheet sheet = workbook.createSheet("Entries");
		Row header = sheet.createRow(startRowNumber++);
		int cellNumber = 0;
		for (String key : entryFields.keySet()) {
			header.createCell(cellNumber++).setCellValue(key);
		}
		header.setRowStyle(styles.get("header"));

		for (Entry entry : (List<Entry>) data.get("entries")) {
			Row entryRow = sheet.createRow(startRowNumber++);
			cellNumber = 0;
			for (String key : entryFields.keySet()) {
				entryRow.createCell(cellNumber++)
						.setCellValue(getValueFromObject(entryFields.get(key), entry, Entry.class));
			}
			entryRow.setRowStyle((startRowNumber % 2 == 0 ? styles.get("even") : styles.get("odd")));
		}
	}

	private void autoSizeColumns(Workbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				int startRow = sheet.getFirstRowNum();
				int endRow = sheet.getLastRowNum();
				for (int rowNum = startRow; rowNum < endRow; rowNum++) {
					Row row = sheet.getRow(rowNum);
					int startCell = row.getFirstCellNum();
					int endCell = row.getLastCellNum();
					for (int cellNum = startCell; cellNum < endCell; cellNum++) {
						sheet.autoSizeColumn(cellNum);
					}
				}
			}
		}
	}

	private CellStyle createHeaderStyle() {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontName("Calibri");
		font.setFontHeightInPoints((short) 12);
		font.setColor(HSSFColor.WHITE.index);

		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		style.setFont(font);
		style.setFillPattern(FillPatternType.DIAMONDS);
		style.setFillBackgroundColor(HSSFColor.BLACK.index);

		styles.put("header", style);

		return style;
	}

	private void createDataStyles() {
		for (String type : new String[] { "even", "odd" }) {
			short color;
			XSSFColor background;
			FillPatternType pattern;
			String name;
			if ("even".equals(type)) {
				color = HSSFColor.BLUE_GREY.index;
				background = new XSSFColor(new byte[] { (byte) 50, (byte) 50, (byte) 50 });
				pattern = FillPatternType.DIAMONDS;
				name = "even";
			} else {
				color = HSSFColor.BLUE.index;
				background = new XSSFColor(new byte[] { (byte) 40, (byte) 40, (byte) 40 });
				pattern = FillPatternType.DIAMONDS;
				name = "odd";
			}
			XSSFFont font = workbook.createFont();
			font.setBold(false);
			font.setFontName("Calibri");
			font.setFontHeightInPoints((short) 12);
			font.setColor(color);

			XSSFCellStyle style = workbook.createCellStyle();
			style.setFillBackgroundColor(background);
			style.setFillPattern(pattern);

			style.setWrapText(true);
			style.setBorderLeft(BorderStyle.MEDIUM);
			style.setBorderRight(BorderStyle.MEDIUM);
			style.setBorderTop(BorderStyle.MEDIUM);
			style.setBorderBottom(BorderStyle.MEDIUM);
			style.setFont(font);
			styles.put(name, style);
		}
	}
}
