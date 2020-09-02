package com.kk.common.vo;

import java.util.List;

/**
 * 生成树节点
 */
public class TreeNode {

    private String code;
    private String notetype;
    private String pcode;
    private String name;
    private String id;
    private List<TreeNode> children;


    public TreeNode() {
    }

    public TreeNode(String code, String notetype, String pcode, String name, String id, List<TreeNode> children) {
        this.code = code;
        this.notetype = notetype;
        this.pcode = pcode;
        this.name = name;
        this.id = id;
        this.children = children;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setNotetype(String notetype) {
        this.notetype = notetype;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }


    public String getCode() {
        return code;
    }

    public String getNotetype() {
        return notetype;
    }

    public String getPcode() {
        return pcode;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

}