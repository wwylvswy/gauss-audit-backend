package com.icbc.audit.assits.repository;

import com.icbc.audit.assits.model.ReviewRule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 对应【规则知识库模块】的数据访问接口
 * 继承 CrudRepository 以获得基本的CRUD功能
 */
@Repository
public interface RuleRepository extends CrudRepository<ReviewRule, Long> {

    /**
     * 定义一个方法来查找所有被启用的规则
     * Spring Data JPA 会根据方法名自动生成查询
     * @return 一个包含所有启用规则的列表
     */
    List<ReviewRule> findByEnabledTrue();
}