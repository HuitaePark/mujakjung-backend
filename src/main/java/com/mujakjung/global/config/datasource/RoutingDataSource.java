package com.mujakjung.global.config.datasource;

import com.mujakjung.global.enums.DataSourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // AOP에서 설정한 DataSourceType을 반환합니다.
        DataSourceType currentDataSourceType = DataSourceContextHolder.getDataSourceType();
        log.info("Current DataSource is: {}", currentDataSourceType);
        return currentDataSourceType;
    }

}
