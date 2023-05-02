package com.example.easytutonotes.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easytutonotes.R;
import com.example.easytutonotes.activities.AddNoteActivity;
import com.example.easytutonotes.activities.AddTodoActivity;
import com.example.easytutonotes.adapter.TodoAdapter;
import com.example.easytutonotes.dal.SQLiteHelper;
import com.example.easytutonotes.model.Todo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ToDoFragment extends Fragment implements View.OnClickListener,TodoAdapter.ItemListener,PopupMenu.OnMenuItemClickListener {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private SQLiteHelper db;
    private TodoAdapter adapter;
    private List<Todo> list = new ArrayList<>();
    private Todo selectedTodo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init view
        recyclerView = view.findViewById(R.id.recyclerview);
        floatingActionButton = view.findViewById(R.id.fab);

        adapter = new TodoAdapter();
        db = new SQLiteHelper(getContext());
        list = db.getAllTodo();
        adapter.setList(list);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);

        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onItemClick(View view, int pos) {
        Intent intent =new Intent(getActivity(), AddTodoActivity.class);
        intent.putExtra("isViewOrUpdate",true);
        intent.putExtra("todo",adapter.getItem(pos));
        startActivityForResult(intent,1002);
    }

    @Override
    public void onLongClick(Todo todo, CardView cardView) {
        selectedTodo = new Todo();
        selectedTodo = todo;
        showPopup(cardView);
    }

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(getContext(),cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu_todo);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                db.deleteTodo(selectedTodo.getId());
                Toast.makeText(getContext(), "Todo deleted!", Toast.LENGTH_SHORT).show();
                list.clear();
                list = db.getAllTodo();
                adapter.setList(list);
                return true;
            default:
                return false;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001){
            if(resultCode == RESULT_OK){
                Todo new_note = (Todo) data.getSerializableExtra("todo");
                db.addItemTodo(new_note);
                Toast.makeText(getContext(),"todo saved",Toast.LENGTH_SHORT).show();
                list.clear();
                list = db.getAllTodo();
                adapter.setList(list);
            }
        }
        else if (requestCode == 1002) {
            if(resultCode == RESULT_OK){
                Todo new_note = (Todo) data.getSerializableExtra("todo");
                db.updateTodo(new_note);
                Toast.makeText(getContext(),"Updated todo",Toast.LENGTH_SHORT).show();
                list.clear();
                list = db.getAllTodo();
                adapter.setList(list);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view == floatingActionButton){
            Intent intent = new Intent(getActivity(),AddTodoActivity.class);
            startActivityForResult(intent,1001);
        }
    }
}
