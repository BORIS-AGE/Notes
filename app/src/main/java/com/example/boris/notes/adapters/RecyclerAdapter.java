package com.example.boris.notes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.boris.notes.R;
import com.example.boris.notes.models.NoteItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    List<NoteItem> noteItems;
    Context context;

    public RecyclerAdapter(List<NoteItem> noteItems, Context context) {
        this.noteItems = noteItems;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_item, viewGroup, false);

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int i) {

        NoteItem item = noteItems.get(i);

        //setting date
        Date date = new Date();
        date.setTime(item.getDate());
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy.MM.dd");//  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        holder.date.setText(sdf.format(date));

        //setting body
        if (item.getBody().length() > 30)
        holder.preview.setText(item.getBody().substring(0, 30) + "...");

        //setting time
        sdf =  new SimpleDateFormat("HH:mm");//  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        holder.time.setText(sdf.format(date));
    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder{

        TextView date, time, preview;
        ImageButton button;

        public RecyclerHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.recyclerDate);
            time = itemView.findViewById(R.id.recyclerTime);
            preview = itemView.findViewById(R.id.recyclerPrev);
            button = itemView.findViewById(R.id.recyclerButton);
        }
    }
}
