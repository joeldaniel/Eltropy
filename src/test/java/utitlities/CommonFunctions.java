package utitlities;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.vdurmont.emoji.EmojiParser;

import TestFramework.Testbase;



public class CommonFunctions extends Testbase {
	
	
	public void takeSnapShot(String fileWithPath) throws Exception{

        //Convert web driver object to TakeScreenshot

        TakesScreenshot scrShot =((TakesScreenshot)driver);

        //Call getScreenshotAs method to create image file

                File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);

            //Move image file to new destination

                File DestFile=new File(fileWithPath);

                //Copy file at destination

                FileUtils.copyFile(SrcFile, DestFile);

    }
	public  void scrolltoWebElement(By locator) throws Exception {
		WebElement element = driver.findElement(locator);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		Thread.sleep(500);
	}
	
	public void ClickElement(By Locator) {
		if(driver.findElements(Locator).size()>0) {
			wait.until(ExpectedConditions.elementToBeClickable(Locator));
			driver.findElement(Locator).click();
		}
		else {
			assertTrue(false,"Element not found to click");
		}
	}
	

	public  void scrolltoTop() throws Exception {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		//Scroll to bottom
		js.executeScript("window.scrollTo(0, 0)");
	}

	
	public String GetAttributeValue(By Loc,String Attribute) {
		WebElement e=wait.until(ExpectedConditions.visibilityOfElementLocated(Loc));
		return e.getAttribute(Attribute);
	}   
	public String GetTextValue(By Loc) {
		WebElement e=wait.until(ExpectedConditions.visibilityOfElementLocated(Loc));
		return e.getText();
	}
	public String removeUrl(String commentstr)
    {
        String urlPattern = "((...https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentstr);
        int i = 0;
        while (m.find()) {
            commentstr = commentstr.replaceAll(m.group(i),"").trim();
            i++;
        }
        return commentstr;
    }
	public  void scrollDown() throws Exception {
		/*JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("scroll(0, 250)");*/
		Actions actions = new Actions(driver);
		actions.sendKeys(Keys.PAGE_DOWN).build().perform();
	}
	public int returnCount(By Locator) {
 		if(driver.findElements(Locator).size()>0)
 			wait.until(ExpectedConditions.visibilityOfElementLocated(Locator));
		return driver.findElements(Locator).size();
	}
	public String parse(String tweetText) {
		 
	     // Search for URLs
	     if (!tweetText.isEmpty() && tweetText.contains("http:")) {
	         int indexOfHttp = tweetText.indexOf("http:");
	         int endPoint = (tweetText.indexOf(' ', indexOfHttp) != -1) ? tweetText.indexOf(' ', indexOfHttp) : tweetText.length();
	         String url = tweetText.substring(indexOfHttp, endPoint);
	         String targetUrlHtml=  "<a href='${url}' target='_blank'>${url}</a>";
	         tweetText = tweetText.replace(url,targetUrlHtml );
	     }
	 
	     String patternStr = "(?:\\s|\\A)[##]+([A-Za-z0-9-_]+)";
	     Pattern pattern = Pattern.compile(patternStr);
	     Matcher matcher = pattern.matcher(tweetText);
	     String result = "";
	 
	     // Search for Hashtags
	     while (matcher.find()) {
	         result = matcher.group();
	         result = result.replace(" ", "");
	         String search = result.replace("#", "");
	         String searchHTML="<a href='http://search.twitter.com/search?q=" + search + "'>" + result + "</a>";
	         tweetText = tweetText.replace(result,searchHTML);
	     }
	 
	     // Search for Users
	     patternStr = "(?:\\s|\\A)[@]+([A-Za-z0-9-_]+)";
	     pattern = Pattern.compile(patternStr);
	     matcher = pattern.matcher(tweetText);
	     while (matcher.find()) {
	         result = matcher.group();
	         result = result.replace(" ", "");
	         String rawName = result.replace("@", "");
	         String userHTML="<a href='http://twitter.com/${"+rawName+"}'>" + result + "</a>";
	         tweetText = tweetText.replace(result,userHTML);
	     }
	     return tweetText;
	 }
	
	public String elaborateTwitterText(String text) {
		String newText = text;
		for (String key : new String[]{"#", "@", "http://", "https://"}) {
		int findIndex = 0;
		int lastIndex = 0;
		while (findIndex != -1) {
		findIndex = text.indexOf(key, lastIndex);
		lastIndex = findIndex;
		if (findIndex != -1 && lastIndex < text.length()) {
		String tag = null;
		try {
		tag = text.substring(findIndex, text.indexOf(' ', lastIndex));
		} catch (StringIndexOutOfBoundsException e) {
		// No ' ' found so substringing till the end of the string
		tag = text.substring(findIndex);
		}
		switch (key) {
		case "#":
		newText = newText.replace(tag, "" );
		break;
		case "@":
		newText = newText.replace(tag, "" );
		break;
		default:
		newText = newText.replace(tag, "" );
		break;
		}
		}
		lastIndex++;
		}
		}
		newText=EmojiParser.removeAllEmojis(newText);
		newText=newText.replaceAll("[^\\x00-\\x7f&&][^']", "").trim();
		
		System.out.println("Elaborated text: "+newText);
		return newText;
		}
}
