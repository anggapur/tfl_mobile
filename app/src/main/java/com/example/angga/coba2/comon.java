package com.example.angga.coba2;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ANGGA on 7/5/2017.
 */

public class comon {

    public static String jumlah_barang_keranjang = "B";
    public static void beli(String server_url, final String data_id, final View view, final Context context)
    {
        //kirim data
        // TODO Auto-generated method stub
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //muncul snackbar kalau berhasil
                        Snackbar.make(view,"Berhasil Masuk Keranjang", Snackbar.LENGTH_LONG)
                                .setAction("Keranjang", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent a = new Intent(context, keranjangActivity.class);
                                        view.getContext().startActivity(a);
                                    }
                                }).show();
                        //end snackbar
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error ...", Toast.LENGTH_LONG).show();
                error.printStackTrace();
                requestQueue.stop();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_barang",data_id);
                return params;
            }

        };
        requestQueue.add(stringRequest);
    }
    public static void hapus(final View view,final String data_id, final String title, final Context context,String server_url)
    {
        //kirim data
        // TODO Auto-generated method stub
        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        comon.cek_keranjang(response,context);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error ...", Toast.LENGTH_LONG).show();
                error.printStackTrace();
                requestQueue.stop();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_barang",data_id);
                return params;
            }

        };
        requestQueue.add(stringRequest);


        //Snackbar.make(view,title+" berhasil dihapus", Snackbar.LENGTH_LONG).show();

    }

    public static void cek_keranjang(String response,Context context)
    {
        if(response.length() > 1)
            response = response.substring(response.length() - 1);

        Log.v("Hslog",response);
        //Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
        if(response.matches("0"))
            keranjangActivity.tv.setVisibility(View.VISIBLE);
        else
            keranjangActivity.tv.setVisibility(View.GONE);
    }
}
