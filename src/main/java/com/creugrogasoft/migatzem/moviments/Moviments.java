package com.creugrogasoft.migatzem.moviments;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.creugrogasoft.migatzem.article.Article;

@Entity
public class Moviments {

	@Id
	@Column(name="id",updatable=false,nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "diaHora")
	private LocalDateTime diaHora;
	@Column(name = "quantitat")
	private int quantitat;
	@ManyToOne
	private Article article;
	@Column(name = "usuari")
	private String usuari;
	
	public Moviments() {
		super();
	}
	public Moviments(LocalDateTime diaHora, int quantitat, Article article, String usuari) {
		super();
		this.diaHora = diaHora;
		this.quantitat = quantitat;
		this.article = article;
		this.usuari = usuari;
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDateTime getDiaHora() {
		return diaHora;
	}
	public void setDiaHora(LocalDateTime diaHora) {
		this.diaHora = diaHora;
	}
	public int getQuantitat() {
		return quantitat;
	}
	public void setQuantitat(int quantitat) {
		this.quantitat = quantitat;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public String getUsuari() {
		return usuari;
	}
	public void setUsuari(String usuari) {
		this.usuari = usuari;
	}
	
	
}
