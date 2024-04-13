package br.com.eztest.core.fixture.data

class CalendarInterval implements Serializable {
    private int calendarField
    private int value

    CalendarInterval(int interval, int calendarField) {
        this.calendarField = calendarField
        this.value = interval
    }

    int getCalendarField() {
        return this.calendarField
    }

    int getValue() {
        return this.value
    }
}