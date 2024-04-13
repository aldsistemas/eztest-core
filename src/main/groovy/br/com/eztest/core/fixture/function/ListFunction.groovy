package br.com.eztest.core.fixture.function

import br.com.eztest.core.fixture.data.FlatList

import java.lang.reflect.Field

public class ListFunction implements RelationFunction {

    private final String              targetProperty;
    private final FlatList<?>         list;
    private Integer                   maxQuantity;

    private static Map<String, Field> fieldsCache = new HashMap<>();

    public ListFunction(final String targetProperty, Object... list) {
        this.targetProperty = targetProperty;
        this.list = new FixedList<>(Arrays.asList(list));
    }

    public ListFunction(final String targetProperty, List<?> list) {
        this.targetProperty = targetProperty;
        this.list = new FixedList<>(list);
    }

    public ListFunction(final String targetProperty, final FlatList<?> list) {
        this.targetProperty = targetProperty;
        this.list = list;
    }

    public ListFunction(final String targetProperty, final FlatList<?> list, final int maxQuantity) {
        this.targetProperty = targetProperty;
        this.list = list;
        this.maxQuantity = maxQuantity;
    }

    private static class FixedList<T> implements FlatList<T> {
        private Collection<T> list;

        public FixedList(final List<T> list) {
            this.list = list;
        }

        public FixedList(final T... list) {
            this(Arrays.asList(list));
        }

        @Override
        public Collection<T> get() {
            return this.list;
        }
    }

    @Override
    public <T> T generateValue(final Object owner) {
        final Collection<?> query = this.list.get();
        if (this.maxQuantity != null && query.size() > this.maxQuantity.intValue()) {
            final List<Object> l = new ArrayList<>();
            final Iterator<?> i = query.iterator();
            int remain = this.maxQuantity.intValue();
            while (remain-- > 0) {
                l.add(i.next());
            }
        }
        Class<?> pub;
        try {
            Collection c = null;
            pub = getPropertyType(owner, this.targetProperty);
            if (pub.isAssignableFrom(Set.class)) {
                c = new HashSet<>(query);
            } else if (pub.isAssignableFrom(List.class)) {
                c = new ArrayList<>(query);
            } else {
                throw new UnsupportedOperationException("Collection " + pub.getName() + " not supported");
            }
            ReflectionUtils.setProperty(owner, this.targetProperty, c);
            return (T) c;
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private Field getField(final Class<?> clazz, final String attribute) {
        final String key = clazz.getName() + "#" + attribute;
        if (!ListFunction.fieldsCache.containsKey(key)) {
            Class<?> current = clazz;
            Field f = null;
            while (!current.equals(Object.class) && f == null) {
                final Field[] fields = current.getDeclaredFields();
                for (final Field field : fields) {
                    if (field.getName().equals(attribute)) {
                        f = field;
                        break;
                    }
                }
                current = current.getSuperclass();
            }
            ListFunction.fieldsCache.put(key, f);
        }
        return ListFunction.fieldsCache.get(key);
    }

    private Class<?> getPropertyType(final Object owner, final String targetProperty2) throws NoSuchFieldException, SecurityException {

        final String[] parts = targetProperty2.split("[.]");
        Class<?> current = owner.getClass();
        for (final String part : parts) {

            final Field field = getField(current, part);
            if (field == null) {
                // chamada deste metodo existe apenas para forcar o throw new NoSuchFieldException. Neste ponto sabe-se
                // que o field nao existe nem na classe e nem nas superclasses.
                owner.getClass().getDeclaredField(part);
            }
            current = field.getType();
        }
        return current;
    }

}
