package com.example.easytutonotes.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easytutonotes.R;
import com.example.easytutonotes.model.Note;
import com.example.easytutonotes.model.Todo;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTodoActivity extends AppCompatActivity {

    private EditText eDescription;
    private TextView tvSave;
    private Todo alreadyAvaiableTodo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        initView();
        if(getIntent().getBooleanExtra("isViewOrUpdate",false)){
            alreadyAvaiableTodo = (Todo) getIntent().getSerializableExtra("todo");
            setViewOrUpdateNote();
        }

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = eDescription.getText().toString();

                if(description.isEmpty()){
                    Toast.makeText(AddTodoActivity.this, "Please add some todo!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Todo todo = new Todo();
                todo.setDescription(description);
                todo.setDone(false);
                if(alreadyAvaiableTodo != null){
                    todo.setId(alreadyAvaiableTodo.getId());
                }
                Intent intent = new Intent();
                intent.putExtra("todo",todo);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    private void initView() {
        eDescription = findViewById(R.id.eDescription);
        tvSave = findViewById(R.id.tvSave);
    }

    private void setViewOrUpdateNote() {
        eDescription.setText(alreadyAvaiableTodo.getDescription());
    }
}