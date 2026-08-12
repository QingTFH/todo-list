package util.handler;

import exception.InputException;
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

}
