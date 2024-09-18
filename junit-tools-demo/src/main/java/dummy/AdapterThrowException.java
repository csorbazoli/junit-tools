package dummy;

import lombok.experimental.Adapter;

public class AdapterThrowException {

    @Adapter(throwException = IllegalStateException.class, message = "NOT_IMPLEMENTED")
    public static class AdapterClass implements FooInterface {

	@Override
	public String getStatus() {
	    return "TestStatus";
	}

    }

    public static interface FooInterface {
	void doStuff();

	int countMyObjects(String userName) throws IllegalArgumentException;

	String getStatus() throws IllegalStateException;
    }
}