package br.com.eztest.core.fixture.function

import br.com.eztest.core.fixture.factory.SessionProvider
import br.com.eztest.core.fixture.rule.Fixture
import br.com.eztest.core.fixture.rule.ObjectFactory
import org.hibernate.Session

public class FixtureFunction implements AtomicFunction, RelationFunction {

    private final Class<?>  clazz;

    private final String    label;

    private Integer         quantity;

    private SessionProvider sessionProvider;

    public FixtureFunction(final Class<?> clazz, final String label) {
        this.clazz = clazz;
        this.label = label;
    }

    public FixtureFunction(final Class<?> clazz, final String label, final Integer quantity) {
        this(clazz, label);
        this.quantity = quantity;
    }

    public FixtureFunction(final SessionProvider sessionProvider, final Class<?> clazz, final String label) {
        this.sessionProvider = sessionProvider;
        this.clazz = clazz;
        this.label = label;
    }

    public FixtureFunction(final SessionProvider sessionProvider, final Class<?> clazz, final String label, final Integer quantity) {
        this(sessionProvider, clazz, label);
        this.quantity = quantity;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T generateValue() {
        return (T) generate(Fixture.from(this.clazz));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T generateValue(final Object owner) {
        return (T) generate(new ObjectFactory(Fixture.of(this.clazz), owner));
    }

    @SuppressWarnings("unchecked")
    private <T> T generate(final ObjectFactory objectFactory) {
        return (T) (this.quantity != null ? persist(objectFactory.gimme(this.quantity, this.label)) : persist(objectFactory
                .gimme(this.label)));
    }

    private <T> List<T> persist(final List<T> l) {
        for (final Object object : l) {
            persist(object);
        }
        return l;
    }

    private Object persist(final Object object) {
        if (this.sessionProvider != null) {
            final Session session = this.sessionProvider.getSession();
            session.persist(object);
            session.flush();
        }
        return object;
    }
}
