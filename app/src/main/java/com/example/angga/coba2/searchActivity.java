package com.example.angga.coba2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class searchActivity extends AppCompatActivity implements  list_adapter_2.ItemClickListener{

    RequestQueue queue;
    String url;
    RecyclerView recyclerView;
    ArrayList<HashMap<String,String>> myArray;
    list_adapter_2 adapter;
    LinearLayout master;
    Toolbar  mActionBarToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //intent
        Intent callIntent = getIntent();
//        String data_query = callIntent.getStringExtra("data_query");
//        String data_title = callIntent.getStringExtra("data_title");
        String data_title = "Pencarian";
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(data_title);
        //Toast.makeText(listActivity.this,data_query,Toast.LENGTH_SHORT).show();

        //deklatasi recycle view
        recyclerView= (RecyclerView)findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new GridLayoutManager(searchActivity.this,2));
        queue = Volley.newRequestQueue(this);
        /*SELECTION*/
        url = getString(R.string.api) + "api.php";

        callApi(url);

        //back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //search button
        final SearchView searchView = (SearchView)findViewById(R.id.search);
        searchView.setIconifiedByDefault(true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                url = getString(R.string.api)+"products/search/"+query;
                //url = getString(R.string.api) + "api.php";

                callApi(url);
                getSupportActionBar().setTitle(query);
                //Toast.makeText(searchActivity.this,url,Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(searchActivity.this,"new:"+newText,Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //call api data
    public void callApi(String url){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject respon =new JSONObject(response);
                            JSONObject results = (JSONObject)respon.get("results");
                            JSONArray result= (JSONArray)results.get("data");
                            //Toast.makeText(searchActivity.this,result.toString(),Toast.LENGTH_LONG).show();
                            myArray = new ArrayList<>();
                            for (int i=0;i<result.length();i++){
                                JSONObject item= (JSONObject)result.get(i);
                                HashMap<String,String> temp= new HashMap<>();
                                temp.put("id",item.get("id").toString());
                                temp.put("title",item.get("name").toString());
                                temp.put("overview",item.get("desc").toString());
                                temp.put("poster_path",item.get("featured_image").toString());
                                temp.put("harga",item.get("price_sell").toString());
                                temp.put("stock",item.get("stock").toString());
                                myArray.add(temp);
                            }

                            //Toast.makeText(searchActivity.this,"Tidak Ditemukan",Toast.LENGTH_SHORT).show();
                            setAdapter();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
                Toast.makeText(searchActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", "1234567890");
                params.put("Accept", "application/json");

                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public  void setAdapter(){
        adapter = new list_adapter_2(searchActivity.this,myArray);
        adapter.setClickListener(searchActivity.this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "Klik"+myArray.get(position), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(searchActivity.this,detailActivity.class);
        i.putExtra("data_id",myArray.get(position).get("id"));
        i.putExtra("data_title",myArray.get(position).get("title"));
        i.putExtra("data_overview",myArray.get(position).get("overview"));
        i.putExtra("data_poster_path",myArray.get(position).get("poster_path"));
        i.putExtra("data_harga",myArray.get(position).get("harga"));
        i.putExtra("data_stock",myArray.get(position).get("stock"));
        startActivity(i);
    }
}
