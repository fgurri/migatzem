package com.creugrogasoft.migatzem.article;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Digits;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.creugrogasoft.migatzem.LoggingController;
import com.creugrogasoft.migatzem.UserInfo;
import com.creugrogasoft.migatzem.article.mesura.MesuraService;
import com.creugrogasoft.migatzem.moviments.Moviments;
import com.creugrogasoft.migatzem.moviments.MovimentsService;
import com.creugrogasoft.migatzem.registrecanvis.RegistreCanvisService;

@Controller
public class ArticleController {

	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private MovimentsService movimentService;
	
	@Autowired
	private MesuraService mesuraService;
	
	@Autowired
	private RegistreCanvisService registreCanvisService;

	@Resource(name = "userInfoBean")
	private UserInfo userInfo;
	
	@Autowired
	private Environment env;
	
	Logger logger = LoggerFactory.getLogger(LoggingController.class);

	
	@Value("${migatzem.pageable.page-size}")
	private Integer pageSize;
	
	@Value("${migatzem.pageable.default-page}")
	private Integer defaultPage;
	
	@Value("${migatzem.pageable.default-order-field}")
	private String defaultOrderField;
	
	@RequestMapping("/articles")
	public String  getAllArticles(ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		
		// redirect to an empty search to have a unique entry point to panel
	    return "redirect:/articles/search";
	}
	
	@RequestMapping("/articles/search")
	public String  getAllArticlesSearched(@RequestParam (value = "searchtext", required = false, defaultValue = "") String searchtext,
			@RequestParam (value = "message", required = false, defaultValue = "") String message,
			@RequestParam (value = "currentpage", required = false, defaultValue = "-1") Integer currentpage,
			@RequestParam (value = "command", required = false, defaultValue = "/articles/search") String command,
			@RequestParam (value = "orderfield", required = false, defaultValue = "") String orderfield,
			ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		
		String title = "Llista d'articles";
		// Check if searchtext received the value "null"
		if (searchtext.compareTo("null") == 0) {
			searchtext = "";
		}
		if (searchtext.compareTo("") != 0) {
			title += " que contenen '" + searchtext+ "'";
		}
		if (currentpage == -1) {
			currentpage = this.defaultPage;
		}
		if (orderfield.compareTo("") == 0) {
			orderfield = this.defaultOrderField;
		}

		List<Article> myarticles = articleService.getAllArticlesByAnySearch(searchtext, PageRequest.of(currentpage, this.pageSize, new Sort(Sort.Direction.ASC, orderfield)));
		
		// manually get total articles to calc pagination. As an improvement, Pageable interface should give us the number of pages automatically
		int totalpages = (int)Math.round(Math.ceil(1.0*articleService.getAllArticlesByAnySearch(searchtext, orderfield).size()/this.pageSize));
		// https://stackoverflow.com/questions/49384795/spring-data-jpa-getting-all-pages-from-pageable
		
		model.addAttribute("viewmyarticles", myarticles);
	    model.addAttribute("searchtext", searchtext);
	    model.addAttribute("currentpage", currentpage);
	    model.addAttribute("command", "/articles/search");
	    model.addAttribute("totalpages", totalpages);
	    model.addAttribute("title", title);
	    model.addAttribute("message", message);
		
	    return "articles";
	}
	@RequestMapping("/articles/lowstock")
	public String getLowStock(@RequestParam (value = "currentpage", required = false, defaultValue = "-1") Integer currentpage,
			@RequestParam (value = "orderfield", required = false, defaultValue = "") String orderfield,
			ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		
		if (currentpage == -1) {
			currentpage = this.defaultPage;
		}
		if (orderfield.compareTo("") == 0) {
			orderfield = this.defaultOrderField;
		}
		
		int totalpages = (int)Math.round(Math.ceil(1.0*articleService.getLowStockArticles().size()/this.pageSize));
		
		model.addAttribute("viewmyarticles", articleService.getLowStockArticles(PageRequest.of(currentpage, this.pageSize, new Sort(Sort.Direction.ASC, orderfield))));
		model.addAttribute("currentpage", currentpage);
		model.addAttribute("command", "/articles/lowstock");
	    model.addAttribute("totalpages", totalpages);
	    model.addAttribute("title", "Lista d'articles en baix stock");
	    return "articles";
	}
	
	@RequestMapping("/articles/comanda-form")
	public String comandaForm(ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		
		model.addAttribute("title", "Enviament de comanda");
		return "comanda-form";
	}
	
	@RequestMapping("/articles/comanda")
	public String sendComanda(@RequestParam (value = "to", required = true) String to,
			ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		
		// InternetAddress[] to = null;
		try {
			// to = InternetAddress.parse(env.getProperty("migatzem.mail.to"));
			model.addAttribute("viewmyarticles", articleService.getLowStockArticles());
			model.addAttribute("message", "Comanda enviada correctament al correu " + to);
			model.addAttribute("umbral", Integer.parseInt(env.getProperty("migatzem.umbral")));
			articleService.sendEmail(to, articleService.getLowStockArticles(), "Comanda " + LocalDateTime.now());
		} catch (Exception ex) {
			model.addAttribute("message", "No s'ha pogut enviar la comanda al correu: " + to + ". Contacti amb el departament de informàtica.");
			logger.error(ex.getLocalizedMessage());
		}
		
		return "redirect:/articles/search";
		// return "comanda";
	}
	
