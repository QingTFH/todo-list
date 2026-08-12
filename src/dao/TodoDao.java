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

import static main.Config.FILE_PATH;
import static main.Config.FINISH_FILE_PATH;

public class TodoDao {
    // 数据助手

    public static List<TodoToken> loadTodo() {
        return loadFromFile(FILE_PATH);
    }

    public static List<TodoToken> loadFinished() {
        return loadFromFile(FINISH_FILE_PATH);
    }

    public static void saveTodo(List<TodoToken> todoList) {
        saveToFile(FILE_PATH, todoList);
    }

    public static void saveFinished(List<TodoToken> finishedList) {
        saveToFile(FINISH_FILE_PATH, finishedList);
    }

    /** 逐行读取文件，不存在则自动新建 */
    private static List<TodoToken> loadFromFile(String path) {
        List<TodoToken> list = new ArrayList<>();
        File file = new File(path);

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new LoadSaveException("load新建文件时发生IOException" + e);
            }
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                list.add(TodoUtil.parseTodoToken(line));
            }
        } catch (IOException e) {
            throw new LoadSaveException("load发生IOException" + e);
        } catch (InputException e) {
            throw new InputException("load读取时Input错误, 请检查" + path + ": " + e);
        }

        return list;
    }

    /** 将列表整体写回文件，一条记录占一行 */
    private static void saveToFile(String path, List<TodoToken> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for(TodoToken token : list) {
                bw.write(token.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new LoadSaveException("save发生IOException" + e);
        }
    }

}
