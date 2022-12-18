package org.junit.tools.base;

import javax.annotation.Generated;

import org.junit.Test;
import org.junit.tools.generator.IMockClassGenerator;

@Generated(value = "org.junit-tools-1.1.0")
@ExtendWith(MockitoExtension.class)
public class ExtensionPointHandlerTest {

    private ExtensionPointHandler createTestSubject() {
	return new ExtensionPointHandler();
    }

    @MethodRef(name = "getMockClassGenerator", signature = "()QIMockClassGenerator;")
    @Test
    public void testGetMockClassGenerator() throws Exception {
	ExtensionPointHandler testSubject;
	IMockClassGenerator result;

	// default test
	testSubject = createTestSubject();
	result = testSubject.getMockClassGenerator();
    }
}