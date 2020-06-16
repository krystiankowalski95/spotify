package pl.lodz.p.it.zzpj.spotify;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class AppSeleniumTest {
    private WebDriver driver;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    public void setUp() throws Exception {
        FirefoxOptions options = new FirefoxOptions();
        options.setAcceptInsecureCerts(true);
        driver = new FirefoxDriver(options);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void AppSeleniumTestCase() throws Exception {
        InputStream inputStream = this.getClass().getResourceAsStream("/selenium.config.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        driver.get("http://localhost:8080/login");
        Thread.sleep(5000);
        driver.findElement(By.id("login-username")).click();
        driver.findElement(By.id("login-username")).clear();
        driver.findElement(By.id("login-username")).sendKeys(properties.getProperty("spotifyLogin"));
        driver.findElement(By.xpath("//html[@id='app']/body/div/div[2]/div/form/div[3]/div/div/label")).click();
        driver.findElement(By.id("login-password")).click();
        driver.findElement(By.id("login-password")).clear();
        driver.findElement(By.id("login-password")).sendKeys(properties.getProperty("spotifyPassword"));
        driver.findElement(By.id("login-button")).click();
        assertEquals("User Playlists", driver.findElement(By.linkText("User Playlists")).getText());


        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("killer");
        driver.findElement(By.xpath("//input[@value='Search for tracks']")).click();
        assertEquals("Queen", driver.findElement(By.xpath("//td")).getText());
        assertEquals("Killer Queen - 2011 Mix", driver.findElement(By.xpath("//td[2]")).getText());
        driver.findElement(By.linkText("Go back")).click();
        driver.findElement(By.linkText("User Details")).click();
        assertEquals(properties.getProperty("spotifyLogin"), driver.findElement(By.xpath("//h3[2]/span")).getText());
        driver.findElement(By.linkText("Go back")).click();
        driver.findElement(By.linkText("User Playlists")).click();


        driver.findElement(By.name("submit")).click();
        driver.findElement(By.xpath("(//button[@name='submit'])[2]")).click();

        driver.findElement(By.name("name")).click();
        driver.findElement(By.name("name")).clear();
        driver.findElement(By.name("name")).sendKeys("selenium");
        driver.findElement(By.xpath("//input[@value='Add new empty playlist with given name']")).click();
        assertEquals("selenium", driver.findElement(By.xpath("//td[2]")).getText());
        assertEquals("0", driver.findElement(By.xpath("//td[4]")).getText());
        driver.findElement(By.name("submit")).click();

        assertEquals(properties.getProperty("playlistName"), driver.findElement(By.xpath("//td[2]")).getText());
        assertEquals(properties.getProperty("songsNumber"), driver.findElement(By.xpath("//td[4]")).getText());
        driver.findElement(By.name("submit")).click();
        driver.findElement(By.xpath("//tr[2]/td[2]")).click();
        driver.findElement(By.xpath("//td[2]")).click();
        driver.findElement(By.xpath("(//button[@name='submit'])[2]")).click();

        driver.findElement(By.linkText("Go back")).click();
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

}
