package com.example.easytutonotes.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.easytutonotes.activities.AddNoteActivity;
import com.example.easytutonotes.R;
import com.example.easytutonotes.adapter.NoteAdapter;
import com.example.easytutonotes.dal.SQLiteHelper;
import com.example.easytutonotes.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment implements View.OnClickListener, NoteAdapter.ItemListener,PopupMenu.OnMenuItemClickListener{
    public static final int REQUEST_CODE_ADD_NOTE = 101;
    public static final int REQUEST_CODE_SELECT_IMAGE = 104;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 105;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private SQLiteHelper db;
    private NoteAdapter adapter;

    private SearchView searchView;
    private List<Note> list = new ArrayList<>();

    private Note selectedNote;

    private AlertDialog dialogAddURL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        adapter = new NoteAdapter();
        db = new SQLiteHelper(getContext());
        list = db.getAll();
        adapter.setList(list);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        floatingActionButton.setOnClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        view.findViewById(R.id.imageAddNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddNoteActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ADD_NOTE);
            }
        });
        view.findViewById(R.id.imageAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(
                        getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE
                )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION);
                }
                else{
                    selectImage();
                }
            }
        });
        view.findViewById(R.id.imageAddWebLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddURLDialog(view);
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent.resolveActivity(getContext().getPackageManager()) != null){
            startActivityForResult(intent,REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }
            else{
                Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor = getContext().getContentResolver()
                .query(contentUri,null,null,null,null);
        if(cursor == null){
            filePath = contentUri.getPath();
        }
        else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
    private void filter(String newText) {
        List<Note> filterList = new ArrayList<>();
        for(Note note: list){
            if(note.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || note.getDescription().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(note);
            }
        }
        adapter.filterList(filterList);
    }
    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        floatingActionButton = view.findViewById(R.id.fab);
        searchView = view.findViewById(R.id.searchViewHome);
    }

    @Override
    public void onClick(View view) {
        if(view == floatingActionButton){
            Intent intent = new Intent(getActivity(),AddNoteActivity.class);
            startActivityForResult(intent,REQUEST_CODE_ADD_NOTE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        list = db.getAll();
        adapter.setList(list);
    }

    @Override
    public void onItemClick(View view, int pos) {
        Intent intent =new Intent(getActivity(),AddNoteActivity.class);
        intent.putExtra("isViewOrUpdate",true);
        intent.putExtra("note",adapter.getItem(pos));
        startActivityForResult(intent,102);
    }

    @Override
    public void onLongClick(Note note, CardView cardView) {
        selectedNote = new Note();
        selectedNote = note;
        showPopup(cardView);
    }

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(getContext(),cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(resultCode == RESULT_OK){
                Note new_note = (Note) data.getSerializableExtra("note");
                db.addItem(new_note);
                Toast.makeText(getContext(),"Note saved",Toast.LENGTH_SHORT).show();
                list.clear();
                list = db.getAll();
                adapter.setList(list);
            }
        } else if (requestCode == 102) {
            if(resultCode == RESULT_OK){
                Note new_note = (Note) data.getSerializableExtra("note");
                db.update(new_note);
                Toast.makeText(getContext(),"Updated note",Toast.LENGTH_SHORT).show();
                list.clear();
                list = db.getAll();
                adapter.setList(list);
            }
        }else if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if(data != null){
                Uri selectedImageUri = data.getData();
                if(selectedImageUri != null){
                    try {
                        String selectedImagePath = getPathFromUri(selectedImageUri);
                        Intent intent = new Intent(getActivity(),AddNoteActivity.class);
                        intent.putExtra("isFromQuickActions",true);
                        intent.putExtra("quickActionType","image");
                        intent.putExtra("imagePath",selectedImagePath);
                        startActivityForResult(intent,REQUEST_CODE_ADD_NOTE);
                    }catch (Exception exception){
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if(selectedNote.isPinned()){
                    db.pin(selectedNote,0);
                    Toast.makeText(getContext(), "Unpinned!", Toast.LENGTH_SHORT).show();
                }
                else{
                    db.pin(selectedNote,1);
                    Toast.makeText(getContext(), "Pinned!", Toast.LENGTH_SHORT).show();
                }
                list.clear();
                list = db.getAll();
                adapter.setList(list);
                return true;
            case R.id.delete:
                db.delete(selectedNote.getId());
                Toast.makeText(getContext(), "Note deleted!", Toast.LENGTH_SHORT).show();
                list.clear();
                list = db.getAll();
                adapter.setList(list);
                return true;
            default:
                return false;
        }
    }
    private void showAddURLDialog(View v){
        if(dialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_add_url,(ViewGroup) (v.findViewById(R.id.layoutAddUrlContainer))
            );
            builder.setView(view);
            dialogAddURL = builder.create();
            if(dialogAddURL.getWindow() != null){
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            final EditText inputURL = view.findViewById(R.id.inputURL);
            inputURL.requestFocus();
            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(inputURL.getText().toString().trim().isEmpty()){
                        Toast.makeText(getContext(), "Enter URL", Toast.LENGTH_SHORT).show();
                    }else if(!Patterns.WEB_URL.matcher(inputURL.getText().toString()).matches()){
                        Toast.makeText(getContext(), "Enter valid URL", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(getActivity(),AddNoteActivity.class);
                        intent.putExtra("isFromQuickActions",true);
                        intent.putExtra("quickActionType","URL");
                        intent.putExtra("URL",inputURL.getText()+"");
                        startActivityForResult(intent,REQUEST_CODE_ADD_NOTE);
                        dialogAddURL.dismiss();
                    }
                }
            });
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogAddURL.dismiss();
                }
            });
        }
        dialogAddURL.show();
    }
}
