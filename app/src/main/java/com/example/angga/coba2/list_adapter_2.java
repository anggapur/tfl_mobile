package com.example.angga.coba2;


import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by ANGGA on 6/22/2017.
 */

public class list_adapter_2 extends RecyclerView.Adapter<list_adapter_2.ViewHolder>{
    private ArrayList<HashMap<String,String>> mData= new ArrayList<>();
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;
    private Context context;
    PopupWindow popUpWindow;
    public list_adapter_2(Context context, ArrayList<HashMap<String,String>> data) {
        this.mData = data;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.list_row_2,parent,false);
        ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String,String> row= mData.get(position);
        //TODO set holder
        holder.titleTextView.setText(row.get("title"));
        NumberFormat rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);
        String rupiah = rupiahFormat.format(Double.parseDouble(row.get("harga")));
        holder.subtitleTextView.setText("Rp"+rupiah);
        Glide.with(this.context).load("http://grab-ind.esy.es/api/images/"+row.get("poster_path")).into(holder.movieImage);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTextView,subtitleTextView;
        public ImageView movieImage;
        public Button btn_beli;
        public ViewHolder(View itemView){
            super(itemView);
            titleTextView= (TextView)itemView.findViewById(R.id.txt_title);
            subtitleTextView= (TextView)itemView.findViewById(R.id.txt_subtitle);
            movieImage = (ImageView)itemView.findViewById(R.id.img_movie);
            btn_beli = (Button)itemView.findViewById(R.id.btn_beli);
            btn_beli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //mData.get(getAdapterPosition()).get("id")mData.get(getAdapterPosition()).get("id")
                    //Toast.makeText(context,,Toast.LENGTH_SHORT).show();
                    String ids = mData.get(getAdapterPosition()).get("id");
                    Snackbar.make(view,"Berhasil Masuk Keranjang "+ids, Snackbar.LENGTH_LONG)
                            .setAction("Keranjang", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent a = new Intent(context, keranjangActivity.class);
                                    view.getContext().startActivity(a);
                                }
                            }).show();
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener!=null){
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public String getItem(int id){
        HashMap<String,String> item= mData.get(id);
        return item.get("title");
    }

    public  void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener=itemClickListener;
    }

    public interface ItemClickListener{
        void onItemClick(View view, int position);
    }
}
