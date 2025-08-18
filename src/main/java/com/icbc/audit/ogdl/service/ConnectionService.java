package com.icbc.audit.ogdl.service;

import com.icbc.audit.ogdl.model.dto.ConnectionTestRequestDTO;
import com.icbc.audit.ogdl.model.dto.ConnectionTestResultDTO;
import com.icbc.audit.ogdl.model.dto.TableMetadataDTO;

import java.sql.SQLException;
import java.util.List;

public interface ConnectionService {
    ConnectionTestResultDTO testConnection(ConnectionTestRequestDTO request) throws SQLException;
    List<TableMetadataDTO> getTableMetadata(String datasourceId, String tableName) throws SQLException;
    String generateSql(String datasourceId, String tableName) throws SQLException;
}
