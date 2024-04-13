package br.com.eztest.core.fixture.function;

public class SimpleValueFunction implements AtomicFunction {

    private Object value;

    public SimpleValueFunction(Object value) {
        this.value = value;
    }

    @Override
    public <T> T generateValue() {
        return (T) this.value;
    }
}
