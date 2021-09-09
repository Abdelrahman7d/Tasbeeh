package com.jsla.tasbeeh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class athkar_al_masaa_adapter extends RecyclerView.Adapter<athkar_al_masaa_adapter.thker_holder>  {

    private ArrayList<Theker> athkar;
    private Context context;
    private OnCountBtnClickListener countBtnClickListener ;

    interface OnCountBtnClickListener {
        void onClicked (int position);
        void onDeleteItem(athkar_al_masaa_adapter athkarAlMasaaAdapter,int position);
    }

    public athkar_al_masaa_adapter(ArrayList<Theker> athkar, Context context, OnCountBtnClickListener countBtnClickListener) {
        this.athkar = athkar;
        this.context = context;
        this.countBtnClickListener = countBtnClickListener;
    }

    @NonNull
    @Override
    public thker_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.theker_masaa_holder,parent,false);

        return new thker_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull athkar_al_masaa_adapter.thker_holder holder, int position) {
        holder.theker_txt_view.setText(athkar.get(position).getTheker());
        holder.count_btn.setText("" + athkar.get(position).getCounter());
    }

    @Override
    public int getItemCount() {
        return athkar.size();
    }

     class thker_holder extends RecyclerView.ViewHolder{

        ConstraintLayout constraintLayout;
        TextView theker_txt_view;
        Button count_btn;

        public thker_holder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.theker_cl);
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
                                    if(getAdapterPosition() != RecyclerView.NO_POSITION){
                                        countBtnClickListener.onDeleteItem(athkar_al_masaa_adapter.this,getAdapterPosition());
                                    }
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
