package br.com.eztest.core.fixture.rule

class PropertyProcessor extends ValueProcessor {
    private final Object result;

    PropertyProcessor(final Object result) {
        this.result = result;
    }

    @Override
    protected String getValue(String propertyName) {
        return br.com.eztest.core.util.ReflectionUtils.getProperty(result, propertyName).toString();
    }
}
