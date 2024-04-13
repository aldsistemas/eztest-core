package br.com.eztest.core.fixture

class DataFactory {
    
    static roundRobin(List list){
        return new Circular(list)
    }
    static roundRobin(Class clazz, Map data = [:]){
        return new PersistentCircular(clazz, data)
    }
    static sequence(Map mapa){
        
        def ret = new DataSequence()
        mapa.each { k,v ->
            ret.setProperty(k,v);
        }
        return ret;
    }
}
