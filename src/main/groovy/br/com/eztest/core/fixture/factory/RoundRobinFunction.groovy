package br.com.eztest.core.fixture.factory


import br.com.eztest.core.fixture.function.AtomicFunction

class RoundRobinFunction implements AtomicFunction {

    private int counter = 0;
    private final br.com.eztest.core.fixture.data.CircularList<?> list;

    RoundRobinFunction(final br.com.eztest.core.fixture.data.CircularList<?> list) {
        this.list = list;
    }

    RoundRobinFunction(final List<?> list) {
        this(list.toArray());
    }

    RoundRobinFunction(final Object... list) {
        this.list = new FixedCircular<>(list);
    }

    @Override
    def generateValue() {

        return this.list.getValue(this.counter++);
    }

    private static class FixedCircular<T> implements br.com.eztest.core.fixture.data.CircularList<T> {
        private Object[] list;

        public FixedCircular(final List<T> list) {
            this(list.toArray());
        }

        public FixedCircular(final Object... list) {
            this.list = list;
        }

        @Override
        public <T> T getValue(int index) {
            return (T) this.list[index++ % this.list.length];
        }

    }
}
