package at.ticketline.dao;

import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.regex.Pattern;

public class ReservierungErstellenTest extends SeleneseTestCase {
	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8080/tllight/");
		selenium.start();
	}

	@Test
	public void testReservierungErstellen() throws Exception {
		selenium.open("/tllight");
		selenium.click("veranstaltung_1");
		selenium.click("auffuehrung_1");
		selenium.click("platz_1_4");
		selenium.click("executeReservierung");
		verifyTrue(selenium.isTextPresent("Die Reservierung wurde erfolgreich durchgeführt"));
		selenium.click("link=Veranstaltungen");
		selenium.click("//li[5]/a");
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
