package br.com.eztest.core.common


import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.postgresql.Driver

import javax.persistence.Table
import javax.sql.DataSource
import java.sql.Connection

abstract class AbstractPostgresqlDatasourceProvider extends DatasourceProvider {

    private SessionFactory sessionFactory1
    private Configuration configuration1

    private Session session1;

    private List tableNamesCache
    private DataSource dataSource

    private List entityClasses = getEntityClasses()

    abstract List<Class> getEntityClasses();

    abstract String getDatasourceUrl()

    abstract String getDatasourceUsername()

    abstract String getDatasourcePassword()

    List getTableNames() {
        if (tableNamesCache == null) {
            tableNamesCache = getEntityClasses().findAll { it.getAnnotation(Table.class) != null }.collect {
                it.getAnnotation(Table.class).name()
            }
        }
        return tableNamesCache
    }

    DataSource getDS() {
        if (dataSource == null) {
            dataSource = createDataSource()
        }
        return dataSource
    }

    Connection getConnection() {
        return getDS().getConnection()
    }

    Configuration getConfiguration1() {
        if (configuration1 == null) {
            configuration1 = getConfiguration(entityClasses)
        }
        return configuration1
    }

    SessionFactory getDefaultSessionFactory() {
        return getSessionFactory1()
    }

    SessionFactory getSessionFactory1() {
        if (sessionFactory1 == null) {
            sessionFactory1 = getConfiguration1().buildSessionFactory();
        }
        return sessionFactory1;
    }

    Configuration getConfiguration(Collection classes) {
        Configuration config = new Configuration();

        config.setProperty("hibernate.connection.url", getDatasourceUrl());
        config.setProperty("hibernate.connection.username", getDatasourceUsername());
        config.setProperty("hibernate.connection.password", getDatasourcePassword());
        config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        config.setProperty("hibernate.default_schema", "public");
        config.setProperty("hibernate.transaction.flush_before_completion", "true");
        config.setProperty("hibernate.current_session_context_class", "thread");
        for (Class<?> c : classes) {
            config.addAnnotatedClass(c);
        }
        return config
    }

    void closeAllSessions() {
        closeSession()
    }

    void closeSession() {
        commonCloseSession(session1)
    }

    void commonCloseSession(Session sess) {
        if (sess == null || !sess.isOpen()) {
            return;
        }
        try {
            if (sess.getTransaction().isActive()) {
                sess.getTransaction().commit();
            }
        } catch (final Throwable e) {
            e.printStackTrace();
            sess.getTransaction().rollback();
            return;
        }
        try {
            if (sess.getTransaction().isActive() && sess.getTransaction().wasCommitted()) {
                sess.getTransaction().rollback();
            }
        } catch (final Throwable e) {
            e.printStackTrace();
            sess.getTransaction().rollback();
            return;
        }
        try {
            if (sess.isOpen()) {
                sess.close();
            }
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }

    void finishAllSessions() {
        finishSession()
    }

    void finishSession() {
        if (session != null) {
            final Session em = getSession();
            em.flush();
            em.getTransaction().commit();
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    Session getDefaultSession() {
        return getSession()
    }

    Session getSession() {
        if (session1 == null || !session1.isOpen()) {
            return newSession();
        }
        return session1;
    }

    Session newSession() {
        final Session ret = sessionFactory1.openSession();
        ret.getTransaction().begin();
        session1 = ret;
        return ret;
    }


    def DataSource createDataSource() {
        return new SimpleDataSource(url: getDatasourceUrl(), username: getDatasourceUsername(), password: getDatasourcePassword(), driver: Driver.class)
    }
}


