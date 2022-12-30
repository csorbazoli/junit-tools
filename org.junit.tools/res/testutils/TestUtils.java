package testutils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mockito.stubbing.Answer;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class TestUtils {

	private static Logger log = Logger.getLogger("TestUtils");
	private static final String SRC_TEST_RESOURCES = "test/"; // "src/test/resources/";

	public static class TestUtilException extends RuntimeException {
		private static final long serialVersionUID = 429319185965049555L;

		public TestUtilException(String message, Throwable cause) {
			super(message, cause);
		}

		public TestUtilException(String message) {
			super(message);
		}

	}

	private static ObjectMapper objectMapper;

	private TestUtils() {
		// private constructor
	}

	/**
	 * @return normalized string where the hash-code values are replaced with string
	 *         literal @HASH
	 */
	public static String replaceHashCodes(String toString) {
		return toString.replaceAll("@[0-9a-f]+", "@HASH");
	}

	public static String readTestFile(String relativePath) {
		try {
			// TODO create if not exist
			// TODO alert if file name case is mismatching
			return Files.readAllLines(Paths.get(SRC_TEST_RESOURCES + relativePath)).stream()
					.collect(Collectors.joining("\n"));
		} catch (IOException e) {
			throw new TestUtilException("Failed to read test file: " + relativePath, e);
		}
	}

	public static byte[] readTestFileBinary(String relativePath) {
		try {
			return Files.readAllBytes(Paths.get(SRC_TEST_RESOURCES + relativePath));
		} catch (IOException e) {
			throw new TestUtilException("Failed to read test file binary: " + relativePath, e);
		}
	}

	/**
	 * @return Answer to be used in
	 *         <code>doAnswer(answer).when(mock).someMethod(matchers);</code><br/>
	 *         which simply returns the <b>argumentIdx</b> parameter as a return
	 *         value of the triggered method.<br/>
	 *         This is very useful for mocking repositories (i.e. save, update,
	 *         persist,...)
	 */
	public static <T> Answer<T> getSimplestAnswer(int argumentIdx) {
		return invocation -> invocation.getArgument(argumentIdx);
	}

	public static <T> T readObjectFromJsonFile(String relativePath, Class<T> clazz) {
		return objectFromJson(readTestFile(relativePath), clazz);
	}

	public static <T> List<T> readObjectListFromJsonFile(String relativePath, Class<T> clazz) {
		return readObjectListFromJson(readTestFile(relativePath), clazz);
	}

	public static <T> T objectFromJson(String json, TypeReference<T> clazz) {
		try {
			return !StringUtils.hasLength(json) ? null : getObjectMapper().readValue(json, clazz);
		} catch (Exception e) {
			throw new TestUtilException("Failed to read object from json: " + e.getMessage(), e);
		}
	}

	public static <T> T objectFromJson(String json, Class<T> clazz) {
		try {
			return !StringUtils.hasLength(json) ? null : getObjectMapper().readValue(json, clazz);
		} catch (Exception e) {
			throw new TestUtilException("Failed to read object from json: " + e.getMessage(), e);
		}
	}

	public static <T> List<T> readObjectListFromJson(String json, Class<T> clazz) {
		List<T> ret = new ArrayList<>();
		if (StringUtils.hasLength(json)) {
			ObjectMapper mapper = getObjectMapper();
			try {
				MappingIterator<T[]> readValues = mapper.readerForArrayOf(clazz).readValues(json);
				while (readValues.hasNext()) {
					Stream.of(readValues.nextValue()).forEach(ret::add);
				}
			} catch (Exception e) {
				throw new TestUtilException("Failed to read object list from json: " + e.getMessage(), e);
			}
		}
		return ret;
	}

	private synchronized void waitingInternal(long timeout) {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < timeout) {
			try {
				wait(100);
			} catch (Exception e) {
				log.warning("Waiting for " + timeout + " was interrupted: " + e.getMessage());
				break;
			}
		}
	}

	public static void waiting(long timeout) {
		new TestUtils().waitingInternal(timeout);
	}

	public static String objectToJson(Object object) {
		if (object == null) {
			return "{}";
		}
		try {
			ObjectMapper mapper = getObjectMapper();
			return mapper.writeValueAsString(object).replace("\r\n", "\n");
		} catch (Exception e) {
			return "JSON serialization failuire: " + e.getMessage();
		}
	}

	public static class BenchmarkResults<T> {

		private String testCase;
		private int iterations;
		private long totalNano;
		private long maxNano;
		private T lastResult;

		public String getTestCase() {
			return testCase;
		}

		public void setTestCase(String testCase) {
			this.testCase = testCase;
		}

		public int getIterations() {
			return iterations;
		}

		public void setIterations(int iterations) {
			this.iterations = iterations;
		}

		public long getTotalNano() {
			return totalNano;
		}

		public void setTotalNano(long totalNano) {
			this.totalNano = totalNano;
		}

		public long getMaxNano() {
			return maxNano;
		}

		public void setMaxNano(long maxNano) {
			this.maxNano = maxNano;
		}

		public T getLastResult() {
			return lastResult;
		}

		public void setLastResult(T lastResult) {
			this.lastResult = lastResult;
		}

		public long getAvgNano() {
			return totalNano / iterations;
		}

		public double getAvgMs() {
			return totalNano / iterations / 1_000 / 1_000d;
		}

		public double getMaxMs() {
			return maxNano / 1_000 / 1_000d;
		}

		@Override
		public String toString() {
			return String.format("%1$-20s %2$10d iterations, elapsed: %3$10d ms, max: %4$10.3f ms, avg: %5$10.3f ms",
					testCase, iterations, totalNano / 1_000_000L, getMaxMs(), getAvgMs());
		}
	}

	public static <T> BenchmarkResults<T> benchmark(String testCase, int iterations, Supplier<T> testMethod) {
		BenchmarkResults<T> ret = new BenchmarkResults<>();
		ret.testCase = testCase;
		ret.iterations = iterations;
		long start = System.nanoTime();
		long last;
		long current;
		long max = 0L;
		for (int idx = 0; idx < iterations; idx++) {
			last = System.nanoTime();
			ret.lastResult = testMethod.get();
			current = System.nanoTime() - last;
			max = Math.max(max, current);
		}

		long elapsed = System.nanoTime() - start;
		ret.totalNano = elapsed;
		ret.maxNano = max;
		log.info("Benchmark results: " + ret);
		return ret;
	}

	public static ObjectMapper getObjectMapper() {
		if (objectMapper == null) {
			objectMapper = getDefaultObjectMapper();
		}
		return objectMapper;
	}

	/**
	 * Use it only internally! For general usage the {@link #getObjectMapper()} is
	 * preferred.
	 */
	private static ObjectMapper getDefaultObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//	    mapper.setDateFormat(new SimpleDateFormat(getDateFormat()));

		mapper.setSerializationInclusion(Include.ALWAYS); // show all values, even nulls

		mapper.registerModule(new JavaTimeModule());

		return mapper;
	}

}
