package util;

public class ErrorUtil {

    public static String getErrorMsg(String message) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String className = stack[2].getClassName(); // stack[2] = 真正调用这个方法的地方（dispatch方法）
        className = className.substring(className.lastIndexOf(".") + 1); // 只保留类名（去掉包名）
        String methodName = stack[2].getMethodName();
        return String.format("%s::%s：%s", className, methodName, message);
    }

}
