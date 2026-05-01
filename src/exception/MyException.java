package exception;

public class MyException extends Exception{

    private final String cause;

    public MyException(String cause){
        this.cause = cause;
    }

    public void print() {
        System.out.println("[Error] xxException::print未重写");
    }

    @Override
    public String toString() {
        return cause;
    }

}
