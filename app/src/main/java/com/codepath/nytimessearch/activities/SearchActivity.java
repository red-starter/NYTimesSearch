package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.codepath.nytimessearch.Article;
import com.codepath.nytimessearch.ArticleArrayAdapter;
import com.codepath.nytimessearch.EndlessScrollListener;
import com.codepath.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {
    EditText etQuery;
    Button btnSearch;
    GridView gvResults;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    private final int REQUEST_CODE = 200;
    String sort;
    String beginDate;
    String newsDesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
                customLoadMoreDataFromApi(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
                return true; // ONLY if more data is actually being loaded; false otherwise.
            }
        });
    }

    public void setupViews(){
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);

        //hookup listener for grid click
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create an intent for displaying the article
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);
                // pass in that article into intent
                Article article = articles.get(position);
                intent.putExtra("article", article);
                //launch the activity
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivityForResult(i, REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void customLoadMoreDataFromApi(int page){
        fetchArticles(page);
    }
    public void onArticleSearch(View view){
        adapter.clear();
        fetchArticles(0);
    }

    public void fetchArticles(int page) {
        String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key","935950ac35df4cbb80c1eb1091ffc8ad");
        params.put("page", page);
        if (!TextUtils.isEmpty(sort)){
            params.put("sort",sort);
        }
        if (!TextUtils.isEmpty(beginDate)){
            params.put("begin_date", beginDate);
        }
        if (!TextUtils.isEmpty(newsDesk)){
            params.put("fq","news_desk:(" +  newsDesk + ")");
        }
        params.put("q",query);
        Log.d("DEBUG","params: "+params.toString());
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG", "response: " + responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJSONResults;
                try {
                    articleJSONResults = response.getJSONObject("response").getJSONArray("docs");
                    //saves us adding to the article and then using notifydatasetchanged
                    adapter.addAll(Article.fromJSONArray(articleJSONResults));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //Toast.makeText(this, query, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            beginDate = data.getExtras().getString("begin_date");
            sort = data.getExtras().getString("sort");
            newsDesk = data.getExtras().getString("newsDesk");
            // Toast the name to display temporarily on screen
            Toast.makeText(this, beginDate +" "+sort+" "+newsDesk, Toast.LENGTH_SHORT).show();
        }
    }
}
