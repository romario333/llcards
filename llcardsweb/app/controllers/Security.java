package controllers;

import models.LocalUser;
import play.libs.OpenID;
import play.libs.OpenID.UserInfo;
import play.mvc.Before;
import play.mvc.Controller;

public class Security extends Controller {
	
	@Before
	static void setupSecurity() {
		LocalUser user = getUserFromSession();
		if (user != null) {
			renderArgs.put("user", user);
		}
	}
	
	public static void login() {
		render();
	}
	
	public static void authenticateWithGoogle() {
		authenticate("https://www.google.com/accounts/o8/id");
	}
	
	public static void authenticate(String user) {
	    if (!OpenID.isAuthenticationResponse()) {
	    	// TODO: ziskat vic udaju nez jen mail
	    	OpenID.id(user)
	    		.required("firstname", "http://axschema.org/namePerson/first")
	    		.required("lastname", "http://axschema.org/namePerson/last")
	    		.required("email", "http://axschema.org/contact/email").verify();
	    } else {
	        UserInfo verifiedUser = OpenID.getVerifiedID();
	        if(verifiedUser == null) {
	            flash.put("error", "Oops. Authentication has failed");
	            login();
	        }
	        
	        // TODO: dat uzivateli moznost zadat novy nick a zmenit mail adresu
	        LocalUser localUser = LocalUser.find("byOpenId", verifiedUser.id).first();
	        if (localUser == null)  {
	        	String nickname = verifiedUser.extensions.get("firstname") + " " 
	        		+ verifiedUser.extensions.get("lastname"); 
	        	String email = verifiedUser.extensions.get("email");
	        	localUser = new LocalUser(verifiedUser.id, nickname, email).save();
	        }
	        
	        session.put("user", localUser.openId);
	        String mail = verifiedUser.extensions.get("email");
	        Application.index();
	    }
	}
	
	public static void logout() {
		session.remove("user");
		Application.index();
	}
	
	public static LocalUser getUser() {
		LocalUser user = (LocalUser) renderArgs.get("user");
		
		if (user == null) {
			user = getUserFromSession();
		}
		
		return user;
	}

	
	private static LocalUser getUserFromSession() {
	    if (session.contains("user")) {
	    	String openId = session.get("user");
	    	return LocalUser.find("byOpenId", openId).first();
	    }
		return null;
	}
	
	

	
}
