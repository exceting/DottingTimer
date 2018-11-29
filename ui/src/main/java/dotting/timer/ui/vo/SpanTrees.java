/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.ui.vo;

import dotting.timer.ui.po.SpanTree;

import java.util.List;

/**
 * @author sunqinwen
 * @version \: SpanTrees.java,v 0.1 2018-11-22 18:11
 */
public class SpanTrees {

    private SpanTree masterThread;

    private List<SpanTree> slaveThread;

    public static SpanTrees build(SpanTree masterThread, List<SpanTree> slaveThread) {
        return new SpanTrees(masterThread, slaveThread);
    }

    public SpanTrees(SpanTree masterThread, List<SpanTree> slaveThread) {
        this.masterThread = masterThread;
        this.slaveThread = slaveThread;
    }

    public SpanTree getMasterThread() {
        return masterThread;
    }

    public void setMasterThread(SpanTree masterThread) {
        this.masterThread = masterThread;
    }

    public List<SpanTree> getSlaveThread() {
        return slaveThread;
    }

    public void setSlaveThread(List<SpanTree> slaveThread) {
        this.slaveThread = slaveThread;
    }
}