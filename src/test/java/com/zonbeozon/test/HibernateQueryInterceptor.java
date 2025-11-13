package com.zonbeozon.test;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
public class HibernateQueryInterceptor implements StatementInspector {

    private final transient ThreadLocal<Long> queryCount = new ThreadLocal<>();

    public void startQueryCount() {
        queryCount.set(0L);
    }

    public Long getQueryCount() {
        return queryCount.get();
    }

    @Override
    public String inspect(String sql) {
        Long count = queryCount.get();
        if (count != null) {
            queryCount.set(count + 1);
        }
        return sql;
    }
}