package exception;

public class AppException extends RuntimeException{

    private final String cause;

    public AppException(String cause){
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
