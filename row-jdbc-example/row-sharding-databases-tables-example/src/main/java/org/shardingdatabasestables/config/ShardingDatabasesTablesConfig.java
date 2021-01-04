package org.shardingdatabasestables.config;


import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

public final class ShardingDatabasesTablesConfig {
    /**
     * description : get datasource and config the rules
     *
     * @return : javax.sql.DataSource
     */
    public static DataSource getDataSource() throws SQLException {
        ShardingRuleConfiguration ruleConfig = new ShardingRuleConfiguration();
        ShardingTableRuleConfiguration tableRuleConfiguration = new ShardingTableRuleConfiguration("tab_user","ds$->{0..1}.tab_user$->{0..2}");
        // Configure database sharding strategy
        tableRuleConfiguration.setDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("age","database-inline"));
        // Configure table sharding strategy
        tableRuleConfiguration.setTableShardingStrategy(new StandardShardingStrategyConfiguration("id", "table-inline"));
        ruleConfig.getTables().add(tableRuleConfiguration);

        // Sharding algorithm configuration
        Map<String, ShardingSphereAlgorithmConfiguration> shardingAlgorithms = new LinkedHashMap<>();
        // Configure database Sharding algorithm configuration
        Properties databaseAlgorithms = new Properties();
        databaseAlgorithms.setProperty("algorithm-expression", "ds$->{age % 2}");
        ShardingSphereAlgorithmConfiguration databaseAlgorithmConfiguration = new ShardingSphereAlgorithmConfiguration("INLINE", databaseAlgorithms);
        shardingAlgorithms.put("database-inline", databaseAlgorithmConfiguration);
        // Configure table Sharding algorithm configuration
        Properties tableAlgorithms = new Properties();
        tableAlgorithms.setProperty("algorithm-expression", "tab_user$->{id % 3}");
        ShardingSphereAlgorithmConfiguration tableAlgorithmConfiguration = new ShardingSphereAlgorithmConfiguration("INLINE", tableAlgorithms);
        shardingAlgorithms.put("table-inline", tableAlgorithmConfiguration);
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
    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>(2, 1);
        result.put("ds0", DataSourceUtil.createDataSource("ds0"));
        result.put("ds1", DataSourceUtil.createDataSource("ds1"));
        return result;
    }

}
