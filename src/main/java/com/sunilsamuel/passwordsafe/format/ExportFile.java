package com.sunilsamuel.passwordsafe.format;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.sunilsamuel.passwordsafe.model.Category;
import com.sunilsamuel.passwordsafe.model.Entry;

@SuppressWarnings("unchecked")
public abstract class ExportFile {
	protected Map<String, Object> data = new HashMap<String, Object>();

	protected Map<String, String> entryFields;
	protected Map<String, String> categoryFields;

	public void setFields(Map<String, String> category, Map<String, String> entry) {
		this.categoryFields = category;
		this.entryFields = entry;
	}

	public abstract void process(Map<String, Object> data);

	public abstract void writeTo(ServletOutputStream stream);

	/**
	 * Return the content disposition with the fileName
	 * 
	 * @param fileName
	 * @return
	 */
	public String getContentDisposition(String fileName) {
		return "attachment; filename=\"" + fileName + "\"";
	}

	public abstract String getContentType();

	public abstract void close();

	protected List<Category> getCategories() {
		return (List<Category>) data.get("categories");
	}

	protected List<Entry> getEntries() {
		return (List<Entry>) data.get("entries");
	}

	protected String getValueFromObject(String fieldName, Object obj, Class<?> clazz) {
		try {
			PropertyDescriptor pd;
			pd = new PropertyDescriptor(fieldName, clazz);
			Method getter = pd.getReadMethod();
			Object value = getter.invoke(obj);
			return (value == null ? "" : value.toString());
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

}
