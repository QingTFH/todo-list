package exception;

public class WrongException extends MyException {

    public WrongException(String s) {
        super(s);
    }

    public void print() {
        System.out.println("[Error] " + this);
    }
}
