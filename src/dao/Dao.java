package dao;

import exception.InputException;
import exception.LoadSaveException;
import token.dataToken.TodoToken;
import util.TodoUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dao {
    // 数据助手
    private static final String FILE_PATH = "todo.txt"; // 文件路径

    // 读取本地文件
    public static List<TodoToken> loadTodo() {
        List<TodoToken> todoList = new ArrayList<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) { // 文件不存在就新建
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new LoadSaveException("load新建文件时发生IOException" + e);
            }
            return todoList;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) { // 逐行读取
            String line;
            while ((line = br.readLine()) != null) {
                todoList.add(TodoUtil.parseTodoToken(line));
            }
        } catch (IOException e) {
            throw new LoadSaveException("load发生IOException" + e);
        } catch (InputException e) {
            throw new InputException("load读取时Input错误, 请检查todo.txt");
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
            throw new LoadSaveException("save发生IOException" + e);
        }
    }

}
