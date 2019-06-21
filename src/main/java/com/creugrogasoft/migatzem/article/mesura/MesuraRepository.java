package com.creugrogasoft.migatzem.article.mesura;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.creugrogasoft.migatzem.article.Article;


public interface MesuraRepository extends CrudRepository<Mesura, String> {

	@Query("FROM Mesura")
	List<Mesura> findAllSorted(Sort sort);
}
