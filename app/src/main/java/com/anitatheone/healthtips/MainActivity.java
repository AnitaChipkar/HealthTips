package com.anitatheone.healthtips;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Health> healthList = new ArrayList<Health>();
    ScrollView scrollView;
    private static Dialog progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        init();
    }

    public void init() {
        TableLayout tableLayout = findViewById(R.id.table_layout);
        prepareData();
        for (Health health : healthList) {
            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            RelativeLayout row = (RelativeLayout) inflater.inflate(R.layout.layout_table_view,null);
            tableLayout.addView(row);
            ImageView imgHealth = (ImageView) row.getChildAt(0);
            TextView textName = (TextView) row.getChildAt(1);
            textName.setText(health.getName());
            TextView textLikes = (TextView) row.getChildAt(3);
            textLikes.setText(health.getLike());
            TextView textViwes = (TextView) row.getChildAt(5);
            textViwes.setText(health.getViews());

        }
    }

    private void prepareData() {
        Health health = new Health("Health Benifit Of A Vegetarian Diet", "1", "1");
        healthList.add(health);
        health = new Health("Avoid Processed Junk Food", "2", "2");
        healthList.add(health);
        health = new Health("Get Enough Sleep", "3", "3");
        healthList.add(health);
        health = new Health("Take Care Of Your Gut Health With Fiber", "4", "4");
        healthList.add(health);
        health = new Health("Drink Some Water , Especially Before Meal", "5", "5");
        healthList.add(health);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_bar_menu, menu);

        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.app_bar_menu_search);

        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        searchView.setQueryHint(Html.fromHtml("<font color = #A9A9A9>" + getResources().getString(R.string.text_search_view) + "</font>"));

        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.WHITE);
        searchAutoComplete.setTextColor(getResources().getColor(R.color.searchHint));
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.holo_blue_light);

        // Create a new ArrayAdapter and add data to search auto complete object.
        String dataArr[] = {"Apple", "Amazon", "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
                Toast.makeText(MainActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setMessage("Search keyword is " + query);
                alertDialog.show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void callGetMovieListAPI() {
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=b7cd3340a794e5a2f35e3abb820b497f";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        MainActivity.hideProgressDialog();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray healthArray = obj.getJSONArray("results");
                            for (int i = 0; i < healthArray.length(); i++) {
                                JSONObject healthObject = healthArray.getJSONObject(i);
                                healthList.add(new Health(healthObject.getString("name"), healthObject.getString("like"),
                                        healthObject.getString("views")));
                                // newsModelList.add(newsModel);
                                //newsAdaptor.notifyDataSetChanged();
                                //fragment.setMovieArrayList(movieArrayList);
                                //fragment.setMovieArrayListOnUI();
                                VolleyLog.d("Data", response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Error: " + error.getMessage());
                //Hide Progress bar
                MainActivity.hideProgressDialog();
            }


        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);

        // add the request object to the queue to be executed
        // MovieApplication.getInstance().addToRequestQueue(jsonArrayRequest);
    }
    public static void showProgressDialog(Context context) {
        try {
            if (progressBar != null && progressBar.isShowing()) {
                //No need to show again.
                return;
            }
            hideProgressDialog();
            progressBar = new ProgressDialog(context);
            progressBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressBar.show();
            progressBar.setCancelable(false);
            progressBar.setContentView(R.layout.layout_progress_bar);
            ProgressBar progressBar = (ProgressBar) MainActivity.progressBar.findViewById(R.id.progress_bar);
            progressBar.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.progress_bar_color), android.graphics.PorterDuff.Mode.MULTIPLY);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide Progress bar
     */
    public static void hideProgressDialog() {
        try {
            if (progressBar != null && progressBar.isShowing()) {
                progressBar.dismiss();
                progressBar = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

