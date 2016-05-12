package org.gaosheng.demo.read;

import java.util.Date;

public class Xls2Do {

	public static void main(String[] args) {
		String filename = "E:/教学中心对应.xlsx";
		String hxlsTable = "site_temp";
		String xlsxTable = "site_temp";
		System.out.println(new Date().toString());
		xls2Database(filename,hxlsTable,xlsxTable);
		System.out.println(new Date().toString());
		
		filename = "E:/专业对应.xlsx";
		xlsxTable = "major_temp";
		xls2Database(filename,hxlsTable,xlsxTable);
		
//		xls2Print(filename);
	}
	
	//excel->databse
	public static void xls2Database(String filename,String hxlsTable,String xlsxTable){

		String fileType = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
		try {
			if (fileType.equals("xls")) {
				HxlsBig xls2csv = new HxlsBig(filename, hxlsTable);
				xls2csv.process();
				xls2csv.close();
			} else if (fileType.equals("xlsx")) {
				XxlsBig howto = new XxlsBig(xlsxTable);
				howto.setCreate(true);
				// howto.processOneSheet("F:/new.xlsx",1);
				howto.process(filename);
				howto.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}	
	}
	
	//excel->print
	public static void xls2Print(String filename){
		String fileType = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
		try {
			if (fileType.equals("xls")) {
				HxlsPrint xls2csv;
				xls2csv = new HxlsPrint(filename);
				xls2csv.process();
			} else if (fileType.equals("xlsx")) {
				XxlsPrint howto = new XxlsPrint();
				howto.setTitleRow(0);
				howto.process(filename);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
}
