package br.com.eztest.core.fixture.function

public class ExplicitValueFunction<T> implements AtomicFunction {

    private ValueEntryPoint<T> entryPoint;

    public ExplicitValueFunction(ValueEntryPoint<T> entryPoint) {
        this.entryPoint = entryPoint;
    }

    public ExplicitValueFunction() {
        this(new ValueEntryPoint<T>());
    }

    @Override
    public <T> T generateValue() {
        return (T) this.entryPoint.next();
    }

    public ValueEntryPoint<T> getEntryPoint() {
        return this.entryPoint;
    }

    public static class ValueEntryPoint<T> {

        private List<T> values = new ArrayList<>();
        private int     counter;

        public ValueEntryPoint() {
            super();
        }

        public ValueEntryPoint(final T... values) {
            this();
            set(values);
        }

        public ValueEntryPoint(final Collection<T> values) {
            this();
            set(values);
        }

        public T next() {
            if (this.values.isEmpty()) {
                return null;
            }
            return this.values.get((this.counter++) % this.values.size());
        }

        public void set(final Collection<T> values) {
            reset();
            add(values);
        }

        public void add(final Collection<T> values) {
            for (final T object : values) {
                this.values.add(object);
            }
        }

        public void set(final T... values) {
            reset();
            add(values);
        }

        public void add(final T... values) {
            for (final T object : values) {
                this.values.add(object);
            }
        }

        private void reset() {
            this.values.clear();
            this.counter = 0;
        }

    }

}
