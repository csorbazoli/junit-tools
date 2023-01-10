package org.junit.tools.generator.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("unchecked")
public class TestValueFactory {

    private static final Logger log = Logger.getLogger("TestValueFactory");

    private static final int DEPTH_LIMIT = 100;

    private static final char CHAR = 'a';
    private static final byte BYTE = 23;
    private static final int INT = 123;
    private static final long LONG = 123456L;
    private static final float FLOAT = 23.4567f;
    private static final double DOUBLE = 12.3456d;
    private static final int BIGINTEGER = 127;
    private static final double BIGDECIMAL = 543.21;

    private TestValueFactory() {
	// private constructor
    }

    public static <T> T getValueForType(Type type, String name) {
	return (T) getValueForTypeInner(type, name, new ArrayDeque<>());
    }

    public static <T> T fillFields(T item) {
	fillNonFinalFields(item.getClass(), item, new ArrayDeque<>());
	return item;
    }

    public static <T> T fillFieldsWithoutDeprecated(T item) {
	fillNonFinalFields(item.getClass(), item, new ArrayDeque<>(), true);
	return item;
    }

    private static Object getValueForTypeInner(Type type, String name, Deque<FieldReference> stack) {
	if (stack.size() >= 100) {
	    log.severe("Object structure is too deep (>" + DEPTH_LIMIT + "), could be a recursive structure?! "
		    + getStackInfo(stack));
	    // we could detect and avoid "parent" references, that could cause infinite
	    // recursions
	    return null;
	}
	Object ret = getValueForPrimitiveTypes(type, name);
	if (ret == null) {
	    Class<?> clazz = getClassOfType(type);
	    if (clazz == null) {
		log.warning("Failed to determine type of " + type);
		ret = null;
	    } else if (clazz.isArray()) {
		ret = toSingletonArray(getValueForTypeInner(clazz.getComponentType(), name, stack));
	    } else {
		ret = getValueForObjects(type, name, stack);
	    }
	}
	return ret;
    }

    private static String getStackInfo(Deque<FieldReference> stack) {
	StringBuilder ret = new StringBuilder();
	Iterator<FieldReference> iterator = stack.iterator();
	String sep = "";
	Set<String> items = new HashSet<>();
	while (iterator.hasNext()) {
	    String nextRef = iterator.next().getReference();
	    ret.append(sep).append(nextRef);
	    sep = " -> ";
	    if (!items.add(nextRef)) {
		ret.append("...");
		break;
	    }
	}
	return ret.toString();
    }

    // https://stackoverflow.com/questions/3152290/java-how-can-i-dynamically-create-an-array-of-a-specified-type-based-on-the-typ
    private static <T> T[] toArray(Collection<T> c) {
	if (c != null) {
	    Iterator<T> i = c.iterator();
	    if (i.hasNext()) {
		T o = i.next();
		if (o != null) {
		    /* Create an array of the type of the first non-null element. */
		    Class<?> type = o.getClass();
		    T[] arr = (T[]) Array.newInstance(type, c.size());
		    arr[0] = o;
		    int idx = 1;
		    while (i.hasNext()) {
			/* Make sure collection is really homogenous with cast() */
			arr[idx++] = (T) type.cast(i.next());
		    }
		    return arr;
		}
	    }
	}
	return (T[]) new Object[0];
    }

    private static <T> T[] toSingletonArray(T item) {
	if (item != null) {
	    /* Create an array of the type of the first non-null element. */
	    Class<?> type = item.getClass();
	    T[] arr = (T[]) Array.newInstance(type, 1);
	    arr[0] = item;
	    return arr;
	}
	return (T[]) new Object[0];
    }

    public static Object getValueForPrimitiveTypes(Type type, String name) {
	Object ret;
	if (boolean.class.equals(type) || Boolean.class.equals(type)) {
	    ret = true;
	} else if (byte.class.equals(type) || Byte.class.equals(type)) {
	    ret = BYTE;
	} else if (char.class.equals(type) || Character.class.equals(type)) {
	    ret = CHAR;
	} else if (int.class.equals(type) || Integer.class.equals(type)) {
	    ret = INT;
	} else if (long.class.equals(type) || Long.class.equals(type)) {
	    ret = LONG;
	} else if (double.class.equals(type) || Double.class.equals(type)) {
	    ret = DOUBLE;
	} else if (float.class.equals(type) || Float.class.equals(type)) {
	    ret = FLOAT;
	} else if (String.class.equals(type)) {
	    ret = "Test" + StringUtils.capitalize(name);
	} else {
	    ret = null;
	}
	return ret;
    }

