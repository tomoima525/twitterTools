package twitterTools;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;


public class SearchTweets {
	Twitter twitter;
	private static final int COUNT_MAX=200;
	private static final int RATE_LIMITED_STATUS_CODE=400;
	public SearchTweets(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret){

		Configuration configuration = new ConfigurationBuilder()
		.setOAuthAccessToken(accessToken)
		.setOAuthConsumerKey(consumerKey)
		.setOAuthAccessTokenSecret(accessTokenSecret)
		.setOAuthConsumerSecret(consumerSecret)
		.build();
		
		TwitterFactory factory = new TwitterFactory(configuration);
		twitter=factory.getInstance();

	}
	

	 
	 public List<Status> search(String word)throws TwitterException{
		 
			 Query query = new Query(word);
			 QueryResult result;
			 List<Status>tweets=new ArrayList<Status>();
			 
			 //query.setResultType(Query.RECENT);
			 query.setSince("2013-08-10");
			 //query.setUntil("2013-11-06");
			 query.setCount(100);
			 do{

				 result= twitter.search(query);
				 tweets.addAll(result.getTweets());
				 System.out.printf("count is %s\n", tweets.size());
			 }while((query=result.nextQuery())!=null);

			 return tweets;
		 
	 }
	 public List<Status> searchUserTweets(String user_name)throws TwitterException{
		 int page=1;

		 List<Status>tweets=null;
		// List<Status>tweets=new ArrayList<Status>();
		 User user= twitter.showUser(user_name);
		 int total=0;
		 long id=user.getId();
		 System.out.println("id is "+id);
		 while(true){
			 Paging paging=new Paging(page++,COUNT_MAX);
			 try{
				 if(tweets==null){
					 tweets=twitter.getUserTimeline(id,paging);
				 }else{
					 total=tweets.size();
					 tweets.addAll(twitter.getUserTimeline(id,paging));
				 }
				 System.out.println("page= "+page);
			 }catch(TwitterException e){
				 if(RATE_LIMITED_STATUS_CODE!=e.getStatusCode()){
					 e.printStackTrace();
					 continue;
				 }
				 e.printStackTrace();
				 break;
			 }
			 if(tweets.size()==total){
				 break;
			 }
			 
		 }
		// tweets=twitter.getUserTimeline(id);

		return tweets;
		 
	 }
	 
	
}
