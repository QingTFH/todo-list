package manager;

import dao.TodoDao;
import exception.InputException;
import io.output.DebugOutput;
import io.output.Output;
import main.Config;
import token.dataToken.TodoToken;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class TodoManager {

    private static TodoManager instance = null;
    private final List<TodoToken> todoList;
    private final List<TodoToken> finishedList;
    private int finishedCount; // 已完成事件数，finish 时递增，避免每次统计

    private TodoManager() { // 初始化
        todoList = TodoDao.loadTodo();
        finishedList = TodoDao.loadFinished();
        finishedCount = finishedList.size();
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
        DebugOutput.debugPrint("sort");
        todoList.sort(Comparator.comparingDouble(TodoManager::priorityScore)
                .reversed()
                .thenComparing(TodoToken::getDeadline));
    }

    /** score = 重要度权重×重要度 + 反比例紧迫度；越大越优先 */
    private static double priorityScore(TodoToken t) {
        long days = ChronoUnit.DAYS.between(LocalDateTime.now(), t.getDeadline());
        double urgency = (double) Config.URGENCY_SCALE / Math.max(days, 1); // 今天/超期取满值, 1天vs2天差一倍
        return Config.IMPORTANCE_WEIGHT * t.getImportance() + urgency;
    }

    public void save() {
        sort();
        TodoDao.saveTodo(todoList);
    }

    public void saveFinished() {
        TodoDao.saveFinished(finishedList);
    }

    /** 保存待办与已完成两份数据 */
    public void saveAll() {
        save();
        saveFinished();
    }

    /*---------- 外部操作 ----------*/

    public void add(TodoToken token) {
        todoList.add(token);
        save();
        Output.print("add: "
                + token.getContent()
                + "; ddl: "
                + token.getDeadline().format(Config.ALL_FORMATTER));
    }

    public void finish(int index) {
        if (index < 0 || index >= todoList.size()) {
            throw new InputException("finish索引越界");
        }
        TodoToken token = todoList.remove(index);
        finishedList.add(token);
        finishedCount++;
        saveAll();
        Output.print("finish: " + token.getContent());
    }

    /** 从待办中彻底删除，不进入已完成列表 */
    public void delete(int index) {
        if(index < 0 || index >= todoList.size()) {
            throw new InputException("delete索引越界");
        }
        TodoToken token = todoList.remove(index);
        save();
        Output.print("delete: " + token.getContent());
    }

    /** 编辑第 index 条：newContent / newDeadline / newImportance 为 null 时保留原值 */
    public void edit(int index, String newContent, LocalDateTime newDeadline, Integer newImportance) {
        if(index < 0 || index >= todoList.size()) {
            throw new InputException("edit索引越界");
        }
        TodoToken old = todoList.get(index);
        String content = newContent != null ? newContent : old.getContent();
        LocalDateTime deadline = newDeadline != null ? newDeadline : old.getDeadline();
        int importance = newImportance != null ? newImportance : old.getImportance();

        todoList.set(index, new TodoToken(content, deadline, importance));
        save();
        Output.print("edit: " + content + "; ddl: " + deadline.format(Config.ALL_FORMATTER)
                + "; 重要度: " + importance);
    }

    public void query(int n, boolean detail) {
        if(n < 1) {
            throw new InputException("query -n 需要大于0");
        }
        queryItems(n, detail);
    }

    public void query(boolean detail) {
        Output.print("size: " + size());
        queryItems(todoList.size(), detail);
    }

    /** 展示格式: content; ddl[: pri][: score]，score 仅待办查询提供 */
    private String formatDisplay(TodoToken t, boolean detail, boolean withScore) {
        StringBuilder sb = new StringBuilder(t.getContent()).append("; ddl:")
                .append(t.getDeadline().format(Config.ALL_FORMATTER));
        if(detail) {
            sb.append("; pri:").append(t.getImportance());
            if(withScore) {
                sb.append("; score ").append(String.format("%.1f", priorityScore(t)));
            }
        }
        return sb.toString();
    }

    /** 打印前 count 条，count 超过列表长度时打印全部 */
    private void queryItems(int count, boolean detail) {
        int limit = Math.min(count, todoList.size());
        for(int i = 0; i < limit; i++) {
            Output.print((i + 1) + ": " + formatDisplay(todoList.get(i), detail, true));
        }
    }

    /*---------- 已完成事项查询 ----------*/

    /** 默认只展示前 DEFAULT_FINISHED_QUERY_LIMIT 条 */
    public void queryFinished(boolean detail) {
        queryFinished(Config.DEFAULT_FINISHED_QUERY_LIMIT, detail);
    }

    public void queryFinished(int n, boolean detail) {
        if(n < 1) {
            throw new InputException("query -f -n 需要大于0");
        }
        printFinished(Math.min(n, finishedList.size()), detail);
    }

    public void queryFinishedAll(boolean detail) {
        printFinished(finishedList.size(), detail);
    }

    /** 开头打印已完成事件数（用计数器），再列出前 count 条 */
    private void printFinished(int count, boolean detail) {
        Output.print("已完成事件数：" + finishedCount);
        for(int i = 0; i < count; i++) {
            Output.print((i + 1) + ": " + formatDisplay(finishedList.get(i), detail, false));
        }
    }

}
