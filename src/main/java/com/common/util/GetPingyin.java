package com.common.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/3
 * @description
 */
public class GetPingyin {
    public static String getPingYin(String src) throws BadHanyuPinyinOutputFormatCombination {
        char[] t1 = null;

        t1 = src.toCharArray();

        String[] t2 = new String[t1.length];

        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);

        String t4 = "";

        int t0 = t1.length;

        for(int i = 0; i < t0; i++) {

            if(java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {

                t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);

                t4 += t2[0];

            } else {

                t4 += java.lang.Character.toString(t1[i]);
            }
        }

        return t4;
    }

    public static String getPinYinHeadChar(String str) {

        String convert = "";

        for(int j = 0; j < str.length(); j++) {

            char word = str.charAt(j);

            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);

            if(pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }

        return convert.toUpperCase();
    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {
//        System.out.println(getPinYinHeadChar("汉字"));
        System.out.println("获取汉字拼音：  " + getPingYin("获取汉字拼音"));
    }
}
