package br.com.eztest.core.fixture

class FixtureFactory {

    static Map map = [:]
    private static Map providers = [:]

    static reset() {
        map.clear()
    }

    static registerProvider(Class clazz, Closure provider) {
        providers[clazz] = provider
    }

    static <T> T from(Class<T> clazz) {
        if (!map.containsKey(clazz)) {
            map.put(clazz, providers[clazz]())
        }
        return map[clazz]
    }
}
