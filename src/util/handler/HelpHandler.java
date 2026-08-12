package util.handler;

import exception.InputException;
import io.output.Output;
import token.command.Command;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HelpHandler implements Handler {
    // help 无参数: 列出所有指令; help add / help -add: 列出该指令的参数及作用

    private static final Map<Command.Operator, OptionDoc> DOCS = new LinkedHashMap<>();

    static {
        DOCS.put(Command.Operator.stop,   new OptionDoc("stop", "停止程序", null));
        DOCS.put(Command.Operator.add,    new OptionDoc("add -m 内容 [-d 日期] [-h 时间] [-i 重要度]", "新增待办",
                new String[][]{{"-m,--message", "设定内容"}, {"-d,--date", "设定日期"}, {"-h,--hour", "设定具体小时"}, {"-i,--importance", "设定重要度(0~3, 缺省1)"}}));
        DOCS.put(Command.Operator.query,  new OptionDoc("query [-f|-a|-n num] [-d]", "查询待办, 加 -f 查询已完成",
                new String[][]{{"-f,--finished", "查询已完成事项"}, {"-n,--num", "只查询前n条"}, {"-a,--all", "查询全部"}, {"-d,--detail", "额外展示pri与score"}}));
        DOCS.put(Command.Operator.finish, new OptionDoc("finish num 或 finish -n num", "完成待办, 移入finish.txt",
                new String[][]{{"-n,--num", "完成第n条"}, {"-o,--overdue", "一键完成所有已逾期任务"}}));
        DOCS.put(Command.Operator.edit,   new OptionDoc("edit num 或 edit -n num [-c 内容] [-d 日期] [-h 时间] [-i 重要度]", "修改待办内容/ddl/重要度",
                new String[][]{{"-n,--num", "待编辑序号"}, {"-c,--content", "新内容"}, {"-d,--date", "新日期"}, {"-h,--hour", "新时间"}, {"-i,--importance", "新重要度(0~3)"}}));
        DOCS.put(Command.Operator.delete, new OptionDoc("delete num 或 delete -n num", "彻底删除待办",
                new String[][]{{"-n,--num", "删除第n条"}, {"-o,--overdue", "一键删除所有已逾期任务"}}));
        DOCS.put(Command.Operator.help,   new OptionDoc("help [指令名]", "显示指令帮助",
                new String[][]{{"add", "例: help add 或 help -add"}}));
        DOCS.put(Command.Operator.undo,   new OptionDoc("undo", "撤销上一步修改", null));
    }

    @Override
    public void handle(Command cmd) {
        String target = resolveTarget(cmd);
        if(target == null) {
            printAll();
        } else {
            printOne(target);
        }
    }

    /** 目标指令名：裸参数优先，其次从选项键中找（支持 help add 与 help -add/--add） */
    private String resolveTarget(Command cmd) {
        List<String> others = cmd.getOthers();
        if(!others.isEmpty()) {
            return others.get(0);
        }
        for(String key : cmd.optionKeys()) {
            if(findOperator(key) != null) {
                return key;
            }
        }
        return null;
    }

    private Command.Operator findOperator(String name) {
        for(Command.Operator op : Command.Operator.values()) {
            if(op.toString().equals(name)) {
                return op;
            }
        }
        return null;
    }

    private void printAll() {
        Output.print("可用指令:");
        for(OptionDoc doc : DOCS.values()) {
            Output.print("  " + doc.usage + "  " + doc.summary);
        }
        Output.print("输入 \"help <指令名>\" 或 \"help -<指令名>\" 查看某指令的参数");
    }

    private void printOne(String name) {
        Command.Operator op = findOperator(name);
        if(op == null) {
            throw new InputException("指令 " + name + " 不存在, 请输入 help 查看可用指令");
        }
        OptionDoc doc = DOCS.get(op);
        Output.print(doc.usage);
        if(doc.options != null) {
            for(String[] opt : doc.options) {
                Output.print("  " + opt[0] + "  " + opt[1]);
            }
        }
    }

    private static class OptionDoc {
        final String usage;
        final String summary;
        final String[][] options;
        OptionDoc(String usage, String summary, String[][] options) {
            this.usage = usage;
            this.summary = summary;
            this.options = options;
        }
    }
}
