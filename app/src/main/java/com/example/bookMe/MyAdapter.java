package com.example.bookMe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    ArrayList<Bookings> list;

    // add arraylist of bookings
    public MyAdapter(Context context, ArrayList<Bookings> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set the text holders with the individual results from the bookings list
        Bookings bookings = list.get(position);
        holder.year.setText(String.valueOf(bookings.getYear()));
        holder.month.setText(String.valueOf(bookings.getMonth()));
        holder.dayOfMonth.setText(String.valueOf(bookings.getDayOfMonth()));
        holder.hourOfDay.setText(String.valueOf(bookings.getHourOfDay()));
        holder.minute.setText(String.valueOf(bookings.getMinute()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView year, month, dayOfMonth, hourOfDay, minute;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //find the textviews of where to place the data
            year = itemView.findViewById(R.id.year);
            month = itemView.findViewById(R.id.month);
            dayOfMonth = itemView.findViewById(R.id.day);
            hourOfDay = itemView.findViewById(R.id.hour);
            minute = itemView.findViewById(R.id.minute);


        }
    }

}
