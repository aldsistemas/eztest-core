package br.com.eztest.core.fixture.factory

import br.com.eztest.core.fixture.data.Sequence

public class StringWrappedSequence implements Sequence<String> {

    private final Number base;
    private final int    amount;
    private int          multiplier;
    private final String prefix;
    private final String suffix;
    private Integer      mod;

    public StringWrappedSequence(final String prefix, final String suffix, final Number base, final int amount, Integer mod) {
        super();
        this.mod = mod;
        this.prefix = prefix != null ? prefix : "";
        this.suffix = suffix != null ? suffix : "";
        this.base = base;
        this.amount = amount;
    }

    public Number getNext() {
        Number result = null;

        if (this.base instanceof Integer) {
            result = this.base.intValue() + this.amount * this.multiplier;
            if (mod != null) {
                result = (int) (result.intValue() % mod.intValue());
            }

        } else if (this.base instanceof Long) {
            result = this.base.longValue() + this.amount * this.multiplier;
            if (mod != null) {
                result = (long) (result.longValue() % mod.intValue());
            }

        } else if (this.base instanceof Float) {
            result = this.base.floatValue() + this.amount * this.multiplier;
            if (mod != null) {
                result = (float) (result.floatValue() % mod.intValue());
            }

        } else if (this.base instanceof Double) {
            result = this.base.doubleValue() + this.amount * this.multiplier;
            if (mod != null) {
                result = (double) (result.doubleValue() % mod.intValue());
            }
        }

        this.multiplier++;

        return result;
    }

    @Override
    public String nextValue() {
        return prefix + getNext() + suffix;
    }
}
