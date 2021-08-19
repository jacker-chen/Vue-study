package com.bjfn.shop.admin.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@EnableConfigurationProperties({DruidDataSourceProperties.class})
@Slf4j
public class DataSourceConfig {


    @Autowired
    private DruidDataSourceProperties druidDataSourceProperties;

    @Bean
    public DataSource druidDataSource() {
        log.info("==================================== InitDatabaseConfig -> dataSource -> 开始创建数据库 ====================================");
        // 数据库连接对象
        Connection connection = null;
        Statement statement = null;
        String url = druidDataSourceProperties.getUrl();
        String driverClassName = druidDataSourceProperties.getDriverClassName();
        String username = druidDataSourceProperties.getUsername();
        String password = druidDataSourceProperties.getPassword();
        log.info(druidDataSourceProperties.toString());
        try {

            // 如果尝试去连接不存在的数据库会报错，所以这里连接的时候不带数据库名称
            String connectionUrl = url.replace(("/" + (url.substring(0, url.indexOf("?"))).substring(((url.substring(0, url.indexOf("?"))).lastIndexOf("/")) + 1)), "");
            // 从连接地址中截取出数据库名称
            String databaseName = (url.substring(0, url.indexOf("?"))).substring(((url.substring(0, url.indexOf("?"))).lastIndexOf("/")) + 1);

            // 设置驱动
            Class.forName(driverClassName);
            // 连接数据库
            connection = DriverManager.getConnection(connectionUrl, username, password);
            statement = connection.createStatement();

            // 创建数据库
            statement.executeUpdate("create database if not exists `" + databaseName + "` default character set utf8mb4 COLLATE utf8mb4_general_ci");

        }catch (Exception e) {
            e.printStackTrace();
            log.info("==================================== InitDatabaseConfig -> dataSource -> 创建数据库出错：" + e.getMessage() + " ====================================");
        }finally {
            try {
                // 关闭连接
                statement.close();
                connection.close();
            }catch (SQLException e) {
                log.info("==================================== InitDatabaseConfig -> dataSource -> 关闭数据库出错：" + e.getMessage() + " ====================================");
            }
            log.info("==================================== InitDatabaseConfig -> dataSource -> 创建数据库结束 ====================================");
        }

        // 创建数据源
        DruidDataSource druidDataSource = new DruidDataSource();
        // 设置数据源
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(druidDataSourceProperties.getInitialSize());
        druidDataSource.setMinIdle(druidDataSourceProperties.getMinIdle());
        druidDataSource.setMaxActive(druidDataSourceProperties.getMaxActive());
        druidDataSource.setMaxWait(druidDataSourceProperties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(druidDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(druidDataSourceProperties.getMinEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(druidDataSourceProperties.getValidationQuery());
        druidDataSource.setTestWhileIdle(druidDataSourceProperties.isTestWhileIdle());
        druidDataSource.setTestOnBorrow(druidDataSourceProperties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(druidDataSourceProperties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(druidDataSourceProperties.isPoolPreparedStatements());
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(druidDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            druidDataSource.setFilters(druidDataSourceProperties.getFilters());
            druidDataSource.init();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        // 返回数据源
        return druidDataSource;
    }

    /**
     * 注册Servlet信息， 配置监控视图
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public ServletRegistrationBean<Servlet> druidServlet() {
        ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<Servlet>(new StatViewServlet(), "/druid/*");

        //白名单：
        servletRegistrationBean.addInitParameter("allow","192.168.0.100");
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        // servletRegistrationBean.addInitParameter("deny","192.168.1.119");
        //登录查看信息的账号密码, 用于登录Druid监控后台
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
        //是否能够重置数据.
        servletRegistrationBean.addInitParameter("resetEnable", "true");
        return servletRegistrationBean;

    }

    /**
     * 注册Filter信息, 监控拦截器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<Filter> filterRegistrationBean() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
