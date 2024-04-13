package br.com.eztest.core.fixture.factory

import br.com.eztest.core.fixture.function.AtomicFunction
import br.com.eztest.core.fixture.function.RelationFunction

public class CollectionWrapper extends FunctionPipeline {

    private Class<? extends Collection> clazz;

    public CollectionWrapper(Class<? extends Collection> clazz, RelationFunction function) {
        super(function);
        this.clazz = clazz;
    }

    public CollectionWrapper(Class<? extends Collection> clazz, AtomicFunction function) {
        super(function);
        this.clazz = clazz;
    }

    @Override
    public <T> T pipeline(Object owner, Object value) {
        Collection<Object> c = null;

        if (Set.class.isAssignableFrom(this.clazz)) {
            c = new HashSet<>();
        } else if (List.class.isAssignableFrom(this.clazz)) {
            c = new ArrayList<>();
        } else {
            c = new HashSet<>();
        }
        c.add(value);
        return (T) c;
    }

}
