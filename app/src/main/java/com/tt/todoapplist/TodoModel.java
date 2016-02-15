package com.tt.todoapplist;

/**
 * Created by vijay on 2/6/2016.
 */
public class TodoModel {

    int id;
    String todo;
    boolean isSelected;

    public TodoModel() {
    }

    public TodoModel(int id, String todo, boolean isSelected) {
        this.id = id;

        this.todo = todo;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "Todo [id=" + id + ", todo=" + todo + ", isSelected=" + isSelected + "]";
    }


}
