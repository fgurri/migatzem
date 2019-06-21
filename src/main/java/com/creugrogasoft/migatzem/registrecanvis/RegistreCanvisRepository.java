package com.creugrogasoft.migatzem.registrecanvis;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.creugrogasoft.migatzem.registrecanvis.*;

public interface RegistreCanvisRepository extends CrudRepository<RegistreCanvis, Integer> {

	@Query("FROM RegistreCanvis WHERE article_id=?1")
	List<RegistreCanvis> findByArticleId(Integer id);
	
	@Query("FROM RegistreCanvis WHERE article_id=?1")
	List<RegistreCanvis> findByArticleId(Integer id, Pageable pageable);

}
