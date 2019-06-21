package com.creugrogasoft.migatzem.article.mesura;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.creugrogasoft.migatzem.article.Article;

@Service
public class MesuraService {

	@Autowired
	private MesuraRepository mesuraRepository;
	
	public List<Mesura> getAllSorted() {
		return mesuraRepository.findAllSorted(new Sort(Sort.Direction.ASC, "id"));
	}
	
}
