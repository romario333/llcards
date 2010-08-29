package models;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class TagTest extends UnitTest {

    @Before
    public void setup() {
    	Fixtures.deleteAll();
    }
    
    @Test
    public void createTag() {
    	Tag tag = new Tag("tag1", true).save();
    	assertEquals(1, Tag.count());
    	
    	tag = Tag.all().first();
    	
    	assertEquals("tag1", tag.name);
    	assertEquals(true, tag.systemGenerated);
    }
    
    @Test
    public void cardRelation() {
    	Tag tag = new Tag("tag1", true).save();
		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
		Card card = new Card(user, "card").save();
		
		card.tags.add(tag);
		card.save();
    	
		card = Card.all().first();
		assertEquals(1, card.tags.size());
		assertTrue(card.tags.contains(new Tag("tag1", true)));
    }

}
