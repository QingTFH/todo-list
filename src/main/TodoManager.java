package main;

import token.TodoToken;
import util.TodoUtil;

import java.util.Comparator;
import java.util.List;

public class TodoManager {

    private static TodoManager instance = null;
    private final List<TodoToken> todoList;;

    private TodoManager() { // 初始化
        todoList = TodoUtil.loadTodo();
    }

    public static TodoManager getInstance() {
        if(instance == null) {
            instance = new TodoManager();
        }
        return instance;
    }

    public void add(TodoToken token) {
        todoList.add(token);
        save();
    }

    public boolean finish(int index) {
        if (index < 0 || index >= todoList.size()) {
            System.out.println("finish 越界");
            return false;
        }
        todoList.remove(index);
        save();
        return true;
    }

    public int size() {
        return todoList.size();
    }

    public void sort() {
        todoList.sort(Comparator.comparing(TodoToken::getDeadline));
    }

    public void save() {
        sort();
        TodoUtil.saveTodo(todoList);
    }

    public void query(int n) {
        int i = 1;
        for(TodoToken t : todoList) {
            System.out.println(i++ + ": " + t.toString());
            if(i == n+1) {
                return;
            }
        }
    }

    public void query() {
        System.out.println("size: " + size());
        int i = 1;
        for(TodoToken t : todoList) {
            System.out.println(i++ + ": " + t.toString());
        }
    }

}
