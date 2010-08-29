package controllers;

import models.LocalUser;
import play.libs.OpenID;
import play.libs.OpenID.UserInfo;
import play.mvc.*;

@With(Security.class)
public class Application extends Controller {

	// TODO: doresit
	@Before(unless={"login", "authenticate"})
	static void checkAuthenticated() {
	    if (session.contains("user")) {
	    	String openId = session.get("user");
	    	LocalUser user = LocalUser.find("byOpenId", openId).first();
	    	renderArgs.put("user", user);
	    }
	}    
	
	public static void index() {
        render();
    }
	

}