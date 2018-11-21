/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.ui.po;

import com.google.common.collect.Lists;

import java.util.List;

/**
 *
 * @author sunqinwen
 * @version \: SpanTree.java,v 0.1 2018-11-19 16:47 
 *
 */
public class SpanTree {

    private Span node;

    private List<SpanTree> child = Lists.newArrayList();

    public Span getNode() {
        return node;
    }

    public void setNode(Span node) {
        this.node = node;
    }

    public List<SpanTree> getChild() {
        return child;
    }

    public dotting.timer.ui.po.SpanTree setChild(Span span) {
        dotting.timer.ui.po.SpanTree childNode = new dotting.timer.ui.po.SpanTree();
        childNode.setNode(span);
        child.add(childNode);
        return childNode;
    }
}
