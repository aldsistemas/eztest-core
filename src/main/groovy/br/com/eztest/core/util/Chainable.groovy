package br.com.eztest.core.util

public interface Chainable {

    br.com.eztest.core.fixture.function.Function of(Class<?> clazz, String label);

    br.com.eztest.core.fixture.function.Function of(Class<?> clazz, String label, String targetAttribute);

    br.com.eztest.core.fixture.function.Function of(Class<? extends Enum<?>> clazz);

}
