package exception;

public class InputException extends AppException{

    public InputException(String cause) {
        super(cause);
    }

    public void print() {
        System.out.println("[Input错误] " + this);
    }

}
