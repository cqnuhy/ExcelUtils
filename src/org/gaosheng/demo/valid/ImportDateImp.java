package org.gaosheng.demo.valid;

import java.io.File;

import org.gaosheng.util.exception.EntityException;
import org.gaosheng.util.xls.valid.ImportData;


public class ImportDateImp extends ImportData {

	public static void main(String[] args) throws SecurityException, NoSuchMethodException, EntityException {
		File importFile = new File("F:/08秋复转毕业成绩全/郑州08复转军人课程成绩总表5.xls");
		File errorFile = new File("F:/error.xls");
		ImportDateImp importDateImp = new ImportDateImp();
		importDateImp.setImportExcel(importFile);
		importDateImp.setErrorExcel(errorFile);
		importDateImp.setStartRow(1);
		importDateImp.execute();
		importDateImp.getErrorExcel();
		for (String error : importDateImp.getErrors()) {
			System.out.println(error);
		}
	}

	public String validColumn_1(){
		if (!this.getCurCell().getContents().equals("陈贻亭")) {
			return "姓名错误";
		}
		return null;
	}
	
	public String validColumn_2(){
		if (!this.getCurCell().getContents().equals("陈贻亭")) {
			return "姓名错误";
		}
		return null;
	}

	public String validColumn_3(){
	
		return null;
	}

	public boolean save() {
		
		return false;
	}
}
