package exception;

public class WrongException extends MyException {

    public WrongException(String s) {
        super(getErrorMsg(s));
    }

    public void print() {
        System.out.println("[Error] " + this);
    }

    private static String getErrorMsg(String message) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String className = stack[3].getClassName(); // 调用new WrongExc的地方
        className = className.substring(className.lastIndexOf(".") + 1);
        String methodName = stack[3].getMethodName();
        return String.format("%s::%s：%s", className, methodName, message);
    }
}
