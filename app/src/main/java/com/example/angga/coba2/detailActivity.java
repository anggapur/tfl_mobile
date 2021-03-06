package com.example.angga.coba2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class detailActivity extends AppCompatActivity {

    ImageView imgv;
    boolean zoomOut;
    RequestQueue queue, queueData;
    String url, data_title, rupiah, data_poster_path, server_url, urls,data_desc;
    TextView textv, titlev, hargav, stockv, terjual, dilihat;
    LinearLayout layout;
    Button btn_beli;
    PopupWindow popUpWindow;
    String data_overview, data_harga, data_stock, data_id;
    SwipeRefreshLayout refreshSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent callIntent = getIntent();
        data_id = callIntent.getStringExtra("data_id");
        data_title = callIntent.getStringExtra("data_title");
        data_harga = callIntent.getStringExtra("data_harga");
        data_poster_path  = callIntent.getStringExtra("data_poster_path");
        data_desc = callIntent.getStringExtra("data_overview");

        imgv = (ImageView) findViewById(R.id.imgv);
        textv = (TextView) findViewById(R.id.textv);
        titlev = (TextView) findViewById(R.id.titlev);
        hargav = (TextView) findViewById(R.id.hargav);
        stockv = (TextView) findViewById(R.id.stock);
        terjual = (TextView) findViewById(R.id.terjual);
        dilihat = (TextView) findViewById(R.id.dilihat);

        titlev.setText(data_title);
        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
        rupiah = rupiahFormat.format(Double.parseDouble(data_harga));
        hargav.setText("Rp" + rupiah);
        Glide.with(detailActivity.this).load(getString(R.string.api_img)+data_poster_path).into(imgv);
        textv.setText(data_overview);

        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(detailActivity.this, detailImageActivity.class);
                a.putExtra("imgsrc", data_poster_path);

                a.putExtra("title", data_title);
                a.putExtra("harga", "Rp" + rupiah);
                a.putExtra("poster_path",data_poster_path);
                startActivity(a);
            }
        });
        //back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //api
        queue = Volley.newRequestQueue(this);
        url = getString(R.string.api) + "list_img.php?id=" + data_id;
        layout = (LinearLayout) findViewById(R.id.lineimage);
        //callApi();
        queueData = Volley.newRequestQueue(this);
        urls = getString(R.string.api)+"products/"+ data_id;
        //callApiData();

        callApi();
        callApiData();


        //button beli
        server_url = getString(R.string.api) + "beli.php";
        btn_beli = (Button) findViewById(R.id.btn_beli);
        btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comon.beli(server_url, data_id, view, detailActivity.this);
            }
        });

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
                        callApiData();
                        refreshSwipe.setRefreshing(false);
                    }
                }, 3000);

            }
        });
    }

    public void callApiData() {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject respon = new JSONObject(response);
//                            JSONArray result = (JSONArray) respon.get("results");


                            JSONObject item = (JSONObject) respon.get("results");
                            //Toast.makeText(detailActivity.this,item.toString(),Toast.LENGTH_LONG).show();
                            textv.setText(item.get("desc").toString());
                            titlev.setText(item.get("name").toString());
                            NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
                            rupiah = rupiahFormat.format(Double.parseDouble(item.get("price_sell").toString()));
                            hargav.setText("Rp" + rupiah);
                            stockv.setText(item.get("stock").toString());
                            terjual.setText(item.get("stock").toString());
                            dilihat.setText(item.get("seen_count").toString());
                            data_title = item.get("name").toString();
                            getSupportActionBar().setTitle(data_title);
                            Glide.with(detailActivity.this).load(getString(R.string.api_img) + item.get("featured_image").toString()).into(imgv);
                            Log.v("image",getString(R.string.api_img) + item.get("featured_image").toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
                Toast.makeText(detailActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
        queueData.add(stringRequest);
    }

    public void callApi() {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject respon = new JSONObject(response);
                            JSONObject results = (JSONObject) respon.get("results");
                            JSONArray result = (JSONArray) results.get("image");
                            //Toast.makeText(detailActivity.this,result.toString(),Toast.LENGTH_LONG).show();
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject item = (JSONObject) result.get(i);
                                HashMap<String, String> temp = new HashMap<>();
                                final PhotoView image = new PhotoView(detailActivity.this);
                                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(400, 300));
                                image.setMaxHeight(400);
                                image.setMaxWidth(400);
                                image.setPaddingRelative(0, 0, 5, 0);

                                //image.setBackgroundResource(R.drawable.border_all);
                                image.setImageResource(R.drawable.idul);
                                image.setTag(item.get("image").toString());
                                Picasso.with(detailActivity.this)
                                        .load(getString(R.string.api_img)+ item.get("image").toString())
                                        .into(image);

                                image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        /** Initiate Popup view **/
                                        Intent a = new Intent(detailActivity.this, detailImageActivity.class);
                                        a.putExtra("imgsrc", image.getTag().toString());

                                        a.putExtra("title", data_title);
                                        a.putExtra("harga", "Rp" + rupiah);
                                        a.putExtra("poster_path", image.getTag().toString());
                                        startActivity(a);
                                        //Toast.makeText(detailActivity.this,image.getTag().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                                // Adds the view to the layout
                                layout.addView(image);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
               // Toast.makeText(detailActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
