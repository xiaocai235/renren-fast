package io.renren.common.utils;

import java.util.ArrayList;
import java.util.Random;

public class RandomUtils {
    /**
     * 生成各个省份的随机数
     * @param total 总数
     * @param splitCount 个数
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public static ArrayList<Integer> splitRedPacket(int total, int splitCount, int min, int max) {
        ArrayList<Integer> al = new ArrayList<Integer>();
        Random random = new Random();

        if ((splitCount & 1) == 1) {

            int num = 0;
            do {
                num = random.nextInt(max);
            } while (num >= max || num <= min);

            total = total - num;
            al.add(num);
        }
        int couples = splitCount >> 1;
        int perCoupleSum = total / couples;

        for (int i = 0; i < couples; i++) {
            int num1 = 0;
            int num2 = 0;
            do {
                num1 = random.nextInt(max);
                num2 = perCoupleSum - num1;
                if (!al.contains(num1) && !al.contains(num2)) {
                    if (i == 0) {
                        num1 = (total - couples * perCoupleSum) + num1;
                    }
                }
            } while (num1 < min || num1 > max || num2 < min || num2 > max);
            al.add(num1);
            al.add(num2);
        }
        return al;
    }
}
