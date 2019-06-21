package com.creugrogasoft.migatzem.registrecanvis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.creugrogasoft.migatzem.moviments.Moviments;

@Service
public class RegistreCanvisService {

	@Autowired
	private RegistreCanvisRepository registreCanvisRepository;
	
	public List<RegistreCanvis> findByArticleId(Integer id, Pageable pageable) {
		return registreCanvisRepository.findByArticleId(id, pageable);
	}
	
	public List<RegistreCanvis> findByArticleId(Integer id) {
		return registreCanvisRepository.findByArticleId(id);
	}
}
