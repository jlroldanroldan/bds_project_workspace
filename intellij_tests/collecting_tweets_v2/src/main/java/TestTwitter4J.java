/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.mycompany.twitterdump;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Properties;

/**
 *
 * @author kamil
 */
public class TestTwitter4J {

    public static void main(String[] args) throws IOException
    {
        System.out.print("Testing 1");
        System.out.print(args[0]);
        String canonicalPath = new File(".").getCanonicalPath();
        System.out.print(canonicalPath );
        System.out.print("\nProperty:"+ System.getProperty("java.class.path"));
        System.out.print("\n ##### Output ###### \n " );


        try {

            // The factory instance is re-useable and thread safe.
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("Lyxd00oKyOJFZ4FxqiKkJUBwG")
                    .setOAuthConsumerSecret("RA8J3wYdj1XKB5NrZQ4ny5vhfs2YHIdgrPcDIP8RXrQsaK31yc")
                    .setOAuthAccessToken("4141536579-QYXCOhe4vw3ciWLkaqHYxeRk1OhHX3Z0MfBNbFs")
                    .setOAuthAccessTokenSecret("9VyHq0MIYAUE8taqStT9ZMiPfHbOdXGkfbps5oHJT2smK");
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();

            //List<Status> statuses = twitter.getHomeTimeline();
            // just testing

            //for (Status status: statuses) {
            //    System.out.println(status.getUser().getName() + ":" + status.getText());
            //}

            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            String strDate = dateFormat.format(date);
            // Key word search on tweets
            int i = 0;
            int len = args.length;
            String querytxt = args[0] ;
            Query query = new Query(querytxt); // Look for exact match syntax on twitter docs "(Mark Widmar) OR (FSLR)  OR (First Solar)"
            //query.setCount(10); //tweet count limit
            query.setLang( "en");
            QueryResult result = twitter.search(query);


//            FileWriter csvWriter = new FileWriter("D:/NYU/Semester 2/Big Data Science/Project Idea/Final Project/twitterdumps_output/datadump_firstsolar.csv",true);// change to relative path later

            FileWriter csvWriter = new FileWriter("/Users/Jroldan001/nyu/spring_2021/bds/bds_project_workspace/intellij_tests/collecting_tweets_v1/datadump_firstsolar.csv",true);// change to relative path later


            csvWriter.append("Rundate:"+ strDate+ "ScreenName");
            csvWriter.append(",");
            csvWriter.append("TweetText");
            csvWriter.append("\n");
            for (Status st : result.getTweets()) {
                csvWriter.append( st.getUser().getScreenName());
                csvWriter.append(",");
                //System.out.println(st.getText().replace("\n", "").replace("\r", "").replace(",", " "));
                csvWriter.append( st.getText().replace("\n", "").replace("\r", "").replace(",", " "));
                csvWriter.append("\n");
                System.out.println("Written");
            }//For ends
            csvWriter.flush();

            csvWriter.close();
            System.out.println("EndJob");



        } catch (TwitterException ex) {
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//main ends
}