	@RequestMapping("/articles/new")
	public String getTopic(@ModelAttribute("article") Article article, BindingResult bindingResult,  ModelMap model) {
		
		if (!userInfo.isLogged())
			return "login";
		
		model.addAttribute("article", new Article());
		model.addAttribute("command", "newArticle");
		model.addAttribute("title", "Nou Article");
		model.addAttribute("mesures", mesuraService.getAllSorted());
		
		return "article";
	}
	
	@RequestMapping("/articles/{id}")
	public String getTopic(@PathVariable Integer id, @ModelAttribute("article") Article article, BindingResult bindingResult,  ModelMap model) {
		
		if (!userInfo.isLogged())
			return "login";
		
		if (id == null) {
			model.addAttribute("article", new Article());
			model.addAttribute("command", "newArticle");
			model.addAttribute("title", "Nou Article");
		} else {
			article = articleService.getArticle(id);
			model.addAttribute("article", article);
			model.addAttribute("command", id + "/updateArticle");
			model.addAttribute("title", "Modificar l'article " + article.getNom());
		}
		model.addAttribute("mesures", mesuraService.getAllSorted());
		
		return "article";
	}
	
	@RequestMapping("/moviments/{id}")
	public String getMoviments(@PathVariable Integer id, 
			@RequestParam (value = "currentpage", required = false, defaultValue = "-1") Integer currentpage,
			ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		if (currentpage == -1) {
			currentpage = this.defaultPage;
		}
		int totalpages = (int)Math.round(Math.ceil(1.0*movimentService.findByArticleId(id).size()/this.pageSize));
		
		Article article = articleService.getArticle(id);
		model.addAttribute("moves", movimentService.findByArticleId(id, PageRequest.of(currentpage, this.pageSize, Direction.DESC, "diaHora", "id")));
		model.addAttribute("idArticle", id);
	    model.addAttribute("currentpage", currentpage);
	    model.addAttribute("totalpages", totalpages);
	    model.addAttribute("title", "Moviments de l'article " + article.getNom());
		return "moviments";
	}
	
	@RequestMapping("/registrecanvis/{id}")
	public String getRegistreCanvis(@PathVariable Integer id, 
			@RequestParam (value = "currentpage", required = false, defaultValue = "-1") Integer currentpage,
			ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		
		if (currentpage == -1) {
			currentpage = this.defaultPage;
		}
		int totalpages = (int)Math.round(Math.ceil(1.0*registreCanvisService.findByArticleId(id).size()/this.pageSize));
		
		Article article = articleService.getArticle(id);
		model.addAttribute("registrecanvis", registreCanvisService.findByArticleId(id, PageRequest.of(currentpage, this.pageSize, Direction.DESC, "diaHora", "id")));
		model.addAttribute("idArticle", id);
	    model.addAttribute("currentpage", currentpage);
	    model.addAttribute("totalpages", totalpages);
	    model.addAttribute("title", "Registre de canvis de l'article " + article.getNom());
		return "registrecanvis";
	}
	
	@RequestMapping("/moviments/{id}/add")
	public String addMoviment(@PathVariable Integer id, 
			@RequestParam (value = "quantitat", required = true) Integer quantitat,
			@RequestParam (value = "searchtext", required = false, defaultValue = "") String searchtext,
			@RequestParam (value = "action", required = false, defaultValue = "") String action,
			ModelMap model) {
		
		if (!userInfo.isLogged())
			return "login";
		
		Article article = articleService.getArticle(id);
		if (action.compareTo("-") == 0) {
			// we pressed "-" button
			quantitat = -1*quantitat;
		}
		Moviments entrada = new Moviments(LocalDateTime.now(), quantitat, article, userInfo.getFullName());
		movimentService.addEntrades(entrada);
		article.setStockactual(article.getStockactual()+quantitat);
		articleService.updateArticle(article, id);
		model.clear();
		model.addAttribute("message", "Stock de l'article " + article.getNom()+ " modificat correctament. Stock actual: " + article.getStockactual());
		model.addAttribute("searchtext", searchtext);
		return "redirect:/articles/search";
	}
	
	@RequestMapping(value="/{id}/updateArticle")
	public String updateArticle(@PathVariable Integer id, @Valid @ModelAttribute("article") Article article, BindingResult bindingResult, ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		if (bindingResult.hasErrors()) {
			model.addAttribute("command", "updateArticle");
			return "article";
		}
		
		this.articleService.updateArticle(article, id);
				
		model.addAttribute("message", "Article modificat correctament");
		return "redirect:/articles/search";
	}
	
	@RequestMapping(value="/deleteArticle/{id}")
	public String deleteArticle(@PathVariable Integer id, ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		
		this.articleService.deleteArticle(id);
		
		model.addAttribute("message", "Article eliminat correctament");
			
		return "redirect:/articles/search";
	}
	
	@RequestMapping(value="/newArticle")
	public String newArticle(@Valid @ModelAttribute("article") Article article, BindingResult bindingResult, ModelMap model) {
		if (!userInfo.isLogged())
			return "login";
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("command", "newArticle");
			return "article";
		}
				
		this.articleService.addArticle(article);
				
		model.addAttribute("message", "Article creat correctament");
		return "redirect:/articles/search";
	}
	
	@ExceptionHandler({MethodArgumentTypeMismatchException.class, NullPointerException.class, IllegalStateException.class})
	public String handleTypeMismatch(Exception ex, RedirectAttributes redirectAttrs, WebRequest request) {
	    redirectAttrs.addAttribute("searchtext", request.getParameter("searchtext"));
	    redirectAttrs.addAttribute("message", messageSource.getMessage("GenericError.article", null, null));
	    logger.error(ex.getLocalizedMessage());
	    return "redirect:/articles/search";
	}
	
}
