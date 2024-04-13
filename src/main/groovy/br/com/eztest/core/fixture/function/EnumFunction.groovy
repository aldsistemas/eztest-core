package br.com.eztest.core.fixture.function

public class EnumFunction implements AtomicFunction {
    public Class<? extends Enum<?>> clazz;
    public int quantity;
    
    public EnumFunction(Class<? extends Enum<?>> clazz, int quantity) {
        this.clazz = clazz;
        this.quantity = quantity;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T generateValue() {
        List<T> results = new ArrayList<T>();
        AtomicFunction function = new RandomFunction(clazz);
        for (int i = 0; i < quantity; i++) {
            results.add((T) function.generateValue());
        }
        
        return (T) results;
    }

}
