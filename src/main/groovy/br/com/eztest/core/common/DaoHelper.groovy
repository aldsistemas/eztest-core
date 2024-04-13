package br.com.eztest.core.common

import org.hibernate.Session

class DaoHelper {
    
    static get(Class c, id = null, session = null){
        Session s = session;
        if (s == null){
            s = AbstractPostgresqlDatasourceProvider.getSession()
        }
        String query = "SELECT e FROM ${c.name} AS e"
        if (id != null){
            query += ' WHERE e.id = '+id
        }
        def l = s.createQuery(query).list()
        return l.isEmpty() ? null : l[0]
    }

    static int count(Class c, session = null) {
        Session s = session;
        if (s == null){
            s = AbstractPostgresqlDatasourceProvider.getSession()
        }
        String query = "SELECT count(e) FROM ${c.name} AS e"
        def l = s.createQuery(query).list()
        return l.isEmpty() ? 0 : ((Number)l[0]).intValue()

    }
}
