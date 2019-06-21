package com.creugrogasoft.migatzem.article;

import javax.validation.constraints.NotEmpty;

public class ArticleFastMovementForm {

	
	//id article
	@NotEmpty
	private String id;
	
	private Integer quantitat;
	
	public ArticleFastMovementForm() {
		super();
	}
	public ArticleFastMovementForm(String id, Integer quantitat) {
		super();
		this.id = id;
		this.quantitat = quantitat;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getQuantitat() {
		return quantitat;
	}
	public void setQuantitat(Integer quantitat) {
		this.quantitat = quantitat;
	}
	
	
}
