package br.com.eztest.core.fixture.function


import br.com.eztest.core.fixture.data.Sequence

public class BoundedSequenceFunction implements RelationFunction {

    private SequenceProvider         sequenceProvider;
    private Map<Object, Sequence<?>> sequences = new HashMap<>();
    private String                   attribute;

    public BoundedSequenceFunction(String attribute, SequenceProvider sequenceProvider) {
        this.attribute = attribute;
        this.sequenceProvider = sequenceProvider;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T generateValue(Object owner) {
        Object key = br.com.eztest.core.util.ReflectionUtils.getProperty(owner, this.attribute);
        Sequence<?> seq = sequences.get(key);
        if (seq == null) {
            seq = this.sequenceProvider.newSequence();
            sequences.put(key, seq);
        }
        return (T) seq.nextValue();
    }
}
