package dotting.timer.core.utils;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Random;

/**
 * @author sunqinwen
 * @version \: PushUtils.java,v 0.1 2018-09-18 19:26
 */
public class PushUtils {

    private static final Random random = new Random();

    private static final Map<String, Long> urlMap = Maps.newConcurrentMap();

    public static boolean sampled(String url) {
        Long start = urlMap.get(url);
        Long end = System.currentTimeMillis();
        if (start == null) {
            urlMap.put(url, end);
            return true;
        }
        if ((end - start) >= 60000) {
            urlMap.put(url, end);
            return true;
        } else {
            if (random.nextInt(999) == 0) {
                urlMap.put(url, end);
                return true;
            }
        }
        return false;
    }
}
