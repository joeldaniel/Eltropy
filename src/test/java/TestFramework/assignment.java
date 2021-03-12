package TestFramework;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import pages.LandingPage;
import utitlities.CommonFunctions;


public class assignment extends Testbase{
	
	Testbase Tb;
	LandingPage LP;
	CommonFunctions CFS;
	@BeforeTest(alwaysRun = true)
	public void init() {
		Tb= new Testbase();
		LP=new LandingPage();
		CFS=new CommonFunctions();
	}
	
	public ArrayList<String> gettweets(String user) {
		RestAssured.baseURI = "https://api.twitter.com";
		Response res=RestAssured.given().auth().oauth2("AAAAAAAAAAAAAAAAAAAAAALF%2FQAAAAAALRWNULyfgi76PsFaDBCHXeZVVKs%3DGX4NKmdsnJpXglnfoWaRxH8eN0AEFfMWGjCW73YBLrmzIHRiw8").
				param("screen_name",user).
				param("count","10").
				param("include_rts","false").
				when().
				get("/1.1/statuses/user_timeline.json").
				then().extract().response();
		JsonPath js= ReusableMethods.rawToJson(res);
		   ArrayList<String> id=js.get("id");
		   System.out.println("Selected Tweet ID : "+id);
		   return id;
				
	}
	
	
	public HashMap<String,Integer> gettweetsinfo(String user) {
		//List<Long> ids=Arrays.asList(1353255109606711296L);
		HashMap<String,Integer> HM=new HashMap<String,Integer>();
		ArrayList<String> ids=gettweets(user);
		for(int i=0;i<ids.size();i++) {
			RestAssured.baseURI = "https://api.twitter.com";
			Response res=RestAssured.given().auth().oauth2("AAAAAAAAAAAAAAAAAAAAAALF%2FQAAAAAALRWNULyfgi76PsFaDBCHXeZVVKs%3DGX4NKmdsnJpXglnfoWaRxH8eN0AEFfMWGjCW73YBLrmzIHRiw8").
					param("id",ids.get(i)).
					when().
					get("/1.1/statuses/show.json").
					then().extract().response();
			JsonPath js= ReusableMethods.rawToJson(res);
			   Integer retweet_count=js.get("retweet_count");
			   HM.put(String.valueOf(ids.get(i)), retweet_count);
		}
		//System.out.println(sortByValue(HM));
		return sortByValue(HM);
		
	}
	
	
	
	public HashMap<String,Integer> getfriendsofUser(String user) throws IOException {
		HashMap<String,Integer> HM=new HashMap<String,Integer>();
		List<String> userslist=readuserlist();
		RestAssured.baseURI = "https://api.twitter.com";
		Response res=RestAssured.given().auth().oauth2("AAAAAAAAAAAAAAAAAAAAAALF%2FQAAAAAALRWNULyfgi76PsFaDBCHXeZVVKs%3DGX4NKmdsnJpXglnfoWaRxH8eN0AEFfMWGjCW73YBLrmzIHRiw8").
				param("screen_name",user).
				param("count","10").
				when().
				get("/1.1/friends/list.json").
				then().extract().response();
		JsonPath js= ReusableMethods.rawToJson(res);
		ArrayList<String> friends=js.get("users.screen_name");
		ArrayList<Integer> friendsfollowers=js.get("users.followers_count");
		for(int i=0;i<friends.size();i++) {
			if(!userslist.contains(friends.get(i)))
				HM.put(friends.get(i),friendsfollowers.get(i));
		}
		System.out.println("Top 10 Friends of user : "+user+" is "+sortByValue(HM) );
		return sortByValue(HM);	   
	}
	
	public int getVerifiedfriendsofUser(String user) throws IOException {
		HashMap<String,Integer> HM=new HashMap<String,Integer>();
		RestAssured.baseURI = "https://api.twitter.com";
		Response res=RestAssured.given().auth().oauth2("AAAAAAAAAAAAAAAAAAAAAALF%2FQAAAAAALRWNULyfgi76PsFaDBCHXeZVVKs%3DGX4NKmdsnJpXglnfoWaRxH8eN0AEFfMWGjCW73YBLrmzIHRiw8").
				param("screen_name",user).
				param("count","200").
				when().
				get("/1.1/friends/list.json").
				then().extract().response();
		JsonPath js= ReusableMethods.rawToJson(res);
		ArrayList<String> friends=js.get("users.verified");
		
		int count=Collections.frequency(friends, true);
		return count;	   
	}
	
	public HashMap<String,String> getuserdetail(String user) {
		HashMap<String,String> HM=new HashMap<String,String>();
		RestAssured.baseURI = "https://api.twitter.com";
		Response res=RestAssured.given().auth().oauth2("AAAAAAAAAAAAAAAAAAAAAALF%2FQAAAAAALRWNULyfgi76PsFaDBCHXeZVVKs%3DGX4NKmdsnJpXglnfoWaRxH8eN0AEFfMWGjCW73YBLrmzIHRiw8").
				param("screen_name",user).
				param("count","1").
				when().
				get("/1.1/statuses/user_timeline.json").
				then().extract().response();
		JsonPath js= ReusableMethods.rawToJson(res);
		ArrayList<String>  screenname=js.get("user.screen_name");
		ArrayList<String>  followerscount=js.get("user.followers_count");
		ArrayList<String>  friendscount=js.get("user.friends_count");
		   HM.put("screen_name", screenname.get(0));
		   HM.put("followers_count", String.valueOf(followerscount.get(0)));
		   HM.put("friends_count", String.valueOf(friendscount.get(0)));
		   return HM;
				
	}
	
	
	public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer> > list = 
               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return -(o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    } 
	
	
	public List<String> readuserlist() throws IOException {
		FileInputStream fis=new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\resources\\userlist.properties");
		Properties usersList = new Properties();
		usersList.load(fis);
		return Arrays.asList(usersList.getProperty("users").split(","));
		
	}
	
	
	@Test
	public void execution() throws Exception {
		List<String> userslist=readuserlist();
		HashMap<String,Integer> tweets=new HashMap<String,Integer>();
		HashMap<String,Integer> friends=new HashMap<String,Integer>();
		int count=0;
		for(String user:userslist) {
			Tb.navigateTo(user);
			System.out.println("Twitter Handle is : "+user);
			System.out.println("Twitter Handle URL is : "+ "https://twitter.com/"+user);
			LP.takeLandingPageScreenshot(System.getProperty("user.dir")+"\\src\\main\\java\\Screenshots\\LandingPage\\"+user+".png");
			tweets=gettweetsinfo(user);
			LP.gettweetdetails(user,tweets);
			friends=getfriendsofUser(user);
			count=getVerifiedfriendsofUser(user);
			
		}
		Tb.tearDown();
		
	}

	public void testing() {
		/*//String s="If you haven’t read Michelle’s memoir yet, I hope you do—especially if the young readers’ edition might resonate wi… https://t.co/rvzY55pNEl";
		String s="Can’t wait for @russellheather to bring the icing, the cake, and the whole damn bakery to Hollywood ߎߍ°ߧ#AmericanIdol";
		System.out.println(CFS.elaborateTwitterText(s));*/
		System.out.println(getuserdetail("BarackObama"));
	}

}
