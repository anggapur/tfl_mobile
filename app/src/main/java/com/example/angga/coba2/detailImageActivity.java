package com.example.angga.coba2;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class detailImageActivity extends AppCompatActivity {

    TextView titlev,hargav;
    PhotoView imgv;
    ImageView btn_download;

    // Progress Dialog
    private ProgressDialog pDialog;
    ImageView my_image;
    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    // File url to download


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        imgv = (PhotoView)findViewById(R.id.imgv);
        imgv.setImageResource(R.drawable.idul);

        //call intent and get data
        Intent callIntent = getIntent();
        String valueIntent = callIntent.getStringExtra("imgsrc");
        String poster_path = callIntent.getStringExtra("poster_path");
        String valueTitle = callIntent.getStringExtra("title");
        String harga = callIntent.getStringExtra("harga");
        //Toast.makeText(this, valueIntent+" "+valueTitle, Toast.LENGTH_SHORT).show();
        titlev = (TextView)findViewById(R.id.titlev);
        titlev.setText(valueTitle);
        hargav = (TextView)findViewById(R.id.hargav);
        hargav.setText(harga);
        final String file_url = getString(R.string.api)+"images/"+poster_path;
        //Toast.makeText(detailImageActivity.this,file_url,Toast.LENGTH_SHORT).show();
        //hide it bro
        getSupportActionBar().hide();

        //download
        btn_download = (ImageView)findViewById(R.id.btn_download);

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // starting new Async Task
                if (shouldAskPermissions()) {
                    askPermissions();
                }
                File folder = new File(Environment.getExternalStorageDirectory() +File.separator+"Download"+
                        File.separator + "TrashForLife");
                boolean success = true;
                if (!folder.exists()) {
                    success = folder.mkdirs();
                }
                if (success) {
                    //Toast.makeText(detailImageActivity.this,"folder created",Toast.LENGTH_LONG).show();
                } else {
                    // Do something else on failure
                }
                new DownloadFileFromURL().execute(file_url);
            }
        });

    }
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    /**
     * Showing Dialog
     * */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream
                Intent callIntent = getIntent();
                String poster_path = callIntent.getStringExtra("poster_path");
                OutputStream output = new FileOutputStream("/sdcard/Download/TrashForLife/"+poster_path);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            String imagePath = Environment.getExternalStorageDirectory().toString() + "/testing.jpg";
            Snackbar.make(getWindow().getDecorView().getRootView(), "Image Saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            // setting downloaded into image view
//            my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }

    }


}
