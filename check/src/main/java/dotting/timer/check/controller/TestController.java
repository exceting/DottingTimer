package dotting.timer.check.controller;

import dotting.timer.check.service.TestService;
import dotting.timer.core.annos.DottingNode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Create by 18073 on 2018/12/7.
 */
@RestController
public class TestController {

    @Resource
    private TestService testService;

    @RequestMapping("/test1")
    @DottingNode(moudle = "dotting.timer.check", expect = 40, root = true, debug = true)
    public String test1() {
        StringBuilder sb = new StringBuilder();
        sb.append("one is :")
                .append(testService.getResultService1())
                .append(",  other one is : ")
                .append(testService.getResultService2());
        testService.loopService1();
        return sb.toString();
    }

}
