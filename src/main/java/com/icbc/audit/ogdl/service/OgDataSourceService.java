package com.icbc.audit.ogdl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icbc.audit.ogdl.model.dto.ConnectionTestRequestDTO;
import com.icbc.audit.ogdl.model.dto.ConnectionTestResultDTO;
import com.icbc.audit.ogdl.model.dto.DataSourceDTO;
import com.icbc.audit.ogdl.model.entity.OgDataSourceEntity;

import java.sql.SQLException;
import java.util.List;

public interface OgDataSourceService extends IService<OgDataSourceEntity> {
    IPage<OgDataSourceEntity> pageDatasources(int current, int size);
    OgDataSourceEntity getDatasourceById(Long id);
    boolean addDatasource(DataSourceDTO dataSourceDTO);
    boolean updateDatasource(DataSourceDTO dataSourceDTO);
    ConnectionTestResultDTO testConnection(ConnectionTestRequestDTO request) throws SQLException;
    boolean deleteDatasource(Long id);
    boolean batchDeleteDatasources(List<Long> ids);

    boolean enableDatasource(DataSourceDTO dataSourceDTO);
}
