package br.com.eztest.core.fixture.factory

import br.com.eztest.core.fixture.data.Sequence

import java.sql.Timestamp

public class DateSequence implements Sequence<Date> {

    private final int                   calendarFieldInc;
    private final int                   incAmount;
    private final GregorianCalendar     gc;
    private final Class<? extends Date> clazz;

    public DateSequence(final Date base, final int calendarFieldInc, final int incAmount) {
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(base);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        this.gc = gc;
        this.calendarFieldInc = calendarFieldInc;
        this.incAmount = incAmount;
        this.clazz = base.getClass();
    }

    @Override
    public Date nextValue() {
        final Date ret = this.gc.getTime();
        this.gc.add(this.calendarFieldInc, this.incAmount);
        if (Timestamp.class.isAssignableFrom(this.clazz)) {
            return new Timestamp(ret.getTime());
        }
        if (java.sql.Date.class.isAssignableFrom(this.clazz)) {
            return new java.sql.Date(ret.getTime());
        }
        if (Date.class.isAssignableFrom(this.clazz)) {
            return ret;
        }
        throw new UnsupportedOperationException("Classe base desconhecida. Permitido: java.util.Data, java.sql.Date, java.sql.Timestamp");
    }
}
