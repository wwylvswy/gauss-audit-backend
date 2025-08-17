package com.icbc.audit.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icbc.audit.user.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<MenuEntity> {

    @Select("""
        SELECT m.* FROM sys_menu m
        WHERE JSON_CONTAINS(m.roles, CONCAT('\"', #{roleKey}, '\"'))
        ORDER BY m.m_rank ASC
        """)
    List<MenuEntity> selectMenusByRole(@Param("roleKey") String roleKey);
}
