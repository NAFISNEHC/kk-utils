package com.kk.common.utils;

import com.kk.common.vo.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 生成树结构数据
 * 使用方式：
 * List<TreeNode> list = menuLeftMapper.selectNodeByLength(length);
 * String json = new TreeBuilder().buildTree(list);
 * </p>
 * @author tangbin
 * @since 2018-07-30
 */
public class BuildTree {

    List<TreeNode> TreeNodes = new ArrayList<>();


    public List<TreeNode> buildTree(List<TreeNode> TreeNodes) {

        BuildTree treeBuilder = new BuildTree(TreeNodes);

        return treeBuilder.buildJSONTree();
    }

    public BuildTree() {
    }

    public BuildTree(List<TreeNode> TreeNodes) {
        super();
        this.TreeNodes = TreeNodes;
    }

    // 构建JSON树形结构
    public List<TreeNode> buildJSONTree() {
        List<TreeNode> TreeNodeTree = buildTree();
        return TreeNodeTree;
    }

    // 构建树形结构
    public List<TreeNode> buildTree() {
        List<TreeNode> treeTreeNodes = new ArrayList<>();
        //获取所有根节点
        List<TreeNode> rootTreeNodes = getRootTreeNodes();
        //获取每个根节点
        for (TreeNode rootTreeNode : rootTreeNodes) {
            try {
                //递归这个根节点的子节点
                buildChildTreeNodes(rootTreeNode);

                //用list来存放每个根节点
                treeTreeNodes.add(rootTreeNode);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return treeTreeNodes;
    }

    // 递归子节点
    public void buildChildTreeNodes(TreeNode TreeNode) {
        List<TreeNode> children = getChildTreeNodes(TreeNode);
        if (!children.isEmpty()) {
            for (TreeNode child : children)
                try {
                    buildChildTreeNodes(child);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            TreeNode.setChildren(children);
        }
    }

    /**
     * 获取父节点下所有的子节点
     * @param pTreeNode
     * @return
     */
    public List<TreeNode> getChildTreeNodes(TreeNode pTreeNode) {
        List<TreeNode> childTreeNodes = new ArrayList<>();

        for (TreeNode n : TreeNodes) {
            if (pTreeNode.getCode().equals(n.getPcode())) {
                childTreeNodes.add(n);
            }
        }
        return childTreeNodes;
    }

    // 判断是否为根节点
    public boolean rootTreeNode(TreeNode TreeNode) {
        boolean isRootTreeNode = true;
        for (TreeNode n : TreeNodes) {
            if (TreeNode.getPcode().equals(n.getCode())) {
                isRootTreeNode = false;
                break;
            }
        }
        return isRootTreeNode;
    }

    // 获取集合中所有的根节点
    public List<TreeNode> getRootTreeNodes() {
        List<TreeNode> rootTreeNodes = new ArrayList<>();
        for (TreeNode n : TreeNodes) {
            if (rootTreeNode(n)) {
                rootTreeNodes.add(n);
            }
        }
        return rootTreeNodes;
    }
}