package org.seamoo.webapp.integration.user;

import org.seamoo.webapp.integration.shared.AutoLogOutSeleneseTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

public class MatchPageIntegrationTest extends AutoLogOutSeleneseTest {

	private void loadFirstMatchPage() {
		// clear cookie on the open id provider page
		selenium.open("/leagues/2/sample-league");
		selenium.click("//div[@id='content']/div/div[3]/div[1]/table/tbody/tr[1]/td[2]/a[1]");
		selenium.waitForPageToLoad("30000");

	}

	@Test
	public void userShouldBeAskedToLoginBeforeViewingMatch() {
		loadFirstMatchPage();
		verifyEquals(selenium.getTitle(), "Đăng nhập với Open ID của bạn - SeaMoo");
	}

	@Test
	public void competitorCanViewMatchDetailsAfterLogin() throws InterruptedException {
		loadFirstMatchPage();
		loginAs("mrcold");
		verifyEquals(selenium.getTitle(), "Xem trận đấu - SeaMoo");
		verifyTrue(selenium.isTextPresent("Câu hỏi"));
		verifyTrue(selenium.isTextPresent("Câu trả lời đúng: yes"));
		verifyTrue(selenium.isTextPresent("Phuong Nguyen : yes"));
		verifyTrue(selenium.isTextPresent("Mr Cold : yes"));
	}
}
