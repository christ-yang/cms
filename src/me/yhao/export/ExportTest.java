package me.yhao.export;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExportTest {

	public static void main(String[] args) {
		export();
	}
	
	
	public static void export(){
		//声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		//声明一个单子并命名
		HSSFSheet sheet = wb.createSheet("学生表");
		//给单子名称一个长度
		sheet.setDefaultColumnWidth(15);
		//生成样式
		HSSFCellStyle style = wb.createCellStyle();
		//创建表头
		HSSFRow row = sheet.createRow(0);
		//样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);;
		//给表头第一行一次创建单元格
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("学生编号");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("学生姓名");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("学生性别");
		cell.setCellStyle(style);
		
		//添加数据
		List<Map<String, String>> list = new ArrayList<>();
		Map<String, String> map = new HashMap<>();
		map.put("stuId", "1653");
		map.put("stuName", "张三");
		map.put("stuSex", "男");
		list.add(map);
		map = new HashMap<>();
		map.put("stuId", "163");
		map.put("stuName", "小红");
		map.put("stuSex", "女");
		list.add(map);
		
		//往单元格中填充数据
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(list.get(i).get("stuId"));
			row.createCell(1).setCellValue(list.get(i).get("stuName"));
			row.createCell(2).setCellValue(list.get(i).get("stuSex"));
		}
		
		try {
			FileOutputStream fos = new FileOutputStream("E://学生表.xls");
			wb.write(fos);
			fos.close();
			wb.close();
			JOptionPane.showMessageDialog(null, "导出成功！");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "导出失败！");
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "导出失败！");
			e.printStackTrace();
		}
	}
	
	
	public String[][] getDate(File file, int ignoreRows) throws IOException {
		List<String> result = new ArrayList<>();
		int rowSize = 0;
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		//打开HSSFWorkbook
		POIFSFileSystem fs = new POIFSFileSystem();
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFCell cell = null;
		for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
			HSSFSheet st = wb.getSheetAt(sheetIndex);
			//ignorerows行 不取
			for (int rowIndex = ignoreRows; rowIndex < st.getLastRowNum(); rowIndex++) {
				HSSFRow row = st.getRow(rowIndex);
				if (row == null) {
					continue;
				}
				int tempRowSize = row.getLastCellNum()+1;
				if (tempRowSize > rowSize) {
					rowSize = tempRowSize;
				}
				String[] values = new String[rowSize];
				Arrays.fill(values, "");
				boolean hasvalue = false;
				
			}
		}
		return null;
	}

}
