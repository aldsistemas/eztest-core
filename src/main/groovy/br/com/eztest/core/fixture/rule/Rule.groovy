package br.com.eztest.core.fixture.rule

import br.com.eztest.core.fixture.data.CalendarInterval
import br.com.eztest.core.fixture.data.CalendarSequence
import br.com.eztest.core.fixture.data.CircularList
import br.com.eztest.core.fixture.data.Interval
import br.com.eztest.core.fixture.data.Range
import br.com.eztest.core.fixture.data.Sequence
import br.com.eztest.core.fixture.factory.AutoReferenceFunction
import br.com.eztest.core.fixture.factory.CnpjSequence
import br.com.eztest.core.fixture.factory.CollectionWrapper
import br.com.eztest.core.fixture.factory.DateSequence
import br.com.eztest.core.fixture.factory.HibernateCircularList
import br.com.eztest.core.fixture.factory.PropertyExtractor
import br.com.eztest.core.fixture.factory.RoundRobinFunction
import br.com.eztest.core.fixture.factory.StringWrappedSequence
import br.com.eztest.core.fixture.function.AssociationFunction
import br.com.eztest.core.fixture.function.AtomicFunction
import br.com.eztest.core.fixture.function.DateTimeFunction
import br.com.eztest.core.fixture.function.FixtureFunction
import br.com.eztest.core.fixture.function.Function
import br.com.eztest.core.fixture.function.ListFunction
import br.com.eztest.core.fixture.function.NullFunction
import br.com.eztest.core.fixture.function.NumberSequence
import br.com.eztest.core.fixture.function.RandomFunction
import br.com.eztest.core.fixture.function.RelationFunction
import br.com.eztest.core.fixture.function.SequenceFunction
import br.com.eztest.core.fixture.function.SimpleValueFunction
import br.com.eztest.core.fixture.factory.HibernateList
import br.com.eztest.core.fixture.factory.SessionProvider

import java.text.DateFormat
import java.text.SimpleDateFormat

public class Rule {

    private Set<Property> properties = new LinkedHashSet<Property>();

    private static Map<String, Function> registeredFunctions = new HashMap<String, Function>();

    public Rule() {
    }

    public Rule(final Rule baseRule, final Rule extendedRule) {
        this.properties = new LinkedHashSet<>();
        final Map<String, Property> propertiesExetended = getPropertiesByName(extendedRule.getProperties());

        // Percorre as propriedades basicas. Se encontrar override, substitui por este
        for (final Property p : baseRule.getProperties()) {
            final Property propertyOverrided = propertiesExetended.remove(p.getName());
            if (propertyOverrided != null) {
                this.properties.add(propertyOverrided);
            } else {
                this.properties.add(p);
            }
        }

        // Adiciona os remanescentes
        this.properties.addAll(propertiesExetended.values());
    }

    public void add(final String property, final Function function) {
        this.properties.add(new Property(property, function));
    }

    public void add(final String property, final Object value) {
        this.properties.add(new Property(property, value));
    }

    public static PipelineAssembler from(final Function f) {
        return new PipelineAssembler(f);
    }

    public Set<Property> getProperties() {
        return this.properties;
    }

    private Map<String, Property> getPropertiesByName(final Set<Property> props) {

        final Map<String, Property> ret = new HashMap<>();
        for (final Property property : props) {
            ret.put(property.getName(), property);
        }
        return ret;
    }


    public static Function all(final Class<?> clazz, final String targetAttribute, final SessionProvider sp) {
        return new ListFunction(targetAttribute, new HibernateList<>(clazz, sp));
    }


    public static Function cnpj() {
        return new SequenceFunction(new CnpjSequence());
    }

    public static Interval decrement(final int interval) {
        return new Interval(interval * -1);
    }

    public static Function exact(final Object value) {
        return new SimpleValueFunction(value);
    }


    public static Function global(final String name) {
        return Rule.registeredFunctions.get(name);
    }

    public static Function global(final String name, final Function defFun) {
        Function ret = Rule.registeredFunctions.get(name);
        if (ret == null) {
            Rule.register(name, defFun);
            ret = defFun;
        }
        return ret;
    }

    public static br.com.eztest.core.util.Chainable has(final int quantity) {
        return new AssociationFunction(quantity);
    }

    public static Interval increment(final int interval) {
        return new Interval(interval);
    }


    public static Function list(final Class<?> clazz, final SessionProvider sp, final String targetAttribute) {
        return Rule.all(clazz, targetAttribute, sp);
    }

    public static Function list(final Class<?> clazz, final SessionProvider sp, final String targetAttribute, final int maxItens) {
        return new ListFunction(targetAttribute, new HibernateList<>(clazz, sp, maxItens));
    }


    public static Function nullValue() {
        return new NullFunction();
    }

    public static AssociationFunction one(final Class<?> clazz, final String label) {
        return new AssociationFunction(clazz, label);
    }

