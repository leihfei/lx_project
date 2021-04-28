package com.lnlr.config.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author:leihfei
 * @description:
 * @date:Create in 10:24 2018/8/29
 * @email:leihfein@gmail.com
 */


@ServletComponentScan
@Configuration
@Slf4j
public class DruidDBConfig {
    /**
     * 公用数据源属性配置
     */
    @Autowired
    private CommonDBProperties commonDBProperties;

    /**
     * 第一数据源url
     */
    @Value("${spring.datasource.primary.url}")
    private String dbUrl1;

    /**
     * 第一数据源用户名
     */
    @Value("${spring.datasource.primary.username}")
    private String username1;

    /**
     * 第一数据源密码
     */
    @Value("${spring.datasource.primary.password}")
    private String password1;


    @Value("SELECT 1 FROM DUAL")
    private String validationQuery;


    @Bean(name = "masterDataSource")
    @Qualifier("masterDataSource")
    @Primary
    public DataSource dataSource() {
        return getDruidDataSource(username1, password1, dbUrl1);
    }


    private DruidDataSource getDruidDataSource(String username, String password, String url) {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(commonDBProperties.getDriverClassName());
        // 配置文件数据
        datasource.setInitialSize(commonDBProperties.getInitialSize());
        datasource.setMinIdle(commonDBProperties.getMinIdle());
        datasource.setMaxActive(commonDBProperties.getMaxActive());
        datasource.setMaxWait(commonDBProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(commonDBProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(commonDBProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(commonDBProperties.getValidationQuery());
        datasource.setTestWhileIdle(commonDBProperties.getTestWhileIdle());
        datasource.setTestOnBorrow(commonDBProperties.getTestOnBorrow());
        datasource.setTestOnReturn(commonDBProperties.getTestOnReturn());
        datasource.setPoolPreparedStatements(commonDBProperties.getPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(commonDBProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(commonDBProperties.getFilters());
        } catch (SQLException e) {
            log.error("druid configuration initialization filter : {0}", e);
        }
        datasource.setConnectionProperties(commonDBProperties.getConnectionProperties());
        return datasource;
    }

    /**
     * 配置druid web界面操作
     * 访问地址: http://127.0.0.1:8889/dietc/leihfei/druid/index.html
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean registrationBean() {
        //添加初始化参数：initParams
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/leihfei/druid/*");
        //白名单：
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1,192.168.1.189");
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        servletRegistrationBean.addInitParameter("deny", "");
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername", "lnlr");
        servletRegistrationBean.addInitParameter("loginPassword", "lnlr");

        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    /**
     * 配置druid拦截器
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则.
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
