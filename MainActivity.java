package jlfletcher.cpsc4367.ualr.edu.finalproject;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    Button bigpiney;
    Button buffalo;
    Button ouachita;
    Button white;
    Button spring;
    private final String TWIT_CONS_KEY = "QfAscRM49arDiAlYtiqvL5CWR";
    private final String TWIT_CONS_SEC_KEY = "0WmHA1qxw6y31YZJswxHzXu5vurqwFZvOdDTRCW4yyxwVPodJA";
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spring = (Button)findViewById(R.id.spring);
        white = (Button) findViewById(R.id.white);
        ouachita = (Button) findViewById(R.id.ouachita);
        bigpiney = (Button) findViewById(R.id.bigpiney);
        buffalo = (Button) findViewById(R.id.buffalo);
        list = (ListView) findViewById(R.id.list);

        spring.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new SearchOnTwitter().execute(spring.getText().toString());
            }
        });

        white.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new SearchOnTwitter().execute(white.getText().toString());
            }
        });

        ouachita.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new SearchOnTwitter().execute(ouachita.getText().toString());
            }
        });

        bigpiney.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new SearchOnTwitter().execute(bigpiney.getText().toString());
            }
        });

        buffalo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new SearchOnTwitter().execute(buffalo.getText().toString());
            }
        });
    }

    class SearchOnTwitter extends AsyncTask<String, Void, Integer> {
        ArrayList<Tweet> tweets;
        final int SUCCESS = 0;
        final int FAILURE = SUCCESS + 1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setApplicationOnlyAuthEnabled(true);
                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);

                OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();

                builder = new ConfigurationBuilder();
                builder.setApplicationOnlyAuthEnabled(true);
                builder.setOAuthConsumerKey(TWIT_CONS_KEY);
                builder.setOAuthConsumerSecret(TWIT_CONS_SEC_KEY);
                builder.setOAuth2TokenType(token.getTokenType());
                builder.setOAuth2AccessToken(token.getAccessToken());

                Twitter twitter = new TwitterFactory(builder.build()).getInstance();

                Query query = new Query(params[0]);
                // YOu can set the count of maximum records here
                query.setCount(50);
                QueryResult result;
                result = twitter.search(query);
                List<twitter4j.Status> tweets = result.getTweets();
                StringBuilder str = new StringBuilder();
                if (tweets != null) {
                    this.tweets = new ArrayList<Tweet>();
                    for (twitter4j.Status tweet : tweets) {
                        str.append("@" + tweet.getUser().getScreenName() + " - " + tweet.getText() + "\n");
                        System.out.println(str);
                        this.tweets.add(new Tweet("@" + tweet.getUser().getScreenName(), tweet.getText()));
                    }
                    return SUCCESS;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return FAILURE;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == SUCCESS) {
                list.setAdapter(new TweetAdapter(MainActivity.this, tweets));
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_LONG).show();
            }
        }
    }
}

