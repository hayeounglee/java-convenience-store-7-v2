package store.util;


public interface Task<T> {
    static <T> T repeatUntilValid(Task<T> task) {
        while (true) {
            try {
                return task.run();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    T run();
}
