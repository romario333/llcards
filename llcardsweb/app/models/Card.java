package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
	
	@ManyToMany
	public Set<Tag> tags = new HashSet<Tag>();
	
	@ManyToOne(optional=true)
	public WordFrequency wordFrequency;
	
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
		// TODO: jaktoze je null kdyz vytvarim novy zaznam??
		if (items != null) {
			while (items.size() != 0) {
				CardItem item = items.remove(0);
				item.delete();
			}
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
	
	public void addTag(String name) {
		Tag tag = Tag.getInstance(user, name, false);
		tags.add(tag);
	}
	
	public void removeTag(String name) {
		Tag tag = Tag.getInstance(user, name, false);
		tags.remove(tag);
	}
	
	public String getItemTypeName(CardItem item) {
		if (item.itemType == CardItemType.DEFINITION) {
			int definitionCount = 0, definitionRank = 0;
			for (CardItem i : items) {
				if (i.itemType == CardItemType.DEFINITION)
					definitionCount++;
					
				if (i == item)
					definitionRank = definitionCount;
			}
			
			if (definitionCount > 1)
				return String.format("%s (%s)", item.itemType.getName(), definitionRank);
			else
				return item.itemType.getName();
		} else {
			return item.itemType.getName();
		}
	}
	

	// TODO: tezce neoptimalni
	public Card next() {
		// TODO: debug
		List<Card> cards = Card.all().fetch();
		for (int i = 0; i < cards.size(); i++) {
			if (cards.get(i).id.equals(id)) {
				if (i+1 < cards.size())
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
		
		// TODO: tohle asi nebude nejstastnejsi misto
		wordFrequency = WordFrequency.find("word = ?", title.trim()).first();
		
		return super.save();
	}
}
