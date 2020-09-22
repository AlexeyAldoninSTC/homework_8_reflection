package app;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {
        TestClass testClass = new TestClass(10, 'A', true, new HashMap<>(), "name");
        testClass.getMap().put(1, 10);
        testClass.getMap().put(2, 20);
        testClass.getMap().put(3, 30);

        Set<String> fieldsToCleanUp = new HashSet<>();
        fieldsToCleanUp.add("a");
        fieldsToCleanUp.add("isTrue");
        fieldsToCleanUp.add("name");
        fieldsToCleanUp.add("2");
        fieldsToCleanUp.add("c");

        Set<String> fieldsToOutput = new HashSet<>();
        fieldsToOutput.add("1");
        fieldsToOutput.add("3");

        System.out.println("testClass = " + testClass);
        cleanUp(testClass, fieldsToCleanUp, fieldsToOutput);
        System.out.println("testClass = " + testClass);
    }

    /**
     * Inspects and manages object fields.
     *
     * @param object          - target object to be modified
     * @param fieldsToCleanup - list of field names which are to be set in default values (null, 0, false)
     * @param fieldsToOutput  - list of field names which are to be printed out
     * @throws IllegalAccessException
     */
    static void cleanUp(Object object, Set<String> fieldsToCleanup, Set<String> fieldsToOutput) throws IllegalAccessException {
        final Field[] fields = object.getClass().getDeclaredFields();
        int matches = 0;
        for (Field field : fields) {
            if (Arrays.asList(field.getType().getInterfaces()).contains(Map.class) ||
                    field.getType().equals(Map.class)) {
                manageTheMap(object, fieldsToCleanup, fieldsToOutput, field);
                matches++;
                continue;
            }
            if (fieldsToCleanup.contains(field.getName())) {
                setFieldDefaultValue(object, field);
                matches++;
            }
            if (fieldsToOutput.contains(field.getName())) {
                System.out.println("field = " + field);
                matches++;
            }
        }
        if (matches == 0) {
            throw new IllegalArgumentException("В процессе проверки не выявлено ни одного совпадения " +
                    "со значениями в предоставленных списках полей");
        }
    }

    /**
     * Checks if any of Map keys string value is contained
     * in lists to clean up or output and takes appropriate actions
     * @param object - source object which field is to be managed
     * @param fieldsToCleanup - list of String values of possible keys to be removed
     * @param fieldsToOutput - list of String values of possible keys to be printed out
     * @param field - current field to be managed
     * @throws IllegalAccessException - in case of access failure
     */
    private static void manageTheMap(Object object, Set<String> fieldsToCleanup,
                                     Set<String> fieldsToOutput, Field field) throws IllegalAccessException {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        Map<Object, Object> map = (Map<Object, Object>) field.get(object);
        final Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
        int matchesCount = 0;
        while (iterator.hasNext()) {
            final Map.Entry<Object, Object> entry = iterator.next();
            Object key = entry.getKey();
            if (fieldsToOutput.contains(key.toString())) {
                matchesCount++;
                System.out.println(key);
            }
            if (fieldsToCleanup.contains(key.toString())) {
                matchesCount++;
                iterator.remove();
            }
        }
        if (matchesCount == 0) {
            throw new IllegalArgumentException("При анализе поля-наследника интерфейса Map " +
                    "не выявлено совпадений со значниями в списках на удаление или вывод");
        }
        field.setAccessible(isAccessible);
    }

    /**
     * Sets current field into its default value depending on its type
     * @param object - object with field is to be managed
     * @param field - current field
     * @throws IllegalAccessException - in case of failed access
     */
    private static void setFieldDefaultValue(Object object, Field field) throws IllegalAccessException {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        if (!field.getType().isPrimitive()) {
            field.set(object, null);
            return;
        }
        Class type = field.getType();
        Object value = field.get(object);
        if (type == boolean.class && Boolean.TRUE.equals(value)) {
            field.set(object, false);
            return;
        }
        if (type == char.class && (Character) value != 0) {
            field.set(object, (char) 0);
            return;
        }
        field.set(object, 0);
        field.setAccessible(accessible);
    }
}
