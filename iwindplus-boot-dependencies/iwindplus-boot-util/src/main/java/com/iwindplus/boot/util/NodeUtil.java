/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.util;

import com.iwindplus.boot.util.domain.vo.BaseNodeVO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 树形无限极工具类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
public class NodeUtil {
    /**
     * 封装树形结构.
     *
     * @param <TT>  泛型
     * @param nodes 节点集合
     * @return List<TT>
     */
    public static <TT extends BaseNodeVO<TT>> List<TT> getTree(List<TT> nodes) {
        return findRootNodes(nodes);
    }

    /**
     * 找出根节点.
     *
     * @param <TT>  泛型
     * @param nodes 节点集合
     * @return List<TT>
     */
    private static <TT extends BaseNodeVO<TT>> List<TT> findRootNodes(List<TT> nodes) {
        List<TT> rootNodes = new ArrayList<>();
        nodes.stream().forEach(node -> {
            if (null == node.getParentId() || (null != node.getParentId() && StringUtils.equalsIgnoreCase("0",
                    node.getParentId()))) {
                rootNodes.add(node);
                findChildNodes(node, nodes);
            }
        });
        // 对父节点进行排序
        Collections.sort(rootNodes, Comparator.comparing(TT::getSeq));
        return rootNodes;
    }

    /**
     * 找出子节点.
     *
     * @param parentNode 父节点对象
     * @param nodes      节点集合
     * @param <TT>       泛型
     */
    private static <TT extends BaseNodeVO<TT>> void findChildNodes(TT parentNode, List<TT> nodes) {
        List<TT> children = new ArrayList<>();
        parentNode.setNodes(children);
        nodes.stream().forEach(node -> {
            if (null != node.getParentId() && null != parentNode.getId() && StringUtils.equalsIgnoreCase(
                    node.getParentId(), parentNode.getId())) {
                children.add(node);
                findChildNodes(node, nodes);
            }
        });
        // 对子节点进行排序
        Collections.sort(children, Comparator.comparing(TT::getSeq));
    }
}
