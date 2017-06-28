package com.example.angga.coba2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , list_adapter.ItemClickListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    list_adapter adapter;
    String url,urls;
    ArrayList<HashMap<String,String>> myArray;
    RequestQueue queue;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshSwipe;
    //slider
    private SliderLayout mDemoSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //deklatasi recycle view
        recyclerView= (RecyclerView)findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        queue = Volley.newRequestQueue(this);
        url =getString(R.string.api)+"api.php";
        callApi();

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

        //slider
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        final HashMap<String,String> url_maps = new HashMap<String, String>();
//        url_maps.put("Slider1", getString(R.string.api)+"sliders/slider1.jpg");
//        url_maps.put("Slider2", getString(R.string.api)+"sliders/slider2.jpg");
//        url_maps.put("Slider3", getString(R.string.api)+"sliders/slider3.jpg");
//        url_maps.put("Slider4", getString(R.string.api)+"sliders/slider4.jpg");

        // Request a string response from the provided URL.

        for(String name : url_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //.description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

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
        mDemoSlider.addOnPageChangeListener(this);
        ListView l = (ListView)findViewById(R.id.transformers);
        l.setAdapter(new TransformerAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDemoSlider.setPresetTransformer(((TextView) view).getText().toString());
                Toast.makeText(MainActivity.this, ((TextView) view).getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

   // @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_custom_indicator:
//                mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
//                break;
//            case R.id.action_custom_child_animation:
//                mDemoSlider.setCustomAnimation(new ChildAnimationExample());
//                break;
//            case R.id.action_restore_default:
//                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
//                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
//                break;
//            case R.id.action_github:
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/daimajia/AndroidImageSlider"));
//                startActivity(browserIntent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
    //end slider
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
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public  void setAdapter(){
        adapter = new list_adapter(MainActivity.this,myArray);
        adapter.setClickListener(MainActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "Klik"+myArray.get(position), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this,detailActivity.class);
        i.putExtra("data_id",myArray.get(position).get("id"));
        i.putExtra("data_title",myArray.get(position).get("title"));
        i.putExtra("data_overview",myArray.get(position).get("overview"));
        i.putExtra("data_poster_path",myArray.get(position).get("poster_path"));
        i.putExtra("data_harga",myArray.get(position).get("harga"));
        i.putExtra("data_stock",myArray.get(position).get("stock"));
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
