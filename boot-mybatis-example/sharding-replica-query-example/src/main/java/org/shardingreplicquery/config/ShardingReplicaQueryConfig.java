package org.shardingreplicquery.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.replicaquery.api.config.ReplicaQueryRuleConfiguration;
import org.apache.shardingsphere.replicaquery.api.config.rule.ReplicaQueryDataSourceRuleConfiguration;
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
@ComponentScan(basePackageClasses = ShardingReplicaQueryConfig.class)
@MapperScan(basePackages = "org.shardingdatabasetables.mapper")
public class ShardingReplicaQueryConfig {

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
        // Primary datasource name and Replica data source names
        ReplicaQueryDataSourceRuleConfiguration mds0=new ReplicaQueryDataSourceRuleConfiguration("logds_0","mds0",Arrays.asList("ds0","ds1"),"round-robin");
        ReplicaQueryDataSourceRuleConfiguration mds1=new ReplicaQueryDataSourceRuleConfiguration("logds_1","mds0",Arrays.asList("ds2","ds3"),"round-robin");

        // Load balance algorithm configuration
        Properties algorithmProperties = new Properties();
        algorithmProperties.setProperty("work-id", "123");
        ShardingSphereAlgorithmConfiguration replicaAlgorithm=new ShardingSphereAlgorithmConfiguration("ROUND_ROBIN", algorithmProperties);
        HashMap<String, ShardingSphereAlgorithmConfiguration> algorithm = new HashMap<>();
        algorithm.put("round-robin", replicaAlgorithm);
        ReplicaQueryRuleConfiguration replicaQueryRuleConfiguration = new ReplicaQueryRuleConfiguration(Arrays.asList(mds0,mds1), algorithm);

        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        ShardingTableRuleConfiguration tableRuleConfiguration
                = new ShardingTableRuleConfiguration("tab_user","logds_$->{0..1}.tab_user$->{0..2}");
        // Configure database sharding strategy
        tableRuleConfiguration.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("age","database-inline"));
        // Configure table sharding strategy
        tableRuleConfiguration.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "table-inline"));
        shardingRuleConfiguration.getTables().add(tableRuleConfiguration);

        // Sharding algorithm configuration
        Map<String, ShardingSphereAlgorithmConfiguration> shardingAlgorithms = new LinkedHashMap<>();
        // Configure database Sharding algorithm configuration
        Properties databaseAlgorithms = new Properties();
        databaseAlgorithms.setProperty("algorithm-expression", "logds_$->{age % 2}");
        ShardingSphereAlgorithmConfiguration databaseAlgorithmConfiguration = new ShardingSphereAlgorithmConfiguration("INLINE", databaseAlgorithms);
        shardingAlgorithms.put("database-inline", databaseAlgorithmConfiguration);
        // Configure table Sharding algorithm configuration
        Properties tableAlgorithms = new Properties();
        tableAlgorithms.setProperty("algorithm-expression", "tab_user$->{id % 3}");
        ShardingSphereAlgorithmConfiguration tableAlgorithmConfiguration = new ShardingSphereAlgorithmConfiguration("INLINE", tableAlgorithms);
        shardingAlgorithms.put("table-inline", tableAlgorithmConfiguration);
        shardingRuleConfiguration.setShardingAlgorithms(shardingAlgorithms);

        // Show sql
        Properties properties = new Properties();
        properties.setProperty("sql-show", "true");
        return ShardingSphereDataSourceFactory.createDataSource(createDataSourceMap(), Arrays.asList(shardingRuleConfiguration,replicaQueryRuleConfiguration), properties);
    }

    /**
     * description : config the datasource
     *
     * @return : java.util.Map<java.lang.String,javax.sql.DataSource>
     */
    private Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>(6, 1);
        result.put("mds0", DataSourceUtil.createDataSource("mds0"));
        result.put("mds1", DataSourceUtil.createDataSource("mds1"));
        result.put("ds0", DataSourceUtil.createDataSource("ds0"));
        result.put("ds1", DataSourceUtil.createDataSource("ds1"));
        result.put("ds2", DataSourceUtil.createDataSource("ds2"));
        result.put("ds3", DataSourceUtil.createDataSource("ds3"));
        return result;
    }

}
