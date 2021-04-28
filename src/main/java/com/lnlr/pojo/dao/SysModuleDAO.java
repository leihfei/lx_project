package com.lnlr.pojo.dao;

import com.lnlr.pojo.base.BaseDAO;
import com.lnlr.pojo.entity.SysModule;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

/**
 * @author leihfei
 * @description 菜单持久化接口
 * @date 11:58:26 2019-04-10
 */
public interface SysModuleDAO extends BaseDAO<SysModule> {

    /**
     * @param parentId 父节点id
     * @return java.util.List<com.xzl.security.pojo.master.entity.SysModule>
     * @author leihfei
     * @description 通过父节点查询数据
     * @date 11:58:10 2019-04-10
     */
    List<SysModule> findAllByParentIdOrderByShowLevel(String parentId);

    /**
     * @param type 数据类型，0-菜单，1-按钮
     * @return java.util.List<com.xzl.security.pojo.master.entity.SysModule>
     * @author leihfei
     * @description 查询指定类型的数据
     * @date 12:05:56 2019-04-10
     */
    List<SysModule> findAllByType(Integer type);

    /**
     * @param ids id列表
     * @return void
     * @author leihfei
     * @description 删除
     * @date 12:23:43 2019-04-24
     */
    void deleteAllByIdIn(Set<String> ids);

    /**
     * 查询匹配所有满足url的数据
     * 主要用于动态参数/sys/auth/view/1
     *
     * @param url
     * @return
     */
    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM sys_module where url is not null and url <> '' and   ?1 REGEXP url", nativeQuery = true)
    List<SysModule> findAllByUrl(String url);


    /**
     * @return java.lang.Integer
     * @author leihfei
     * @description 查询最大的key
     * @date 13:53:27 2019-05-12
     */
    @Transactional
    @Query(value = "select max(auth_key) from sys_module", nativeQuery = true)
    Integer findMaxAuthKey();

    /**
     * 查询菜单
     *
     * @param type 菜单类型
     * @param ids  ids
     * @return 菜单
     */
    List<SysModule> findAllByTypeAndIdIn(Integer type, Iterable<String> ids);
}
