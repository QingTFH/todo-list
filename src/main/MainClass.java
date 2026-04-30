package main;

import io.input.Input;
import io.output.Output;

public class MainClass {

    public static void main(String[] args) {
        Output.printFirst();
        Input input = new Input(System.in);
        input.run();
    }

}
