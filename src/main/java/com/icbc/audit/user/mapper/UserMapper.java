package com.icbc.audit.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.icbc.audit.user.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
    @Select("""
        SELECT r.role_key
        FROM t_user u
        JOIN sys_user_role ur ON u.id = ur.user_id
        JOIN sys_role r ON ur.role_id = r.id
        WHERE u.account = #{account} AND u.enabled = 1
        """)
    List<String> selectRolesByAccount(String account);
}
