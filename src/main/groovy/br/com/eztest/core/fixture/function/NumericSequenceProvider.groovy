package br.com.eztest.core.fixture.function

import br.com.eztest.core.fixture.data.Sequence

public class NumericSequenceProvider implements SequenceProvider {

    private Number base;
    private int    amount;

    NumericSequenceProvider(Number base, int amount) {
        this.base = base;
        this.amount = amount;
    }

    @Override
    public Sequence<?> newSequence() {
        return new NumberSequence(base, amount);
    }

}
