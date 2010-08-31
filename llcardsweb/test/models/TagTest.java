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
		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
    	Tag tag = new Tag(user, "tag1", true).save();
    	assertEquals(1, Tag.count());
    	
    	tag = Tag.all().first();
    	
    	assertEquals("tag1", tag.name);
    	assertEquals(true, tag.systemGenerated);
    }
    
    @Test
    public void createTag_nullUser_cannotBeCreated() {
    	new Tag(null, "tag1", false);
    	fail();
    }
    
    @Test
    public void cardRelation() {
		LocalUser user = new LocalUser("openId", "nickname", "mail").save();
    	Tag tag = new Tag(user, "tag1", true).save();
		Card card = new Card(user, "card").save();
		
		card.tags.add(tag);
		card.save();
    	
		card = Card.all().first();
		assertEquals(1, card.tags.size());
		assertTrue(card.tags.contains(new Tag(user, "tag1", true)));
    }
    
    // TODO: testy pro equals
    
    @Test
    public void getInstance() {
		LocalUser user1 = new LocalUser("user1", "nickname", "mail").save();
		LocalUser user2 = new LocalUser("user2", "nickname", "mail").save();
		
		Tag tag;
		assertEquals(0, Tag.count());
		// new row in db
		tag = Tag.getInstance(user1, "tag1", true);
		assertEquals(1, Tag.count());
		// different tag name -> new row in db
		tag = Tag.getInstance(user1, "tag2", true);
		assertEquals(2, Tag.count());
		// different user -> new row in db
		tag = Tag.getInstance(user2, "tag2", true);
		assertEquals(3, Tag.count());
		// already stored combination, nothing changes
		tag = Tag.getInstance(user1, "tag2", true);
		assertEquals(3, Tag.count());
    }

}
