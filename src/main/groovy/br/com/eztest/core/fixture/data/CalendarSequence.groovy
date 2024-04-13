package br.com.eztest.core.fixture.data

class CalendarSequence implements Sequence<Calendar> {

    private Calendar baseCalendar;
    private CalendarInterval interval;
    private int multiplier;

    CalendarSequence(Calendar baseCalendar, CalendarInterval interval) {
        this.baseCalendar = (Calendar) baseCalendar.clone();
        this.interval = interval;
    }

    @Override
    Calendar nextValue() {
        Calendar result = (Calendar) this.baseCalendar.clone();
        result.add(this.interval.getCalendarField(), this.interval.getValue() * this.multiplier);

        this.multiplier++;
        return result;
    }
}
