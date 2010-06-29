package org.seamoo.webapp.integration.user;

import org.seamoo.webapp.integration.shared.AutoLogOutSeleneseTest;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.Selenium;

public class UserPageIntegrationTest extends AutoLogOutSeleneseTest {

	@Test
	public void userPageDisplayUserDetails() {
		selenium.open("/users/4/mrcold");
		verifyEquals(selenium.getTitle(), "View Profile - Mr Cold - SeaMoo");

	}

	@Test
	public void viewOtherUserPageDoesNotGiveEditAccess() {
		selenium.open("/users/login");
		selenium.waitForPageToLoad("30000");
		loginAs("phuongnd08");
		selenium.open("/users/4/mrcold");
		assertFalse(selenium.isElementPresent("link=sửa"));
	}

	@Test
	public void viewOwnUserPageGiveEditAccess() {
		selenium.open("/users/login");
		selenium.waitForPageToLoad("30000");
		loginAs("mrcold");
		selenium.open("/users/4/mrcold");
		assertTrue(selenium.isElementPresent("link=sửa"));

		selenium.click("link=sửa");
		selenium.waitForPageToLoad("30000");
		selenium.type("email", "mrcold@seamoo.com");
		selenium.type("website", "http://mrcold.blogspot.com");
		selenium.type("aboutMe", "I'm a guy of destiny");
		selenium.type("quote", "Luck is everything");
		selenium.click("submit-button");
		selenium.waitForPageToLoad("30000");

		verifyTrue(selenium.isTextPresent("Mr Cold\n Luck is everything"));
		assertTrue(selenium.isElementPresent("link=exact:http://mrcold.blogspot.com"));
		assertEquals(selenium.getText("user-about-me"), "I'm a guy of destiny");
		assertTrue(selenium.isElementPresent("//img[contains(@src,'http://www.gravatar.com/avatar/c95d72236a89128cddc6e11473d968ff?s=128&d=wavatar&r=PG')]"));

		selenium.click("link=sửa");
		selenium.waitForPageToLoad("30000");
		selenium.type("email", "");
		selenium.type("website", "");
		selenium.type("aboutMe", "");
		selenium.type("quote", "");
		selenium.click("submit-button");
		selenium.waitForPageToLoad("30000");

		verifyFalse(selenium.isTextPresent("Mr Cold\n Luck is everything"));
		assertFalse(selenium.isElementPresent("link=exact:http://mrcold.blogspot.com"));
		assertNotEquals(selenium.getText("user-about-me"), "I'm a guy of destiny");
		assertTrue(selenium.isElementPresent("//img[contains(@src,'http://www.gravatar.com/avatar/d41d8cd98f00b204e9800998ecf8427e?s=128&d=wavatar&r=PG')]"));
	}
}
