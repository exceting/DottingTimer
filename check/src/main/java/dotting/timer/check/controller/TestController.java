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

    public static ExecutorService threadPool = DottingTracerTTL.transToTTL(Executors.newFixedThreadPool(4));

    public static ExecutorService threadPool2 = DottingTracerTTL.transToTTL(Executors.newFixedThreadPool(4));

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
    public String asyncTest1() {
        StringBuilder sb = new StringBuilder();
        sb.append("one is :")
                .append(testService.getResultService1())
                .append(",  other one is : ")
                .append(testService.getResultService2());
        for (int i = 0; i < 10; i++) {
            testService.loopService1();
        }
        testDao.asyncMethodDao1();
        testDao.asyncMethodDao0();
        for (int i = 0; i < 7; i++) {
            testDao.asyncMethodDao0();
        }
        TestController.threadPool.submit(() -> {
            testService.asyncMethod0();
        });
        threadPool.submit(() -> testService.asyncMethod1());
        threadPool.submit(() -> testService.asyncMethod2());
        threadPool.submit(() -> testService.asyncMethod1());
        threadPool.submit(() -> testService.asyncMethod2());
        threadPool2.submit(() -> testService.asyncMethod1());
        return sb.toString();
    }

}
