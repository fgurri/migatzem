package com.creugrogasoft.migatzem.article;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.time.LocalDateTime;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.creugrogasoft.migatzem.UserInfo;
import com.creugrogasoft.migatzem.registrecanvis.RegistreCanvis;
import com.creugrogasoft.migatzem.registrecanvis.RegistreCanvisRepository;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class ArticleService {

	@Autowired
	private ArticleRepository articleRepository;
	
    @Autowired
    private JavaMailSender sender;
    
	@Autowired
	private RegistreCanvisRepository registreCanvisRepository;
    
    private TemplateEngine templateEngine;
    
    @Resource(name = "userInfoBean")
	private UserInfo userInfo;
	
	@Autowired
	private Environment env;
    
    @Autowired
    public ArticleService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
 
    public String build(List<Article> myarticles) {
        Context context = new Context();
        context.setVariable("viewmyarticles", myarticles);
        return templateEngine.process("comanda", context);
    }
	
	public List<Article> getArticlesByNameSorted(String name, String sorting) {
		return fillArticleList(articleRepository.findByNameAndSort(name, new Sort(Sort.Direction.ASC, sorting)));
	}
	
	public List<Article> getArticlesSorted(Pageable pageable) {
		return fillArticleList(articleRepository.findAllSorted(pageable));
	}
	
	public List<Article> getAllArticles() {
		return fillArticleList(articleRepository.findAll());
	}
	
	public List<Article> getAllArticlesByAnySearch(String search, String sorting) {
		return fillArticleList(articleRepository.findAllByIdOrNomOrSerialNumber(search, new Sort(Sort.Direction.ASC, sorting)));
	}
	
	public List<Article> getAllArticlesByAnySearch(String search, Pageable pageable) {
		return fillArticleList(articleRepository.findAllByIdOrNomOrSerialNumber(search, pageable));
	}
	
	public List<Article> getLowStockArticles(Pageable pageable) {
		return fillArticleList(articleRepository.findLowStockArticles(pageable));
	}
	
	public List<Article> getLowStockArticles() {
		return fillArticleList(articleRepository.findLowStockArticles());
	}

	public boolean existsArticle (Integer id) {
		return (articleRepository.findById(id).isPresent());
	}
	
	public Article getArticle(Integer id) {
		return articleRepository.findById(id).get();
	}

	public void addArticle(Article article) {
		articleRepository.save(article);
	}

	public void updateArticle(Article article, Integer id) {
		Article actualArticle = getArticle(id);
		if (actualArticle.getMesura() != null && article.getMesura() != null) {
			if (actualArticle.getMesura().getId() != article.getMesura().getId()) {
				registreCanvisRepository.save(new RegistreCanvis(article, LocalDateTime.now(), "mesura", actualArticle.getMesura().getId(), article.getMesura().getId(), userInfo.getFullName()));
			}	
		}
		if (actualArticle.getNom().compareTo(article.getNom()) != 0) {
			registreCanvisRepository.save(new RegistreCanvis(article, LocalDateTime.now(), "nom", actualArticle.getNom(), article.getNom(), userInfo.getFullName()));
		}
		if (actualArticle.getReference().compareTo(article.getReference()) != 0) {
			registreCanvisRepository.save(new RegistreCanvis(article, LocalDateTime.now(), "referencia", actualArticle.getReference(), article.getReference(), userInfo.getFullName()));
		}
		if (actualArticle.getSerialNumber().compareTo(article.getSerialNumber()) != 0) {
			registreCanvisRepository.save(new RegistreCanvis(article, LocalDateTime.now(), "numero de serie", actualArticle.getSerialNumber(), article.getSerialNumber(), userInfo.getFullName()));
		}
		if (actualArticle.getStockactual() != article.getStockactual()) {
			registreCanvisRepository.save(new RegistreCanvis(article, LocalDateTime.now(), "stock actual", actualArticle.getStockactual()+"", article.getStockactual()+"", userInfo.getFullName()));
		}
		if (actualArticle.getStockminim() != article.getStockminim()) {
			registreCanvisRepository.save(new RegistreCanvis(article, LocalDateTime.now(), "stock minim", actualArticle.getStockminim()+"", article.getStockminim()+"", userInfo.getFullName()));
		}
		articleRepository.save(article);
	}

	public void deleteArticle(Integer id) {
		articleRepository.deleteById(id);
	}
	
	public List<Article> fillArticleList(Iterable<Article> list) {
		List<Article> articles = new ArrayList<>();
		Iterator<Article> it = list.iterator();
		while(it.hasNext()) {
			Article article = it.next();
			article.setEntradesByUse(articleRepository.findMostEntradaById(article.getId()));
			articles.add(article);
		}
		return articles;
	}
	
	public List<Article> getPropostaComanda () {
		List<Article> articles = new ArrayList<>();
		
		
		return articles;
	}
	
	public void sendEmail(InternetAddress[] to, List<Article> articles, String title) throws Exception {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
			messageHelper.setTo(to);
			messageHelper.setSubject(title);

			File file = new File("comanda.xlsx");
			FileOutputStream fileOut = new FileOutputStream(file);
			this.generaExcel(articles).write(fileOut);
			fileOut.close();

			messageHelper.addAttachment("comanda.xlsx", file);
			String content = this.build(articles);
			messageHelper.setText(content, true);
		};
		sender.send(messagePreparator);

	}
	
	public void sendEmail(String to, List<Article> articles, String title) throws Exception {
		MimeMessagePreparator messagePreparator = mimeMessage -> {
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
			messageHelper.setTo(to);
			messageHelper.setSubject(title);

			File file = new File("./comanda.xlsx");
			FileOutputStream fileOut = new FileOutputStream(file);
			this.generaExcel(articles).write(fileOut);
			fileOut.close();

			messageHelper.addAttachment("comanda.xlsx", file);
			String content = this.build(articles);
			messageHelper.setText(content, true);
		};
		sender.send(messagePreparator);

	}
	
	public Workbook generaExcel (List<Article> articles) {
		// Create a Workbook
		int rownum = 0;
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Comanda");
        
        Row row_header = sheet.createRow(rownum);
        // referencia
    	Cell cell_header = row_header.createCell(0);
    	cell_header.setCellValue("Referencia");
    	// article
    	cell_header = row_header.createCell(1);
    	cell_header.setCellValue("Article");
    	// comanda
    	cell_header = row_header.createCell(2);
    	cell_header.setCellValue("Comanda");
    	// %Stock
    	cell_header = row_header.createCell(3);
    	cell_header.setCellValue("% Stock");

    	rownum++;
    	Iterator<Article> itarticles = articles.iterator();
    	while(itarticles.hasNext()) {
    		Article article = itarticles.next();
    		Row row = sheet.createRow(rownum);
    		Cell cell = row.createCell(0);
    		cell.setCellValue(article.getId());
    		cell = row.createCell(1);
    		cell.setCellValue(article.getNom());
    		cell = row.createCell(2);
    		cell.setCellValue(article.getStockminim()-article.getStockactual());
    		cell = row.createCell(3);
    		String percent = new DecimalFormat("#.00").format((article.getStockactual()*1.0 / article.getStockminim())*100);
    		cell.setCellValue( percent + "% (" + article.getStockactual() + " de "+ article.getStockminim() +")");
    		
    		rownum++;
    	}
    	
        return workbook;
	}
   
}
