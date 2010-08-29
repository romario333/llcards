package controllers;

import play.mvc.Before;
import play.mvc.Controller;
import models.LocalUser;

public class AuthenticationRequired extends Security {

	@Before
	static void checkAuthenticated() {
		LocalUser user = Security.getUser();
		if (user == null) {
	        Security.login();
	    }
	}
	
	
}
