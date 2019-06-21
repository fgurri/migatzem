package com.creugrogasoft.migatzem.moviments;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.creugrogasoft.migatzem.UserInfo;
import com.creugrogasoft.migatzem.registrecanvis.RegistreCanvis;
import com.creugrogasoft.migatzem.registrecanvis.RegistreCanvisRepository;


@Service
public class MovimentsService {

	@Autowired
	private MovimentsRepository entradesRepository;
	
	@Autowired
	private RegistreCanvisRepository registreCanvisRepository;
	
	@Resource(name = "userInfoBean")
	private UserInfo userInfo;
		
	@Autowired
	private Environment env;
	
	public List<Moviments> getAllEntrades() {
		List<Moviments> entrades = new ArrayList<>();
		entradesRepository.findAll().forEach(entrades::add);
		return entrades;
	}
	
	public List<Moviments> findByArticleId(Integer id) {
		return entradesRepository.findByArticleId(id);
	}
	
	public List<Moviments> findByArticleId(Integer id, Pageable pageable) {
		return entradesRepository.findByArticleId(id, pageable);
	}
	
	public Moviments getEntrades(int id) {
		return entradesRepository.findById(id).get();
	}

	public void addEntrades(Moviments entrades) {
		registreCanvisRepository.save(new RegistreCanvis(entrades.getArticle(), LocalDateTime.now(), "stock actual", entrades.getArticle().getStockactual()+"", entrades.getArticle().getStockactual()+entrades.getQuantitat()+"", userInfo.getFullName()));
		entradesRepository.save(entrades);
	}

	public void updateEntrades(Moviments entrades, String id) {
		registreCanvisRepository.save(new RegistreCanvis(entrades.getArticle(), LocalDateTime.now(), "stock actual", entrades.getArticle().getStockactual()+"", entrades.getArticle().getStockactual()+entrades.getQuantitat()+"", userInfo.getFullName()));
		entradesRepository.save(entrades);
	}

}
