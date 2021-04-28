package com.lnlr.service;


import com.lnlr.common.entity.IdEntity;
import com.lnlr.pojo.entity.SysModule;
import com.lnlr.pojo.param.base.ModuleParam;
import com.lnlr.pojo.vo.auth.ModuleListVO;
import com.lnlr.pojo.vo.auth.ModuleNgZorroTreeVO;
import com.lnlr.pojo.vo.auth.ModuleVO;

import java.util.List;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 菜单管理业务接口
 * @date 2019-04-10 11:09:30
 */
public interface ModuleServices {
    /**
     * @param idEntity 节点
     * @return com.xzl.common.jpa.model.NgData
     * @author leihfei
     * @description 分页
     * @date 11:28:51 2019-04-10
     */
    List<ModuleListVO> page(IdEntity idEntity);

    /**
     * @param ids 菜单id
     * @return void
     * @author leihfei
     * @description 删除菜单
     * @date 11:29:08 2019-04-10
     */
    void delete(IdEntity ids);

    /**
     * @param param 菜单对象
     * @return void
     * @author leihfei
     * @description 更新菜单
     * @date 11:29:27 2019-04-10
     */
    void update(ModuleParam param);

    /**
     * @param idEntity 菜单id
     * @return void
     * @author leihfei
     * @description 查询菜单
     * @date 11:29:50 2019-04-10
     */
    ModuleVO view(IdEntity idEntity);

    /**
     * @param
     * @return List
     * @author leihfei
     * @description 查询菜单树形接口
     * @date 11:30:14 2019-04-10
     */
    List<ModuleNgZorroTreeVO> list();

    /**
     * @param param 菜单对象
     * @return void
     * @author leihfei
     * @description 新增菜单
     * @date 11:30:31 2019-04-10
     */
    void create(ModuleParam param);

    /**
     * @param idEntity
     * @return com.xzl.security.pojo.master.entity.SysModule
     * @author leihfei
     * @description 点击编辑调用接口
     * @date 14:12:04 2019-04-10
     */
    SysModule edit(IdEntity idEntity);

    /**
     * @param list 菜单数据
     * @return java.util.List<com.xzl.security.pojo.master.vo.zorro.NgZorroTreeVO>
     * @author leihfei
     * @description 组装树形结构
     * @date 16:58:49 2019-04-15
     */
    List<ModuleNgZorroTreeVO> tree(List<SysModule> list);

    /**
     * @param idEntity id
     * @return java.util.List<com.xzl.security.pojo.master.vo.zorro.NgZorroTreeVO>
     * @author leihfei
     * @description 查询所有权限，菜单数据
     * @date 17:07:49 2019-04-15
     */
    List<ModuleNgZorroTreeVO> pageTree(IdEntity idEntity);

    List<SysModule> findAll();

    /**
     * 通过权限接口查询权限点
     *
     * @param path 权限接口
     * @return
     */
    List<SysModule> findAllByUrl(String path);

    /**
     * @param aclIds 菜单id
     * @return java.util.List<com.xzl.security.pojo.master.entity.SysModule>
     * @author leihfei
     * @description 通过菜单id查询时数据
     * @date 12:45:04 2019-04-24
     */
    List<SysModule> findAllByIds(List<String> aclIds);

    List<SysModule> findAllBySortShowLevel();

    /**
     * 二级菜单及以下所有子菜单
     *
     * @return 二级菜单及以下所有子菜单
     */
    List<SysModule> listChildMenu();
}
