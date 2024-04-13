package br.com.eztest.core


import br.com.eztest.core.common.AbstractPostgresqlDatasourceProvider

import org.hibernate.Session
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration

import spock.lang.Shared

import spock.lang.Specification

abstract class AbstractTest extends Specification {

    abstract AbstractPostgresqlDatasourceProvider getDatasourceProvider()

    @Shared
    SessionFactory sessionFactory1

    @Shared
    Configuration dbConfiguration1

    def setupSpec() {

        br.com.eztest.core.fixture.FixtureFactory.reset()
        sessionFactory1 = getDatasourceProvider().getSessionFactory1()
        dbConfiguration1 = getDatasourceProvider().getConfiguration1()

        String port = "8080";
        String property = System.getProperty("webServerPort");
        if (property) {
            port = property;
        }
        context.setBaseApplicationURL("http://localhost:" + port + "");

    }

    def cleanup() {
        getDatasourceProvider().finishAllSessions()
        getDatasourceProvider().closeAllSessions()
    }

    def setup() {
        br.com.eztest.core.fixture.FixtureFactory.reset()
        clearDatabase()
    }

    def clearDatabase() {
        def sc1 = getDatasourceProvider().getTableNames().findAll { !'schema_version'.equals(it) }.collect {
            'public.' + it
        }.join(',')

        getDatasourceProvider().getConnection().createStatement().execute('truncate ' + sc1 + ' cascade;');
    }

    def getTestContext() {
        context
    }

    def cleanupSpec() {
    }


    /**
     * Transforma uma entidade em um mapa, de modo que as chaves sao os nomes dos atributos e os valores, os valores desses atributos dentro da entidade.
     *
     * @param ent entidade a ser mapeada
     * @param properties propriedades da entidade a serem mapeadas
     * @return mapa da entidade
     */
    Map map(ent, List properties) {
        def ret = [:]
        properties.each {
            def val = ent.properties[it]
            if (val != null) {
                ret[it] = val
            }
        }
        ret
    }

    def save(entity) {
        Session sess = getDatasourceProvider().getSession()
        if (entity instanceof List) {
            entity.each { sess.save(it) }
        } else {
            sess.save(entity)
        }
        sess.flush();
    }

    def commitData() {
        getDatasourceProvider().finishAllSessions()
    }

//    void insertConfig(String key, String value) {
//        Session sess = DatasourceProvider.getSession()
//        sess.doWork(new Work() {
//            @Override
//            void execute(final Connection connection) throws SQLException {
//                def s = connection.createStatement()
//                s.executeUpdate("INSERT INTO ${getDatasourceProvider().schema1}.app_config VALUES ('${key}', '${value}')")
//            }
//        });
//    }
}

