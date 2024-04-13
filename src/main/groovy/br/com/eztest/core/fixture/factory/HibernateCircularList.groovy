package br.com.eztest.core.fixture.factory


import br.com.eztest.core.fixture.rule.Fixture
import org.hibernate.Session

public class HibernateCircularList<T> implements br.com.eztest.core.fixture.data.CircularList<T> {

    private static Map<Class<?>, String> identifiers = new HashMap<Class<?>, String>();
    private final Class<?> clazz;
    private final SessionProvider sessionProvider;
    private final String[] resctrictions;
    private String templateToCreateIfNeeded;
    private static final int MAX_TRIES = 5;

    HibernateCircularList(final Class<T> clazz, final SessionProvider sessionProvider, final String... resctrictions) {
        this.clazz = clazz;
        this.sessionProvider = sessionProvider;
        this.resctrictions = resctrictions;
    }

    HibernateCircularList(final Class<T> clazz, final String templateToCreateIfNeeded, final SessionProvider sessionProvider,
                          final String... resctrictions) {
        this.clazz = clazz;
        this.templateToCreateIfNeeded = templateToCreateIfNeeded;
        this.sessionProvider = sessionProvider;
        this.resctrictions = resctrictions;
    }

    @Override
    T getValue(final int index) {
        final Session session = this.sessionProvider.getSession();
        List<T> list = query(session);
        while (list.isEmpty()) {
            if (this.templateToCreateIfNeeded != null) {
                final Object gen = Fixture.from(this.clazz).gimme(this.templateToCreateIfNeeded);
                session.persist(gen);
                session.flush();
                list = query(session);
            } else {
                throw new IllegalStateException("Insufficient quantity of " + this.clazz.getName());
            }
        }
        if (list.isEmpty()) {
            throw new IllegalStateException("Impossivel criar um " + this.clazz.getName() + " que atenda as restricoes: "
                    + Arrays.asList(this.resctrictions));
        }
        return list.get(index % list.size());
    }

    private <T> List<T> query(Session session) {

        String idProp = HibernateCircularList.identifiers.get(this.clazz);
        if (idProp == null) {
            idProp = session.getSessionFactory().getClassMetadata(this.clazz).getIdentifierPropertyName();
            HibernateCircularList.identifiers.put(this.clazz, idProp);
        }
        String where = "";
        if (this.resctrictions != null && this.resctrictions.length > 0) {
            where = " WHERE 1=1 ";
            for (final String r : this.resctrictions) {
                where += " AND " + r.trim() + " ";
            }
        }
        final String string = "select x from " + this.clazz.getName() + " as x " + where + " order by x." + idProp;
        final List<T> list = session.createQuery(string).list();
        return list;
    }
}
