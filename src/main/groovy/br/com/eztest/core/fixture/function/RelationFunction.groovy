package br.com.eztest.core.fixture.function;


public interface RelationFunction extends Function {

    <T> T generateValue(Object owner);
    
}
