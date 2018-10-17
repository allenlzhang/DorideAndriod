package com.carlt.chelepie.utils;

import java.io.File;
import java.util.Comparator;

/**
 * 自定义排序规则类
 * @author Daisy
 *
 */
public class MyComparator {

	/**
	 * 按文件创建顺序排序
	 * @author Daisy
	 *
	 */
	public static class  CreatTimeComparator implements Comparator<Object> {

		@Override
		public int compare(Object object1, Object object2) {
			File mInfo1=(File) object1;
			File mInfo2=(File) object2;
			long createTime1=mInfo1.lastModified();
			long createTime2=mInfo2.lastModified();
			int i = (int) (createTime2 - createTime1);
			return i;
		}
		
	}
}
