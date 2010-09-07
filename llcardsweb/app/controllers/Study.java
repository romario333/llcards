package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.mapping.Array;
import org.joda.time.DateTime;

import models.Card;
import models.Tag;
import models.leitner.CardWithLevel;

import play.mvc.Controller;
import play.mvc.With;

@With(AuthenticationRequired.class)
public class Study extends Controller {

	private static Tag[] levelTags;
	
	static {
//		levelTags = new Tag[4];
//		levelTags[0] = Tag.getInstance(Security.getUser(), "Level 1", true);
//		levelTags[1] = Tag.getInstance(Security.getUser(), "Level 2", true);
//		levelTags[2] = Tag.getInstance(Security.getUser(), "Level 3", true);
//		levelTags[3] = Tag.getInstance(Security.getUser(), "Level 4", true);
//		levelTags[4] = Tag.getInstance(Security.getUser(), "Level 5", true);
	}
	
	public static void randomCard() {
		
	}
	
//	private static List<Card> getCardsToStudy() {
//		List<Card> cards = new ArrayList<Card>();
//		Date now = new Date();
//		cards.addAll(getCardsForLevel(now, 1));
//		cards.addAll(getCardsForLevel(now, 2));
//		cards.addAll(getCardsForLevel(now, 3));
//		cards.addAll(getCardsForLevel(now, 4));
//		
//		
//	}
	
	private static List<Card> getCardsForLevel(Date now, int level) {
		Calendar studyInterval = Calendar.getInstance();
		studyInterval.setTime(now);
		studyInterval.add(Calendar.DATE, -level);
		List<Card> cards = Card.find("select c from Card c inner join c.tags t where t = ? and c.lastLearned > ?", 
				levelTags[level-1],
				studyInterval
				).fetch();
		return cards;
	}
	
	
}
