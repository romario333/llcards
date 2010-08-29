package helpers;

import java.util.Date;

import junit.framework.Assert;

public class AssertionHelper {

	public static void assertBetween(Date expected1, Date expected2, Date actual) {
		Assert.assertTrue(actual.equals(expected1) || actual.after(expected1));
		Assert.assertTrue(actual.equals(expected2) || actual.before(expected2));
	}
	
}
