package com.lnlr.common.jpa;

import com.lnlr.common.utils.IpUtils;
import com.lnlr.common.utils.RequestHolder;
import com.lnlr.pojo.base.BaseEntity;
import com.lnlr.pojo.entity.SysOperatorLog;
import com.lnlr.pojo.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

/**
 * 自定义repository的方法接口实现类
 *
 * @author wangwt
 * @date 2019-06-10 17:25
 */
public class CustomRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements JpaRepository<T, ID> {

    public CustomRepositoryImpl() {
        super((Class) null,  null);
    }

    public CustomRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public CustomRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
    }

    /**
     * 重写 save 方法，补上其他信息
     * @param entity
     * @param <S>
     * @return
     */
    @Override
    public <S extends T> S save(S entity) {
        // 日志信息直接保存
        if(entity instanceof SysOperatorLog){
            return super.save(entity);
        }
        // 业务信息，补上操作人信息和操作时间
        if(BaseEntity.class.isAssignableFrom(entity.getClass())){
            BaseEntity baseEntity = (BaseEntity)entity;
            if(baseEntity.getId() == null ||
                    "".equals(baseEntity.getId().trim())){
                baseEntity.setGmtCreate(LocalDateTime.now());
            }
            SysUser currentUser = RequestHolder.currentUser();
            baseEntity.setGmtModified(LocalDateTime.now());
            if(currentUser != null) {
                baseEntity.setOperator(currentUser.getRealName());
            }
            baseEntity.setOperatorIp(IpUtils.getRemoteIp(RequestHolder.currentRequest()));
        }
        return super.save(entity);
    }
}