package token.command;

import exception.InputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Command {

    public enum Operator {
        stop, add, query, finish, edit, delete, help
    }

    /** 长选项别名 → 短选项(规范名)。输入 -m/--message/-message 都解析为 m */
    private static final Map<String, String> OPTION_ALIASES = new HashMap<>();

    static {
        OPTION_ALIASES.put("message", "m");
        OPTION_ALIASES.put("date", "d");
        OPTION_ALIASES.put("hour", "h");
        OPTION_ALIASES.put("num", "n");
        OPTION_ALIASES.put("content", "c");
        OPTION_ALIASES.put("all", "a");
        OPTION_ALIASES.put("finished", "f");
        OPTION_ALIASES.put("importance", "i");
        OPTION_ALIASES.put("detail", "d");
        OPTION_ALIASES.put("overdue", "o");
    }

    private final Operator commandType;
    private final HashMap<String, String> options = new HashMap<>();
    private final List<String> others = new ArrayList<>();

    public Command(Operator type) {
        commandType = type;
    }

    public Operator getCommandType() {
        return commandType;
    }

    public String getOption(String key) {
        return options.get(normalize(key));
    }

    public List<String> getOthers() {
        return others;
    }

    /** 已解析的选项名（原始键） */
    public Set<String> optionKeys() {
        return options.keySet();
    }

    /** 长选项名映射到短选项名；本身就是短选项名则原样返回 */
    private static String normalize(String key) {
        String canonical = OPTION_ALIASES.get(key);
        return canonical != null ? canonical : key;
    }

    public static class Parser {
        public static Command parseCommand(String line) {
            String[] command = line.split("\\s+"); // 用空白符分割

            if(command[0].isEmpty()) {
                return null;
            }

            Operator operator = parseOperator(command[0]);
            Command cmd = new Command(operator);
            int i = 1;
            while (i < command.length) {
                String part = command[i];
                if (part.startsWith("-")) {
                    // 支持 -x、-xxx、--xxx，去掉前导横杠并归一到短选项名
                    String key = part.startsWith("--") ? part.substring(2) : part.substring(1);
                    boolean isNextParameter = ((i+1 != command.length) && ! command[i+1].startsWith("-"));
                    if(isNextParameter) {
                        cmd.options.put(normalize(key), command[i + 1]);
                        i += 2;
                    } else { // 下一项不是参数
                        cmd.options.put(normalize(key), "");
                        i++;
                    }
                } else {
                    cmd.others.add(part);
                    i++;
                }
            }
            return cmd;
        }

        private static Operator parseOperator(String s) {

            for(Operator o : Operator.values()) {
                if(o.toString().equals(s)) {
                    return o;
                }
            }

            throw new InputException("指令 " + s + " 不存在, 请输入 help 查看可用指令");
        }
    }

}
