package com.example.angga.coba2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class pilihJalur extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private TextView mTextMessage;
    private SliderLayout mDemoSlider;
    String urls;
    RequestQueue queueSlider;
    HashMap<String,String> url_maps = new HashMap<String, String>();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_login:
                    Intent i = new Intent(pilihJalur.this,jalurLogin.class);
                    startActivity(i);
                    //mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_daftar:
                    //mTextMessage.setText(R.string.title_daftar);
                    return true;
            }
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_jalur);

       // mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //slider
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);


//        url_maps.put("Slider1", getString(R.string.api)+"sliders/slider1.jpg");
//        url_maps.put("Slider2", getString(R.string.api)+"sliders/slider2.jpg");
//        url_maps.put("Slider3", getString(R.string.api)+"sliders/slider3.jpg");
//        url_maps.put("Slider4", getString(R.string.api)+"sliders/slider4.jpg");

        // Request a string response from the provided URL.
        urls = getString(R.string.api)+"slider.php";
        queueSlider = Volley.newRequestQueue(this);
        callSlider();

        getSupportActionBar().setTitle(getString(R.string.keuntungan_mendaftar));
        //back button
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
//        getSupportActionBar().setElevation(0);
    }

    public void callSlider(){
        // Request a string response from the provided URL.
        StringRequest sliderRequest = new StringRequest(Request.Method.GET, urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONObject respon =new JSONObject(response);
                            JSONArray result= (JSONArray)respon.get("results");

                            for (int i=0;i<result.length();i++){
                                JSONObject item= (JSONObject)result.get(i);

                                url_maps.put(item.get("caption").toString(),getString(R.string.api)+"sliders/"+item.get("img").toString());
                                Log.v("Hasil",getString(R.string.api)+"sliders/"+item.get("img").toString());

                            }
                            for(String name : url_maps.keySet()){
                                DefaultSliderView textSliderView = new DefaultSliderView(pilihJalur.this);
                                // initialize a SliderLayout
                                textSliderView
                                        //.description(name)
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit);
                                //.setOnSliderClickListener(MainActivity.this);

                                //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);

                                mDemoSlider.addSlider(textSliderView);
                            }
                            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                            mDemoSlider.setDuration(4000);
                            mDemoSlider.addOnPageChangeListener(pilihJalur.this);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
                Toast.makeText(pilihJalur.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queueSlider.add(sliderRequest);
    }

    //slider
    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
