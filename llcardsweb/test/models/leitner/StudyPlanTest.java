package models.leitner;

import java.util.Date;
import java.util.List;

import models.Card;
import models.LocalUser;
import models.Tag;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class StudyPlanTest extends UnitTest {

    @Before
    public void setup() {
    	Fixtures.deleteAll();
    }
    

    private void createTestData() {
		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
    	Tag tag = Tag.getInstance(user, CardWithLevel.TAG_GROUP_LEITNER, "1", true);
		Card card = new Card(user, "card1").save();
		card.tags.add(tag);
		card.save();
		card = new Card(user, "card2").save();
    }
    
    @Test
    public void createPlanFor() {
    	createTestData();
		Date today = Date.
    	createPlanFor		
    	
    }
	
}
