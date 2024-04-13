package br.com.eztest.core.fixture.factory

import br.com.eztest.core.fixture.function.AtomicFunction
import br.com.eztest.core.fixture.function.RelationFunction

public abstract class FunctionPipeline implements RelationFunction {

    private AtomicFunction   aFunction;
    private RelationFunction rFunction;

    public FunctionPipeline(RelationFunction function) {
        this.rFunction = function;
    }

    public FunctionPipeline(AtomicFunction function) {
        this.aFunction = function;
    }

    public abstract <T> T pipeline(Object owner, final Object value);

    @Override
    public final <T> T generateValue(Object owner) {
        if (this.aFunction != null) {
            return pipeline(owner, this.aFunction.generateValue());
        }
        return pipeline(owner, this.rFunction.generateValue(owner));
    }

}
