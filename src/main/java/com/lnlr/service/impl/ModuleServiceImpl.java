package com.lnlr.service.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.lnlr.common.annonation.ServiceLogAnonation;
import com.lnlr.common.constains.LogConstants;
import com.lnlr.common.constains.SystemConstants;
import com.lnlr.common.entity.IdEntity;
import com.lnlr.common.exception.WarnException;
import com.lnlr.common.utils.*;
import com.lnlr.pojo.dao.SysModuleDAO;
import com.lnlr.pojo.dao.SysRoleModuleDAO;
import com.lnlr.pojo.dao.SysUserRoleDAO;
import com.lnlr.pojo.entity.SysModule;
import com.lnlr.pojo.entity.SysRoleModule;
import com.lnlr.pojo.entity.SysUserRole;
import com.lnlr.pojo.enums.ModuleTargetMenu;
import com.lnlr.pojo.param.base.ModuleParam;
import com.lnlr.pojo.vo.auth.ModuleListVO;
import com.lnlr.pojo.vo.auth.ModuleNgZorroTreeVO;
import com.lnlr.pojo.vo.auth.ModuleVO;
import com.lnlr.service.ModuleServices;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author leihf
 * @email leihfein@gmail.com
 * @description 菜单管理业务接口实现
 * @date 2019-04-10 11:09:55
 */
@Service
public class ModuleServiceImpl implements ModuleServices {

    @Autowired
    private SysModuleDAO moduleDAO;

    @Autowired
    private SysRoleModuleDAO roleModuleDAO;

    @Autowired
    private SysUserRoleDAO userRoleDAO;


    @Autowired
    private RedisUtil redisUtil;


    @Value("${module_expire}")
    private Integer moduleExpire;

