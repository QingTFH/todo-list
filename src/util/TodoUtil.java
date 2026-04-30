package util;
import main.Dao;
import token.TodoToken;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TodoUtil {

    private static final String FILE_PATH = "todo.txt"; // 文件路径

    // 读取本地文件
    public static List<TodoToken> loadTodo() {
        List<TodoToken> todoList = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) { // 文件不存在就新建
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return todoList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) { // 逐行读取
            String line;
            while ((line = br.readLine()) != null) {
                todoList.add(Dao.parseTodoToken(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todoList;
    }

    // 保存列表到本地文件
    public static void saveTodo(List<TodoToken> todoList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (TodoToken todo : todoList) {
                bw.write(todo.toString());
                bw.newLine(); // 换行，一条待办占一行
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
