package dummy;

import java.util.List;

import lombok.experimental.Adapter;

public class AdapterConflictingGenericNames<T extends Number> {

    @Adapter(silent = true)
    public static class AdapterClass<T> implements FooInterface {

    }

    public static interface FooInterface {

	<T extends Exception> List<T> getListOfExceptions(Class<T> clazz);

    }

}