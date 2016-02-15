package com.tt.todoapplist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TodoAdapter todoAdapter;
    List<TodoModel> listOfTodos;
    int id = 0;
    MySQLiteHelper todoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoDB = new MySQLiteHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //created adapter
        listOfTodos = new ArrayList<TodoModel>();

        if (todoDB.getAllTodos() != null) {
            listOfTodos = todoDB.getAllTodos();
        }else {
            Log.d("MainActivity", "listoftodos is null - " + todoDB.getAllTodos());
        }

        todoAdapter = new TodoAdapter(this, listOfTodos);

        recyclerView.setAdapter(todoAdapter);

    }

    public void openDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText etAddTodo = (EditText) dialogView.findViewById(R.id.et_add_todo);

        dialogBuilder.setTitle("Add ToDo");
        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                id++;

                String todo = etAddTodo.getText().toString().trim();

                updateAdapter(id, todo);

                Toast.makeText(MainActivity.this, todo, Toast.LENGTH_LONG).show();

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void updateAdapter(int id, String todo) {

        TodoModel todoModel = new TodoModel(id, todo, false);

        listOfTodos.add(todoModel);

        todoDB.addTodo(todoModel);

        todoAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_remove) {
            removeTodoFromList();
        }
        return true;
    }

    private void removeTodoFromList() {

        todoAdapter.removeSelectedItems();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
