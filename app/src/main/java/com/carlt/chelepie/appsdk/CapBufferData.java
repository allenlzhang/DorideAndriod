package com.carlt.chelepie.appsdk;

import java.util.LinkedList;
import java.util.List;

public class CapBufferData {

	public static final int MAX = 16;
	
	private int index = 0;
	
	
	private List<byte[]> list;
	
	public CapBufferData() {
		list = new LinkedList<byte[]>();
	}
	
	public synchronized void add(byte[] b){
		if(index >= MAX){
			index = 0;
		}
		if(list.size() > index){
			list.set(index, b);
		}else{
			list.add(index, b);
		}
		index ++ ;
	}
	
	public synchronized List<byte[]> getList(){
//		System.err.println(list.toString());
		if(list.size() < MAX){
			return list;
		}else{
			List<byte[]> list2 = new LinkedList<byte[]>();
			
			int j = 0;
			for (int i = 0; i < list.size(); i++) {
				if(index + i < MAX){
					list2.add(i,list.get(index + i));
				}else{
					list2.add(i,list.get(j));
					j++;
				}
			}
			
			return list2;
		}
	}
}
