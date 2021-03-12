package TestFramework;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;





public class Testbase {

	public static WebDriver driver;
	public static WebDriverWait wait;
    public static String browser;
    public static Actions actions;

    @BeforeSuite
	public void setUp() throws Exception {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
		wait = new WebDriverWait(driver,120);
	}
   
	public void navigateTo(String user) {
		System.out.println("https://twitter.com/"+user);
		driver.get("https://twitter.com/"+user);
	}
	
	public void tearDown() throws Exception {
		try {
		        
			//Quit all drivers
			if(driver!=null) {
				driver.quit();			
			}
		}catch(Exception e) {
			throw e;
		}
	}
	
}		   
	