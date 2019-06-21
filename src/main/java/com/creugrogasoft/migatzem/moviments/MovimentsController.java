package com.creugrogasoft.migatzem.moviments;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.creugrogasoft.migatzem.UserInfo;
import com.creugrogasoft.migatzem.article.Article;
import com.creugrogasoft.migatzem.article.ArticleService;

@Controller
public class MovimentsController {

	@Autowired
	private MovimentsService entradesService;
	
	@Autowired
	private ArticleService articlesServices;
	
	@RequestMapping("/moviments")
	public List<Moviments> getAllEntrades() {
		
		return entradesService.getAllEntrades();
	}
}
