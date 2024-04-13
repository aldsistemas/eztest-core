package br.com.eztest.core.fixture.rule

class TemplateHolder {
    private Class<?> clazz
    private Map<String, Rule> rules = new LinkedHashMap<String, Rule>()

    TemplateHolder(Class<?> clazz) {
        this.clazz = clazz
    }

    TemplateHolder addTemplate(String label, Rule rule) {
        rules.put(label, rule)
        return this
    }

    ExtendedTemplateHolder addTemplate(String label) {
        return new ExtendedTemplateHolder(this, label)
    }

    Class<?> getClazz() {
        return clazz
    }

    Map<String, Rule> getRules() {
        return rules
    }
}