package com.example.angga.coba2;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class keranjangActivity extends AppCompatActivity  implements keranjang_adapter.ItemClickListener{


    keranjang_adapter adapter;
    String url;
    ArrayList<HashMap<String,String>> myArray;
    RequestQueue queue;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshSwipe;
    public static LinearLayout tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        //textview kosong
        tv = (LinearLayout) findViewById(R.id.kosong);
        //deklatasi recycle view
        recyclerView= (RecyclerView)findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        queue = Volley.newRequestQueue(this);
        url = getString(R.string.api)+"keranjang.php?id=1";
        callApi();

        //set title bar
        getSupportActionBar().setTitle("Keranjang Belanja");

        Spinner dropdown = (Spinner)findViewById(R.id.spinnerJumlah);

        //back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //swipe to refresh
        refreshSwipe = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshSwipe.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        refreshSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callApi();
                        refreshSwipe.setRefreshing(false);
                    }
                }, 3000);

            }
        });

    }

    public void callApi(){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        comon.cek_keranjang(response,keranjangActivity.this);
                        try{
                            JSONObject respon =new JSONObject(response);
                            JSONArray result= (JSONArray)respon.get("results");
                            myArray = new ArrayList<>();
                            for (int i=0;i<result.length();i++){
                                JSONObject item= (JSONObject)result.get(i);
                                HashMap<String,String> temp= new HashMap<>();
                                temp.put("id",item.get("id_barang").toString());
                                temp.put("title",item.get("title").toString());
                                temp.put("overview",item.get("overview").toString());
                                temp.put("poster_path",item.get("poster_path").toString());
                                temp.put("harga",item.get("harga").toString());
                                temp.put("stock",item.get("stock").toString());
                                temp.put("totalHarga",item.get("totalHarga").toString());
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
                Toast.makeText(keranjangActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public  void setAdapter(){
        adapter = new keranjang_adapter(keranjangActivity.this,myArray);
        adapter.setClickListener(keranjangActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
//        Intent i = new Intent(keranjangActivity.this,detailActivity.class);
//        i.putExtra("data_id",myArray.get(position).get("id"));
//        i.putExtra("data_title",myArray.get(position).get("title"));
//        i.putExtra("data_overview",myArray.get(position).get("overview"));
//        i.putExtra("data_poster_path",myArray.get(position).get("poster_path"));
//        i.putExtra("data_harga",myArray.get(position).get("harga"));
//        i.putExtra("data_stock",myArray.get(position).get("stock"));
        //startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
