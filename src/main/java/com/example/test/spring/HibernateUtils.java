package com.example.test.spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtils {

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;
	
	@Transactional 
	public void createNewSchema(String schemaName) throws SQLException {
		entityManager.createNativeQuery("CREATE SCHEMA " + schemaName).executeUpdate();
//		Query query = entityManager.createNativeQuery("CREATE SCHEMA " + schemaName);
//		int res = query.executeUpdate();
//		if (res < 0) {
//		    throw new DatabaseCreationException("Unable to create db");
		updateSchema(schemaName);
	}
		
	@Transactional
	public void updateSchema(String schemaName) throws SQLException {
		TenantContext.setCurrentTenant(schemaName);
		
		Metadata metadata = getMetaData();
		
//		SchemaExport schemaExport = new SchemaExport();
//		schemaExport.create(EnumSet.of(TargetType.STDOUT), metadata);
		
		SchemaUpdate schemaUpdate = new SchemaUpdate();
		schemaUpdate.execute(EnumSet.of(TargetType.STDOUT), metadata);
		
//
////		new SchemaExport(cfg, dataSource.getConnection()).execute(true, true, false, true);
//		entityManager.get
//			
//		MetadataSources metadataSources = new MetadataSources(
////			    new StandardServiceRegistryBuilder()		
////			        .applySetting("javax.persistence.schema-generation-connection", dataSource.getConnection())
////			        .build());
//				new StandardServiceRegistryBuilder().build());
//		
//		MetadataSources metadataSources = new MetadataSources(new BootstrapServiceRegistryBuilder().build());
//		Metadata metadata = metadataSources.buildMetadata();
//		
////		properties.put(Environment.HBM2DDL_CREATE_SCRIPT_SOURCE, "import.sql");
//		SchemaExport schemaExport = new SchemaExport();
//		schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);
//		
////		SchemaUpdate schemaUpdate = new SchemaUpdate();
////		schemaUpdate.execute(EnumSet.of(TargetType.DATABASE), metadata.buildMetadata());

	}
	
//	public void f() {
//	EntityManagerFactoryImpl emf=(EntityManagerFactoryImpl)lcemfb.getNativeEntityManagerFactory();
//	SessionFactoryImpl sf=emf.getSessionFactory();
//	StandardServiceRegistry serviceRegistry = sf.getSessionFactoryOptions().getServiceRegistry();
//	MetadataSources metadataSources = new MetadataSources(new BootstrapServiceRegistryBuilder().build());
//	Metadata metadata = metadataSources.buildMetadata(serviceRegistry);
//	}
	
	protected SessionFactoryImpl getSessionFactory() {
        Session session = (Session) entityManager.getDelegate();
        return (SessionFactoryImpl) session.getSessionFactory();
    }

    protected Metadata getMetaData() {
//        Properties prop = new Properties();
//        prop.put("hibernate.dialect", getSessionFactory().getJdbcServices().getDialect().toString());
//        prop.put("hibernate.hbm2ddl.auto", "create");
//        prop.put("hibernate.show_sql", "true");
//        prop.put("hibernate.connection.username", environment.getProperty("axboot.dataSource.username", ""));
//        prop.put("hibernate.connection.password", environment.getProperty("axboot.dataSource.password", ""));
//        prop.put("hibernate.connection.url", environment.getProperty("axboot.dataSource.url", ""));
    	
    	StandardServiceRegistry serviceRegistry = getSessionFactory().getSessionFactoryOptions().getServiceRegistry();
    	MetadataSources metadataSources = new MetadataSources(new BootstrapServiceRegistryBuilder().build());
    	Metadata metadata = metadataSources.buildMetadata(serviceRegistry);

//        BootstrapServiceRegistry bsr = new BootstrapServiceRegistryBuilder().build();
//        StandardServiceRegistryBuilder ssrBuilder = new StandardServiceRegistryBuilder(bsr);
//        ssrBuilder.applySettings(prop);
//        StandardServiceRegistry standardServiceRegistry = ssrBuilder.build();
//
//        MetadataSources metadataSources = new MetadataSources(standardServiceRegistry);
//
//        Reflections reflections = new Reflections(axBootContextConfig.getBasePackageName());
//
//        reflections.getTypesAnnotatedWith(Entity.class)
//                .forEach(metadataSources::addAnnotatedClass);

//        return metadataSources.buildMetadata();
    	return metadata;
    }
	
	@Transactional 
	public void dropSchema(String schemaName) throws SQLException {
		entityManager.createNativeQuery("DROP SCHEMA IF EXISTS " + schemaName).executeUpdate();
	}
}
