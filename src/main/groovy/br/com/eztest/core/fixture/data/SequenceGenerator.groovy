package br.com.eztest.core.fixture.data

public class SequenceGenerator {

    private SequenceGenerator() {
        super();
    }

    public static Object[] seq(Number start, Number end) {
        int size = end.intValue() - start.intValue();
        Object[] ret = new Object[size];
        Number current = start;
        for (int i = 0; i < size; i++) {
            ret[i] = current;
            current = inc(current);
        }
        return ret;
    }

    private static Number inc(Number current) {

        if (current instanceof BigDecimal) {
            return new BigDecimal(current.toString()).add(BigDecimal.ONE);
        }
        if (current instanceof Integer) {
            return current.intValue() + 1;
        }
        if (current instanceof Long) {
            return current.longValue() + 1;
        }
        if (current instanceof Double) {
            return current.doubleValue() + 1;
        }
        throw new IllegalArgumentException("Sequence generator nao aceita numbers do tipo " + current.getClass().getName());
    }
}
