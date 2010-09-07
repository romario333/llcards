package models.leitner;

import java.util.List;

import models.Card;
import models.LocalUser;
import models.Tag;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class CardWithLevelTest extends UnitTest {

    LocalUser user;
	Card cardNoLevel;
    Card cardLevel1;
	
    @Before
    public void setup() {
    	Fixtures.deleteAll();
    	
		user = new LocalUser("openId", "nickname", "mail").save();
    	

    	cardNoLevel = new Card(user, "card-no-level").save();    	
    	
    	cardLevel1 = new Card(user, "card-level1").save();
    	Tag tagLevel1 = Tag.getInstance(user, CardWithLevel.TAG_GROUP_LEITNER, "1", true);
    	cardLevel1.tags.add(tagLevel1);
		cardLevel1.save();
    }
    
    
    @Test
    public void getCardsWithLevel() {
		List<CardWithLevel> cards = CardWithLevel.getCardsWithLevel(user);
		assertEquals(1, cards.size());
		assertEquals(Integer.valueOf(1), cards.get(0).getLevel());
		assertEquals(cardLevel1.id, cards.get(0).getCard().id);
    }

    @Test
    public void getCardsWithoutLevel() {
		List<CardWithLevel> cards = CardWithLevel.getCardsWithoutLevel(user);
		assertEquals(1, cards.size());
		assertEquals(null, cards.get(0).getLevel());
		assertEquals(cardNoLevel.id, cards.get(0).getCard().id);
    }
    
}
