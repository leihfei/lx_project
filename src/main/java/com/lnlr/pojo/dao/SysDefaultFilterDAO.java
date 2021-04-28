package com.lnlr.pojo.dao;

import com.lnlr.pojo.entity.SysDefaultFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author:leihfei
 * @description 默认数据库加载过滤器地址
 * @date:Create in 9:24 2018/10/9
 * @email:leihfein@gmail.com
 */
@Repository
public interface SysDefaultFilterDAO extends JpaRepository<SysDefaultFilter, String>, JpaSpecificationExecutor<SysDefaultFilter> {

    /**
     * @param type 过滤类型
     * @return
     * @author: leihfei
     * @description
     * @date: 16:52 2018/12/14
     * @email: leihfein@gmail.com
     */
    List<SysDefaultFilter> findAllByType(Integer type);
}
