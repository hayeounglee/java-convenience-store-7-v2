package store;

import store.controller.Screen;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        try {
            Screen screen = new Screen(new InputView(), new OutputView());
            screen.run();
        } catch (IllegalArgumentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }
}
