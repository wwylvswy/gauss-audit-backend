package com.icbc.audit.ogdl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icbc.audit.ogdl.mapper.OgDataSourceMapper;
import com.icbc.audit.ogdl.model.dto.DataSourceDTO;
import com.icbc.audit.ogdl.model.entity.OgDataSourceEntity;
import com.icbc.audit.ogdl.service.OgDataSourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OgDataSourceServiceImpl extends ServiceImpl<OgDataSourceMapper, OgDataSourceEntity> implements OgDataSourceService {
    @Override
    public IPage<OgDataSourceEntity> pageDatasources(int current, int size) {
        Page<OgDataSourceEntity> page = new Page<>(current, size);

        return this.page(page);
    }

    @Override
    public OgDataSourceEntity getDatasourceById(Long id) {
        return this.getById(id);
    }

    @Override
    public boolean addDatasource(DataSourceDTO dataSourceDTO) {
        OgDataSourceEntity datasource = new OgDataSourceEntity();
        BeanUtils.copyProperties(dataSourceDTO, datasource);
        datasource.setCreateTime(LocalDateTime.now());
        // datasource.setCreator("system"); // 实际应从上下文中获取当前用户
        return this.save(datasource);
    }

    @Override
    public boolean updateDatasource(DataSourceDTO dataSourceDTO) {
        if (dataSourceDTO.getId() == null) {
            return false;
        }
        OgDataSourceEntity datasource = this.getById(dataSourceDTO.getId());
        if (datasource == null) {
            return false;
        }
        BeanUtils.copyProperties(dataSourceDTO, datasource);
        // 可以选择性更新创建时间和创建人等字段
        return this.updateById(datasource);
    }

    @Override
    public boolean deleteDatasource(Long id) {
        return this.removeById(id);
    }

    @Override
    public boolean batchDeleteDatasources(List<Long> ids) {
        return this.removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableDatasource(DataSourceDTO dataSourceDTO) {
        OgDataSourceEntity datasource = new OgDataSourceEntity();
        datasource.setId(dataSourceDTO.getId());
        datasource.setStatus(dataSourceDTO.getStatus());
        return this.updateById(datasource);
    }
}
