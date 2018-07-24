/**
 * Copyright (C) 2009-2010 Yichuan, Fuchun All rights reserved.
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * @(#) IdcardUtils.java Date: 2010-06-17
 */
package com.carlt.sesame.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 产品ID验证工具
 * 
 * @author June
 * @version 1.0, 2010-06-17
 */
public class DeviceIdUtils {

    /** ID长度 */
    public static final int DEVICEID_LENGTH = 16;


    /** 每位加权因子 */
    public static final int power[] = {
    	37, 11, 3, 13, 2, 19, 17, 27, 1, 31, 7, 6, 19, 8, 1
    };
    
    public static Map<String, String> Types = new HashMap<String, String>();

    static {
    	Types.put("00", "One");
    }
    
    public static void main(String[] args) {
    	
    	for(int i = 0;i < 20 ;i++)
    	{
    		String id = getDeviceId(i);
    		String code = getIdValidateCode(id);
    		System.out.println(id + code.toUpperCase());
    	}
    	
    	
    	/*
    	Map<String, Integer> map = new HashMap<String, Integer>(); 
    	for(int i = 0;i < 99999;i++)
    	{
    		//String id = getRandomString(16);
    		
    		String id = getDeviceId(i);
    		String code = getIdValidateCode(id);
        	//System.out.println(id + " " +code);
        	
        	//统计频数
        	Integer count = map.get(code); 
        	map.put(code, (count == null) ? 1 : count + 1); 
    	}
    	
    	int count = 0;
    	for (Map.Entry<String, Integer> entry : map.entrySet()) { 
            System.out.println("Key : " + entry.getKey() + " Value : "
                    + entry.getValue()); 
            count ++ ;
        }
    	System.out.println(count + " in  total.");
    	*/
    }
    
    
    /**
     * 获取指定ID的验证码
     * @param id
     * @return
     */
    public static String getIdValidateCode(String idCard)
    {
    	String code15 = "";
    	if (idCard.length() == DEVICEID_LENGTH)
    	{
            code15 = idCard.substring(0, DEVICEID_LENGTH-1);
    	}else if (idCard.length() == (DEVICEID_LENGTH-1))
    	{
            code15 = idCard;
    	}
    	char[] cArr = code15.toCharArray();
        if ((cArr != null) && (cArr.length == (DEVICEID_LENGTH-1))) {
            int[] iCard = converCharToInt(cArr);
            int iSum15 = getPowerSum(iCard);
            return getCheckCode16(iSum15);
        }
        
    	return null;
    }
    /**
     * 验证设备ID是否合法
     * 
     * @param idCard 身份编码
     * @return 是否合法
     */
    public static boolean validateDeviceId(String idCard) {
    	String code = getIdValidateCode(idCard);
    	if(null != code)
    	{
    		return code.equalsIgnoreCase(idCard.substring(DEVICEID_LENGTH-1,DEVICEID_LENGTH));
    	}
        return false;
    }

    /**
     * 将字符数组转换成数字数组
     * 
     * @param ca
     *            字符数组
     * @return 数字数组
     */
    public static int[] converCharToInt(char[] ca) {
        int len = ca.length;
        int[] iArr = new int[len];
        try {
            for (int i = 0; i < len; i++) {
                iArr[i] = (int)Long.parseLong(String.valueOf(ca[i]), 36);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return iArr;
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和
     * 
     * @param iArr
     * @return 身份证编码
     */
    public static int getPowerSum(int[] iArr) {
        int iSum = 0;
        if (power.length == iArr.length) {
            for (int i = 0; i < iArr.length; i++) {
                for (int j = 0; j < power.length; j++) {
                    if (i == j) {
                        iSum = iSum + iArr[i] * power[j];
                    }
                }
            }
        }
        return iSum;
    }

    /**
     * 将power和�?�?1取模获得余数进行校验码判�?
     * 
     * @param iSum
     * @return 校验
     */
    public static String getCheckCode16(int iSum) {
        return Integer.toString(iSum % 36, 36);
    }

    /**
     * 根据ID获取产品类型
     * 
     * @param idCard
     *            身份编号
     * @return 年龄
     */
    public static String getDeviceTypeById(String id) {
        String type = "";
        if (id.length() == DEVICEID_LENGTH) {
        	String code = id.substring(2, 4);
        	 type = Types.get(code);
        }
        return type;
    }
    
    /** 
     * 产生随机的字符串 
     *   
     * @param 字符串长度
     * @return 
     */ 
    public static String getRandomString(int length) {   
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";   
        Random random = new Random();   
        StringBuffer sb = new StringBuffer();   
        for (int i = 0; i < length; i++) {   
            int number = random.nextInt(base.length());   
            sb.append(base.charAt(number));   
        }   
        return sb.toString();   
    }
    
    public static String getDeviceId(int t)
    {
    	return "LW0011D37"+String.format("%05d", t)+"0";
    }
}
