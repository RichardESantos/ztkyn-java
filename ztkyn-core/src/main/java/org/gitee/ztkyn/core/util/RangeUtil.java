package org.gitee.ztkyn.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * range 生成工具
 */
public class RangeUtil {

    private static final Logger logger = LoggerFactory.getLogger(RangeUtil.class);

    public static int[] rangeInt(int from, int to) {
        if (to <= from) {
            logger.error("to is less than from, which is {} < {}", from, to);
            throw new RuntimeException("to is less than from");
        }
        return rangeInt(from, to, 1);
    }

    public static int[] rangeInt(int from, int to, int step) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = from; i < to; i = i + step) {
            integerList.add(i);
        }
        int size = integerList.size();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = integerList.get(i);
        }
        return result;
    }

}
