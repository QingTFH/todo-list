package main;

import exception.InputException;
import exception.LoadSaveException;
import exception.WrongException;
import io.input.Input;
import io.output.Output;

public class Main {

    public static void main(String[] args)  {


        try {
            Input input = new Input(System.in);
            input.run();
        } catch (InputException | LoadSaveException e) {
            throw new WrongException("初始化失败");
        }

    }

}
