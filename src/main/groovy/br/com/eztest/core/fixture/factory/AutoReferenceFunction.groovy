package br.com.eztest.core.fixture.factory


import br.com.eztest.core.fixture.function.RelationFunction

public class AutoReferenceFunction implements RelationFunction {

    private final String attributeName;

    public AutoReferenceFunction(final String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public <T> T generateValue(final Object owner) {
        return (T) br.com.eztest.core.util.ReflectionUtils.getProperty(owner, this.attributeName);
    }

}
