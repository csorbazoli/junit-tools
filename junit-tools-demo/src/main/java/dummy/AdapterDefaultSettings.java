package dummy;

import lombok.experimental.Adapter;

public class AdapterDefaultSettings {

    @Adapter
    public static class AdapterClass implements FooInterface {

	@Override
	public String getStatus() {
	    return "TestStatus";
	}

    }

    public static interface FooInterface {
	void doStuff();

	default int countMyObjects(String userName) {
	    return 1;
	}

	String getStatus() throws IllegalStateException;

	FooInterface someFluentMethod(String test);
    }

}