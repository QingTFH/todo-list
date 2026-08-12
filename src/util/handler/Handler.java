package util.handler;

import exception.InputException;
import main.Config;
import token.command.Command;

import java.util.List;

public interface Handler {

    void handle(Command cmd);

    /** 从指令中解析 1 基序号并转为 0 基索引，支持 -n num 与裸参数 num 两种写法 */
    static int indexOf(Command cmd, String operatorName) {
        String n = cmd.getOption("n");
        if(n == null) {
            List<String> others = cmd.getOthers();
            if(others.isEmpty()) {
                throw new InputException(operatorName + "缺少序号");
            }
            n = others.get(0);
        }

        try {
            return Integer.parseInt(n) - 1;
        } catch (NumberFormatException e) {
            throw new InputException(operatorName + "序号不是数字");
        }
    }

    /** 解析 -i 重要度选项；未提供时返回 null（由调用方决定默认值或保留原值） */
    static Integer importanceOf(Command cmd, String operatorName) {
        String s = cmd.getOption("i");
        if(s == null || s.isEmpty()) {
            return null;
        }
        try {
            int v = Integer.parseInt(s);
            if(v < Config.IMPORTANCE_MIN || v > Config.IMPORTANCE_MAX) {
                throw new InputException(operatorName + " -i 重要度需在"
                        + Config.IMPORTANCE_MIN + "~" + Config.IMPORTANCE_MAX + "之间");
            }
            return v;
        } catch (NumberFormatException e) {
            throw new InputException(operatorName + " -i 重要度不是数字");
        }
    }

}
