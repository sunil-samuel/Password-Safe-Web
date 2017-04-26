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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sunilsamuel.passwordsafe.model.Category;
import com.sunilsamuel.passwordsafe.model.Entry;

public class ExportPdfFile extends ExportFile {
	ByteArrayOutputStream baos;
	Document document;
	PdfWriter writer;
	PdfPTable table;

	@Override
	public void process(Map<String, Object> data) {
		this.data = data;
		baos = new ByteArrayOutputStream();
		createDocument();
		processCategory();
		processEntry();

		document.close();
	}

	private void processCategory() {
		try {
			document.add(new Paragraph("Categories", getTitleFont()));
			createTable(categoryFields.size());
			/**
			 * Add Headers
			 */
			Font headerFont = getHeaderFont();
			PdfPCell headerCell = getHeaderCell();

			/**
			 * Add the headers first.
			 */
			for (String field : categoryFields.keySet()) {
				headerCell.setPhrase(new Phrase(new Chunk(field, headerFont)));
				table.addCell(headerCell);
			}
			/**
			 * Add the actual data into each row.
			 */
			PdfPCell evenCell = getBodyEvenCell();
			PdfPCell oddCell = getBodyOddCell();
			Font bodyFont = getBodyFont();
			int rowCount = 0;
			for (Category category : getCategories()) {
				PdfPCell cell = (rowCount % 2 == 0 ? evenCell : oddCell);
				for (String key : categoryFields.keySet()) {
					Chunk chunk = new Chunk(getValueFromObject(categoryFields.get(key), category, Category.class),
							bodyFont);
					cell.setPhrase(new Phrase(chunk));
					table.addCell(cell);
					rowCount++;
				}
			}
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	private void processEntry() {
		try {
			document.add(new Paragraph("Entries", getTitleFont()));
			createTable(entryFields.size());
			Font headerFont = getHeaderFont();
			PdfPCell headerCell = getHeaderCell();

			/**
			 * Add the headers first.
			 */
			for (String field : entryFields.keySet()) {
				headerCell.setPhrase(new Phrase(new Chunk(field, headerFont)));
				table.addCell(headerCell);
			}
			PdfPCell evenCell = getBodyEvenCell();
			PdfPCell oddCell = getBodyOddCell();
			Font bodyFont = getBodyFont();
			int rowCount = 0;
			for (Entry entry : getEntries()) {
				PdfPCell cell = (rowCount % 2 == 0 ? evenCell : oddCell);
				for (String key : entryFields.keySet()) {
					Chunk chunk = new Chunk(getValueFromObject(entryFields.get(key), entry, Entry.class), bodyFont);
					cell.setPhrase(new Phrase(chunk));
					table.addCell(cell);
					rowCount++;
				}
			}
			document.add(table);
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void writeTo(ServletOutputStream stream) {
		try {
			baos.writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getContentType() {
		return "application/pdf";
	}

	@Override
	public void close() {
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createDocument() {
		try {
			String password = (String) data.get("password");

			document = new Document(PageSize.LETTER.rotate());
			writer = PdfWriter.getInstance(document, baos);
			writer.setEncryption(password.getBytes(), password.getBytes(), PdfWriter.ALLOW_PRINTING,
					PdfWriter.STANDARD_ENCRYPTION_128);
			writer.createXmpMetadata();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		writer.setViewerPreferences(PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage);
		document.open();
	}

	private Font getTitleFont() {
		Font font = FontFactory.getFont(FontFactory.TIMES);
		font.setSize(12.0f);
		font.setColor(BaseColor.BLACK);
		return font;
	}

	private Font getHeaderFont() {
		Font font = FontFactory.getFont(FontFactory.TIMES);
		font.setSize(10.0f);
		font.setColor(BaseColor.WHITE);
		return font;
	}

	private Font getBodyFont() {
		Font font = FontFactory.getFont(FontFactory.TIMES);
		font.setSize(8.0f);
		font.setColor(BaseColor.BLACK);
		return font;
	}

	private void createTable(int size) {
		table = new PdfPTable(size);
		table.setWidthPercentage(100.0f);
		table.setSpacingBefore(5);
		table.setSpacingAfter(5);
	}

	private PdfPCell getHeaderCell() {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(BaseColor.BLACK);
		cell.setPaddingLeft(2);
		cell.setPaddingRight(2);
		cell.setBorder(2);
		cell.setBorderColor(BaseColor.WHITE);
		return cell;
	}

	private PdfPCell getBodyOddCell() {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(new BaseColor(129, 207, 224));
		cell.setPaddingLeft(5);
		cell.setPaddingRight(5);
		cell.setBorder(2);
		cell.setBorderColor(BaseColor.BLACK);
		return cell;
	}

	private PdfPCell getBodyEvenCell() {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(new BaseColor(197, 239, 247));
		cell.setPadding(5);
		cell.setBorder(2);
		cell.setBorderColor(BaseColor.BLACK);
		return cell;
	}

}
