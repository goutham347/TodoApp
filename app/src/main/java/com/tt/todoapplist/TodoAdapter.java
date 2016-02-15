package com.tt.todoapplist;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vijay on 2/6/2016.
 */
public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private static final String TAG = "TodoAdapter";

    List<TodoModel> listOfTodos;
    List<Integer> listOfPos = new ArrayList<Integer>();

    Context context;

    MySQLiteHelper todoDB;

    public TodoAdapter(Context context, List<TodoModel> listOfTodos) {
        this.context = context;
        this.listOfTodos = listOfTodos;
        todoDB = new MySQLiteHelper(context);
    }

    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TodoAdapter.ViewHolder holder, final int position) {

        holder.tvToDo.setText(listOfTodos.get(position).getTodo());


        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!getItem(position).isSelected) {
                    Log.d(TAG, "onClick: " + getItem(position).getId());
                    listOfPos.add(position);
                    holder.checkBox.setChecked(true);
                    getItem(position).setIsSelected(true);
                }else {
                    if (listOfPos.contains(position)) {
                        listOfPos.remove(position);
                    }
                    holder.checkBox.setChecked(false);
                    getItem(position).setIsSelected(false);
                }

            }
        });

    }

    public TodoModel getItem(int pos){
        return listOfTodos.get(pos);
    }

    @Override
    public int getItemCount() {
        return listOfTodos.size();
    }

    public void removeSelectedItems() {

        for (int i = 0; i < listOfTodos.size(); i++) {
            if (listOfTodos.get(i).isSelected) {
                todoDB.deleteTodo(listOfTodos.get(i));
                listOfTodos.remove(i);
            }
        }
        notifyDataSetChanged();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvToDo;
        AppCompatCheckBox checkBox;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);

            tvToDo = (TextView) itemView.findViewById(R.id.tv_todo);
            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.check_box);
            this.view = itemView;
        }

    }
}

