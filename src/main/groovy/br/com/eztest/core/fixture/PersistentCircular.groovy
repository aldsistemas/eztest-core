package br.com.eztest.core.fixture

import br.com.eztest.core.common.DatasourceProvider
import org.hibernate.Session

class PersistentCircular extends DataGenerator{
    
    int index = 0
    Class clazz
    Closure create
    Closure sessionProvider
    List restrictionClosures = []
    boolean nullable = false
    DatasourceProvider datasourceProvider
    
    PersistentCircular(Class clazz, Map config){
        if (!config.containsKey('datasourceProvider')){
            throw new IllegalArgumentException('datasourceProvider is required')
        }
        if (!config['datasourceProvider'] instanceof DatasourceProvider){
            throw new IllegalArgumentException('datasourceProvider must be a DatasourceProvider')
        }
        this.datasourceProvider = config['datasourceProvider']

        this.clazz = clazz;
        this.create = config.create
        if (config.sessionProvider){
            sessionProvider = config.sessionProvider
        } else {
            sessionProvider = { return datasourceProvider.getDefaultSession() }
        }
        if (config.restrictions){
            if (config.restrictions instanceof Collection){
                restrictionClosures.addAll(config.restrictions)
            }else{
                restrictionClosures.add(config.restrictions)
            }
        }
        if (config.containsKey('nullable')){
            this.nullable = config.nullable
        }
    }
    
    def next() {
        Session session = sessionProvider()
        String query = "SELECT e FROM ${clazz.name} as e"
        if (!restrictionClosures.isEmpty()){
            List rests = []
            restrictionClosures.each { rests.add( it() ) }
            query += ' WHERE '+rests.join(" AND ")+' ORDER BY id'
        }
        List ents = session.createQuery(query).list()
        if (ents.isEmpty()) {
            if (create == null){
                if (this.nullable){
                    return null;
                }else {
                    throw new IllegalStateException('Nao foi encontrada nenhuma instancia de '+clazz.getName())
                }
            }
            def newEnt = create()
            session.persist(newEnt)
            session.flush()
            ents.add(newEnt);
        }
        return ents.get((index++) % ents.size())
    }
}
