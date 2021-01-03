package org.shardingdatabases.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

//@Configuration
@ComponentScan(basePackageClasses = ShardingDatabasesConfig.class)
@MapperScan(basePackages = "org.shardingdatabses.mapper")
public class ShardingDatabasesConfig {

    /**
     * description : Get objects of the SqlSessionFactory
     *
     * @return : org.apache.ibatis.session.SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(getDataSource());
        sessionFactory.setFailFast(true);
        sessionFactory.setMapperLocations(resolver.getResources("classpath:/mapper/*Mapper.xml"));
        return sessionFactory.getObject();
    }

    /**
     * description : get datasource and config the rules
     *
     * @return : javax.sql.DataSource
     */
    public DataSource getDataSource() throws SQLException {
        ShardingRuleConfiguration ruleConfig = new ShardingRuleConfiguration();
        // Configure database sharding strategy
        ShardingTableRuleConfiguration tableRuleConfiguration = new ShardingTableRuleConfiguration("tab_user");
        tableRuleConfiguration.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("age", "database-inline"));
        ruleConfig.getTables().add(tableRuleConfiguration);
        // Sharding algorithm configuration
        Map<String, ShardingSphereAlgorithmConfiguration> shardingAlgorithms = new LinkedHashMap<>();
        Properties algorithms = new Properties();
        algorithms.setProperty("algorithm-expression", "ds$->{age % 2}");
        ShardingSphereAlgorithmConfiguration algorithmConfiguration = new ShardingSphereAlgorithmConfiguration("INLINE", algorithms);
        shardingAlgorithms.put("database-inline", algorithmConfiguration);
        ruleConfig.setShardingAlgorithms(shardingAlgorithms);

        // Show sql
        Properties properties = new Properties();
        properties.setProperty("sql-show", "true");
        return ShardingSphereDataSourceFactory.createDataSource(createDataSourceMap(), Collections.singleton(ruleConfig), properties);
    }

    /**
     * description : config the datasource
     *
     * @return : java.util.Map<java.lang.String,javax.sql.DataSource>
     */
    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>(2, 1);
        result.put("ds0", DataSourceUtil.createDataSource("ds0"));
        result.put("ds1", DataSourceUtil.createDataSource("ds1"));
        return result;
    }

}
