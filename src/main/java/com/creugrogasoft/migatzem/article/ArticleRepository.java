package com.creugrogasoft.migatzem.article;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleRepository extends CrudRepository<Article, Integer> {

	
	@Query("FROM Article")
	List<Article> findAllSorted(Pageable pageable);
	
	@Query("FROM Article where nom like %?1%")
	List<Article> findByNameAndSort(String lastname, Sort sort);
	
	@Query("FROM Article where nom like %?1% or id like %?1% or serialnumber like %?1%")
	List<Article> findAllByIdOrNomOrSerialNumber (String search, Sort sort);
	
	@Query("FROM Article where nom like %?1% or id like %?1% or serialnumber like %?1%")
	List<Article> findAllByIdOrNomOrSerialNumber (String search, Pageable pageable);
	
	@Query("select new com.creugrogasoft.migatzem.article.ArticleMovementsCount(m.quantitat, count(*)) "
				+ " from Moviments m where article_id=?1 group by quantitat order by count(*) desc")
	List<ArticleMovementsCount> findMostEntradaById(Integer id);
	
	@Query("FROM Article where stockactual < stockminim")
	List<Article> findLowStockArticles(Pageable pageable);
	
	@Query("FROM Article where stockactual < stockminim")
	List<Article> findLowStockArticles();
	
}
