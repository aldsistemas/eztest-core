package br.com.eztest.core.fixture.rule

class ConstructorArgumentProcessor extends ValueProcessor {
    private final Map<String, Object> parameters

    ConstructorArgumentProcessor(Map<String, Object> parameters) {
        this.parameters = parameters
    }

    @Override
    protected String getValue(String parameterName) {
        return parameters.get(parameterName).toString()
    }
}
