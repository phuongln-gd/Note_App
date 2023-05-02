package com.example.easytutonotes.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.easytutonotes.model.Note;
import com.example.easytutonotes.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="GhiChu_3.db";
    private static int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql ="CREATE TABLE items(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "description TEXT, " +
                "date TEXT," +
                "imagePath TEXT," +
                "webLink TEXT," +
                "color TEXT," +
                "pinned INTEGER)";
        db.execSQL(sql);
        String sql_2 = "CREATE TABLE todo_items(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT, " +
                "done INTEGER)";
        db.execSQL(sql_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    //getAll order by date
    // SELECT * from items ORDER BY pinned DESC, STR_TO_DATE(date,'%H:%i, %d/%m') DESC;
    public List<Note> getAll(){
        List<Note> list= new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        //SQLiteDatabase st = this.getWritableDatabase();
        String order = "pinned DESC";
        Cursor rs = st.query("items",null,null,null,null,null, order);
        //Cursor rs = st.rawQuery("SELECT * from items ORDER BY pinned DESC, STR_TO_DATE(date,'%H:%i, %d/%m') DESC",null);
        while(rs != null && rs.moveToNext()){
            int id = rs.getInt(0);
            String title = rs.getString(1);
            String description = rs.getString(2);
            String date = rs.getString(3);
            String imagePath = rs.getString(4);
            String webLink = rs.getString(5);
            String color = rs.getString(6);
            int pinned = rs.getInt(7);
            Note note = new Note();
            note.setId(id);
            note.setTitle(title);
            note.setDescription(description);
            note.setCreatedTime(date);
            note.setImagePath(imagePath);
            note.setWeblink(webLink);
            note.setColor(color);
            if(pinned == 0){
                note.setPinned(false);
            }
            else {
                note.setPinned(true);
            }
            list.add(note);
        }
        return list;
    }

    public List<Todo> getAllTodo(){
        List<Todo> list= new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String order = "id DESC";
        Cursor rs = st.query("todo_items",null,null,null,null,null, order);
        while(rs != null && rs.moveToNext()){
            int id = rs.getInt(0);
            String description = rs.getString(1);
            int done = rs.getInt(2);
            Todo todo = new Todo();
            todo.setId(id);
            todo.setDescription(description);
            if(done == 0){
                todo.setDone(false);
            }
            else {
                todo.setDone(true);
            }
            list.add(todo);
        }
        return list;
    }

    //add
    public long addItem(Note item){
        ContentValues values = new ContentValues();
        values.put("title",item.getTitle());
        values.put("description",item.getDescription());
        values.put("date",item.getCreatedTime());
        values.put("imagePath",item.getImagePath());
        values.put("webLink",item.getWeblink());
        values.put("color",item.getColor());
        values.put("pinned",item.isPinned());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("items",null,values);
    }

    public long addItemTodo(Todo item){
        ContentValues values = new ContentValues();
        values.put("description",item.getDescription());
        values.put("done",item.isDone());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("todo_items",null,values);
    }
    //update
    public int update(Note item){
        ContentValues values = new ContentValues();
        values.put("title",item.getTitle());
        values.put("description",item.getDescription());
        values.put("date",item.getCreatedTime());
        values.put("imagePath",item.getImagePath());
        values.put("webLink",item.getWeblink());
        values.put("color",item.getColor());
        values.put("pinned",item.isPinned());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "id = ?";
        String[] whereArgs = {item.getId()+""};
        return sqLiteDatabase.update("items",values,whereClause,whereArgs);
    }
    public int updateTodo(Todo item){
        ContentValues values = new ContentValues();
        values.put("description",item.getDescription());
        values.put("done",item.isDone());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "id = ?";
        String[] whereArgs = {item.getId()+""};
        return sqLiteDatabase.update("todo_items",values,whereClause,whereArgs);
    }
    //update pin
    public int pin(Note item,int b){
        ContentValues values = new ContentValues();
        values.put("pinned",b);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = "id = ?";
        String[] whereArgs = {item.getId()+""};
        return sqLiteDatabase.update("items",values,whereClause,whereArgs);
    }

    //delete
    public int delete(int id){
        String whereClause = "id = ?";
        String[] whereArgs = {id+""};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("items",whereClause,whereArgs);
    }
    public int deleteTodo(int id){
        String whereClause = "id = ?";
        String[] whereArgs = {id+""};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("todo_items",whereClause,whereArgs);
    }
}
