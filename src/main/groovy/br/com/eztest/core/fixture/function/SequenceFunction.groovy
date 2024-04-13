package br.com.eztest.core.fixture.function

import br.com.eztest.core.fixture.data.Sequence

public class SequenceFunction implements AtomicFunction {

    private Sequence<?> sequence;
    
    public SequenceFunction(Sequence<?> sequence) {
        super();
        this.sequence = sequence;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T generateValue() {
        return (T) sequence.nextValue();
    }
}
