package com.icbc.audit.ogdl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.icbc.audit.ogdl.model.dto.DataSourceDTO;
import com.icbc.audit.ogdl.model.entity.OgDataSourceEntity;

import java.util.List;

public interface OgDataSourceService extends IService<OgDataSourceEntity> {
    IPage<OgDataSourceEntity> pageDatasources(int current, int size);
    OgDataSourceEntity getDatasourceById(Long id);
    boolean addDatasource(DataSourceDTO dataSourceDTO);
    boolean updateDatasource(DataSourceDTO dataSourceDTO);
    boolean deleteDatasource(Long id);
    boolean batchDeleteDatasources(List<Long> ids);

    boolean enableDatasource(DataSourceDTO dataSourceDTO);
}
