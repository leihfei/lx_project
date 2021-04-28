package com.lnlr.pojo.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author:leihfei
 * @description: 基础dao接口
 * @date:Create in 10:30 2018/12/9
 * @email:leihfein@gmail.com
 */
@NoRepositoryBean
public interface BaseDAO<T> extends JpaRepository<T, String>, JpaSpecificationExecutor<T> {
}
