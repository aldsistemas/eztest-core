package br.com.eztest.core.fixture

class Circular extends DataGenerator{
    
    int index = 0
    List list
    
    Circular(List list){
        this.list = list
    }
    
    def next() {
        return list.get((index++) % list.size())
    }
}
