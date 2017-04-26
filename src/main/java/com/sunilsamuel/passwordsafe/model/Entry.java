/**
 * Entry.java (Sep 17, 2014 - 11:30:10 PM)
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

package com.sunilsamuel.passwordsafe.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "Entry")
public class Entry implements Serializable {

	private static final long serialVersionUID = 5395228167310817453L;
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * This id is used to save history. Every time this record is updated, we
	 * create a new record. We use the generated id of the original record to
	 * keep track of all of the updated record.
	 */
	private Long rootSelfId;
	/**
	 * This variable is true if this is the most recent record for all of the
	 * updates of this current record. Every time this record is updated, a copy
	 * is made, the copy is then made the root element and all others are not
	 * root. Root element means that this is the record that is seen.
	 */
	private Boolean rootElement;

	@Column(nullable = false)
	private Long parentCategoryId;

	@Column(length = 1024)
	private String title;

	@Column(length = 1024)
	private String description;
	private String username;
	private String password;

	@Column(length = 2048)
	private String url;

	@Column(length = 2048)
	private String notes;

	private Date created;
	private Date updated;
	private Date expires;

	@PrePersist
	void createdAt() {
		this.created = this.updated = new Date();
	}

	@PreUpdate
	void updatedAt() {
		this.updated = new Date();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the rootSelfId
	 */
	public Long getRootSelfId() {
		return rootSelfId;
	}

	/**
	 * @param rootSelfId
	 *            the rootSelfId to set
	 */
	public void setRootSelfId(Long rootSelfId) {
		this.rootSelfId = rootSelfId;
	}

	/**
	 * @return the rootElement
	 */
	public Boolean getRootElement() {
		return rootElement;
	}

	/**
	 * @param rootElement
	 *            the rootElement to set
	 */
	public void setRootElement(Boolean rootElement) {
		this.rootElement = rootElement;
	}

	/**
	 * @return the parentCategoryId
	 */
	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	/**
	 * @param parentCategoryId
	 *            the parentCategoryId to set
	 */
	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes
	 *            the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated
	 *            the updated to set
	 */
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * @return the expires
	 */
	public Date getExpires() {
		return expires;
	}

	/**
	 * @param expires
	 *            the expires to set
	 */
	public void setExpires(Date expires) {
		this.expires = expires;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((expires == null) ? 0 : expires.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((parentCategoryId == null) ? 0 : parentCategoryId.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((rootElement == null) ? 0 : rootElement.hashCode());
		result = prime * result + ((rootSelfId == null) ? 0 : rootSelfId.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((updated == null) ? 0 : updated.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entry other = (Entry) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (expires == null) {
			if (other.expires != null)
				return false;
		} else if (!expires.equals(other.expires))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (parentCategoryId == null) {
			if (other.parentCategoryId != null)
				return false;
		} else if (!parentCategoryId.equals(other.parentCategoryId))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (rootElement == null) {
			if (other.rootElement != null)
				return false;
		} else if (!rootElement.equals(other.rootElement))
			return false;
		if (rootSelfId == null) {
			if (other.rootSelfId != null)
				return false;
		} else if (!rootSelfId.equals(other.rootSelfId))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (updated == null) {
			if (other.updated != null)
				return false;
		} else if (!updated.equals(other.updated))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Entry [id=").append(id).append(", rootSelfId=").append(rootSelfId).append(", rootElement=")
				.append(rootElement).append(", parentCategoryId=").append(parentCategoryId).append(", title=")
				.append(title).append(", description=").append(description).append(", username=").append(username)
				.append(", password=").append(password).append(", url=").append(url).append(", notes=").append(notes)
				.append(", created=").append(created).append(", updated=").append(updated).append(", expires=")
				.append(expires).append("]");
		return builder.toString();
	}
}
