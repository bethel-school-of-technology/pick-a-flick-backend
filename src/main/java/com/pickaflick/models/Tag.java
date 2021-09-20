package com.pickaflick.models;

import java.io.Serializable;

import javax.persistence.*;

@Entity
public class Tag implements Serializable {
	
	// not sure if we need this:
	// private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tagId;
	private int authorId;
	private String tagName;
	
	public Tag() {
		
	}
	
	public Tag(Long tagId, int authorId, String tagName) {
		
		this.tagId = tagId;
		this.authorId = authorId;
		this.tagName = tagName;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public int getAuthorId() {
		return authorId;
	}

	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	@Override
	public String toString() {
		return "Tag [tagId=" + tagId + ", authorId=" + authorId + ", tagName=" + tagName + "]";
	}

}
