package org.junit.tools.generator.utils.testmodel;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.tools.generator.utils.TestUtils.BenchmarkResults;

public class BenchmarkResultsTest {

    @Test
    public void testToString() throws Exception {
	// given
	BenchmarkResults<String> underTest = create();
	// when
	String actual = underTest.toString();
	// then
	assertEquals("MyTestCase_100            10000 iterations, elapsed:        234 ms, max:      4.678 ms, avg:      0.023 ms", actual);
    }

    @Test
    public void testGetAvgNano() throws Exception {
	// given
	BenchmarkResults<String> underTest = create();
	// when
	long actual = underTest.getAvgNano();
	// then
	assertEquals(23456, actual);
    }

    @Test
    public void testGetAvgMs() throws Exception {
	// given
	BenchmarkResults<String> underTest = create();
	// when
	double actual = underTest.getAvgMs();
	// then
	assertEquals(.023, actual, .0);
    }

    @Test
    public void testGetMaxMs() throws Exception {
	// given
	BenchmarkResults<String> underTest = create();
	// when
	double actual = underTest.getAvgMs();
	// then
	assertEquals(.023, actual, .0);
    }

    private BenchmarkResults<String> create() {
	BenchmarkResults<String> underTest = new BenchmarkResults<>();
	underTest.setIterations(10_000);
	underTest.setLastResult("RESULT");
	underTest.setTestCase("MyTestCase_100");
	underTest.setTotalNano(234_567_890);
	underTest.setMaxNano(4_678_000);
	return underTest;
    }

}
