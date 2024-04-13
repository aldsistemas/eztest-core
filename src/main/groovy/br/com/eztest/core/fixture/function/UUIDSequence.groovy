package br.com.eztest.core.fixture.function

import br.com.eztest.core.fixture.data.Sequence

class UUIDSequence implements Sequence<UUID> {

    private Random random;

    UUIDSequence(int seed) {
        super();
        this.random = new Random(seed);
    }
    UUIDSequence(String seed) {
        super();
        this.random = new Random(seed.hashCode());
    }
    UUIDSequence() {
        this(0)
    }

    @Override
    UUID nextValue() {
        return UUID.nameUUIDFromBytes(String.valueOf(random.nextLong()).getBytes());
    }
}
