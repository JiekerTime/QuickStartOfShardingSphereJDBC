package org.shardingtables.config;


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
@ComponentScan(basePackageClasses = ShardingTablesConfig.class)
@MapperScan(basePackages = "org.shardingtables.mapper")
public class ShardingTablesConfig {

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
        // Configure table sharding strategy
        ShardingTableRuleConfiguration tableRuleConfiguration = new ShardingTableRuleConfiguration("tab_user","ds1.tab_user$->{0..2}");
        tableRuleConfiguration.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "table-inline"));
        ruleConfig.getTables().add(tableRuleConfiguration);
        // Sharding algorithm configuration
        Map<String, ShardingSphereAlgorithmConfiguration> shardingAlgorithms = new LinkedHashMap<>();
        Properties algorithms = new Properties();
        algorithms.setProperty("algorithm-expression", "tab_user$->{id % 3}");
        ShardingSphereAlgorithmConfiguration algorithmConfiguration = new ShardingSphereAlgorithmConfiguration("INLINE", algorithms);
        shardingAlgorithms.put("table-inline", algorithmConfiguration);
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
        result.put("ds1", DataSourceUtil.createDataSource());
        return result;
    }

}
