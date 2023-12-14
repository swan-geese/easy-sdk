package com.easy.sdk.demo.tree;

import cn.hutool.Hutool;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.json.JSONUtil;
import com.easy.sdk.common.util.EntityUtil;
import com.easy.sdk.demo.tree.entity.HutoolTree;
import com.easy.sdk.demo.tree.entity.TreeAnnotationDTO;
import com.easy.sdk.demo.tree.entity.TreeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zy
 * @version 1.0
 * @date Created in 2023/11/22 6:13 PM
 * @description 递归树测试类
 */
@SpringBootTest
@Slf4j
public class TreeTest {

    /**
     * 递归树测试
     */
    @Test
    public void treeTest() {
        List<TreeDTO> list = new ArrayList<>();
        list.add(TreeDTO.of().setId(1).setParentId(0));
        list.add(TreeDTO.of().setId(2).setParentId(0));
        list.add(TreeDTO.of().setId(3).setParentId(1));
        list.add(TreeDTO.of().setId(4).setParentId(2));
        list.add(TreeDTO.of().setId(5).setParentId(2));
        List<TreeDTO> treeList = EntityUtil.toTreeList(list);
        log.info(JSONUtil.toJsonPrettyStr(treeList));
    }


    /**
     * 注解方式递归树测试
     */
    @Test
    public void treeAnnotationTest() {
        List<TreeAnnotationDTO> list = new ArrayList<>();
        list.add(TreeAnnotationDTO.of().setId(1).setParentId(0));
        list.add(TreeAnnotationDTO.of().setId(2).setParentId(0));
        list.add(TreeAnnotationDTO.of().setId(3).setParentId(1));
        list.add(TreeAnnotationDTO.of().setId(4).setParentId(2));
        list.add(TreeAnnotationDTO.of().setId(5).setParentId(2));
        List<TreeAnnotationDTO> treeList = EntityUtil.toTreeAnnoList(list);
        log.info(JSONUtil.toJsonPrettyStr(treeList));
    }


    /**
     * hutool递归树测试
     */
    @Test
    public void hutoolTreeTest() {
        // 构建node列表(数据源)
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();
        nodeList.add(new TreeNode<>("1", "0", "系统管理", 5));
        nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));
        nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
        nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));
        nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
        nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));
        // 0表示最顶层的id是0
        List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");
        log.info(JSONUtil.toJsonPrettyStr(treeList));

    }


    /**
     * hutool自定义字段名递归树测试
     */
    @Test
    public void hutoolCustomtreeTest() {
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名
        treeNodeConfig.setWeightKey("order"); // 权重排序字段 默认为weight
        treeNodeConfig.setIdKey("id"); // 默认为id可以不设置
        treeNodeConfig.setNameKey("name"); // 节点名对应名称 默认为name
        treeNodeConfig.setParentIdKey("parentId"); // 父节点 默认为parentId
        treeNodeConfig.setChildrenKey("children"); // 子点 默认为children
        treeNodeConfig.setDeep(3); // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
        //数据源
        List<HutoolTree> nodeList = hutoolTreeList();
        //转换器 "0" - 最顶层父id值 一般为0之类  nodeList – 源数据集合
        List<Tree<String>> treeNodes = TreeUtil.build(nodeList, "0", treeNodeConfig,
                // treeNode – 源数据实体
                // tree – 树节点实体
                (treeNode, tree) -> {
                    tree.setId(String.valueOf(treeNode.getId()));
                    tree.setParentId(String.valueOf(treeNode.getParentId()));
                    tree.setWeight(treeNode.getWeight());
                    tree.setName(treeNode.getName());
                    // 扩展属性 ...
                    tree.putExtra("extraField", 666);
                    tree.putExtra("other", new Object());
                });
        log.info(JSONUtil.toJsonPrettyStr(treeNodes));
    }

    private List<HutoolTree> hutoolTreeList() {
        List<HutoolTree> list = new ArrayList<>();
        list.add(HutoolTree.builder().id(1L).parentId(0L).name("111").weight(1).build());
        list.add(HutoolTree.builder().id(2L).parentId(0L).name("2222").weight(2).build());
        list.add(HutoolTree.builder().id(3L).parentId(1L).name("3333").weight(3).build());
        list.add(HutoolTree.builder().id(4L).parentId(1L).name("4444").weight(4).build());
        list.add(HutoolTree.builder().id(5L).parentId(2L).name("5555").weight(5).build());

        return list;
    }
}