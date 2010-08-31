package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import models.Card;
import models.CardItem;
import models.CardItemType;
import models.LocalUser;
import models.Tag;
import play.data.validation.Valid;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;

@With(AuthenticationRequired.class)
public class Cards extends Controller {

	public static void index() {
		list();
	}
	
	public static void list() {
		// TODO: strankovani
		LocalUser user = AuthenticationRequired.getUser();
		//List<Card> cards = Card.find("byUser", user).fetch();
		// TODO: debug
		List<Card> cards = Card.all().fetch();
		render(cards);
	}
	
	public static void listByTag(String tagName) {
		Tag tag = Tag.getInstance(Security.getUser(), tagName, true);
		List<Card> cards = Card.find("select c from Card c inner join c.tags t where t = ?", tag).fetch();
		render("Cards/list.html", cards, tag);
	}
	
	public static void show(Long id) {
		Card card = Card.findById(id);
		render(card);
	}
	
	public static void create() {
		Card card = new Card(AuthenticationRequired.getUser());
		card.items.add(new CardItem(CardItemType.DEFINITION, ""));
		card.items.add(new CardItem(CardItemType.PRONUNCIATION, ""));
		card.items.add(new CardItem(CardItemType.EXAMPLES, ""));

		renderArgs.put("card", card);
		renderArgs.put("cardItems", card.items);
		edit();
	}
	
	public static void edit(Long id) {
		Card card = Card.findById(id);
		renderArgs.put("card", card);
		renderArgs.put("cardItems", card.items);
		edit();
	}
	
	private static void edit() {
		renderArgs.put("cardItemTypes", CardItemType.values());
		render("Cards/edit.html");
	}

	public static void save(@Valid Card card, String[] itemType, String[] itemContent) {
		
		// recreate items list (start with 1 as first item is hidden prototype)
		List<CardItem> items = new ArrayList<CardItem>();
		for (int i = 1; i < itemType.length; i++) {
			CardItemType type = CardItemType.valueOf(itemType[i]);
			String content = itemContent[i].trim();
			if (!content.equals("")) {
				items.add(new CardItem(type, content));
			}
		}
		
		if (validation.hasErrors()) {
			params.flash();
			renderArgs.put("card", card);
			renderArgs.put("cardItems", items);
			//validation.keep();
			edit();
		}
		
		// TODO: retest, ze se tam nekupi orphani
		card.replaceItems(items);
		card.save();
		
		// TODO: flash message
		flash.success("Card has been saved.");
		
		show(card.id);
	}
	
	// TODO: delete by mel byt post
	public static void delete(Long id) {
		Card card = Card.findById(id);
		card.delete();
		list();
	}
	
	public static void importCards() {
		render();
	}
	
	public static void uploadCards(File cardsFile) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
	    DocumentBuilderFactory domFactory = 
	        DocumentBuilderFactory.newInstance();
	              domFactory.setNamespaceAware(true); 
	        DocumentBuilder builder = domFactory.newDocumentBuilder();
	        Document doc = builder.parse(cardsFile);
	        XPath xpath = XPathFactory.newInstance().newXPath();
	           // XPath Query for showing all nodes value
	        XPathExpression expr = xpath.compile("//card");

	        Object result = expr.evaluate(doc, XPathConstants.NODESET);
	        NodeList nodes = (NodeList) result;
	        for (int i = 0; i < nodes.getLength(); i++) {
	        	Element card = (Element) nodes.item(i);
	        	String title = card.getAttribute("title");
	        	String content = card.getAttribute("content");
	        	String lastLearned = card.getAttribute("last_learned");
	        	String level = card.getAttribute("level");
	        	
	        	importCard(title, content, level, null);
	        }
	        flash.success("Successfully imported.");
	        list();
	}
	
	private static void importCard(String title, String content, String level, Date last_learned) {
		LocalUser user = Security.getUser();
		Card card = new Card(user);
		card.title = title;
		
		content = content.replace("<br>", "\n");
		
		String[] parts = content.split("\n\n");
		
		boolean definitionSet = false;
		String examples = "";
		
		for (int i = 0; i < parts.length; ) {
			String part = parts[i].trim();
			if (part.matches("^\\d\\).*")) {
				definitionSet = false;
				parts[i] = parts[i].substring(2);
				if (examples != "") {
					card.addItem(new CardItem(CardItemType.EXAMPLES, examples));
					examples = "";
				}
				continue;
			}
			if (part.startsWith("/")) {
				card.addItem(new CardItem(CardItemType.PRONUNCIATION, part));
				i++;
				continue;
			}
			if (!definitionSet) {
				card.addItem(new CardItem(CardItemType.DEFINITION, part));
				definitionSet = true;
				i++;
				continue;
			}
			examples += "\n" + parts[i];
			i++;
		}
		if (examples != "") {
			card.addItem(new CardItem(CardItemType.EXAMPLES, examples));
		}
		
		card.addTag("Level " + level);
		
		card.save();
	}
	
	public static void deleteAll() {
		CardItem.deleteAll();
		Card.deleteAll();
		list();
	}
	
	
}
