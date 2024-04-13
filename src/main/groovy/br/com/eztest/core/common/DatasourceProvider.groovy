package br.com.eztest.core.common

import org.hibernate.Session

abstract class DatasourceProvider {
    abstract Session getDefaultSession()
}
