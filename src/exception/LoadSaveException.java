package exception;

public class LoadSaveException extends MyException {

    public LoadSaveException(String cause) {
        super(cause);
    }

    public void print() {
        System.out.println("[Error] " + this);
    }
}
