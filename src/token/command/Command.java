package token.command;

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

    public List<String> getOthers() {
        return others;
    }

    public static class Parser {
        public static Command parseCommand(String line) {
            String[] command = line.split("\\s+"); // 用空白符分割
            Operator operator = parseOperator(command[0]);

            if(operator == null) {
                System.out.println("get wrong operator");
                return null;
            }

            Command cmd = new Command(operator);

            int i = 1;
            while (i < command.length) {
                String part = command[i];
                if (part.startsWith("-")) {
                    String key = part.substring(1);
                    if(i+1 == command.length) { // 最后时刻只有option没有context
                        break;
                    }
                    String value = command[i + 1];
                    cmd.options.put(key, value);
                    i += 2;
                } else {
                    cmd.others.add(part);
                    i++;
                }
            }
            return cmd;
        }

        private static Operator parseOperator(String s) {
            switch (s.toLowerCase()) {
                case "add" :
                    return Command.Operator.add;
                case "query" :
                    return Operator.query;
                case "finish" :
                    return Operator.finish;
                case "stop" :
                    return Operator.stop;
                default :
                    return null;
            }
        }
    }

}
