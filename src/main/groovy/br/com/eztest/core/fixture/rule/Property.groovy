package br.com.eztest.core.fixture.rule

class Property {
    private String name
    private Object value
    private br.com.eztest.core.fixture.function.Function function

    Property(String name, br.com.eztest.core.fixture.function.Function function) {
        this.name = name
        this.function = function
    }

    Property(String name, Object value) {
        this.name = name
        this.value = value
    }

    String getName() {
        return this.name
    }

    Object getValue() {
        return this.value == null ? this.function.generateValue() : this.value
    }

    Object getValue(Object owner) {
        return this.function.generateValue(owner)
    }

    boolean hasRelationFunction() {
        return this.function instanceof br.com.eztest.core.fixture.function.RelationFunction
    }

    String getRootAttribute() {
        int index = this.name.indexOf(".")
        return index > 0 ? this.name.substring(0, index) : this.name
    }

    @Override
    int hashCode() {
        final int prime = 31
        int result = 1
        result = prime * result + ((name == null) ? 0 : name.hashCode())
        return result
    }

    @Override
    boolean equals(Object obj) {
        if (this == obj) return true
        if (obj == null) return false
        if (getClass() != obj.getClass()) return false
        Property other = (Property) obj
        if (name == null) {
            if (other.name != null) return false
        } else if (!name.equals(other.name)) return false
        return true
    }
}