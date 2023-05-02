package com.example.easytutonotes.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytutonotes.R;
import com.example.easytutonotes.dal.SQLiteHelper;
import com.example.easytutonotes.model.Note;
import com.example.easytutonotes.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder>{

    private List<Todo> list;
    private ItemListener itemListener;

    public TodoAdapter() {
        list = new ArrayList<>();
    }

    public TodoAdapter(List<Todo> list) {
        this.list = list;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setList(List<Todo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public Todo getItem(int pos){
        return list.get(pos);
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo,parent,false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo item = list.get(position);
        holder.setNote(item);
        holder.rbDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteHelper db = new SQLiteHelper(view.getContext());
                db.deleteTodo(item.getId());
                Toast.makeText(view.getContext(), "Todo finish!", Toast.LENGTH_SHORT).show();
                list.clear();
                list = db.getAllTodo();
                setList(list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder{
        private CardView todo_container;
        private RadioButton rbDone;
        private TextView tvDescription;

        public TodoViewHolder(@NonNull View view) {
            super(view);
            rbDone = view.findViewById(R.id.rbDone);
            tvDescription = view.findViewById(R.id.tvDescription);
            todo_container = view.findViewById(R.id.todo_container);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(itemListener != null){
                        itemListener.onItemClick(view,getAdapterPosition());
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(itemListener != null) {
                        itemListener.onLongClick(list.get(getAdapterPosition()), todo_container);
                    }
                    return true;
                }
            });
        }

        public void setNote(Todo item) {
            tvDescription.setText(item.getDescription());
            rbDone.setChecked(false);
        }
    }

    public interface ItemListener{
        void onItemClick(View view, int pos);
        void onLongClick(Todo todo, CardView cardView);
    }
}
