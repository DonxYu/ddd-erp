package cn.geekhalo.erp.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
@EnableTransactionManagement
public class JpaConfig {


    @PersistenceContext
    protected EntityManager persistenceContextEntityManager;

    @Autowired
    protected DataSource primaryDatasource;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(this.persistenceContextEntityManager);
    }

    @Bean
    public com.querydsl.sql.Configuration querydslConfiguration() {
        SQLTemplates templates = MySQLTemplates.builder().build(); //change to your Templates
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        return configuration;
    }

    @Bean
    public SQLQueryFactory queryFactory() {
        Provider<Connection> provider = new SpringConnectionProvider(this.primaryDatasource);
        return new SQLQueryFactory(querydslConfiguration(), provider);
    }

    @Configuration
    class FlywayConfiguration{
        @Bean
        public Flyway erpFlyway(){
            Flyway flyway = new Flyway();
            flyway.setDataSource(primaryDatasource);
            flyway.setEncoding("utf-8");
            flyway.setTable("schema_erp_version");
            flyway.setLocations("db/erp");
            flyway.setOutOfOrder(true);
            flyway.setBaselineOnMigrate(true);
            flyway.migrate();
            return flyway;
        }
    }

}
