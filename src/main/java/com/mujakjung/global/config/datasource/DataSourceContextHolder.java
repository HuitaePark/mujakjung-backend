package com.mujakjung.global.config.datasource;

import com.mujakjung.global.enums.DataSourceType;

public class DataSourceContextHolder {
    private static final ThreadLocal<DataSourceType> dataSourceTypeThreadLocal = new ThreadLocal<>();

    /**
     * 현재 스레드에서 사용할 데이터소스 타입을 설정합니다.
     * @param dataSourceType DataSourceType (READ 또는 WRITE)
     */
    public static void setDataSourceType(DataSourceType dataSourceType) {
        dataSourceTypeThreadLocal.set(dataSourceType);
    }

    /**
     * 현재 스레드에 설정된 데이터소스 타입을 반환합니다.
     * @return DataSourceType
     */
    public static DataSourceType getDataSourceType() {
        return dataSourceTypeThreadLocal.get();
    }

    /**
     * 현재 스레드의 데이터소스 설정을 초기화합니다.
     * 스레드 풀 환경에서 스레드를 재사용할 때를 대비해 반드시 호출해야 합니다.
     */
    public static void clear() {
        dataSourceTypeThreadLocal.remove();
    }
}
