package models;

import helpers.AssertionHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class CardTest extends UnitTest {
	
    @Before
    public void setup() {
    	Fixtures.deleteAll();
    }
	
	@Test
	public void createCard() {
		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
		
		Date beforeSave = new Date();
		new Card(user, "card").save();
		Date afterSave = new Date();
		
		assertEquals(1, Card.count());
		
		List<Card> userCards = Card.find("byUser", user).fetch();
		
		assertEquals(1, userCards.size());
		Card firstCard = userCards.get(0);
		assertNotNull(firstCard);
		assertEquals(user, firstCard.user);
		assertEquals("card", firstCard.title);
		
		AssertionHelper.assertBetween(beforeSave, afterSave, firstCard.created);
		AssertionHelper.assertBetween(beforeSave, afterSave, firstCard.modified);
	}
	
	@Test
	public void cardItemRelation() {
		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
		Card card = new Card(user, "card").save();
		card.addItem(new CardItem(CardItemType.DEFINITION, "definition"));
		card.addItem(new CardItem(CardItemType.PRONUNCIATION, "pron"));
		card.save();
		
		assertEquals(1, LocalUser.count());
		assertEquals(1, Card.count());
		assertEquals(2, CardItem.count());
		
		card = Card.find("byUser", user).first();
		assertNotNull(card);
		
		assertEquals(2, card.items.size());
		assertEquals("definition", card.items.get(0).content);
		
		card.delete();
		
		assertEquals(1, LocalUser.count());
		assertEquals(0, Card.count());
		assertEquals(0, CardItem.count());
	}
	
	@Test
	public void cardItemOrder() {
		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
		Card card = new Card(user, "card").save();
		card.addItem(new CardItem(CardItemType.DEFINITION, "definition"));
		card.addItem(new CardItem(CardItemType.PRONUNCIATION, "pron"));
		card.save();
		
		card = Card.find("byUser", user).first();
		assertNotNull(card);
		
		int index = 0;
		for (CardItem item : card.items) {
			assertEquals(index++, item.itemOrder);
		}
	}


	@Test
	public void deleteCard() {
		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
		Card card = new Card(user, "card").save();
		card.addItem(new CardItem(CardItemType.DEFINITION, "olditemdef"));

		card.delete();

		// check that card's items has been deleted too
		CardItem oldItem = CardItem.find("byContent", "olditemdef").first();
		assertNull(oldItem);
	}
	
//	@Test
//	public void deleteItems() {
//		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
//		Card card = new Card(user, "card").save();
//		card.addItem(new CardItem(CardItemType.DEFINITION, "olditemdef"));
//
//		assertEquals(1, card.items.size());
//		
//		card.deleteItems();
//
//		assertEquals(0, card.items.size());
//		
//		// check that replaced item has been deleted from db
//		CardItem oldItem = CardItem.find("byContent", "olditemdef").first();
//		assertNull(oldItem);
//	}
//	
//	
//	@Test
//	public void replaceItems() {
//		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
//		Card card = new Card(user, "card").save();
//		card.addItem(new CardItem(CardItemType.DEFINITION, "olditemdef"));
//
//		List<CardItem> items = new ArrayList<CardItem>();
//		items.add(new CardItem(CardItemType.DEFINITION, "newitemdef"));
//		
//		card.replaceItems(items);
//		// TODO: jaktoze to tady funguje bez savu?
//		
//		card = Card.find("byUser", user).first();
//		assertNotNull(card);
//		assertEquals(1, card.items.size());
//		assertEquals("newitemdef", card.items.get(0).content);
//		// check that replaced item has been deleted from db
//		CardItem oldItem = CardItem.find("byContent", "olditemdef").first();
//		assertNull(oldItem);
//	}
	
	

}