    /**
     * @param idEntity 节点id
     * @return com.xzl.common.jpa.model.NgData
     * @author leihfei
     * @description 分页
     * @date 11:28:51 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "分页查询", moduleName = "模块管理")
    public List<ModuleListVO> page(IdEntity idEntity) {
        List<ModuleListVO> list = Lists.newArrayList();
        List<SysModule> data = null;
        if (StringUtils.isEmpty(idEntity.getId())) {
            // 查询所有数据，第一级数据5个平台下面的数据
            data = moduleDAO.findAllByParentIdOrderByShowLevel(SystemConstants.DEFAULT_MODULE_PARENTID);
        } else {
            // 查询当前节点下的所有第一级数据
            data = moduleDAO.findAllByParentIdOrderByShowLevel(idEntity.getId());
        }
        // 进行循环处理data数据之后返回
        data.forEach(item -> {
            ModuleListVO vo = new ModuleListVO();
            vo.setId(item.getId());
            vo.setKey(item.getId());
            vo.setName(item.getName());
            vo.setShowLevel(item.getShowLevel());
            vo.setStatus(item.getStatus());
            vo.setUrl(item.getUrl());
            if (item.getType() == 0) {
                vo.setShowExpand(true);
            }
            list.add(vo);
        });
        return list;
    }

    /**
     * @param ids 菜单id
     * @return public void
     * @author leihfei
     * @description 删除菜单
     * @date 11:29:08 2019-04-10
     */
    @Override
    @Transactional
    @ServiceLogAnonation(type = LogConstants.DELETE_STATUS, value = "删除菜单", moduleName = "模块管理")
    public void delete(IdEntity ids) {
        BeanValidator.check(ids);
        // 查询得到所有数据
        List<SysModule> all = moduleDAO.findAll();
        // 循环找出同一个父节点下的第一级数据
        List<SysModule> data = all.stream().filter(e -> e.getParentId().equals(ids.getId()) || e.getId().equals(ids.getId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(data)) {
            if (data.get(0).getParentId().equals(SystemConstants.DEFAULT_MODULE_PARENTID)) {
                throw new WarnException("该节点不允许删除");
            }
        }
        // 循环第一级数据，找到所有相关联的数据集
        Set<String> deleteIds = Sets.newHashSet();
        data.forEach(item -> {
            // 寻找是否还有值
            deleteIds.addAll(findDelete(item.getId(), all));

            deleteIds.add(item.getId());
        });
        moduleDAO.deleteAllByIdIn(deleteIds);
        // 继续删除角色相关
        List<SysRoleModule> roles = roleModuleDAO.findAllByModuleIdIn(deleteIds);
        if (CollectionUtils.isNotEmpty(roles)) {
            roleModuleDAO.deleteAll(roles);
        }
    }

    /**
     * @param id  待寻找的id
     * @param all 所有数据
     * @return java.util.Collection
     * @description 寻找子菜单
     * @date 13:36:05 2019-04-10
     */
    private Collection<? extends String> findDelete(String id, List<SysModule> all) {
        Set<String> ids = Sets.newHashSet();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getParentId().equals(id)) {
                ids.add(all.get(i).getId());
                if (all.get(i).getType() == 0) {
                    // 继续查询是否存在父节点
                    ids.addAll(findDelete(all.get(i).getId(), all));
                } else {
                    all.remove(i);
                    i--;
                }
            }
        }
        return ids;
    }


    /**
     * @param idEntity 菜单id
     * @return public void
     * @author leihfei
     * @description 查询菜单
     * @date 11:29:50 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "查询菜单", moduleName = "模块管理")
    public ModuleVO view(IdEntity idEntity) {
        BeanValidator.check(idEntity);
        SysModule sysModule = moduleDAO.findById(idEntity.getId()).orElse(null);
        if (sysModule == null) {
            throw new WarnException("数据不存在");
        }
        // 进行数据的补充返回
        ModuleVO vo = new ModuleVO();
        vo.setName(sysModule.getName());
        SysModule parent = moduleDAO.findById(sysModule.getParentId()).orElse(null);
        if (parent != null) {
            vo.setParentName(parent.getName());
        }
        vo.setTypeName(sysModule.getType() == 0 ? "菜单" : "按钮");
        vo.setIcon(sysModule.getIcon());
        vo.setUrl(sysModule.getUrl());
        vo.setShowLevel(sysModule.getShowLevel());
        vo.setRemark(sysModule.getRemark());
        vo.setTargetName(ModuleTargetMenu.getMessage(sysModule.getTargetType()));
        vo.setStatus(sysModule.getStatus() == 0 ? "可见" : "隐藏");
        return vo;
    }

    /**
     * @param
     * @return public void
     * @author leihfei
     * @description 查询菜单树形接口
     * @date 11:30:14 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "查询菜单树形接口", moduleName = "模块管理")
    public List<ModuleNgZorroTreeVO> list() {
        // 1. 首先查询出所有的菜单节点
        List<SysModule> data = moduleDAO.findAllByType(0);
        return tree(data);
    }

    private List<ModuleNgZorroTreeVO> dealTree(List<ModuleNgZorroTreeVO> retuData, Map<String, Collection<SysModule>> maps) {
        maps.forEach((key, list) -> {
            // 循环list，就得到每一个父节点下面的自节点数据
            for (SysModule item : list) {
                if (key.equals(SystemConstants.DEFAULT_MODULE_PARENTID)) {
                    // 表名是父节点，那么需要将数据补充到第一级节点中
                    ModuleNgZorroTreeVO vo = new ModuleNgZorroTreeVO();
                    setValue(maps, item, vo);
                    retuData.add(vo);
                }
            }
        });
        //循环树，添加层数
        retuData.forEach(e -> {
            e.setLevel(1);
            enrichLevel(e);
        });
        return retuData;
    }

    private void enrichLevel(ModuleNgZorroTreeVO treeVO) {
        if (CollectionUtils.isNotEmpty(treeVO.getChildren())) {
            treeVO.getChildren().forEach(e -> {
                e.setLevel(treeVO.getLevel() + 1);
                enrichLevel(e);
            });
        }
    }

    private List<ModuleNgZorroTreeVO> dealChildrenTree(SysModule saveData, Map<String, Collection<SysModule>> maps) {
        // 循环data,利用
        List<SysModule> data = (List<SysModule>) maps.get(saveData.getId());
        if (CollectionUtils.isNotEmpty(data)) {
            List<ModuleNgZorroTreeVO> childrens = Lists.newArrayList();
            // 说明该节点有子节点，那么继续处理返回
            data.forEach(item -> {
                ModuleNgZorroTreeVO vo = new ModuleNgZorroTreeVO();
                setValue(maps, item, vo);
                childrens.add(vo);
            });
            return childrens;
        }
        return Lists.newArrayList();
    }

    private void setValue(Map<String, Collection<SysModule>> maps, SysModule item, ModuleNgZorroTreeVO vo) {
        vo.setKey(item.getId());
        vo.setValue(item.getId());
        vo.setTitle(item.getName());
        vo.setUrl(item.getUrl());
        vo.setIcon(item.getIcon());
        vo.setShowLevel(item.getShowLevel());
        vo.setStatus(item.getStatus());
        // 处理子节点
        List<ModuleNgZorroTreeVO> child = dealChildrenTree(item, maps);
        if (CollectionUtils.isNotEmpty(child)) {
            vo.setChildren(child);
            vo.setLeaf(false);
        }
    }


    /**
     * @param param 菜单对象
     * @return
     * @author leihfei
     * @description 新增菜单
     * @date 11:30:31 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.CREATE_STATUS, value = "新增菜单", moduleName = "模块管理")
    public void create(ModuleParam param) {
        if (StringUtils.isNotEmpty(param.getId())) {
            throw new WarnException("ID不允许有值!");
        }
        BeanValidator.check(param);
        // 检查父节点是否有效
        checkParent(param.getParentId());
        // 进行数据保存操作
        SysModule sysModule = CopyUtils.beanCopy(param, new SysModule());
        sysModule.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
        sysModule.setOperator(RequestHolder.currentUser().getRealName());
        if (param.getTargetType() != null && param.getTargetType() == 1) {
            // 如果是按钮，必须传递url
            if (StringUtils.isEmpty(sysModule.getUrl())) {
                throw new WarnException("链接不允许为空!");
            }
        }
        // 查询最大的auth_key,然后加1
        Integer maxAuthKey = moduleDAO.findMaxAuthKey();
        if (maxAuthKey == null) {
            throw new WarnException("authKey异常，无法新增!");
        }
        sysModule.setAuthKey(maxAuthKey + 1);
        moduleDAO.save(sysModule);
    }

    private void checkParent(String parentId) {
        SysModule sysModule = moduleDAO.findById(parentId).orElse(null);
        if (sysModule == null) {
            throw new WarnException("父节点不存在");
        }
    }

    /**
     * @param param 菜单对象
     * @return public void
     * @author leihfei
     * @description 更新菜单
     * @date 11:29:27 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.UPDATE_STATUS, value = "更新菜单", moduleName = "模块管理")
    public void update(ModuleParam param) {
        if (StringUtils.isEmpty(param.getId())) {
            throw new WarnException("ID不允许为空!");
        }
        BeanValidator.check(param);
        // 查询数据是否存在
        SysModule befor = moduleDAO.findById(param.getId()).orElse(null);
        if (befor == null) {
            throw new WarnException("数据不存在,无法更新!");
        }
        // 检查父节点是否有效
        checkParent(param.getParentId());
        // 需要对新值拷贝到原始值中
        CopyUtils.copyProperties2(param, befor);
        // 进行数据保存操作
        befor.setOperatorIp(IpUtils.getIpAddr(RequestHolder.currentRequest()));
        befor.setOperator(RequestHolder.currentUser().getRealName());
        if (param.getTargetType() == 1) {
            // 如果是按钮，必须传递url
            if (StringUtils.isEmpty(befor.getUrl())) {
                throw new WarnException("链接不允许为空!");
            }
        }
        moduleDAO.save(befor);
    }

    /**
     * @param idEntity
     * @return com.xzl.security.pojo.master.entity.SysModule
     * @author leihfei
     * @description 点击编辑调用接口
     * @date 14:12:04 2019-04-10
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "点击编辑调用接口", moduleName = "模块管理")
    public SysModule edit(IdEntity idEntity) {
        BeanValidator.check(idEntity);
        SysModule sysModule = moduleDAO.findById(idEntity.getId()).orElse(null);
        return sysModule;
    }


    /**
     * @param list 菜单数据
     * @return java.util.List<com.xzl.security.pojo.master.vo.zorro.NgZorroTreeVO>
     * @author leihfei
     * @description 组装树形结构
     * @date 16:58:49 2019-04-15
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "组装树形结构", moduleName = "模块管理")
    public List<ModuleNgZorroTreeVO> tree(List<SysModule> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        // 将数据全部转化为map结构
        Multimap<String, SysModule> map = ArrayListMultimap.create();
        list.forEach(item -> {
            map.put(item.getParentId(), item);
        });
        Map<String, Collection<SysModule>> maps = map.asMap();
        // 组装数据并且返回
        List<ModuleNgZorroTreeVO> tree = dealTree(Lists.newArrayList(), maps);
        return tree;
    }

    /**
     * @param idEntity id
     * @return java.util.List<com.xzl.security.pojo.master.vo.zorro.NgZorroTreeVO>
     * @author leihfei
     * @description 查询所有权限，菜单数据
     * @date 17:07:49 2019-04-15
     */
    @Override
    @ServiceLogAnonation(type = LogConstants.QUERY_STATUS, value = "查询所有权限，菜单数据", moduleName = "模块管理")
    public List<ModuleNgZorroTreeVO> pageTree(IdEntity idEntity) {
        List<SysModule> list = null;
        if (StringUtils.isNotEmpty(idEntity.getId())) {
            list = moduleDAO.findAllByParentIdOrderByShowLevel(idEntity.getId());
        } else {
            list = moduleDAO.findAll(Sort.by(Sort.Direction.ASC, "showLevel"));
        }
        List<ModuleNgZorroTreeVO> tree = null;
        String module_tree = redisUtil.get(SystemConstants.MDOULE_REDIS_PREFIX);
        if (StringUtils.isNotEmpty(module_tree)) {
            tree = JsonUtils.json2List(module_tree, ModuleNgZorroTreeVO.class);
        } else {
            tree = tree(list);
            // 并且将tree进行序列化
            redisUtil.set(SystemConstants.MDOULE_REDIS_PREFIX, JsonUtils.list2Json(tree), moduleExpire);
        }
        return tree;
    }

    @Override
    public List<SysModule> findAll() {
        return moduleDAO.findAll();
    }

    /**
     * 通过权限接口查询权限点
     *
     * @param path 权限接口
     * @return
     */
    @Override
    public List<SysModule> findAllByUrl(String path) {
        return moduleDAO.findAllByUrl(path);
    }

    /**
     * @param aclIds 菜单id
     * @return java.util.List<com.xzl.security.pojo.master.entity.SysModule>
     * @author leihfei
     * @description 通过菜单id查询时数据
     * @date 12:45:04 2019-04-24
     */
    @Override
    public List<SysModule> findAllByIds(List<String> aclIds) {
        return moduleDAO.findAllById(aclIds);
    }


    @Override
    public List<SysModule> findAllBySortShowLevel() {
        return moduleDAO.findAll(Sort.by(Sort.Direction.ASC, "showLevel"));
    }


    @Override
    public List<SysModule> listChildMenu() {
        List<SysUserRole> userRoles = userRoleDAO.findAllByUserId(RequestHolder.currentUser().getId());
        Set<String> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
        List<SysRoleModule> roleModules = roleModuleDAO.findAllByRoleIdIn(roleIds);
        Set<String> moduleIds = roleModules.stream().map(SysRoleModule::getModuleId).collect(Collectors.toSet());
        List<SysModule> modules = moduleDAO.findAllByTypeAndIdIn(0, moduleIds);
        return modules.stream()
                .filter(e -> !e.getParentId().equals(SystemConstants.DEFAULT_MODULE_PARENTID))
                .filter(e -> !e.getId().equals(SystemConstants.DEFAULT_MODULE_PARENTID))
                .collect(Collectors.toList());
    }
}
