/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.common.factory;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.common.type.ShardingType;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public final class YamlDataSourceFactory {
    
    public static DataSource newInstance(final ShardingType shardingType, String configurationLocation) throws SQLException, IOException {
        switch (shardingType) {
            case SHARDING_DATABASES:
                return getDataSource(configurationLocation);
            case SHARDING_TABLES:
                return getDataSource(configurationLocation);
            case SHARDING_DATABASES_AND_TABLES:
                return getDataSource(configurationLocation);
            case REPLICA_QUERY:
                return getDataSource(configurationLocation);
            case SHARDING_REPLICA_QUERY:
                return getDataSource(configurationLocation);
            default:
                throw new UnsupportedOperationException(shardingType.name());
        }
    }
    
    private static DataSource getDataSource(final String configurationLocation) throws IOException, SQLException {
        return YamlShardingSphereDataSourceFactory.createDataSource(new File(YamlDataSourceFactory.class.getResource(configurationLocation).getFile()));
    }
}
