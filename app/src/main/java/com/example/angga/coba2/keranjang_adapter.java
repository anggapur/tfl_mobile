package com.example.angga.coba2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by ANGGA on 6/22/2017.
 */

public class keranjang_adapter extends RecyclerView.Adapter<keranjang_adapter.ViewHolder>{
    private ArrayList<HashMap<String,String>> mData= new ArrayList<>();
    private LayoutInflater inflater;
    private ItemClickListener mClickListener;
    private Context context;
    public keranjang_adapter(Context context, ArrayList<HashMap<String,String>> data) {
        this.mData = data;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.keranjang_row,parent,false);
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
        String rupiah_tot = rupiahFormat.format(Double.parseDouble(row.get("totalHarga")));
        holder.harga_tot.setText("Rp"+rupiah_tot);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleTextView,subtitleTextView, harga_tot;
        public ImageView movieImage;
        public ViewHolder(View itemView){
            super(itemView);
            titleTextView= (TextView)itemView.findViewById(R.id.txt_title);
            subtitleTextView= (TextView)itemView.findViewById(R.id.txt_subtitle);
            movieImage = (ImageView)itemView.findViewById(R.id.img_movie);
            harga_tot = (TextView)itemView.findViewById(R.id.txt_harga_tot);
            itemView.setOnClickListener(this);
            movieImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,detailActivity.class);
                    i.putExtra("data_id",mData.get(getAdapterPosition()).get("id"));
                    i.putExtra("data_title",mData.get(getAdapterPosition()).get("title"));
                    i.putExtra("data_overview",mData.get(getAdapterPosition()).get("overview"));
                    i.putExtra("data_poster_path",mData.get(getAdapterPosition()).get("poster_path"));
                    i.putExtra("data_harga",mData.get(getAdapterPosition()).get("harga"));
                    i.putExtra("data_stock",mData.get(getAdapterPosition()).get("stock"));
                    view.getContext().startActivity(i);
                }
            });
            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,detailActivity.class);
                    i.putExtra("data_id",mData.get(getAdapterPosition()).get("id"));
                    i.putExtra("data_title",mData.get(getAdapterPosition()).get("title"));
                    i.putExtra("data_overview",mData.get(getAdapterPosition()).get("overview"));
                    i.putExtra("data_poster_path",mData.get(getAdapterPosition()).get("poster_path"));
                    i.putExtra("data_harga",mData.get(getAdapterPosition()).get("harga"));
                    i.putExtra("data_stock",mData.get(getAdapterPosition()).get("stock"));
                    view.getContext().startActivity(i);
                }
            });
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
