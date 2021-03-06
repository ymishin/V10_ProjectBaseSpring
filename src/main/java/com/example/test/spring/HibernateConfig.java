package com.example.test.spring;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.boot.SchemaAutoTooling;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import java.util.Properties;
import javax.sql.DataSource;

@Configuration
public class HibernateConfig {

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}

//	@Bean
//	public DataSource dataSource() {
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());	
//		dataSource.setUrl("jdbc:mysql://localhost:3306/my_schema");
//		dataSource.setUsername("root");
//		dataSource.setPassword("1234");
//		return dataSource;
//	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			DataSource dataSource,
			MultiTenantConnectionProvider multiTenantConnectionProviderImpl,
			CurrentTenantIdentifierResolver currentTenantIdentifierResolverImpl) {

		Properties properties = new Properties();
		properties.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
		properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProviderImpl);
		properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolverImpl);
		properties.put(Environment.HBM2DDL_AUTO, SchemaAutoTooling.UPDATE.name().toLowerCase());

		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan(V10_TestApplication.class.getPackage().getName());
		em.setJpaVendorAdapter(jpaVendorAdapter());
		em.setJpaProperties(properties);
		
		return em;
	}
}