    public static Function one(final Class<?> clazz, final String label, final String targetAttribute) {
        return new AssociationFunction(clazz, label, targetAttribute);
    }

    public static Function one(final SessionProvider sessionProvider, final Class<?> clazz, final String label) {
        return new FixtureFunction(sessionProvider, clazz, label);
    }

    public static Function own(final String attributeName) {
        return new AutoReferenceFunction(attributeName);
    }

    public static Function random(final Class<?> clazz, final Object... dataset) {
        return new RandomFunction(clazz, dataset);
    }

    public static Function random(final Class<?> clazz, final Range range) {
        return new RandomFunction(clazz, range);
    }

    public static Function random(final Object... dataset) {
        return new RandomFunction(dataset);
    }

    public static Function randomDate(final String startDate, final String endDate, final DateFormat format) {
        return new DateTimeFunction(br.com.eztest.core.util.DateTimeUtil.toCalendar(startDate, format), br.com.eztest.core.util.DateTimeUtil.toCalendar(endDate, format));
    }

    public static Range range(final Number start, final Number end) {
        return new Range(start, end);
    }


    public static void register(final String name, final Function function) {
        Rule.registeredFunctions.put(name, function);
    }

    public static Function roundRobin(final CircularList<?> list) {
        return new RoundRobinFunction(list);
    }

    public static Function roundRobin(final Class<?> clazz, final SessionProvider sessionProvider, final String... restrictions) {
        return new RoundRobinFunction(new HibernateCircularList<>(clazz, sessionProvider, restrictions));
    }

    public static Function roundRobin(final Class<?> clazz, final String tempateToCreateIfNeeded, final SessionProvider sessionProvider,
                                      final String... restrictions) {
        return new RoundRobinFunction(new HibernateCircularList<>(clazz, tempateToCreateIfNeeded, sessionProvider, restrictions));
    }

    public static Function roundRobin(final List<?> list) {
        return new RoundRobinFunction(list);
    }

    public static Function roundRobin(final Object[] list) {
        return new RoundRobinFunction(list);
    }

    public static Function sequence(Number base) {
        return sequence(base, 1);
    }

//    public static Function sequence(final Class<? extends Number> clazz) {
//        final Number number = br.com.eztest.core.util.ReflectionUtils.newInstance(clazz, Arrays.asList("1"));
//        return new SequenceFunction(new NumberSequence(number, 1));
//    }

    public static Function sequence(final Date base) {
        return new SequenceFunction(new DateSequence(base, Calendar.DAY_OF_YEAR, 1));
    }

    public static Function sequence(final Date base, final int calendarFieldInc, final int incAmount) {
        return new SequenceFunction(new DateSequence(base, calendarFieldInc, incAmount));
    }

    public static Function sequence(final Number startWith, final int incrementBy) {
        return new SequenceFunction(new NumberSequence(startWith, incrementBy));
    }

    public static Function sequence(final Sequence<?> sequence) {
        return new SequenceFunction(sequence);
    }

    public static Function sequence(final String prefix, final Number base, final int incrementBy) {
        return new SequenceFunction(new StringWrappedSequence(prefix, null, base, incrementBy, null));
    }

    public static Function sequence(final String prefix, final String suffix, final Number base, final int incrementBy) {
        return new SequenceFunction(new StringWrappedSequence(prefix, suffix, base, incrementBy, null));
    }

    public static Function sequence(final String prefix, final String suffix, final Number base, final int incrementBy, final int mod) {
        return new SequenceFunction(new StringWrappedSequence(prefix, suffix, base, incrementBy, mod));
    }

    public static Function sequenceDate(final String base, final CalendarInterval interval) {
        return Rule.sequenceDate(base, new SimpleDateFormat("yyyy-MM-dd"), interval);
    }

    public static Function sequenceDate(final String base, final DateFormat simpleDateFormat, final CalendarInterval interval) {
        return new SequenceFunction(new CalendarSequence(br.com.eztest.core.util.DateTimeUtil.toCalendar(base, simpleDateFormat), interval));
    }

    public static class PipelineAssembler {

        private final Function function;

        public PipelineAssembler(final Function function) {
            this.function = function;
        }

        public Function extract(final String property) {
            if (this.function instanceof AtomicFunction) {
                return new PropertyExtractor(property, (AtomicFunction) this.function);
            }
            if (this.function instanceof RelationFunction) {
                return new PropertyExtractor(property, (RelationFunction) this.function);
            }
            throw new IllegalStateException("Only AtomicFunction and RelationFunction functions allowed");
        }

        public Function wrapInSet() {
            if (this.function instanceof AtomicFunction) {
                return new CollectionWrapper(Set.class, (AtomicFunction) this.function);
            }
            if (this.function instanceof RelationFunction) {
                return new CollectionWrapper(Set.class, (RelationFunction) this.function);
            }
            throw new IllegalStateException("Only AtomicFunction and RelationFunction functions allowed");
        }
    }
}
