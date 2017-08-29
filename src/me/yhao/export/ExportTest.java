package me.yhao.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

public class ExportTest {

	@Test
	public void testExport() {
		export();	
	}
	
	/**
	 * 导入excel
	 */
	public void export(){
		//声明一个工作薄
		XSSFWorkbook wb = new XSSFWorkbook();
		//声明一个单子并命名
		XSSFSheet sheet = wb.createSheet("学生表");
		//给单子名称一个长度
		sheet.setDefaultColumnWidth(15);
		//生成样式
		XSSFCellStyle style = wb.createCellStyle();
		//创建表头
		XSSFRow row = sheet.createRow(0);
		//样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		//给表头第一行一次创建单元格
		XSSFCell cell = row.createCell(0);
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
	
	@Test
	public void testReadExcel(){
		try {
			File file = new File("F://test.xls");
			String[][] date = readExcel(file, 1);
			for (int i = 0; i < date.length; i++) {
				for (int j = 0; j < date[i].length; j++) {
					System.out.print(date[i][j] + "、");
				}
				System.out.println();
			}
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Excel导入
	 * @param file
	 * @param ignoreRows
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException 
	 * @throws EncryptedDocumentException 
	 */
	public String[][] readExcel(File file, int ignoreRows) 
			throws IOException, EncryptedDocumentException, InvalidFormatException {
		String[][] result = null;
		int rowSize = 0;
		FileSystemResource in = new FileSystemResource(file);
		InputStream inputstream = in.getInputStream();
		//打开Workbook
		Workbook wb = WorkbookFactory.create(inputstream);
		Cell cell = null;
		Sheet sheet = wb.getSheetAt(0);
		result = new String[sheet.getLastRowNum()-ignoreRows+1][];
		
		//ignorerows行 不取
		for (int rowIndex = ignoreRows; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			int tempRowSize = row.getLastCellNum();
			if (tempRowSize > rowSize) {
				rowSize = tempRowSize;
			}
			String[] values = new String[rowSize];
			boolean isMerge = false; 
			Arrays.fill(values, "");
			for (int cellIndex = 0; cellIndex < rowSize; cellIndex++) {
				cell = row.getCell(cellIndex);
				cell.setCellType(CellType.STRING);
				isMerge = this.isMergedRegion(sheet, rowIndex, cell.getColumnIndex());
				if (isMerge) {
					values[cellIndex] = this.getMergedRegionValue(sheet, rowIndex, cellIndex);
				} else {
					values[cellIndex] = cell.getStringCellValue();
				}
			}
			result[rowIndex-ignoreRows] = values;
		}
		
		wb.close();
		return result;
	}
	
	/**
	 * 判断是否是合并单元格
	 * @param sheet
	 * @param rowIdx 行下标
	 * @param colIdx 列下标
	 * @return
	 */
	private boolean isMergedRegion(Sheet sheet,int rowIdx ,int colIdx) {  
		int sheetMergeCount = sheet.getNumMergedRegions();  
		for (int i = 0; i < sheetMergeCount; i++) {  
			CellRangeAddress range = sheet.getMergedRegion(i);  
			int firstColumn = range.getFirstColumn();  
			int lastColumn = range.getLastColumn();  
			int firstRow = range.getFirstRow();  
			int lastRow = range.getLastRow();  
			if(rowIdx >= firstRow && rowIdx <= lastRow){  
				if(colIdx >= firstColumn && colIdx <= lastColumn){  
					return true;  
				}  
			}  
		}  
		return false;  
	}  
	
	/**
	 * 获取合并单元格的值
	 * @param sheet
	 * @param rowIdx
	 * @param colIdx
	 * @return
	 */
	public String getMergedRegionValue(Sheet sheet ,int rowIdx , int colIdx){    
	    int sheetMergeCount = sheet.getNumMergedRegions();    
	        
	    for(int i = 0 ; i < sheetMergeCount ; i++){    
	        CellRangeAddress ca = sheet.getMergedRegion(i);    
	        int firstColumn = ca.getFirstColumn();    
	        int lastColumn = ca.getLastColumn();    
	        int firstRow = ca.getFirstRow();    
	        int lastRow = ca.getLastRow();    
	            
	        if(rowIdx >= firstRow && rowIdx <= lastRow){    
	            if(colIdx >= firstColumn && colIdx <= lastColumn){    
	                Row fRow = sheet.getRow(firstRow);    
	                Cell fCell = fRow.getCell(firstColumn);    
	                return getCellValue(fCell) ;    
	            }    
	        }    
	    }    
	    return null ;    
	}  
	
	/**   
	* 获取单元格的值   
	* @param cell   
	* @return   
	*/    
	public String getCellValue(Cell cell){    
	    if(cell == null) return "";    
	    if(cell.getCellTypeEnum() == CellType.STRING){    
	        return cell.getStringCellValue();    
	    }else if(cell.getCellTypeEnum() == CellType.BOOLEAN){    
	        return String.valueOf(cell.getBooleanCellValue());    
	    }else if(cell.getCellTypeEnum() == CellType.FORMULA){    
	        return cell.getCellFormula() ;    
	    }else if(cell.getCellTypeEnum() == CellType.NUMERIC){    
	        return String.valueOf(cell.getNumericCellValue());    
	    }    
	    return "";    
	}   

}
