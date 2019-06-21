package com.creugrogasoft.migatzem.article;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.Persistable;

import com.creugrogasoft.migatzem.article.mesura.Mesura;
import com.creugrogasoft.migatzem.moviments.Moviments;
import com.creugrogasoft.migatzem.registrecanvis.RegistreCanvis;


@Entity
public class Article {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	
	@NotEmpty
	@Column(name = "reference")
	private String reference;
	
	@NotEmpty
	@Column(name = "serialNumber")
	private String serialNumber;
	
	@NotEmpty
	@Column(name = "nom")
	private String nom;
	
	@Min(0)
	@Column(name = "stockactual")
	private int stockactual;
	
	@Min(0)
	@Column(name = "stockminim")
	private int stockminim;
	
	@OneToMany(mappedBy="article")
	@OrderBy("diaHora DESC")
	private Set<Moviments> entrades;
	
	@OneToMany(mappedBy="article")
	@OrderBy("diaHora DESC")
	private Set<RegistreCanvis> registreCanvis;
	
	@OneToOne
	private Mesura mesura;
	
	@Transient
	private List<ArticleMovementsCount> entradesByUse;
	
	
	public Article() {
		super();
		//System.out.println("Creating instance of Article " + this.id + " . Now I am new? " + !this.persisted);
	}

	public Article(Integer id, String reference, String serialNumber, String nom, int stockactual, int stockminim) {
		super();
		this.id = id;
		this.reference = reference;
		this.serialNumber = serialNumber;
		this.nom = nom;
		this.stockactual = stockactual;
		this.stockminim = stockminim;
		//System.out.println("Creating instance of Article " + this.id + " . Now I am new? " + !this.persisted);
	}
	
	public Integer getId() {
		return this.id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getStockactual() {
		return stockactual;
	}
	public void setStockactual(int stockactual) {
		this.stockactual = stockactual;
	}
	public int getStockminim() {
		return stockminim;
	}
	public void setStockminim(int stockminim) {
		this.stockminim = stockminim;
	}

	public Mesura getMesura() {
		return this.mesura;
	}

	public void setMesura(Mesura mesura) {
		this.mesura = mesura;
	}

	public Set<Moviments> getEntrades() {
		return entrades;
	}

	public void setEntrades(Set<Moviments> entrades) {
		this.entrades = entrades;
	}

	public List<ArticleMovementsCount> getEntradesByUse() {
		return entradesByUse;
	}

	public void setEntradesByUse(List<ArticleMovementsCount> entradesByUse) {
		this.entradesByUse = entradesByUse;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Set<RegistreCanvis> getRegistreCanvis() {
		return registreCanvis;
	}

	public void setRegistreCanvis(Set<RegistreCanvis> registreCanvis) {
		this.registreCanvis = registreCanvis;
	}

	
}
