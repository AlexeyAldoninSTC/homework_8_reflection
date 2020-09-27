package app;

import java.util.Map;

public class TestClass {
    private final int a;
    private final char c;
    private final boolean isTrue;
    private final byte b;
    private final Map<Integer, Integer> map;
    private final String name;

    public TestClass(int a, char c, boolean isTrue, byte b, Map<Integer, Integer> map, String name) {
        this.a = a;
        this.c = c;
        this.isTrue = isTrue;
        this.b = b;
        this.map = map;
        this.name = name;
    }

    public int getA() {
        return a;
    }

    public boolean isTrue() {
        return isTrue;
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TestClass{" +
                "a=" + a +
                ", c=" + c +
                ", isTrue=" + isTrue +
                ", b=" + b +
                ", map=" + map +
                ", name='" + name + '\'' +
                '}';
    }
}
