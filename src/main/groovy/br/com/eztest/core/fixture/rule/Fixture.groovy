package br.com.eztest.core.fixture.rule

class Fixture {

    private static Map<Class<?>, TemplateHolder> templates = new LinkedHashMap<>();

    static TemplateHolder of(Class<?> clazz) {
        TemplateHolder template = templates.get(clazz);

        if (template == null) {
            template = new TemplateHolder(clazz);
            templates.put(clazz, template);
        }

        return template;
    }

    static ObjectFactory from(Class<?> clazz) {
        return new ObjectFactory(of(clazz));
    }

}
