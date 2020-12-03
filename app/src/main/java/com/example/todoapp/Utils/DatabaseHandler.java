package com.example.todoapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE "+TODO_TABLE+"("+ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            TASK+" TEXT ,"+STATUS +" INTEGER )";
    private SQLiteDatabase database;

    public DatabaseHandler(Context context){
        super(context,NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // DROP OLDER TABLE
        db.execSQL("DROP TABLE IF EXISTS "+TODO_TABLE);

        //CREATING NEW TABLE
        db.execSQL(CREATE_TODO_TABLE);

        onCreate(db);
    }
    public void openDatabase(){
        database = this.getWritableDatabase();
    }
    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS,0);
        database.insert(TODO_TABLE,null,cv);
    }
    // To show data on screen from databse
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> tasklist = new ArrayList<>();
        Cursor cursor = null;
        database.beginTransaction();
        try{
            cursor = database.query(TODO_TABLE,null,null,null,null,null,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        tasklist.add(task);
                    }while (cursor.moveToNext());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            database.endTransaction();
            cursor.close();
        }
        return  tasklist;
    }
    public void updateStatus(int id,int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS,status);
        database.update(TODO_TABLE,cv,ID+"=?",new String[] {String.valueOf(id)});
    }
    public void updateTask(int id,String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        database.update(TODO_TABLE,cv,ID+"=?",new String[] {String.valueOf(id)});
    }
    public void deleteTask(int id){
        database.delete(TODO_TABLE,ID+"=?",new String[] {String.valueOf(id)});
    }

}
