package dotting.timer.check.service;

import dotting.timer.check.controller.TestController;
import dotting.timer.check.dao.TestDao;
import dotting.timer.core.annos.DottingNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Create by 18073 on 2018/12/7.
 */
@Service
public class TestService {

    @Resource
    private TestDao testDao;

    @DottingNode(expect = 20)
    public String getResultService1() {
        StringBuilder sb = new StringBuilder();
        sb.append(testDao.getResultDao11())
                .append(testDao.getResultDao12());
        return sb.toString();
    }

    @DottingNode(expect = 20)
    public String getResultService2() {
        StringBuilder sb = new StringBuilder();
        sb.append(testDao.getResultDao21())
                .append(testDao.getResultDao22());
        return sb.toString();
    }

    @DottingNode(expect = 30)
    public void loopService1() {
        for (int i = 0; i < 21; i++) {
            testDao.loopDao1();
        }
    }

    @DottingNode(expect = 5)
    public void asyncMethod0() {
        TestController.threadPool.submit(() -> {
            testDao.asyncMethodDao1();
        });
        testDao.asyncMethodDao0();
        for (int i = 0; i < 7; i++) {
            testDao.asyncMethodDao0();
        }
    }

    @DottingNode(expect = 5)
    public void asyncMethod1() {
        TestController.threadPool.submit(() -> testDao.asyncMethodDao5());
        for (int i = 0; i < 2; i++) {
            testDao.asyncMethodDao1();
        }
        for (int i = 0; i < 6; i++) {
            testDao.asyncMethodDao2();
        }
    }

    @DottingNode(expect = 2)
    public void asyncMethod2() {
        for (int i = 0; i < 12; i++) {
            testDao.asyncMethodDao3();
        }
        for (int i = 0; i < 21; i++) {
            testDao.asyncMethodDao4();
        }
    }
}
