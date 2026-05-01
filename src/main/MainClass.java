package main;

import exception.InputException;
import exception.LoadSaveException;
import exception.WrongException;
import io.input.Input;
import io.output.Output;

public class MainClass {

    public static void main(String[] args)  {
        Output.printFirst();

        try {
            Input input = new Input(System.in);
            input.run();
        } catch (InputException | LoadSaveException e) {
            throw new WrongException("初始化失败");
        }

    }

}
