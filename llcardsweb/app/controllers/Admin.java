package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.transaction.Transaction;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import models.Language;
import models.WordFrequency;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import play.db.jpa.JPA;
import play.mvc.Controller;

public class Admin extends Controller {
	
	public static void importWordsFreqs() {
		render();
	}
	
	public static void uploadWordsFrequencies(File wordsFile) throws FileNotFoundException {
		WordFrequency.deleteAll();
		
		Scanner scan = new Scanner(wordsFile);
	    
	    Language lang = Language.find("code = ?", "EN").first();
	    if (lang == null) {
	    	lang = new Language();
	    	lang.code = "EN";
	    	lang.name = "English";
	    	lang.save();
	    }
	    
	    
//	    EntityManager em = JPA.newEntityManager();
	    
	    int line = 0;
	    while (scan.hasNextLine() && scan.hasNext()) {
	    	line++;
	    	//System.out.println("line " + line);
	    	
	    	int rank = scan.nextInt();
	    	String word = scan.next();
	    	int count = scan.nextInt();
	    	
	    	WordFrequency wf = new WordFrequency();
	    	wf.wordRank = rank;
	    	wf.word = word;
	    	wf.wordCount = count;
	    	wf.save();
	    	
	    	if (line % 100 == 0) {
	    		System.out.println("line " + line + ", commiting");
//	    		em.getTransaction().commit();
//	    		em.close();
//	    		em = JPA.newEntityManager();
//	    		em.getTransaction().begin();
	    		JPA.em().getTransaction().commit();
	    		JPA.em().getTransaction().begin();
	    	}
	    	
	    }

	    JPA.em().getTransaction().commit();

	    scan.close();
	    
	    flash.success("Successfully imported");
	    Application.index();
	}	

}
