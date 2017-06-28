package com.example.angga.coba2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class detailActivity extends AppCompatActivity {

    ImageView imgv;
    boolean zoomOut;
    RequestQueue queue;
    String url,data_title,rupiah,data_poster_path;
    TextView textv,titlev,hargav,stockv,terjual,dilihat;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent callIntent = getIntent();
        String data_id = callIntent.getStringExtra("data_id");
        data_title = callIntent.getStringExtra("data_title");
        String data_overview = callIntent.getStringExtra("data_overview");
        data_poster_path = callIntent.getStringExtra("data_poster_path");
        String data_harga = callIntent.getStringExtra("data_harga");
        String data_stock = callIntent.getStringExtra("data_stock");

        imgv = (ImageView)findViewById(R.id.imgv);
        textv = (TextView)findViewById(R.id.textv);
        titlev = (TextView)findViewById(R.id.titlev);
        hargav = (TextView)findViewById(R.id.hargav);
        stockv = (TextView)findViewById(R.id.stock);
        terjual = (TextView)findViewById(R.id.terjual);
        dilihat = (TextView)findViewById(R.id.dilihat);
        getSupportActionBar().setTitle(data_title);
        //back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set Image
        Glide.with(detailActivity.this).load(getString(R.string.api)+"images/"+data_poster_path).into(imgv);
        textv.setText(data_overview);
        titlev.setText(data_title);
        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
        rupiah = rupiahFormat.format(Double.parseDouble(data_harga));
        hargav.setText("Rp"+rupiah);
        stockv.setText(data_stock);
        terjual.setText(data_stock);
        dilihat.setText(data_stock);


        //api
        queue = Volley.newRequestQueue(this);
        url = getString(R.string.api)+"list_img.php?id="+data_id;
        layout = (LinearLayout)findViewById(R.id.lineimage);
        callApi();
        //Toast.makeText(detailActivity.this,url,Toast.LENGTH_SHORT).show();
        //callApi();


//        for(int i=0;i<3;i++)
//        {
//
//
//            final PhotoView image = new PhotoView(this);
//            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(400,300));
//            image.setMaxHeight(400);
//            image.setMaxWidth(400);
//            image.setPaddingRelative(0,0,5,0);
//
//            //image.setBackgroundResource(R.drawable.border_all);
//            image.setImageResource(R.drawable.idul);
//            image.setTag("Ini :"+i);
//            image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    /** Initiate Popup view **/
//                    Intent a = new Intent(detailActivity.this, detailImageActivity.class);
//                    a.putExtra("imgsrc",image.getTag().toString());
//                    a.putExtra("title",data_title);
//                    a.putExtra("harga","Rp"+rupiah);
//                    a.putExtra("poster_path",data_poster_path);
//                    startActivity(a);
//                }
//            });
//
//            // Adds the view to the layout
//            layout.addView(image);
//
//
//        }

    }

    public void callApi(){
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject respon =new JSONObject(response);
                            JSONArray result= (JSONArray)respon.get("results");

                            for (int i=0;i<result.length();i++){
                                JSONObject item= (JSONObject)result.get(i);
                                HashMap<String,String> temp= new HashMap<>();
                                final PhotoView image = new PhotoView(detailActivity.this);
                                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(400,300));
                                image.setMaxHeight(400);
                                image.setMaxWidth(400);
                                image.setPaddingRelative(0,0,5,0);

                                //image.setBackgroundResource(R.drawable.border_all);
                                image.setImageResource(R.drawable.idul);
                                image.setTag(item.get("img").toString());
                                Picasso.with(detailActivity.this)
                                        .load(getString(R.string.api)+"images/"+item.get("img").toString())
                                        .into(image);

                                image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        /** Initiate Popup view **/
                                        Intent a = new Intent(detailActivity.this, detailImageActivity.class);
                                        a.putExtra("imgsrc",image.getTag().toString());

                                        a.putExtra("title",data_title);
                                        a.putExtra("harga","Rp"+rupiah);
                                        a.putExtra("poster_path",image.getTag().toString());
                                        startActivity(a);
                                        //Toast.makeText(detailActivity.this,image.getTag().toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                                // Adds the view to the layout
                                layout.addView(image);

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
                Toast.makeText(detailActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
