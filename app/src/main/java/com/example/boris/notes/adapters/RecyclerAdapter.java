package com.example.boris.notes.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.boris.notes.AddNote;
import com.example.boris.notes.MainActivity;
import com.example.boris.notes.R;
import com.example.boris.notes.models.NoteItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private List<NoteItem> noteItems;
    private MainActivity mainActivity;
    private boolean isLoadingAdded = false;

    public RecyclerAdapter(List<NoteItem> noteItems, MainActivity mainActivity) {
        this.noteItems = noteItems;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        //if (!isLoadingAdded){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_item, viewGroup, false);
        /*}else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_item_load, viewGroup, false);
        }*/

        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int i) {

        NoteItem item = noteItems.get(i);
        //if (!isLoadingAdded){
            //setting date
            Date date = new Date();
            date.setTime(item.getDate());
            SimpleDateFormat sdf =  new SimpleDateFormat("yyyy.MM.dd");//  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            holder.date.setText(sdf.format(date));

            //setting body
            if (item.getBody().length() > 50){
                holder.preview.setText(item.getBody().substring(0, 50) + "...");
            }else{
                holder.preview.setText(item.getBody());
            }

            //setting time
            sdf =  new SimpleDateFormat("HH:mm");//  new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            holder.time.setText(sdf.format(date));
        //}
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

                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(mainActivity, AddNote.class);
                    intent.putExtra("date", noteItems.get(getAdapterPosition()).getDate());
                    intent.putExtra("text", noteItems.get(getAdapterPosition()).getBody());

                    mainActivity.startActivity(intent);
                    mainActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                });
            }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        noteItems.add(new NoteItem(-1, ""));
        notifyItemInserted(noteItems.size() - 1);
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = noteItems.size() - 1;
        NoteItem result = noteItems.get(position);

        if (result != null) {
            noteItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateList(List<NoteItem> noteItems){
        this.noteItems = noteItems;
        notifyDataSetChanged();
    }
}
