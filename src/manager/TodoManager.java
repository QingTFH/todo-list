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
import java.util.stream.Collectors;

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

    /** 一键完成所有已逾期任务 */
    public void finishOverdue() {
        List<TodoToken> overdue = overdueList();
        for(TodoToken t : overdue) {
            todoList.remove(t);
            finishedList.add(t);
            finishedCount++;
        }
        saveAll();
        Output.print("finish 逾期任务 " + overdue.size() + " 条");
    }

    /** 一键删除所有已逾期任务 */
    public void deleteOverdue() {
        List<TodoToken> overdue = overdueList();
        todoList.removeAll(overdue);
        save();
        Output.print("delete 逾期任务 " + overdue.size() + " 条");
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
        renderQuery(Math.min(n, todoList.size()), detail);
    }

    public void query(boolean detail) {
        Output.print("size: " + size());
        renderQuery(todoList.size(), detail);
    }

    /** 逾期按 ddl 升序、未逾期按 score 降序，分区展示，全局序号 */
    private void renderQuery(int count, boolean detail) {
        List<TodoToken> overdue = overdueList();
        List<TodoToken> active = activeList();
        int[] counter = {0};
        int overdueCount = Math.min(count, overdue.size());
        int activeCount = count - overdueCount;
        printSection("[---------已逾期---------]", overdue.subList(0, overdueCount), detail, false, counter);
        printSection("[---------未逾期---------]", active.subList(0, Math.min(activeCount, active.size())), detail, true, counter);
    }

    private void printSection(String header, List<TodoToken> items, boolean detail, boolean withScore, int[] counter) {
        if(items.isEmpty()) {
            return;
        }
        Output.print(header);
        int colWidth = columnWidth(items);
        String numFmt = "%" + String.valueOf(todoList.size()).length() + "d";
        for(TodoToken t : items) {
            Output.print(String.format(numFmt, ++counter[0]) + ": "
                    + formatDisplay(t, detail, withScore, colWidth));
        }
    }

    /** 逾期任务：按 ddl 升序 */
    private List<TodoToken> overdueList() {
        return todoList.stream()
                .filter(t -> t.getDeadline().isBefore(LocalDateTime.now()))
                .sorted(Comparator.comparing(TodoToken::getDeadline))
                .collect(Collectors.toList());
    }

    /** 未逾期任务：按 score 降序，同分按 ddl 升序 */
    private List<TodoToken> activeList() {
        return todoList.stream()
                .filter(t -> !t.getDeadline().isBefore(LocalDateTime.now()))
                .sorted(Comparator.comparingDouble(TodoManager::priorityScore)
                        .reversed()
                        .thenComparing(TodoToken::getDeadline))
                .collect(Collectors.toList());
    }

    /** 展示格式: content(定宽) ; ddl[: pri][: score]，score 仅待办查询提供 */
    private String formatDisplay(TodoToken t, boolean detail, boolean withScore, int colWidth) {
        StringBuilder sb = new StringBuilder(fitContent(t.getContent(), colWidth))
                .append("; ddl:").append(t.getDeadline().format(Config.ALL_FORMATTER));
        if(detail) {
            sb.append("; pri:").append(t.getImportance());
            if(withScore) {
                sb.append("; score ").append(String.format("%.1f", priorityScore(t)));
            }
        }
        return sb.toString();
    }

    /** content 列宽 = min(本列表最长显示宽, 上限) */
    private int columnWidth(List<TodoToken> items) {
        int max = 0;
        for(TodoToken t : items) {
            max = Math.max(max, displayWidth(t.getContent()));
        }
        return Math.min(max, Config.CONTENT_COLUMN_CAP);
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
        List<TodoToken> shown = finishedList.subList(0, count);
        int colWidth = columnWidth(shown);
        String numFmt = "%" + String.valueOf(count).length() + "d";
        for(int i = 0; i < count; i++) {
            Output.print(String.format(numFmt, i + 1) + ": "
                    + formatDisplay(shown.get(i), detail, false, colWidth));
        }
    }

    /*---------- 显示辅助 ----------*/

    /** content 左对齐填充到 colWidth；超出则截断补省略号（省略号占 2 列） */
    private static String fitContent(String s, int colWidth) {
        int w = displayWidth(s);
        if(w <= colWidth) {
            return s + spaces(colWidth - w);
        }
        StringBuilder sb = new StringBuilder();
        int cur = 0;
        int reserve = charWidth('…'); // 为省略号预留列宽
        for(int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int cw = charWidth(c);
            if(cur + cw > colWidth - reserve) {
                break;
            }
            sb.append(c);
            cur += cw;
        }
        return sb + "…";
    }

    /** 终端显示宽度：CJK/全角按 2 列，其余 1 列 */
    private static int charWidth(char c) {
        return c > 0xFF ? 2 : 1;
    }

    private static int displayWidth(String s) {
        int w = 0;
        for(int i = 0; i < s.length(); i++) {
            w += charWidth(s.charAt(i));
        }
        return w;
    }

    private static String spaces(int n) {
        StringBuilder sb = new StringBuilder(n);
        for(int i = 0; i < n; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

}
