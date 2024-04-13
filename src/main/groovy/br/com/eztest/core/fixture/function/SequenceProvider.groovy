package br.com.eztest.core.fixture.function

import br.com.eztest.core.fixture.data.Sequence

public interface SequenceProvider {

    Sequence<?> newSequence();
}
