package com.damo.generator.service;

import java.util.*;

public class RandomService {

    private static Random random = new Random();
    private static String[] charsetAll = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9"};
    private static String[] charsetNum = {"0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9"};
    private static String[] charsetChar = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j",
            "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    /**
     * 取n个随机数
     *
     * @param n    in<字符数组长度
     * @param type 类型，1混合，2纯数字，3纯字母
     * @return String
     */
    public static String getRanDomStr(final int n, final int type) {
        String[] charset;

        switch (type) {
            case 1:
                charset = charsetNum;
                break;
            case 2:
                charset = charsetChar;
                break;
            case 3:
                charset = charsetAll;
                break;
            default:
                charset = charsetAll;
        }

        String[] nonces = new String[n];
        //选择数的范围
        List<String> choose = new ArrayList<>(charset.length);
        //取随机数
        Collections.addAll(choose, charset);
        int nl = n;
        while (nl-- > 0) {
            int k = random.nextInt(choose.size());
            nonces[nl] = choose.get(k);
            choose.remove(k);
            if(choose.size() == 0){
                Collections.addAll(choose, charset);
            }
        }
        //将数组转换为str
        String nonce = "";
        for (String str : nonces) {
            nonce += str;
        }
        return nonce;
    }

    public static void main(String[] args) {
        System.out.println(getRanDomStr(32, 2));

        //测试重复率。
        if(true) {
            long start = System.currentTimeMillis();
            int repeat = 0;
            int totalx = 20;
            int totaly = 10000;
            Map<String, String> map = new HashMap<>();
            for (int j = 0; j < totalx; j++) {
                for (int i = 0; i < totaly; i++) {
                    String code = getRanDomStr(15, 3);
                    System.out.println(i + ":" + code);
                    if (map.containsKey(code)) {
                        repeat++;
                        continue;
                    }
                    map.put(code, code);
                    //System.out.println(code);
                }
            }
            System.out.println("生成兑换码" + (totalx * totaly) + "个，重复" + repeat + "个，重复率" + (repeat * 1.0D / (totalx * totaly)) + "耗时： " + (System.currentTimeMillis() - start) + "ms");
        }
    }
}
