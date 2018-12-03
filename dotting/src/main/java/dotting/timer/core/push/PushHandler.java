/**
 * sharemer.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */

package dotting.timer.core.push;

import dotting.timer.core.span.DottingSpan;

/**
 * @author sunqinwen
 * @version \: PushHandler.java,v 0.1 2018-09-20 19:12
 */
public interface PushHandler {

    void pushSpan(DottingSpan span);

}
