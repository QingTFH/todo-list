package exception;

public class LoadSaveException extends AppException {

    public LoadSaveException(String cause) {
        super(cause);
    }

    public void print() {
        System.out.println("[Error] " + this);
    }
}
