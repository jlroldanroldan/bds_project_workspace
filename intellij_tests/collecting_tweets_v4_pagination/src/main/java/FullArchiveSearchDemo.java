import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * Sample code to demonstrate the use of the Full archive search endpoint
 * */
public class FullArchiveSearchDemo {

    private static String start_time = "2020-02-01T00:00:00.000Z";
    private static String end_time = "2020-03-01T02:00:00.000Z";
    private static String next_token = null;
    private static String bearerToken = System.getenv("BEARER_TOKEN");
    // To set your enviornment variables in your terminal run the following line:
    // export 'BEARER_TOKEN'='<your_bearer_token>'

    public static void main(String args[]) throws IOException, URISyntaxException, ParseException {
        if (null != bearerToken) {
            String response = search("$ENPH lang:en", bearerToken);
            System.out.println(response);

            save_response_to_csv(response);
            // request more pages if next_token is available
            while (next_token != null) {
                System.out.println("next_token= " + next_token);
                response = search("$ENPH lang:en", bearerToken);
                save_response_to_csv(response);
            }
            next_token = null;
            System.out.println("No token!!");
        } else {
            System.out.println("There was a problem getting your bearer token. Please make sure you set the BEARER_TOKEN environment variable");
        }
    }

    private static void save_response_to_csv(String response) throws ParseException, IOException, URISyntaxException {
        JSONParser parse = new JSONParser();
        JSONObject response_json = (JSONObject)parse.parse(response);
        JSONArray data = (JSONArray) response_json.get("data");
        JSONObject meta = (JSONObject) response_json.get("meta");
        System.out.println(data);
        System.out.println(meta);

        if (meta.containsKey("next_token")) {
            next_token = meta.get("next_token").toString();
        } else {
            next_token = null;
        }

        save_tweets_to_csv(data);
    }

    private static void save_tweets_to_csv(JSONArray data) throws IOException {
        FileWriter csvWriter = new FileWriter("/Users/Jroldan001/nyu/spring_2021/bds/bds_project_workspace/intellij_tests/collecting_tweets_v4_pagination/data_collected/enph_v4_pagination.csv",true);// change to relative path later
        csvWriter.append("CreatedAt");
        csvWriter.append(",");
        csvWriter.append("TweetId");
        csvWriter.append(",");
        csvWriter.append("TweetText");
        csvWriter.append("\n");
        for(int i=0;i<data.size();i++) {
            JSONObject tweet = (JSONObject)data.get(i);
//            System.out.println("Elements under results array");
            csvWriter.append(tweet.get("created_at").toString());
            csvWriter.append(",");
            csvWriter.append(tweet.get("id").toString());
            csvWriter.append(",");
            csvWriter.append(tweet.get("text").toString().replace("\n", "").replace("\r", "").replace(",", " "));
            csvWriter.append("\n");
        }
        csvWriter.flush();
        csvWriter.close();

    }

    /*
     * This method calls the full-archive search endpoint with a the search term passed to it as a query parameter
     * */
    private static String search(String searchString, String bearerToken) throws IOException, URISyntaxException {
        String searchResponse = null;

        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build())
                .build();

        URIBuilder uriBuilder = new URIBuilder("https://api.twitter.com/2/tweets/search/all");
        ArrayList<NameValuePair> queryParameters;
        queryParameters = new ArrayList<>();
        queryParameters.add(new BasicNameValuePair("query", searchString));
        queryParameters.add(new BasicNameValuePair("max_results", "25"));
        queryParameters.add(new BasicNameValuePair("tweet.fields", "created_at"));
//        queryParameters.add(new BasicNameValuePair("start_time", "2020-01-08T11:30:00.000Z"));
//        queryParameters.add(new BasicNameValuePair("end_time", "2020-02-08T11:30:00.000Z"));
        queryParameters.add(new BasicNameValuePair("start_time", start_time));
        queryParameters.add(new BasicNameValuePair("end_time", end_time));
        if (next_token != null){
            queryParameters.add(new BasicNameValuePair("next_token", next_token));
        }

//        queryParameters.add(new BasicNameValuePair("lang", "en"));
        uriBuilder.addParameters(queryParameters);

        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.setHeader("Authorization", String.format("Bearer %s", bearerToken));
        httpGet.setHeader("Content-Type", "application/json");

        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if (null != entity) {
            searchResponse = EntityUtils.toString(entity, "UTF-8");
        }
        return searchResponse;
    }

}