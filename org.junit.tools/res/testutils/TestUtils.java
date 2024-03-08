package testutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static final String SRC_TEST_RESOURCES = "src/test/resources/";

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

    public static boolean assertTestFileEquals(String relativePath, String actual) throws IOException {
	File testFile = new File(SRC_TEST_RESOURCES + relativePath);
	boolean ret = testFile.canRead();
	if (ret) {
	    String expected = readTestFile(relativePath);
	    assertEquals(expected, actual);
	} else {
	    testFile.getParentFile().mkdirs();
	    Files.write(testFile.toPath(), actual.getBytes(), StandardOpenOption.CREATE);
	}
	return ret;
    }

    public static String readTestFile(String relativePath) {
	try {
	    File testFile = new File(SRC_TEST_RESOURCES + relativePath);
	    // make sure that the filename has correct case to avoid flaky test between
	    // Windows and Linux
	    if (checkFileNameMismatch(testFile)) {
		throw new IllegalArgumentException(
			"Real file is not matching specified path: [" + relativePath + "]");
	    }
	    if (!testFile.exists()) { // create empty file if missing
		Files.write(testFile.toPath(), new byte[0]);
	    }
	    return Files.readAllLines(testFile.toPath()).stream()
		    .collect(Collectors.joining("\n"));
	} catch (IOException e) {
	    throw new TestUtilException("Failed to read test file: " + relativePath, e);
	}
    }

    private static boolean checkFileNameMismatch(File testFile) {
	String fileName = testFile.getName();
	File parentFolder = testFile.getParentFile();
	List<String> matchingNames = parentFolder.canRead() ? Arrays.asList(parentFolder
		.list((dir, name) -> fileName.equalsIgnoreCase(name)))
		: Collections.emptyList();
	return !matchingNames.isEmpty() && !matchingNames.contains(fileName);
    }

    public static byte[] readTestFileBinary(String relativePath) {
	try {
	    return Files.readAllBytes(Paths.get(SRC_TEST_RESOURCES + relativePath));
	} catch (IOException e) {
	    throw new TestUtilException("Failed to read test file binary: " + relativePath, e);
	}
    }

    /**
     * Construct a map using key-value pairs of strings.
     *
     * Becomes obsolete with Java 11, since Map.of can be used.
     */
    public static Map<String, String> mapOf(String... keyValue) {
	Map<String, String> ret = new LinkedHashMap<>();
	for (int i = 0; i < keyValue.length - 1; i += 2) {
	    ret.put(keyValue[i], keyValue[i + 1]);
	}
	return ret;
    }

    /**
     * Construct a map using key-value pairs.
     *
     * Becomes obsolete with Java 11, since Map.of can be used.
     */
    public static <K, V> Map<K, V> mapOf(List<K> keys, List<V> values) {
	Map<K, V> ret = new HashMap<>();
	IntStream.range(0, keys.size())
		.forEach(idx -> ret.put(keys.get(idx), values.get(idx)));
	return ret;
    }

    /**
     * Wrap given object in a ResponseEntity with {@link HttpStatus.OK}
     */
    public static <T> ResponseEntity<T> responseEntity(T body) {
	return new ResponseEntity<>(body, HttpStatus.OK);
    }

    /**
     * This method is very useful to do the "simplest" mocking of a method that is
     * expected to return the same value as one of the input parameters. For example
     * repository.save(entity) should return the entity itself, so it can be mocked
     * as:
     *
     * <pre>
     * when(repository.save(any())).thenAnswer(TestUtils.getSimplestAnswer(0));
     * </pre>
     *
     * or
     *
     * <pre>
     * doAnswer(TestUtils.getSimplestAnswer(0)).when(repository).save(any());
     * </pre>
     */
    public static <T> Answer<T> getSimplestAnswer(int returnArgument) {
	return invocation -> invocation.getArgument(returnArgument);
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
		    Stream.of(readValues.nextValue())
			    .forEach(ret::add);
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
	mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));

	mapper.setSerializationInclusion(Include.ALWAYS); // show all values, even nulls

	mapper.registerModule(new JavaTimeModule());

	return mapper;
    }

}
