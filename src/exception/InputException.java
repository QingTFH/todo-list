package exception;

public class InputException extends MyException{

    public InputException(String cause) {
        super(cause);
    }

    public void print() {
        System.out.println("[Input错误] " + this);
    }

}
