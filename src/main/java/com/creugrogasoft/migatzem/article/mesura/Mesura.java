package com.creugrogasoft.migatzem.article.mesura;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Mesura {

	@Id
	private String id;
	
	public Mesura() {
		super();
	}
	public Mesura(String id) {
		super();
		this.id = id;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
