package pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import TestFramework.ReusableMethods;
import TestFramework.Testbase;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import utitlities.CommonFunctions;



public class LandingPage extends Testbase {
	CommonFunctions CFS;
	
	@FindBy(how = How.XPATH, using = "//header[@role='banner']")
	private WebElement banner;
	
	@FindBy(how = How.XPATH, using = "//main[@role='main']//div[@data-testid='primaryColumn']")
	private WebElement primaryColumn;
	
	@FindBy(how = How.XPATH, using = "//main[@role='main']//div[@data-testid='sidebarColumn']")
	private WebElement sidebarColumn;
	
	@FindBy(how = How.XPATH, using = "//nav[@aria-label='Profile timelines']")
	private WebElement profiletimelines;
	
	
	public LandingPage() {
		 PageFactory.initElements(driver, this);
		 CFS=new CommonFunctions();
	}
	
	
	public void takeLandingPageScreenshot(String fileWithPath) throws Exception {
		wait.until(ExpectedConditions.visibilityOfAllElements(Arrays.asList(banner,primaryColumn,sidebarColumn,profiletimelines)));
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//main[@role='main']//div[@data-testid='sidebarColumn']//div"), 100));
		wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.xpath("//main[@role='main']//div[@data-testid='primaryColumn']//div"), 100));
		CFS.takeSnapShot(fileWithPath);
		
	}
	
	public void gettweetdetails(String user,HashMap<String,Integer> HM) throws Exception {
		HashMap<String,String> tweetdata=new HashMap<String,String>();
		int i=1;
		for (Map.Entry<String,Integer> entry : HM.entrySet())  {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
            tweetdata=gettweetdata(entry.getKey());
            scrooltoTweetandVerify(user,tweetdata);
            i++;
            if(i>10)
            	break;
		}
	}
	public void getfrienddetails(String user,HashMap<String,Integer> HM) throws Exception {
		Object[] friendskeyset = HM.keySet().toArray();
		Object key = friendskeyset[new Random().nextInt(friendskeyset.length)];
		
			
		
		System.out.println("fwefw");
		
	}
	
	
	public void scrooltoTweetandVerify(String user,HashMap<String,String> tweet) throws Exception {
		String text=CFS.elaborateTwitterText(tweet.get("text"));
		if(text.length()>10) {
			text=text.substring(0, 10).replace(".", "");
			if(CFS.returnCount(By.xpath("//span[contains(text(),'"+text+"')]"))==0) {
				do {
					CFS.scrollDown();
					System.out.println("Scrolling down for tweet");
				}while(CFS.returnCount(By.xpath("//span[contains(text(),'"+text+"')]"))==0);
			}
			CFS.scrolltoWebElement(By.xpath("//span[contains(text(),'"+text+"')]"));
			System.out.println("Tweet ID : "+tweet.get("id")+" has been copied at "+user);
			CFS.takeSnapShot(System.getProperty("user.dir")+"\\src\\main\\java\\Screenshots\\Tweet\\"+user+"\\"+tweet.get("id")+ ".png");
			String UItweet=CFS.GetTextValue(By.xpath("//section[@aria-labelledby='accessible-list-0']//div[@data-testid='tweet']//span[contains(text(),'"+text+"')]"));
			String UIcounts=CFS.GetAttributeValue(By.xpath("//section[@aria-labelledby='accessible-list-0']//div[@data-testid='tweet']//span[contains(text(),'"+text+"')]//parent::div//parent::div//following-sibling::div[contains(@aria-label,'replies')]"), "aria-label");
			if(UItweet.contains(text))
				System.out.println("Text from UI : "+UItweet+" has matched with Text from API : "+text);
			else
				System.err.println("Text from UI : "+UItweet+" has'nt matched with Text from API : "+text);
			if(UIcounts.contains(tweet.get("favorite_count")))
				System.out.println("favourite from UI : "+UIcounts+" has matched with favourite from API : "+tweet.get("favorite_count"));
			else
				System.err.println("favourite from UI : "+UIcounts+" has'nt matched with favourite from API : "+tweet.get("favorite_count"));
			if(UIcounts.contains(tweet.get("retweet_count")))
				System.out.println("retweet_count from UI : "+UIcounts+" has matched with favourite from API : "+tweet.get("retweet_count"));
			else
				System.err.println("retweet_count from UI : "+UIcounts+" has'nt matched with favourite from API : "+tweet.get("retweet_count"));
			CFS.scrolltoTop();
		}
	}
	
	
	public HashMap<String,String> gettweetdata(String id) {
		HashMap<String,String> HM=new HashMap<String,String>();
		RestAssured.baseURI = "https://api.twitter.com";
		Response res=RestAssured.given().auth().oauth2("AAAAAAAAAAAAAAAAAAAAAALF%2FQAAAAAALRWNULyfgi76PsFaDBCHXeZVVKs%3DGX4NKmdsnJpXglnfoWaRxH8eN0AEFfMWGjCW73YBLrmzIHRiw8").
				param("id",id).
				when().
				get("/1.1/statuses/show.json").
				then().extract().response();
		JsonPath js= ReusableMethods.rawToJson(res);
		Integer retweet_count=js.get("retweet_count");
		String date=js.get("created_at");
		String text=js.getString("text");
	    String screen_name=js.getString("user.screen_name");
		String favorite_count=js.getString("favorite_count");
		HM.put("id", String.valueOf(id));
		HM.put("retweet_count", String.valueOf(retweet_count));
		HM.put("date", date);
		HM.put("text", text);
		HM.put("screen_name", screen_name);
		HM.put("favorite_count", favorite_count);
		return HM;
	}
	
	
}
