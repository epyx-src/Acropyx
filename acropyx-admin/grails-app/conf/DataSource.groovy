
dataSource {
    //pooled = true
    //driverClassName = "org.hsqldb.jdbcDriver"
    //username = "sa"
    //password = ""
    //Aqui comienza mysql
    //pooled = true
    //driverClassName = "com.mysql.jdbc.Driver"
    //hasta aqui es con mysql
    //Aqui comienza postgresql
        pooled = true
        driverClassName = "org.postgresql.Driver"
        dialect = org.hibernate.dialect.PostgreSQLDialect
	username = "acropyx"
	password = "4cr0pyx.2012"
        //Hasta aqui es con postgresql    
    
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "update"
            //url = "jdbc:hsqldb:file:acropyxDb;shutdown=true"
            //url = "jdbc:mysql://localhost:3306/racetrack_dev?autoreconnect=true"
            //url = "jdbc:mysql://localhost:3306/acropyxDb"
            url = "jdbc:postgresql://localhost:5432/acropyxDb"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            //url = "jdbc:hsqldb:mem:testDb"
            //url = "jdbc:mysql://localhost:3306/racetrack_dev?autoreconnect=true"
            //url = "jdbc:mysql://localhost:3306/acropyxDb"
            url = "jdbc:postgresql://localhost:5432/acropyxDb"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            //url = "jdbc:hsqldb:file:acropyxDb;shutdown=true"
            //url = "jdbc:mysql://localhost:3306/racetrack_dev?autoreconnect=true"
            //url = "jdbc:mysql://localhost:3306/acropyxDb"
            url = "jdbc:postgresql://localhost:5432/acropyxDb"
        }
    }
}
