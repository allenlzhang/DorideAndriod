
package com.carlt.sesame.utility;

/**
 * 验证输入信息合法性的一些方法
 * 
 * @author daisy
 */
public class CheckInfo {

    public final static int LENGTH_MAX_NAME = 16;

    public final static int LENGTH_MIN_NAME = 1;

    public final static int LENGTH_MAX_PASSWORD = 16;

    public final static int LENGTH_MIN_PASSWORD = 6;

    public final static String CORRECT_PSWLENTH = "correct_pswlenth";
    
//	public final static String[] CITY_FILTERS = { 
//			"自治州", "地区", "布依族苗族自治", "苗族侗族", "布依族苗族", "市增补", "哈尼族彝族", "朝鲜族", "藏族","蒙古族", "哈尼族彝族", "壮族苗族", 
//			"傣族", "白族", "傣族景颇族", "傈僳族", "开发区", "回族", "蒙古", "哈萨克", "柯尔克孜自治", "土家族", "苗族","市，省直系统"
//	};

    /**
     * 校验输入的用户名是否合法
     * 
     * @param s：输入的字符串
     * @return erroInfo：错误信息（如果正确返回""）
     */
    public static String checkName(String s) {
        String erroInfo;
        if (s == null || s.length() == 0) {
            erroInfo = "您还没有填写姓名哦";

        } else {
            char[] mChar = s.toCharArray();
            int length = mChar.length;
            int countC = 0;// 中文字符数
            int countE = 0;// 英文字符数
            int countSum;// 总字符数
            for (int i = 0; i < length; i++) {
                if ((char)(byte)mChar[i] != mChar[i]) {
                    // 中文字符
                    countC++;
                } else {
                    // 英文字符
                    countE++;
                }
            }
            countSum = countC + countC + countE;
            if (countSum <= LENGTH_MAX_NAME && countSum >= LENGTH_MIN_NAME) {
                erroInfo = "";
            } else {
                erroInfo = "您的姓名输入过长...";
            }
        }

        return erroInfo;
    }

    /**
     * 验证密码位数
     * 
     * @param s
     * @return error 错误提示
     */
    public static String checkPassword(String s) {
        String error = "";
        if (s == null) {
            error = "你还没有填写您的登录密码...";

        } else {
            if (s.length() < LENGTH_MIN_PASSWORD) {
                error = "密码长度不可小于6位";
            } else if (s.length() > LENGTH_MAX_PASSWORD) {
                error = "密码长度不可大于16位";
            } else {
                error = CORRECT_PSWLENTH;
            }
        }
        return error;
    }

    /**
     * 验证身份证号
     * 
     * @param s
     * @return
     */
    public static boolean checkIdCard(String s) {
        if (s == null) {
            return false;
        } else {
            // String strPattern = "\\d{15}|\\d{18}";
            // Pattern p = Pattern.compile(strPattern);
            // Matcher m = p.matcher(s);
            // return m.matches();

            if (s.length() == 15 || s.length() == 18) {
                return true;
            } else {
                return false;
            }

        }
    }
    
    
    
}
