package br.com.eztest.core.fixture.rule


import java.util.regex.Matcher
import java.util.regex.Pattern

abstract class ValueProcessor {

    //FIXME: verificar essa linha
    private static final Pattern PLACEHOLDER = Pattern.compile('.*?(\\$\\{([^\\}]+)\\}).*')
//    private static final Pattern PLACEHOLDER = '.*?(\\$\\{([^\\}]+)\\}).*'

    protected abstract String getValue(String name);

    public Object process(Object baseValue, Class<?> fieldType) {
        Object result = baseValue;

        if (baseValue instanceof String) {
            Matcher matcher = PLACEHOLDER.matcher((String) baseValue);
            if (matcher.matches()) {
                result = ((String) baseValue).replace(matcher.group(1), getValue(matcher.group(2)));
            } else if (Number.class.isAssignableFrom(fieldType)) {
                result = br.com.eztest.core.util.ReflectionUtils.newInstance(fieldType, Arrays.asList(baseValue));
            }
        }
        if (baseValue instanceof Calendar) {
            result = new br.com.eztest.core.util.CalendarTransformer().transform(baseValue, fieldType);
        }
        if (baseValue instanceof Collection && Set.class.isAssignableFrom(fieldType)) {
            result = new HashSet((Collection) baseValue);
        }

        return result;
    }
}
