package br.com.eztest.core.fixture.factory


import org.hibernate.Query
import org.hibernate.Session

class HibernateList<T> implements br.com.eztest.core.fixture.data.FlatList<T> {

    private static Map<Class<?>, String> identifiers = new HashMap<Class<?>, String>();
    private Class<?> clazz;
    private SessionProvider sessionProvider;
    private String[] restrictions;
    private Integer maxQuantity;

    HibernateList(Class<T> clazz, SessionProvider sessionProvider, String... restrictions) {
        this(clazz, sessionProvider, null, restrictions);
    }

    HibernateList(Class<T> clazz, SessionProvider sessionProvider, Integer maxQuantity, String... restrictions) {
        this.clazz = clazz;
        this.sessionProvider = sessionProvider;
        this.maxQuantity = maxQuantity;
        this.restrictions = restrictions;
    }

    @Override
    public Collection<T> get() {
        String idProp = identifiers.get(clazz);
        Session session = sessionProvider.getSession();
        if (idProp == null) {
            idProp = session.getSessionFactory().getClassMetadata(clazz).getIdentifierPropertyName();
            identifiers.put(clazz, idProp);
        }
        String where = "";
        if (this.restrictions != null && this.restrictions.length > 0) {
            where = " WHERE 1=1 ";
            for (String r : this.restrictions) {
                where += " AND x." + r.trim() + " ";
            }
        }
        Query query = session.createQuery("select x from " + clazz.getName() + " as x " + where + " order by x." + idProp);
        if (this.maxQuantity != null) {
            query = query.setMaxResults(this.maxQuantity);
        }
        List<T> list = query.list();
        return list;

    }
}
