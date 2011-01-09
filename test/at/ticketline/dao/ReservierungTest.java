package at.ticketline.dao;

import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestCase;

@RunWith(value = Parameterized.class)
public class ReservierungTest extends SeleneseTestCase {

    private int[] platz;
    private int[] reihe;
    private boolean erfolg;

    public ReservierungTest(int[] platz, int[] reihe, boolean erfolg) {
        this.platz = platz;
        this.reihe = reihe;
        this.erfolg = erfolg;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { new int[]{4,5,6}, new int[]{1,1,1}, true }, { new int[]{1}, new int[]{5},false}, { new int[]{2}, new int[]{1},false}, { new int[]{2}, new int[]{5},false }, 
                { new int[]{4}, new int[]{1},true }, { new int[]{5}, new int[]{1},true }, { new int[]{6}, new int[]{1},true }, { new int[]{7}, new int[]{1},true },
                { new int[]{4}, new int[]{2},true }, { new int[]{5}, new int[]{2},true }, { new int[]{6}, new int[]{2},true }, { new int[]{7}, new int[]{2},true },
                { new int[]{1}, new int[]{3},true }, { new int[]{2}, new int[]{3},true }, { new int[]{3}, new int[]{3},true }, { new int[]{5,6,7}, new int[]{3,3,3},true }
                                            
        };
        return Arrays.asList(data);
    }

    @Before
    public void setUp() throws Exception {
    	loadData("full.xml");
        selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8080/tllight");
        selenium.setSpeed("100");
        selenium.start();
    }

    @Test
    public void testReservierungErstellen() throws Exception {
        selenium.open("/tllight");
        selenium.click("veranstaltung_1");
        selenium.click("auffuehrung_1");
        if (erfolg) {
            for (int i=0; i<platz.length; i++) {
                selenium.click("platz_"+ reihe[i] +"_" + platz[i]);
            }
            
            selenium.click("executeReservierung");
            verifyTrue(selenium.isTextPresent("Die Reservierung wurde erfolgreich durchgeführt"));
            
            selenium.click("link=Veranstaltungen");
            selenium.click("veranstaltung_1");
            selenium.click("auffuehrung_1");
            
            for (int i=0; i<platz.length; i++) {
                verifyFalse(selenium.isElementPresent("platz_"+reihe[i]+"_" + platz[i]));
            }
            
            selenium.click("link=Veranstaltungen");
            selenium.click("//li[5]/a");
            
            // check if the reservation is really deleted

            selenium.click("link=Veranstaltungen");
            selenium.click("veranstaltung_1");
            selenium.click("auffuehrung_1");
            
            for (int i=0; i<platz.length; i++) {
                verifyTrue(selenium.isElementPresent("platz_"+reihe[i]+"_"+platz[i]));
            }
            
            
        } else {
            for (int i=0; i<platz.length; i++) {
                verifyFalse(selenium.isElementPresent("platz_"+reihe[i]+"_" + platz[i]));
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        selenium.stop();
    }
    
    protected void loadData(String datasetFileName) {
        try {
        	BasicDataSource dataSource = new BasicDataSource();
    		dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
    		dataSource.setUrl("jdbc:hsqldb:hsql://localhost/ticketline");
    		dataSource.setUsername("sa");
    		dataSource.setPassword("");
    		
            IDataSet dataSet = new XmlDataSet(new FileReader(datasetFileName));
            IDatabaseConnection db = new DatabaseConnection(dataSource.getConnection());
            DatabaseOperation.CLEAN_INSERT.execute(db, dataSet);
            DatabaseOperation.REFRESH.execute(db, dataSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
