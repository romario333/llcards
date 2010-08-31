package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class CardItem extends Model {

	public CardItemType itemType;
	public String content;
	public int itemOrder = -1;
	
	@ManyToOne
	public Card card;
	
	public CardItem(CardItemType itemType, String content) {
		this.itemType = itemType;
		this.content = content;
	}
}
