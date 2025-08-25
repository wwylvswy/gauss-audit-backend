package com.icbc.audit.ogdl.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icbc.audit.ogdl.mapper.OgDataSourceMapper;
import com.icbc.audit.ogdl.model.dto.ConnectionTestRequestDTO;
import com.icbc.audit.ogdl.model.dto.ConnectionTestResultDTO;
import com.icbc.audit.ogdl.model.dto.DataSourceDTO;
import com.icbc.audit.ogdl.model.entity.OgDataSourceEntity;
import com.icbc.audit.ogdl.service.OgDataSourceService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

    // JDBC URL format for PostgreSQL-compatible OpenGauss
    // 注意：这里使用的是 postgresql 前缀
    private static final String OG_PG_JDBC_URL_TEMPLATE = "jdbc:postgresql://%s:%d/%s";

    @Override
    public ConnectionTestResultDTO testConnection(ConnectionTestRequestDTO request) throws SQLException {
        ConnectionTestResultDTO result = new ConnectionTestResultDTO();
        // 使用 PostgreSQL 兼容的 URL 格式
        String url = String.format(OG_PG_JDBC_URL_TEMPLATE, request.getHost(), request.getPort(), request.getDatabaseName());

        // 加载 PostgreSQL JDBC 驱动
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("找不到PostgreSQL JDBC驱动", e);
        }

        // 建立连接并设置5秒超时
        try (Connection conn = DriverManager.getConnection(url, request.getUsername(), request.getPassword())) {
            conn.setNetworkTimeout(Runnable::run, (int) TimeUnit.SECONDS.toMillis(5)); // 5秒超时
            boolean isValid = conn.isValid((int) TimeUnit.SECONDS.toMillis(5));
            result.setValid(isValid);

            if (isValid) {
                // 查询数据库版本
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT version()")) {
                    if (rs.next()) {
                        result.setVersion(rs.getString(1));
                    }
                }
            }
        }
        return result;
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
