package models.leitner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;

import play.db.jpa.Model;

import models.Card;
import models.LocalUser;

public class CardWithLevel implements Comparable<CardWithLevel> {
	
	public static final String TAG_GROUP_LEITNER = "leitner";
	
	private Card card;
	private Integer level;
	
	public CardWithLevel(Card card, Integer level) {
		this.card = card;
		this.level = level;
	}
	
	public static List<CardWithLevel> getCardsWithLevel(LocalUser user) {
		List<CardWithLevel> result = new ArrayList<CardWithLevel>();
		
		String q = "select c, t.name from Card c inner join c.tags t where c.user = ? and t.group = ?";
		List<Object[]> rows = Card.find(q, user, TAG_GROUP_LEITNER).fetch();
		
		for (Object[] row : rows) {
			Card card = (Card) row[0];
			String levelTagName = (String) row[1];
			Integer level = null;
			if (levelTagName != null) {
				level = Integer.valueOf(levelTagName);
			}
			result.add(new CardWithLevel(card, level));
		}
		
		return result;
	}
	
	public static List<CardWithLevel> getCardsWithoutLevel(LocalUser user) {
		// TODO: jsem linej
		List<CardWithLevel> result = new ArrayList<CardWithLevel>();
		String q = "select c, t.name from Card c left outer join c.tags t where c.user = ? and (t.group = ? or t.group is null)";
		List<Object[]> rows = Card.find(q, user, TAG_GROUP_LEITNER).fetch();
		for (Object[] row : rows) {
			Card card = (Card) row[0];
			String levelTagName = (String) row[1];
			if (levelTagName == null) {
				result.add(new CardWithLevel(card, null));
			}
		}
		return result;

	}
	
	public Card getCard() {
		return card;
	}
	
	public Integer getLevel() {
		return level;
	}

	@Override
	public int compareTo(CardWithLevel other) {
		if (other == null)
			return -1;
		int level = this.level == null ? -1 : this.level;
		int otherLevel = other.getLevel() == null ? -1 : other.getLevel();
		return level - otherLevel;
	}
	
	
	
	

}
