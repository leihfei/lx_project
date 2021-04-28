package com.lnlr.common.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 树形结构构建工具栏
 * @Date 2019/8/28 15:28
 * @Author yangyi
 */
public class TreeUtil {

    private List<TreeVO> nodes;

    public TreeUtil(List<TreeVO> nodes) {
        this.nodes = nodes;
    }

    /**
     * 根据根节点获取子节点
     *
     * @param root 根节点
     * @return 所有子节点
     */
    private List<TreeVO> getChildren(TreeVO root) {
        List<TreeVO> children = new ArrayList<>();
        //获取子节点
        for (TreeVO node : nodes) {
            if (node.getParentId() != null && node.getParentId().equals(root.getKey())) {
                children.add(node);
            }
        }
        return children;
    }

    /**
     * 构建树结构
     *
     * @return 树结构
     */
    public List<TreeVO> buildTree() {
        List<TreeVO> vos = new ArrayList<>();
        //根节点
        List<TreeVO> roots = nodes.stream()
                .filter(e -> "0".equals(e.getParentId()))
                .collect(Collectors.toList());
        for (TreeVO root : roots) {
            buildChildNodes(root);
            vos.add(root);
        }
        return vos;
    }

    /**
     * 递归子节点
     *
     * @param root 根节点
     */
    private void buildChildNodes(TreeVO root) {
        List<TreeVO> children = getChildren(root);
        if (children.isEmpty()) {
            //叶子节点为空那当前节点设为末端节点
            root.setIsLeaf(true);
        } else {
            for (TreeVO child : children) {
                buildChildNodes(child);
            }
            root.setChildren(children);
        }
    }
}
