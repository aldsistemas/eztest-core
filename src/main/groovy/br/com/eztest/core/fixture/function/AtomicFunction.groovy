package br.com.eztest.core.fixture.function;


public interface AtomicFunction extends Function {

    <T> T generateValue();

}