    private static Object getValueForObjects(Type type, String name, Deque<FieldReference> stack) {
	Object ret;
	try {
	    Class<?> clazz = getClassOfType(type);
	    if (type.equals(Object.class)) {
		ret = "Object";
	    } else if (List.class.isAssignableFrom(clazz) || clazz.equals(Collection.class)) {
		ret = Arrays.asList(getValueForTypeInner(getGenericParameterType(type, 0), name, stack));
	    } else if (SortedSet.class.isAssignableFrom(clazz)) {
		ret = new TreeSet<>();
		createValueForSet(ret, name, type, stack);
	    } else if (Set.class.isAssignableFrom(clazz)) {
		ret = new HashSet<>();
		createValueForSet(ret, name, type, stack);
	    } else if (SortedMap.class.isAssignableFrom(clazz)) {
		ret = new TreeMap<>();
		createValueForMap(ret, name, type, stack);
	    } else if (Map.class.isAssignableFrom(clazz)) {
		ret = new HashMap<>();
		createValueForMap(ret, name, type, stack);
	    } else if (BigDecimal.class.equals(clazz)) {
		ret = BigDecimal.valueOf(BIGDECIMAL);
	    } else if (BigInteger.class.equals(clazz)) {
		ret = BigInteger.valueOf(BIGINTEGER);
	    } else if (Locale.class.equals(clazz)) {
		ret = Locale.ENGLISH;
	    } else if (Charset.class.equals(clazz)) {
		ret = StandardCharsets.UTF_8;
//	    } else if (JSONObject.class.equals(clazz)) {
//		ret = new JSONObject("{}");
	    } else if (Pattern.class.equals(clazz)) {
		ret = Pattern.compile(".*");
	    } else if (Date.class.equals(clazz)) {
		ret = getTestDate();
	    } else if (LocalDate.class.equals(clazz)) {
		ret = LocalDate.of(2019, 3, 26);
	    } else if (LocalTime.class.equals(clazz)) {
		ret = LocalTime.of(12, 34, 56);
	    } else if (LocalDateTime.class.equals(clazz)) {
		ret = LocalDateTime.of(2019, 3, 26, 12, 34, 56);
	    } else if (GregorianCalendar.class.equals(clazz)) {
		ret = createGregorianCalendar();
	    } else if (XMLGregorianCalendar.class.equals(clazz)) {
		ret = DatatypeFactory.newInstance().newXMLGregorianCalendar(createGregorianCalendar());
	    } else if (URL.class.equals(clazz)) {
		ret = new URL("http://google.com");
	    } else if (clazz.isEnum()) {
		ret = getDefaultEnumValue(clazz);
	    } else if (InputStream.class.equals(clazz)) {
		ret = new ByteArrayInputStream(name.getBytes());
	    } else if (Throwable.class.isAssignableFrom(clazz)) {
		ret = createExceptionOfType(type, name);
	    } else if (hasDefaultConstructor(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
		ret = clazz.newInstance();
		fillNonFinalFields(clazz, ret, stack);
	    } else {
		log.warning("Unhandled type " + getTypeAsString(type));
		ret = null;
	    }
	} catch (Exception e) {
	    log.warning("Failed to create initial value for " + getTypeAsString(type) + ": " + e.getMessage());
	    ret = null;
	}
	return ret;
    }

    private static Throwable createExceptionOfType(Type type, String name) {
	Throwable ret = null;
	try {
	    Class<?> clazz = getClassOfType(type);
	    ret = createInstanceByName(type, name, ret, clazz);
	} catch (Exception e) {
	    log.warning("Failed to create initial value for " + getTypeAsString(type) + ": " + e.getMessage());
	}
	return ret;
    }

    private static Throwable createInstanceByName(Type type, String name, Throwable ret, Class<?> clazz)
	    throws InstantiationException, IllegalAccessException, InvocationTargetException {
	try {
	    Constructor<?> stringContructor = clazz.getConstructor(String.class);
	    ret = (Throwable) stringContructor.newInstance(name + " value");
	} catch (NoSuchMethodException e) {
	    if (hasDefaultConstructor(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
		ret = (Throwable) clazz.newInstance();
	    } else {
		log.warning("Failed to create initial value for " + getTypeAsString(type)
			+ ": No String or default constructor is available");
	    }
	}
	return ret;
    }

    public static Date getTestDate() {
	Date ret;
	Calendar date = Calendar.getInstance(Locale.US);
	date.set(2023, 11, 25, 21, 34, 56);
	date.set(Calendar.MILLISECOND, 789);
	ret = Date.from(date.toInstant());
	return ret;
    }

    private static GregorianCalendar createGregorianCalendar() {
	GregorianCalendar ret = new GregorianCalendar(2019, 2, 26, 12, 34, 56);
	ret.setTimeZone(TimeZone.getTimeZone(ZoneId.of("+0100")));
	return ret;
    }

    private static void createValueForMap(Object map, String name, Type maptype, Deque<FieldReference> stack) {
	Object key = getValueForTypeInner(getGenericParameterType(maptype, 0), name, stack);
	if (key == null) {
	    log.warning("Could not create key value with type " + getTypeAsString(maptype) + " for map " + name);
	} else {
	    Object value = getValueForTypeInner(getGenericParameterType(maptype, 1), name, stack);
	    ((Map<Object, Object>) map).put(key, value);
	}
    }

    private static void createValueForSet(Object set, String name, Type settype, Deque<FieldReference> stack) {
	Object value = getValueForTypeInner(getGenericParameterType(settype, 0), name, stack);
	if (value == null) {
	    log.warning("Could not create value value with type " + getTypeAsString(settype) + " for set {}" + name);
	} else {
	    ((Set<Object>) set).add(value);
	}
    }

    private static void fillNonFinalFields(Class<?> type, Object item, Deque<FieldReference> stack) {
	fillNonFinalFields(type, item, stack, false);
    }

    private static void fillNonFinalFields(Class<?> type, Object item, Deque<FieldReference> stack,
	    boolean excludeDeprecated) {
	// set each non-final fields which has a public setter method
	FieldReference parent = stack.peekLast();
	fillFieldsInternal(type, item, stack, parent, excludeDeprecated);
    }

    private static void fillFieldsInternal(Class<?> type, Object item, Deque<FieldReference> stack,
	    FieldReference parent, boolean excludeDeprecated) {
	for (Field f : type.getDeclaredFields()) {
	    if (excludeDeprecated && f.isAnnotationPresent(Deprecated.class)) {
		continue;
	    }
	    if (!Modifier.isAbstract(f.getModifiers()) && !f.isSynthetic() && !Modifier.isStatic(f.getModifiers())) {
		// disregard final primitive values, they cannot be modified
		Object curValue = getInternalState(item, type, f);
		FieldReference fieldRef = new FieldReference(item, type.getSimpleName() + "#" + f.getName());
		if (stack.contains(fieldRef)) { // repeating reference - loop in structure hierarchy?
		    log.warning(
			    "Loop in structure hierarchy? " + getStackInfo(stack) + " -> " + fieldRef.getReference());
		} else {
		    stack.addLast(fieldRef);
		    try {
			if (!Modifier.isFinal(f.getModifiers()) && (curValue == null || f.getType().isPrimitive())) {
			    setFieldValueOfItem(item, f, type, parent, stack);
			} else if (curValue instanceof List<?>) {
			    List<Object> list = (List<Object>) curValue;
			    if (list.isEmpty()) {
				Object listItem = getValueForTypeInner(getGenericParameterType(f.getGenericType(), 0),
					f.getName(), stack);
				if (listItem != null) {
				    list.add(listItem);
				}
			    }
			} else if (curValue instanceof Set<?> && ((Set<?>) curValue).isEmpty()) {
			    createValueForSet(curValue, f.getName(), f.getGenericType(), stack);
			} else if (curValue instanceof Map<?, ?> && ((Map<?, ?>) curValue).isEmpty()) {
			    createValueForMap(curValue, f.getName(), f.getGenericType(), stack);
			}
		    } finally {
			stack.removeLast();
		    }
		}
	    }
	}
	Class<?> parentClass = type.getSuperclass();
	if (parentClass != null && !Object.class.equals(parentClass)) {
	    fillFieldsInternal(parentClass, item, stack, parent, excludeDeprecated);
	}
    }

    private static Object getInternalState(Object item, Class<?> type, Field f) {
	Method getter = findGetterMethod(type, f);
	Object ret = null;
	try {
	    ret = getter.invoke(item);
	} catch (Exception e) {
	    log.warning("Failed to get initial value of " + type.getName() + "." + f.getName() + ": " + e.getMessage());
	}
	return ret;
    }

    private static void setFieldValueOfItem(Object item, Field f, Class<?> type, FieldReference parent,
	    		Deque<FieldReference> stack) {
	Method setter = findSetterMethod(type, f);
	if (setter != null || Modifier.isPublic(f.getModifiers())) {
	    try {
		Object fieldValue;
		if (isParentReference(f, parent)) {
		    fieldValue = parent.getItem();
		} else {
		    fieldValue = getValueForTypeInner(f.getGenericType(), f.getName(), stack);
		}
		if (setter == null) {
		    f.set(item, fieldValue);
		} else {
		    setter.invoke(item, fieldValue);
		}
	    } catch (Exception e) {
		log.warning(
			"Failed to set initial value of " + type.getName() + "." + f.getName() + ": " + e.getMessage());
	    }
	}
    }

    private static boolean isParentReference(Field f, FieldReference parent) {
	return parent != null && f.getType().isAssignableFrom(parent.getItem().getClass());
    }

    protected static Method findSetterMethod(Class<?> clazz, Field f) {
	Method ret = null;
	String setter = "set" + StringUtils.capitalize(f.getName());
	try {
	    ret = clazz.getDeclaredMethod(setter, f.getType());
	} catch (NoSuchMethodException | SecurityException e) {
	    ret = findIsSetterMethod(clazz, f);
	    if (ret == null) {
		log.warning("No setter method found " + clazz.getName() + "." + setter + ": " + e.getMessage());
	    }
	}
	return ret;
    }

    private static Method findGetterMethod(Class<?> clazz, Field f) {
	Method ret = null;
	String getter = "get" + StringUtils.capitalize(f.getName());
	try {
	    ret = clazz.getDeclaredMethod(getter);
	} catch (NoSuchMethodException | SecurityException e) {
	    ret = findIsSetterMethod(clazz, f);
	    if (ret == null) {
		log.warning("No getter method found " + clazz.getName() + "." + getter + ": " + e.getMessage());
	    }
	}
	return ret;
    }

    private static Method findIsSetterMethod(Class<?> clazz, Field f) {
	Method ret = null;
	if (f.getName().startsWith("is")) {
	    String setter = "set" + StringUtils.capitalize(f.getName().replaceFirst("^is", ""));
	    try {
		ret = clazz.getDeclaredMethod(setter, f.getType());
	    } catch (NoSuchMethodException | SecurityException e) {
		log.warning("No setter method found " + clazz.getName() + "." + setter + ": " + e.getMessage());
	    }
	}
	return ret;
    }

    private static boolean hasDefaultConstructor(Class<?> type) {
	try {
	    return type.getConstructor() != null;
	} catch (NoSuchMethodException e) {
	    return false;
	} catch (Exception t) { // COVERAGE: how to test this exception?
	    log.warning("Class loading problem at " + type.getName() + ": " + t.getMessage());
	    return false;
	}
    }

    private static Class<?> getClassOfType(Type type) {
	if (type instanceof Class<?>) {
	    return (Class<?>) type;
	}
	if (type instanceof ParameterizedType) {
	    return (Class<?>) ((ParameterizedType) type).getRawType();
	}
	if (type instanceof TypeVariable) {
	    return Object.class;
	}
	return null;
    }

    private static Type getGenericParameterType(Type type, int idx) {
	Type ret = null;
	if (type instanceof ParameterizedType) {
	    Type[] typeParameters = ((ParameterizedType) type).getActualTypeArguments();
	    if (idx < typeParameters.length) {
		ret = typeParameters[idx];
	    }
	}
	if (ret == null) {
	    log.warning(
		    "There is no generic parameter type with index " + idx + " available for " + getTypeAsString(type));
	}
	return ret;
    }

    private static <T> T getDefaultEnumValue(Class<T> enumType) {
	T ret = null;
	if (enumType.isEnum()) {
	    ret = enumType.getEnumConstants()[0];
	}
	return ret;
    }

    private static String getTypeAsString(Type type) {
	String ret = type.getTypeName().replace("$", "."); // inner classes are marked by "$"
	if (type instanceof Class<?>) {
	    Class<?> clazz = (Class<?>) type;
	    String packageName = clazz.getPackage() == null ? "" : clazz.getPackage().getName();
	    if (clazz.getPackage() == null || "java.lang".equals(packageName)) {
		ret = clazz.getSimpleName();
	    }
	} else if (type instanceof ParameterizedType) {
	    ParameterizedType pclazz = (ParameterizedType) type;
	    Type[] typeParameters = ((ParameterizedType) type).getActualTypeArguments();
	    ret = getTypeAsString(pclazz.getRawType()) + "<" + Arrays.asList(typeParameters).stream()
		    .map(TestValueFactory::getTypeAsString).collect(Collectors.joining(", ")) + ">";
	} else if (type instanceof TypeVariable) {
	    ret = "Object";
	}
	return ret;
    }

    private static class FieldReference {

	private final Object item;
	private final String reference;

	public FieldReference(Object item, String reference) {
	    this.item = item;
	    this.reference = reference;
	}

	public Object getItem() {
	    return item;
	}

	public String getReference() {
	    return reference;
	}

	@Override
	public int hashCode() {
	    int prime = 31;
	    int result = 1;
	    result = prime * result + (reference == null ? 0 : reference.hashCode());
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) {
		return true;
	    }
	    if (obj == null) {
		return false;
	    }
	    if (getClass() != obj.getClass()) {
		return false;
	    }
	    FieldReference other = (FieldReference) obj;
	    if (reference == null) {
		return other.reference == null;
	    }
	    return reference.equals(other.reference);
	}

    }

}
