package com.kk.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZlStringUtil {
    private static final String SQL_STR = "select#insert#delete#alert#java#chr#master#mid#declare#truncate#count#where#script#onclick#onmouseover";

    /**
     * 特殊字符验证
     *
     * @param str 待验证的字符
     * @return boolean 有特殊字符返回true，否则false
     */
    public static boolean isSpecialChar(String... str) {
        boolean strFlag = false;

        for (String strboolean : str) {
            // 判断字符串是否属于敏感字符
            for (String sqlStr : SQL_STR.split("#")) {
                if (null != strboolean && (strboolean.toLowerCase()).contains(sqlStr)) {
                    strFlag = true;
                    break;
                }
            }
            // 如果属于敏感字符，则跳出循环
            if (strFlag) {
                break;
            }
            if (null != strboolean && !"".equals(strboolean)) {
                // 进行字符串特殊字符验证
                String regEx = "[~!#$%^&*<>|{}\\[\\]]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(strboolean);
                if (m.find()) {
                    strFlag = true;
                    break;
                }
            }
        }
        return strFlag;
    }

    /**
     * 生成UUID
     *
     * @return
     */
    public static String getUUID32() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
    }

    /**
     * <p>
     * 描述: 构建用于精确正则查询的字符串，很精确的，完全匹配哦
     * </p>
     *
     * @param regStr 用分号隔开的字符串
     * @return String 返回类型 "^shdj|上山是$"
     * @author: tangbin
     * @version: V1.0
     * @Date: 2018年7月3日 上午11:09:24
     */
    public static String preRegStr(String regStr) {
        if (regStr == null || regStr.length() <= 0) {
            return null;
        }
        String newRegStr = "";
        newRegStr = getString(regStr, newRegStr);
        return "^" + newRegStr + "$";
    }

    /**
     * <p>
     * 描述: 生成用于进行正则查询的字符串，用于查询字符串中是否包含某一些字符串，注意，这只能在进行部门之类的带有层级关系时进行使用
     * </p>
     *
     * @param regStr 待处理的字符串 使用分号隔开
     * @return String 返回类型 "asdasd|sdasdas"
     * @author: tangbin
     * @version: V1.0
     * @Date: 2018年7月4日 上午11:43:17
     */
    public static String preRegIncludeStr(String regStr) {
        if (regStr == null || regStr.length() <= 0) {
            return null;
        }
        String newRegStr = "";
        newRegStr = getString(regStr, newRegStr);
        return newRegStr;
    }

    /**
     * 处理字符串
     *
     * @param regStr    待正则字符串
     * @param newRegStr 处理后的字符串
     * @return 处理后的字符串
     */
    private static String getString(String regStr, String newRegStr) {
        if (regStr.indexOf(";") > 0) {
            String[] strList = regStr.split(";");
            StringBuilder newStr = new StringBuilder();
            for (String stritem : strList) {
                newStr.append(stritem).append("|");
            }
            // 删除最后一个符号
            newStr.deleteCharAt(newStr.length() - 1);
            newRegStr = newStr.toString();
        } else {
            newRegStr = regStr;
        }
        return newRegStr;
    }


    /**
     * <p>
     * 描述: 将得到的时间字符串，转换成给前台进行展示用的毫秒数
     * </p>
     *
     * @param dateStr qew
     * @return String 返回类型
     * @author: tangbin
     * @version: V1.0
     * @Date: 2018年7月3日 下午5:11:10
     */
    public static String dateStrToDate(String dateStr) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr));
            return String.valueOf(c.getTimeInMillis());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * @method 方法作用：取得下一节点CODE值
     *
     * @parm 传入参数据 :传入数据的节点(code),子节点最大值(maxCode,可以为空字符串,但不能为null)
     * 数据规范：002,002001,002001001
     *
     * @return 返回CODE
     */
    public static String getTreeNextCode(String code, String maxCode) {
        // 如果maxCode为空，说明该节点还没有子节点
        if (null == maxCode || "".equals(maxCode.trim())) {
            if (!"1".equals(code)) {
                code = code + "001";
            } else {
                code = "001";
            }
            // maxCode不为空则进行加1操作.
        } else {
            code = NextCode(maxCode);
        }
        return code;
    }

    /*
     * @method 对maxCode值的加1操作
     *
     * @parm maxCode
     */
    public static String NextCode(String maxCode) {
        String result;
        Long retval = 1L;
        // 把STRING转为INTEGER并进行加1操作
        retval = Long.parseLong(maxCode) + 1;
        result = retval.toString();
        // 把得到的CODE值进行规范化处理, 例如：002,002001,002001001
        for (int i = 0; i < 3; i++) {
            if (result.length() % 3 == 0) {
                break;
            }
            result = "0" + result;
        }
        return result;
    }

    /**
     * 说明：判断是字符串是否为空
     *
     * @param str 需要验证的参数
     * @return 是空则 true 不是在 false
     * @author tangbin
     * @date 2018/10/11
     * @time 14:09
     */
    public static Boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 说明：获取树编码的code
     *
     * @param pcode    父节点
     * @param codelist 子列表
     * @return 新的编号
     * @author tangbin
     * @date 2018/12/27
     * @time 14:28
     */
    public static String getTreeCode(String pcode, List<Map<String, Object>> codelist) {
        if (null != codelist.get(0)) {
            String code = (String) codelist.get(0).get("code");
            code = code.substring(code.length() - 3, code.length());
            int nextint = new Integer(code) + 1;
            String nextcode = nextint + "";
            String nextcodeture = "";
            switch (nextcode.length()) {
                case 1:
                    nextcodeture = "00" + nextcode;
                    break;
                case 2:
                    nextcodeture = "0" + nextcode;
                    break;
                case 3:
                    nextcodeture = nextcode;
                    break;
                default:
                    break;
            }
            return pcode != "1" ? pcode + nextcodeture : nextcodeture;
        } else {
            return pcode != "1" ? pcode + "001" : "001";
        }
    }

    /**
     * 说明：
     *
     * @param seqnolist 排序的列表
     * @return 新的序号
     * @author tangbin
     * @date 2018/12/27
     * @time 14:46
     */
    public static Integer getSeqno(List<Map<String, Object>> seqnolist) {
        if (null != seqnolist && seqnolist.size() > 0) {
            Integer seqno = (Integer) seqnolist.get(0).get("seqno");
            return seqno.intValue() + 1;
        } else {
            return new Integer("1");
        }
    }

    /**
     * 将汉字转换为全拼
     *
     * @date 2019年3月28日 16:29:32
     * @author xuhao
     */
    public static String getPinYin(String src) {

        char[] hz = null;
        hz = src.toCharArray();//该方法的作用是返回一个字符数组，该字符数组中存放了当前字符串中的所有字符
        String[] py = new String[hz.length];//该数组用来存储
        //设置汉子拼音输出的格式
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        StringBuilder pys = new StringBuilder(); //存放拼音字符串
        int len = hz.length;

        try {
            for (char c : hz) {
                //先判断是否为汉字字符
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    //将汉字的几种全拼都存到py数组中
                    py = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    //取出改汉字全拼的第一种读音，并存放到字符串pys后
                    pys.append(py[0]);
                } else {
                    //如果不是汉字字符，间接取出字符并连接到 pys 后
                    pys.append(Character.toString(c));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return pys.toString();
    }

	/**
	 * 获取编码格式
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
        String encode;
        encode = "UTF-16";
        try {
            if (str.equals(new String(str.getBytes(), encode))){
                return encode;
            }
        } catch (Exception ignored) { }
        encode = "ASCII";
        try {
            if (str.equals(new String(str.getBytes(), encode))){
                return "字符串<< " + str + " >>中仅由数字和英文字母组成，无法识别其编码格式";
            }
        } catch (Exception ignored) {}
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(), encode))){
                return encode;
            }
        } catch (Exception ignored) { }
        encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(), encode))){
                return encode;
            }
        } catch (Exception ignored) { }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(), encode))){
                return encode;
            }
        } catch (Exception ignored) { }
        return "未识别编码格式";
    }

    /***
     * 去除字符串前后，指定的字符
     * 示例 11ABC-D8811
     * 返回 ABC-D88
     * @param str 字符串
     * @param beTrim 要去除的
     */
    public static String trimStringWith(String str, char beTrim) {
        int st = 0;
        int len = str.length();
        char[] val = str.toCharArray();
        while ((st < len) && (val[st] <= beTrim)) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= beTrim)) {
            len--;
        }
        return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str;
    }
}
