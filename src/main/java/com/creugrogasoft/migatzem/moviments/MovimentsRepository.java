package com.creugrogasoft.migatzem.moviments;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.creugrogasoft.migatzem.article.Article;

public interface MovimentsRepository extends CrudRepository<Moviments, Integer> {

	@Query("FROM Moviments WHERE article_id=?1")
	List<Moviments> findByArticleId(Integer id, Pageable pageable);
	
	@Query("FROM Moviments WHERE article_id=?1")
	List<Moviments> findByArticleId(Integer id);
}
