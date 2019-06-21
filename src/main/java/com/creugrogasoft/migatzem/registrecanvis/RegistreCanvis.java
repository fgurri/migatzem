package com.creugrogasoft.migatzem.registrecanvis;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.creugrogasoft.migatzem.article.Article;

@Entity
public class RegistreCanvis {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int id;
	@ManyToOne
	private Article article;
	private LocalDateTime diaHora;
	private String camp;
	private String valorAnterior;
	private String valorPosterior;
	private String usuari;
	
	public RegistreCanvis() {
		super();
	}

	public RegistreCanvis(int id, Article article, LocalDateTime diaHora, String camp, String valorAnterior, String valorPosterior, String usuari) {
		super();
		this.id = id;
		this.diaHora = diaHora;
		this.camp = camp;
		this.valorAnterior = valorAnterior;
		this.valorPosterior = valorPosterior;
		this.article = article;
		this.usuari = usuari;
	}
	
	public RegistreCanvis(Article article, LocalDateTime diaHora, String camp, String valorAnterior, String valorPosterior, String usuari) {
		super();
		this.diaHora = diaHora;
		this.camp = camp;
		this.valorAnterior = valorAnterior;
		this.valorPosterior = valorPosterior;
		this.article = article;
		this.usuari = usuari;
	}
	
	public int getId() {
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
	public String getCamp() {
		return camp;
	}
	public void setCamp(String camp) {
		this.camp = camp;
	}
	public String getValorAnterior() {
		return valorAnterior;
	}
	public void setValorAnterior(String valorAnterior) {
		this.valorAnterior = valorAnterior;
	}
	public String getValorPosterior() {
		return valorPosterior;
	}
	public void setValorPosterior(String valorPosterior) {
		this.valorPosterior = valorPosterior;
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
