package models.leitner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import controllers.Security;

import models.Card;
import models.CardItem;

import play.db.jpa.Model;

@Entity
public class StudyPlan extends Model {

	public Date date;
	
	@OneToMany(mappedBy="plan", cascade=CascadeType.ALL)
	public List<StudyItem> items = new ArrayList<StudyItem>();
	
	public void addItem(Card card) {
		StudyItem item = new StudyItem();
		item.state = StudyItemState.NOT_STUDIED;
		item.card = card;
		items.add(item);
	}
	
	public static StudyPlan getPlanFor(Date date) {
		StudyPlan plan = StudyPlan.find("date = ?", date).first();
		if (plan == null) {
			plan = createPlanFor(date, 20);
		}
		return plan;
	}
	
	static StudyPlan createPlanFor(Date date, int planSize) {
		
		// get all cards with level
		List<CardWithLevel> cards = CardWithLevel.getCardsWithLevel(Security.getUser());
		// setridim je podle levelu sestupne (cim starsi karta, tim je pro me prioritnejsi)
		Collections.sort(cards, Collections.reverseOrder());
		
		List<Card> toStudy = new ArrayList<Card>();
		
		Calendar cal = Calendar.getInstance();
		for (CardWithLevel c : cards) {
			if (toStudy.size() >= planSize) {
				break;
			}
			
			cal.setTime(c.getCard().lastLearned);
			cal.add(Calendar.DATE, c.getLevel());
			if (cal.after(date)) {
				toStudy.add(c.getCard());
			}
		}
		
		// 2) add some new cards
		if (toStudy.size() < planSize) {
			cards = CardWithLevel.getCardsWithoutLevel(Security.getUser());
			for (CardWithLevel c : cards) {
				if (toStudy.size() >= planSize) {
					break;
				}
				toStudy.add(c.getCard());
			}
		}
		
		StudyPlan plan = new StudyPlan();
		for (Card card : toStudy) {
			plan.addItem(card);
		}
		
		plan.save();
		return plan;
		
	}
}
