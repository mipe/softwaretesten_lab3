package at.ticketline.dao;

import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

@RunWith(value = Parameterized.class)
public class ReservierungErstellenTest extends SeleneseTestCase {

    private int platz;
    private int reihe;

    public ReservierungErstellenTest(int platz, int reihe) {
        this.platz = platz;
        this.reihe = reihe;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { 
                { 4,1 }, { 5,1 }, { 6,1 }, { 7,1 },
                { 4,2 }, { 5,2 }, { 6,2 }, { 7,2 },
                { 1,3 }, { 2,3 }, { 3,3 }, { 4,3 },
                                            
        };
        return Arrays.asList(data);
    }

    @Before
    public void setUp() throws Exception {
        selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8080/tllight/");
        selenium.setSpeed("100");
        selenium.start();
    }

    @Test
    public void testReservierungErstellen() throws Exception {
        selenium.open("/tllight");
        selenium.click("veranstaltung_1");
        selenium.click("auffuehrung_1");
        selenium.click("platz_"+ reihe +"_" + platz);
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
