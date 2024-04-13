package br.com.eztest.core.fixture.function;

public class NullFunction implements AtomicFunction {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T generateValue() {
        return null;
    }

}
