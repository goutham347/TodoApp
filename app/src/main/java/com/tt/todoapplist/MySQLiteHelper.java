package com.tt.todoapplist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TodoDB";
   
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create todo table
		String CREATE_TODO_TABLE = "CREATE TABLE todos ( " +
                "id INTEGER, " +
				"todo TEXT, " +
                "isSelected TEXT" + ")";
		
		// create todo table
		db.execSQL(CREATE_TODO_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older todos table if existed
        db.execSQL("DROP TABLE IF EXISTS todos");
        
        // create fresh todos table
        this.onCreate(db);
	}
	
	// todos table name
    private static final String TABLE_TODOS = "todos";
    
    // todos Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TODO = "todo";
    private static final String KEY_IS_SELECTED = "isSelected";

    private static final String[] COLUMNS = {KEY_ID, KEY_TODO, KEY_IS_SELECTED};
    
	public void addTodo(TodoModel todoModel){
		Log.d("addTodo", todoModel.toString());
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		 
		// 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, todoModel.getId()); // get id
        values.put(KEY_TODO, todoModel.getTodo()); // get todo
        values.put(KEY_IS_SELECTED, todoModel.isSelected()); // get isSelected

        // 3. insert
        db.insert(TABLE_TODOS, // table
        		null, //nullColumnHack
        		values); // key/value -> keys = column names/ values = column values
        
        // 4. close
        db.close(); 
	}
	
	public TodoModel getTodo(String id){

		// 1. get reference to readable DB
		SQLiteDatabase db = this.getReadableDatabase();
		 
		// 2. build query
        Cursor cursor = 
        		db.query(TABLE_TODOS, // a. table
        		COLUMNS, // b. column names
        		" id = ?", // c. selections 
                new String[] { id }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build todo object
        TodoModel todoModel = new TodoModel();
        todoModel.setId(Integer.parseInt(cursor.getString(0)));
        todoModel.setTodo(cursor.getString(1));
        todoModel.setIsSelected(Boolean.parseBoolean(cursor.getString(2)));

		Log.d("getTodo("+id+")", todoModel.toString());

        // 5. return todo
        return todoModel;
	}
	
	// Get All todos
    public List<TodoModel> getAllTodos() {
        List<TodoModel> todos = new LinkedList<TodoModel>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TODOS;
 
    	// 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build todo and add it to list
        TodoModel todoModel = null;
        if (cursor.moveToFirst()) {
            do {
                todoModel = new TodoModel();
                todoModel.setId(Integer.parseInt(cursor.getString(0)));
                todoModel.setTodo(cursor.getString(1));
                todoModel.setIsSelected(Boolean.parseBoolean(cursor.getString(2)));

                // Add todo to todos
                todos.add(todoModel);
            } while (cursor.moveToNext());
        }
        
//		Log.d("getAllTodos()", todoModel.toString());

        // return todos
        return todos;
    }
	
	 // Updating single todo
    public int updateTodo(TodoModel todeModel) {

    	// 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
		// 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, todeModel.getId()); // get title
        values.put(KEY_TODO, todeModel.getTodo()); // get author
        values.put(KEY_IS_SELECTED, todeModel.isSelected()); // get author

        // 3. updating row
        int i = db.update(TABLE_TODOS, //table
        		values, // column/value
        		KEY_ID+" = ?", // selections
                new String[] {String.valueOf(todeModel.getId())}); //selection args
        
        // 4. close
        db.close();
        
        return i;
        
    }

    // Deleting single todo
    public void deleteTodo(TodoModel todeModel) {

    	// 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        
        // 2. delete
        db.delete(TABLE_TODOS,
        		KEY_ID+" = ?",
                new String[] {String.valueOf(todeModel.getId())});
        
        // 3. close
        db.close();
        
		Log.d("deleteTodo", todeModel.toString());

    }

}
