package util;

import com.common.util.AESUtil;
import com.common.util.GetPingyin;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/3
 * @description
 */
public class UtilTest {

    @Test
    public void pingYingTest() throws BadHanyuPinyinOutputFormatCombination {

        GetPingyin pingyin = new GetPingyin();

        System.out.println("获取汉字拼音：  " + pingyin.getPingYin("获取汉字拼音"));

    }

    @Test
    public void AESTest() {

        AESUtil aesUtil = new AESUtil();

        String content = "this is message";

        String serContent = aesUtil.encrypt(content);

        System.out.println(serContent);

        System.out.println(aesUtil.decrypt(serContent));


    }
}
