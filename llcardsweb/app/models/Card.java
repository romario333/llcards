package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;

import play.data.validation.Required;
import play.db.jpa.JPASupport;
import play.db.jpa.Model;

@Entity
public class Card extends Model {

	@Required(message="Card's title is required.")
	public String title;
	public Date created;
	public Date modified;
	
	@ManyToOne
	public LocalUser user;
	
	@OneToMany(mappedBy="card", cascade=CascadeType.ALL)
	public List<CardItem> items = new ArrayList<CardItem>();
	
	@OneToMany()
	public Set<Tag> tags = new HashSet<Tag>();
	
	public Card(LocalUser user) {
		this(user, "");
	}

	public Card(LocalUser user, String title) {
		this.user = user;
		this.title = title;
		this.created = new Date();
		this.modified = new Date();
	}
	
	public void addItem(CardItem item) {
		item.card = this;
		this.items.add(item);
	}
	
	void deleteItems() {
		while (items.size() != 0) {
			CardItem item = items.remove(0);
			item.delete();
		}
	}
	
	/**
	 * Replaces currently set items with new ones. 
	 * 
	 * @param items
	 */
	public void replaceItems(List<CardItem> newItems) {
		deleteItems();
		
		items = new ArrayList<CardItem>();
		
		for (CardItem item : newItems) {
			item.card = this;
			items.add(item);
		}
	}
	
	// TODO: tezce neoptimalni
	public Card next() {
		// TODO: debug
		List<Card> cards = Card.all().fetch();
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).id.equals(id)) {
				if (i < cards.size())
					return cards.get(i+1);
			}
		}
		return null;
	}

	public Card previous() {
		// TODO: debug
		List<Card> cards = Card.all().fetch();
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).id.equals(id)) {
				if (i > 0)
					return cards.get(i-1);
			}
		}
		return null;
	}
	
	@Override
	public <T extends JPASupport> T save() {
		if (items != null) {
			int order = 0;
			for (CardItem item : items) {
				item.itemOrder = order++;
			}
		}
		
		return super.save();
	}
}
