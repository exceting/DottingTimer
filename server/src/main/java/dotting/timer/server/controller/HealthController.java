/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sunqinwen
 * @version \: HealthController.java,v 0.1 2018-12-05 19:23
 */
@RestController
public class HealthController {

    @GetMapping("/check_health.do")
    public String checkHealth() {
        return "success";
    }

}
