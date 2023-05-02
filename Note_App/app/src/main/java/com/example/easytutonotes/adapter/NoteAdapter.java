package com.example.easytutonotes.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytutonotes.R;
import com.example.easytutonotes.model.Note;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.HomeViewHolder> {

    private List<Note> list;
    private ItemListener itemListener;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public NoteAdapter() {
        list= new ArrayList<>();
    }

    public void setList(List<Note> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public Note getItem(int pos){
        return list.get(pos);
    }
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        Note item = list.get(position);
        holder.setNote(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Note> filterlist){
        list = filterlist;
        notifyDataSetChanged();
    }
    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CardView notes_container;
        private TextView tvTitle,tvDescription,tvTime;
        private ImageView imageView_pin;
        private RoundedImageView imageNote;
        private LinearLayout layoutWebURL;
        public HomeViewHolder(@NonNull View view) {
            super(view);
            notes_container = view.findViewById(R.id.notes_container);
            tvTitle = view.findViewById(R.id.titleoutput);
            tvDescription = view.findViewById(R.id.descriptionoutput);
            tvTime = view.findViewById(R.id.timeoutput);
            imageView_pin = view.findViewById(R.id.imageView_pin);
            imageNote = view.findViewById(R.id.imageNote);
            layoutWebURL = view.findViewById(R.id.layoutWebURL);
            view.setOnClickListener(this);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemListener.onLongClick(list.get(getAdapterPosition()),notes_container);
                    return true;
                }
            });
        }

        @Override
        public void onClick(View view) {
            if(itemListener != null){
                itemListener.onItemClick(view,getAdapterPosition());
            }
        }

        public void setNote(Note note){
            tvTitle.setText(note.getTitle());
            tvTime.setSelected(true);

            tvDescription.setText(note.getDescription());

            tvTime.setText(note.getCreatedTime());
            tvTime.setSelected(true);

            if(note.isPinned()){
                imageView_pin.setImageResource(R.drawable.ic_pin);
            }
            else {
                imageView_pin.setImageResource(0);
            }
            if(note.getColor() != null && !note.getColor().trim().isEmpty()){
                notes_container.setCardBackgroundColor(Color.parseColor(note.getColor()));
            }
            else{
                notes_container.setCardBackgroundColor(Color.parseColor("#333333"));
            }
            if(note.getImagePath()!= null && !note.getImagePath().trim().isEmpty()){
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            }
            else {
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    public interface ItemListener{
        void onItemClick(View view, int pos);
        void onLongClick(Note note, CardView cardView);
    }
}
