package misc;

import java.util.function.Consumer;

public interface MaybeConsumer<T, E extends Exception> extends Consumer<T> {

    void except(E exception);

}
