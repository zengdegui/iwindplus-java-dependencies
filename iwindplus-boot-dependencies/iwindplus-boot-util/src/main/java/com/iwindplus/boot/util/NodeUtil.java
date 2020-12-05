/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.util;

import com.iwindplus.boot.util.domain.vo.BaseNodeVO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 树形无限极工具类.
 *
 * @author zengdegui
 * @since 2018年9月1日
 */
public class NodeUtil {
    /**
     * 封装树形结构.
     *
     * @param <T>   泛型
     * @param nodes 节点集合
     * @return List<T>
     */
    public static <T extends BaseNodeVO<T>> List<T> getTree(List<T> nodes) {
        return findRootNodes(nodes);
    }

    /**
     * 找出根节点.
     *
     * @param <T>   泛型
     * @param nodes 节点集合
     * @return List<T>
     */
    private static <T extends BaseNodeVO<T>> List<T> findRootNodes(List<T> nodes) {
        List<T> rootNodes = new ArrayList<T>();
        nodes.stream().forEach(node -> {
            if (null == node.getParentId()
                    || (null != node.getParentId() && StringUtils.equalsIgnoreCase("0", node.getParentId()))) {
                rootNodes.add(node);
                findChildNodes(node, nodes);
            }
        });
        // 对父节点进行排序
        rootNodes.sort(Comparator.comparing(T::getSeq));
        return rootNodes;
    }

    /**
     * 找出子节点.
     *
     * @param parentNode 父节点对象
     * @param nodes      节点集合
     * @param <T>        泛型
     */
    private static <T extends BaseNodeVO<T>> void findChildNodes(T parentNode, List<T> nodes) {
        List<T> children = new ArrayList<T>();
        parentNode.setNodes(children);
        nodes.stream().forEach(node -> {
            if (null != node.getParentId() && null != parentNode.getId()
                    && StringUtils.equalsIgnoreCase(node.getParentId(), parentNode.getId())) {
                children.add(node);
                findChildNodes(node, nodes);
            }
        });
        // 对子节点进行排序
        children.sort(Comparator.comparing(T::getSeq));
    }
}
