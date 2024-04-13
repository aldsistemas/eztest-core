package br.com.eztest.core.fixture.function

import br.com.eztest.core.fixture.data.Sequence

class NumberSequence implements Sequence<Number> {

    private Number base;
    private int amount;
    private int multiplier;

    NumberSequence(Number base, int amount) {
        super();
        this.base = base;
        this.amount = amount;
    }

    @Override
    Number nextValue() {
        Number result = null;

        if (this.base instanceof Integer) {
            result = this.base.intValue() + (this.amount * this.multiplier);

        } else if (this.base instanceof Long) {
            result = this.base.longValue() + (this.amount * this.multiplier);

        } else if (this.base instanceof Float) {
            result = this.base.floatValue() + (this.amount * this.multiplier);

        } else if (this.base instanceof Double) {
            result = this.base.doubleValue() + (this.amount * this.multiplier);
        } else if (this.base instanceof BigDecimal) {
            result = new BigDecimal(base.toString()).add(new BigDecimal(this.amount).multiply(new BigDecimal(this.multiplier)));
        }

        this.multiplier++;

        return result;
    }
}
