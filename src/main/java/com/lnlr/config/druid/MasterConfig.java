package com.lnlr.config.druid;

import com.lnlr.common.jpa.CustomRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author:leihfei
 * @description 主库配置文件
 * @date:Create in 10:27 2018/8/29
 * @email:leihfein@gmail.com
 */
@Configuration
@EnableTransactionManagement//开启事物管理
@EnableJpaRepositories(//自定义数据管理的配置
        //指定EntityManager的创建工厂Bean
        entityManagerFactoryRef = "entityManagerFactoryMaster",
        //指定事物管理的Bean
        transactionManagerRef = "transactionManagerMaster",
        //指定管理的实体位置
        basePackages = {"com.lnlr.pojo"},
        //自定义RepositoryFactoryBean
        repositoryFactoryBeanClass = CustomRepositoryFactoryBean.class)
public class MasterConfig {

    @Resource
    @Qualifier("masterDataSource")
    private DataSource masterDataSource;

    /**
     * 管理器
     *
     * @param builder
     * @return
     */
    @Primary
    @Bean(name = "entityManagerMaster")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactoryMaster(builder).getObject().createEntityManager();
    }

    @Resource
    private JpaProperties jpaProperties;

    @Autowired
    private HibernateProperties hibernateProperties;

    /**
     * 主要配置生成策略
     *
     * @return
     */
    private Map<String, Object> getVendorProperties() {
        return hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
    }

    /**
     * 设置实体类所在位置
     */
    @Primary
    @Bean(name = "entityManagerFactoryMaster")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryMaster(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(masterDataSource)
                .packages("com.lnlr.pojo")
                .persistenceUnit("MasterPersistenceUnit")
                .properties(getVendorProperties())
                .build();
    }

    /**
     * 事务管理器
     *
     * @param builder
     * @return
     */
    @Primary
    @Bean(name = "transactionManagerMaster")
    public PlatformTransactionManager transactionManagerMaster(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactoryMaster(builder).getObject());
    }
}
