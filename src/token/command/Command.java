package token.command;

import exception.InputException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Command {

    public enum Operator {
        stop, add, query, finish
    }

    public static final Command POISON = new Command(Operator.stop);

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
        return options.get(key);
    }

    public String getOption(String key, String defaultValue) {
        // 如果key不存在，返回默认值；存在就返回真实值
        return options.getOrDefault(key, defaultValue);
    }

    public List<String> getOthers() {
        return others;
    }

    public static class Parser {
        public static Command parseCommand(String line) {
            String[] command = line.split("\\s+"); // 用空白符分割

            if(command[0] == null || command[0].isEmpty()) {
                return null;
            }

            Operator operator = parseOperator(command[0]);
            Command cmd = new Command(operator);
            int i = 1;
            while (i < command.length) {
                String part = command[i];
                if (part.startsWith("-")) {
                    String key = part.substring(1);
                    boolean isNextParameter = ((i+1 != command.length) && ! command[i+1].startsWith("-"));
                    if(isNextParameter) {
                        cmd.options.put(key, command[i + 1]);
                        i += 2;
                    } else { // 下一项不是参数
                        cmd.options.put(key, "");
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

            throw new InputException(s + " 指令不存在!");
        }
    }

}
