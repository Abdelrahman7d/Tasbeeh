package com.jsla.tasbeeh;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class athkar_al_masaa_float_adapter extends RecyclerView.Adapter<athkar_al_masaa_float_adapter.thker_masaa_float_holder> {

    private ArrayList<Theker> athkar;
    private Context context;
    private athkar_al_masaa_float_adapter.OnCountBtnClickListener countBtnClickListener;
    private boolean isItMasaa;

    public ArrayList<Theker> getAthkar() {
        return athkar;
    }

    interface OnCountBtnClickListener {
        void onDeleteItem(athkar_al_masaa_float_adapter athkarAlMasaaFloatAdapter, int position);
    }

    public athkar_al_masaa_float_adapter(ArrayList<Theker> athkar, Context context, OnCountBtnClickListener countBtnClickListener, boolean isItMasaa) {
        this.athkar = athkar;
        this.context = context;
        this.countBtnClickListener = countBtnClickListener;
        this.isItMasaa = isItMasaa;
    }

    @NonNull
    @Override
    public thker_masaa_float_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if (isItMasaa) {
            View view = LayoutInflater.from(context).inflate(R.layout.theker_masaa_float_holder, parent, false);
            return new thker_masaa_float_holder(view);
        } else {
           View view = LayoutInflater.from(context).inflate(R.layout.theker_sabaah_float_holder, parent, false);
           return new thker_masaa_float_holder(view);
       }
    }

    @Override
    public void onBindViewHolder(@NonNull athkar_al_masaa_float_adapter.thker_masaa_float_holder holder, int position) {
        holder.theker_txt_view.setText(athkar.get(position).getTheker());
        holder.count_btn.setText("" + athkar.get(position).getCounter());
    }


    @Override
    public int getItemCount() {
        return athkar.size();
    }

    public class thker_masaa_float_holder extends RecyclerView.ViewHolder {

        private TextView theker_txt_view;
        private Button count_btn;
        public thker_masaa_float_holder(@NonNull View itemView) {
            super(itemView);
            theker_txt_view = itemView.findViewById(R.id.theker_txt_view);
            count_btn = itemView.findViewById(R.id.count_btn);

            count_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if (athkar.get(getAdapterPosition()).getCounter() > 0) {
                            count_btn.setText("" + (athkar.get(getAdapterPosition()).getCounter() - 1));
                            athkar.get(getAdapterPosition()).setCounter((athkar.get(getAdapterPosition()).getCounter() - 1));
                            if(athkar.get(getAdapterPosition()).getCounter() == 0) {
                                if (countBtnClickListener != null){
                                    countBtnClickListener.onDeleteItem(athkar_al_masaa_float_adapter.this,getAdapterPosition());
                                }
                            }
                        }
                    }catch (Exception exception){

                    }
                }
            });
        }
        }

    }

