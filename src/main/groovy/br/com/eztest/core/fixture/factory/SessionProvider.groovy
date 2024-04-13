package br.com.eztest.core.fixture.factory;

import org.hibernate.Session;

public interface SessionProvider {

    Session getSession();
}
