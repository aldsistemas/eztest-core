package br.com.eztest.core.fixture.rule

class ObjectFactory {
    private static final String NO_SUCH_LABEL_MESSAGE = "%s-> No such label: %s"
    private TemplateHolder templateHolder
    private Object owner

    ObjectFactory(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder
    }

    ObjectFactory(TemplateHolder templateHolder, Object owner) {
        this.templateHolder = templateHolder
        this.owner = owner
    }

    <T> T gimme(String label) {
        Rule rule = templateHolder.getRules().get(label)
        if (rule == null) {
            throw new IllegalArgumentException(String.format(NO_SUCH_LABEL_MESSAGE, templateHolder.getClazz().getName(), label))
        }
        return (T) createObject(rule)
    }

    <T> List<T> gimme(Integer quantity, String label) {
        Rule rule = templateHolder.getRules().get(label)
        if (rule == null) {
            throw new IllegalArgumentException(String.format(NO_SUCH_LABEL_MESSAGE, templateHolder.getClazz().getName(), label))
        }
        List<T> results = new ArrayList<T>(quantity)
        for (int i = 0; i < quantity; i++) {
            results.add((T) createObject(rule))
        }
        return results
    }

    private Object createObject(Rule rule) {
        Map<String, Object> constructorArguments = new HashMap<String, Object>()
        List<Property> deferredProperties = new ArrayList<Property>()
        List<String> parameterNames = new ArrayList<>()
        for (Property property : rule.getProperties()) {
            if (parameterNames.contains(property.getRootAttribute())) {
                constructorArguments.put(property.getName(), property.getValue())
            } else {
                deferredProperties.add(property)
            }
        }
        Object result = br.com.eztest.core.util.ReflectionUtils.newInstance(templateHolder.getClazz(),
                processConstructorArguments(parameterNames, constructorArguments))
        for (Property property : deferredProperties) {
            br.com.eztest.core.util.ReflectionUtils.setProperty(result, property.getName(), processPropertyValue(result, property))
        }
        return result
    }

    private List<Object> processConstructorArguments(List<String> parameterNames, Map<String, Object> arguments) {
        List<Object> values = new ArrayList<Object>()
        if (owner != null && br.com.eztest.core.util.ReflectionUtils.isInnerClass(templateHolder.getClazz())) {
            values.add(owner)
        }
        ConstructorArgumentProcessor valueProcessor = new ConstructorArgumentProcessor(arguments)
        for (String parameterName : parameterNames) {
            Class<?> fieldType = br.com.eztest.core.util.ReflectionUtils.invokeRecursiveType(templateHolder.getClazz(), parameterName)
            Object result = arguments.get(parameterName)
            if (result == null) {
                result = processChainedProperty(parameterName, fieldType, arguments)
            }
            values.add(valueProcessor.process(result, fieldType))
        }
        return values
    }

    private Object processChainedProperty(String parameterName, Class<?> fieldType, Map<String, Object> arguments) {
        Rule rule = new Rule()
        for (final String argument : arguments.keySet()) {
            int index = argument.indexOf(".")
            if (index > 0 && argument.substring(0, index).equals(parameterName)) {
                rule.add(argument.substring(index + 1), arguments.get(argument))
            }
        }
        return new ObjectFactory(new TemplateHolder(fieldType)).createObject(rule)
    }

    private Object processPropertyValue(Object object, Property property) {
        Class<?> fieldType = br.com.eztest.core.util.ReflectionUtils.invokeRecursiveType(object.getClass(), property.getName())
        Object value = property.hasRelationFunction() || br.com.eztest.core.util.ReflectionUtils.isInnerClass(fieldType) ? property.getValue(object) : property.getValue()
        return new PropertyProcessor(object).process(value, fieldType)
    }

    private <T> List<String> lookupConstructorParameterNames(Class<T> target, Set<Property> properties) {
        Collection<String> propertyNames = br.com.eztest.core.util.ReflectionUtils.map(properties, "rootAttribute")
        return br.com.eztest.core.util.ReflectionUtils.filterConstructorParameterNames(target, propertyNames)
    }
}