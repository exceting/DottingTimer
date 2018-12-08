package dotting.timer.check.controller;

import dotting.timer.check.dao.TestDao;
import dotting.timer.check.service.TestService;
import dotting.timer.core.annos.DottingNode;
import dotting.timer.core.async.DottingTracerTTL;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Create by 18073 on 2018/12/7.
 */
@RestController
public class TestController {

    @Resource
    private TestService testService;

    @Resource
    private TestDao testDao;

    private ExecutorService threadPool = DottingTracerTTL.transToTTL(Executors.newFixedThreadPool(4));

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

    @RequestMapping("/asyncTest1")
    @DottingNode(moudle = "dotting.timer.check", expect = 50, root = true, debug = true)
    public String asyncTest1(){
        StringBuilder sb = new StringBuilder();
        sb.append("one is :")
                .append(testService.getResultService1())
                .append(",  other one is : ")
                .append(testService.getResultService2());
        testService.loopService1();
        threadPool.submit(()-> testService.asyncMethod1());
        threadPool.submit(()-> testService.asyncMethod2());
        threadPool.submit(()-> testService.asyncMethod1());
        threadPool.submit(()-> testService.asyncMethod2());
        threadPool.submit(()-> testService.asyncMethod1());
        return sb.toString();
    }

}
