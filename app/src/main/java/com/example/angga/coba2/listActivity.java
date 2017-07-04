package com.example.angga.coba2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class listActivity extends AppCompatActivity implements  list_adapter_2.ItemClickListener{

    RequestQueue queue;
    String url;
    RecyclerView recyclerView;
    ArrayList<HashMap<String,String>> myArray;
    list_adapter_2 adapter;
    LinearLayout master;
    Toolbar  mActionBarToolbar;
    Button btn_beli;
    SwipeRefreshLayout refreshSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshSwipe = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                callApi();
                refreshSwipe.setRefreshing(false);
                Snackbar.make(getWindow().getDecorView().getRootView(), "Selesai", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });





        //intent
        Intent callIntent = getIntent();
        String data_query = callIntent.getStringExtra("data_query");
        String data_title = callIntent.getStringExtra("data_title");
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(data_title);
        //Toast.makeText(listActivity.this,data_query,Toast.LENGTH_SHORT).show();

        //deklatasi recycle view
        recyclerView= (RecyclerView)findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new GridLayoutManager(listActivity.this,2));
        queue = Volley.newRequestQueue(this);
        /*SELECTION*/
        if(data_query.matches("terbaru"))
            url = getString(R.string.api) + "api.php";
        else
            url = getString(R.string.api) + "data_by_cat.php?id=" + data_query;
        //Toast.makeText(listActivity.this,url,Toast.LENGTH_SHORT).show();
        callApi();

        //back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    //call api data
    public void callApi(){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject respon =new JSONObject(response);
                            JSONArray result= (JSONArray)respon.get("results");
                            myArray = new ArrayList<>();
                            for (int i=0;i<result.length();i++){
                                JSONObject item= (JSONObject)result.get(i);
                                HashMap<String,String> temp= new HashMap<>();
                                temp.put("id",item.get("id").toString());
                                temp.put("title",item.get("title").toString());
                                temp.put("overview",item.get("overview").toString());
                                temp.put("poster_path",item.get("poster_path").toString());
                                temp.put("harga",item.get("harga").toString());
                                temp.put("stock",item.get("stock").toString());
                                myArray.add(temp);
                            }
                            setAdapter();
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
                Toast.makeText(listActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public  void setAdapter(){
        adapter = new list_adapter_2(listActivity.this,myArray);
        adapter.setClickListener(listActivity.this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "Klik"+myArray.get(position), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(listActivity.this,detailActivity.class);
        i.putExtra("data_id",myArray.get(position).get("id"));
        i.putExtra("data_title",myArray.get(position).get("title"));
        i.putExtra("data_overview",myArray.get(position).get("overview"));
        i.putExtra("data_poster_path",myArray.get(position).get("poster_path"));
        i.putExtra("data_harga",myArray.get(position).get("harga"));
        i.putExtra("data_stock",myArray.get(position).get("stock"));
        startActivity(i);
    }

}
