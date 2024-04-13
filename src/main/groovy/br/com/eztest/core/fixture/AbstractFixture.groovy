package br.com.eztest.core.fixture

abstract class AbstractFixture {
    
    def generator = [:]
    
    AbstractFixture(){
    }
    def create(Class clazz, Closure... closures){
        Object entity = clazz.newInstance()
        entity.metaClass.generator = this.generator
        for (c in closures){
            entity.with(c)
        }
        entity
    }
}
