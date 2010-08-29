import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

    @Before
    public void setup() {
    	Fixtures.deleteAll();
    }
	
	@Test
    public void createLocalUser() {
    	new LocalUser("openId", "nickname", "email").save();
    	
    	LocalUser user = LocalUser.find("byOpenId", "openId").first();
    	
    	assertNotNull(user);
    	assertEquals("nickname", user.nickname);
    }
	
	
	@Test
	public void fullTest() {
		Fixtures.load("data.yml");
		
		assertEquals(2, LocalUser.count());
		assertEquals(1, Card.count());
		assertEquals(2, CardItem.count());
		
		List<CardItem> items = CardItem.find("card.user.nickname", "bob").fetch();
		assertEquals(2, items.size());
	}
	
	

}
