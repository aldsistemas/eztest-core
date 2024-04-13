package br.com.eztest.core.fixture.data;

/**
 * Sequencia de valores gerados dinamicamente.
 *
 * @param <T> tipo dos valores
 */
interface Sequence<T> {

    T nextValue();
    
}
