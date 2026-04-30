package exception;

public class Wrong extends Exception {

    private final String wrongCause;

    public Wrong(String s) {
        wrongCause = s;
    }

    @Override
    public String toString() {
        return wrongCause;
    }
}
