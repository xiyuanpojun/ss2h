package com.hill.gwyb.service.impl;

import com.hill.gwyb.api.BigExcelReader;
import com.hill.gwyb.api.Excel2003Reader;

import java.util.*;

public class ReadExcelGw {
	private static String trimStr(String str) {
		String rtStr = "";
		rtStr = (null != str) ? str.trim() : "";
		return rtStr;
	}

	public static String main(String[] args) {
		String fileName = args[0];// test
		String zxName = args[1];
		try {
			if (fileName.contains("xlsx")) {
				BigExcelReader reader = new BigExcelReader(fileName) {
					int i = 0;
					List<List> dataList = new ArrayList<List>();

					@Override
					protected void outputRow(String[] datas, int[] rowTypes, int rowIndex) {
						// 此处输出每一行的数据，第一行表头作为题目，存入题目表
						if (datas.length > 0 && null != datas[0] && !"".equals(datas[0].trim())) {
							i++;
							if(i>1){
								dataList.add(Arrays.asList(datas));
								if (i > 50000) {
									doList(dataList,zxName);
									dataList = new ArrayList<List>();
									i = 1;
								}
							}
						}
					}

					@Override
					protected void parseEnd() {
						if (dataList.size() > 0) {
							doList(dataList,zxName);
						}
					}
				};
				// 执行解析
				reader.parse();
			}else {
				Excel2003Reader excel03 = new Excel2003Reader() {
					int i = 0;
					List<List> dataList = new ArrayList<List>();

					@Override
					protected void outputRow(int sheetIndex, int curRow, List<String> rowlist) {
						int len = rowlist.size();
						if (len > 0 && null != rowlist.get(0) && !"".equals(rowlist.get(0).trim())) {
							i++;
							if(i>1){
								dataList.add(new ArrayList(rowlist));
								if (i > 50000) {
									doList(dataList,zxName);
									dataList = new ArrayList<List>();
									i = 1;
								}
							}
						}
					}

					@Override
					protected void parseEnd() {
						if (dataList.size() > 0) {
							doList(dataList,zxName);
						}
					}
				};
				excel03.process(fileName);
			}
			return "true";
		} catch (Exception ex) {
			ex.printStackTrace();
			return ex.getMessage();
		}
	}

	private static void doList(List<List> li,String zxName) {
		Vector<Thread> threads = new Vector<Thread>();// 线程集合
		int size = li.size();
		int defaultLimit = 10000;
		if (defaultLimit < size) {
			int part = size / defaultLimit;
			for (int i = 0; i < part; i++) {
				// 拆分数据集，分线程执行
				List<List> list = li.subList(defaultLimit * i, defaultLimit * (i + 1));
				ThreadInsertGw ti = new ThreadInsertGw(list,zxName);
				Thread td = new Thread(ti);
				threads.add(td);
				td.start();
			}
			if (size > part * defaultLimit) {
				List<List> list = li.subList(defaultLimit * part, size);
				ThreadInsertGw ti = new ThreadInsertGw(list,zxName);
				Thread td = new Thread(ti);
				threads.add(td);
				td.start();
			}
		} else {
			ThreadInsertGw ti = new ThreadInsertGw(li,zxName);
			Thread td = new Thread(ti);
			threads.add(td);
			td.start();
		}
		System.out.println("开始:"+new Date());
		for (Thread ithd : threads) {
			try {
				ithd.join();// 等待线程执行结束
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("结束:"+new Date());
	}
}
