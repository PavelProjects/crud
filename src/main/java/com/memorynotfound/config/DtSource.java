package com.memorynotfound.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class DtSource {
    public static DataSource getDts (){
        PoolProperties p = new PoolProperties();
        p.setUrl("jdbc:postgresql://localhost/paveldata");
        p.setDriverClassName("org.postgresql.Driver");
        p.setUsername("pavel");
        p.setPassword("75237523");
        p.setMaxActive(1000);
        p.setMaxWait(10000);
        DataSource dataSource = new DataSource();
        dataSource.setPoolProperties(p);
        return dataSource;
    }
}
