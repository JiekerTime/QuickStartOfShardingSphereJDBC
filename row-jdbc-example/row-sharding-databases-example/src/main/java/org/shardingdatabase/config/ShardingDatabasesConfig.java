package org.shardingdatabase.config;


import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

public final class ShardingDatabasesConfig {
    /**
     * description : get datasource and config the rules
     *
     * @return : javax.sql.DataSource
     */
    public static DataSource getDataSource() throws SQLException {
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
    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>(2, 1);
        result.put("ds0", DataSourceUtil.createDataSource("ds0"));
        result.put("ds1", DataSourceUtil.createDataSource("ds1"));
        return result;
    }

}
