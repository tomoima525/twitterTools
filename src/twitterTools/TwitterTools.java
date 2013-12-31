package twitterTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;

public class TwitterTools {
	//自分のキーをせっていする
	private static final String consumerKey = "x";
	private static final String accessToken = "x";
	private static final String accessTokenSecret="x";
	private static final String consumerSecret="x";
	//private static final String searchword = "検索用";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SearchTweets st=new SearchTweets(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		PrintWriter pw = null;
		int count=0;
		
		String regex="\n";
		Pattern pattern=Pattern.compile(regex);
		DateFormat df=new SimpleDateFormat("yyyy/MM/dd");
		Date date;

		try{
			date = df.parse("2013/01/01");
			//出力ファイル
			File file = new File("/home/tomoaki/workspace/owntwit_2013.log");
			pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			
			//List<Status> searchResult=st.search(searchword);
			//タイムラインを検索したいユーザー名
			List<Status> searchResult=st.searchUserTweets("tomoaki_imai");
			//自分のホームタイムライン
			//List<Status> searchResult=st.searchMyTweets();
			//自分のメンション数
			//List<Status> searchResult=st.searchUserMentions();
			
			for(Status status:searchResult){
				if(status.getCreatedAt().after(date)==true){
				String regString=status.getText();
				Matcher matcher=pattern.matcher(regString);
				String resultString =matcher.replaceAll("<n>");
				//必要なものを取得
				//String str=status.getCreatedAt()+","+status.getInReplyToScreenName()+","+resultString+","+status.getRetweetCount();
				String str=status.getCreatedAt()+","+resultString+","+status.getRetweetCount();
				
				//String str=resultString;
				//System.out.printf("%s,%s,%s",status.getCreatedAt(), status.getText(),status.getRetweetCount(),str);	
				pw.println(str);
				count++;
				}
			}
		}catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch(TwitterException te){
			te.printStackTrace();
			System.out.println("Failed to show the list: " + te.getMessage());
			
		}catch(IOException e){
			System.out.println(e);
			
		}
		finally{
		 	pw.close();
		 	System.out.printf("%s tweets searched",count);
		}

	}

}
