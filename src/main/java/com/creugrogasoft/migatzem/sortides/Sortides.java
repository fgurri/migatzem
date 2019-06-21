package com.creugrogasoft.migatzem.sortides;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Sortides {

	@Id
	private int id;
	private Date diaHora;
	private int quantitat;
	
	public Sortides() {
		super();
	}
	public Sortides(int id, Date diaHora, int quantitat) {
		super();
		this.id = id;
		this.diaHora = diaHora;
		this.quantitat = quantitat;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDiaHora() {
		return diaHora;
	}
	public void setDiaHora(Date diaHora) {
		this.diaHora = diaHora;
	}
	public int getQuantitat() {
		return quantitat;
	}
	public void setQuantitat(int quantitat) {
		this.quantitat = quantitat;
	}
	
	
}
