package br.com.eztest.core.fixture.factory


import br.com.eztest.core.fixture.function.AtomicFunction
import br.com.eztest.core.fixture.function.RelationFunction

public class PropertyExtractor extends FunctionPipeline {

    private String property;

    public PropertyExtractor(String property, RelationFunction function) {
        super(function);
        this.property = property;
    }

    public PropertyExtractor(String property, AtomicFunction function) {
        super(function);
        this.property = property;
    }

    @Override
    public <T> T pipeline(Object owner, Object value) {
        return (T) br.com.eztest.core.util.ReflectionUtils.getProperty(value, this.property);
    }

}
