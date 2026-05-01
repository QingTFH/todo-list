package manager;

import dao.Dao;
import exception.InputException;
import exception.LoadSaveException;
import exception.WrongException;
import token.dataToken.TodoToken;
import util.TimeUtil;

import java.util.Comparator;
import java.util.List;

public class TodoManager {

    private static TodoManager instance = null;
    private final List<TodoToken> todoList;;

    private TodoManager() { // 初始化
        todoList = Dao.loadTodo();
    }

    public static TodoManager getInstance() {
        if(instance == null) {
            instance = new TodoManager();
        }
        return instance;
    }

    public int size() {
        return todoList.size();
    }

    private void sort() {
        todoList.sort(Comparator.comparing(TodoToken::getDeadline));
    }

    public void save() {
        sort();
        Dao.saveTodo(todoList);
    }

    /*---------- 外部操作 ----------*/

    public void add(TodoToken token) {
        todoList.add(token);
        sort();
        save();
        System.out.println("add: "
                + token.getContent()
                + "; ddl: "
                + token.getDeadline().format(TimeUtil.all_formatter));
    }

    public void finish(int index) {
        if (index < 0 || index >= todoList.size()) {
            throw new InputException("finish索引越界");
        }
        TodoToken token =  todoList.remove(index);
        sort();
        save();
        System.out.println("finish: " + token.getContent());
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
