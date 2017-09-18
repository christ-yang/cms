package cn.ac.yhao.export;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;

public class ExportExcel {

	@Test
	public void testExcel(){
		String modelPath = "D:/git/cms/src/commons/export/djysgl-014-028.xls";
		FileSystemResource fsr = new FileSystemResource(modelPath);
		try {
			InputStream is = fsr.getInputStream();
			Workbook wb = WorkbookFactory.create(is);
			Sheet sheet = wb.getSheetAt(0);
			deleteColumn(sheet, 'D', 'E');
			
			FileOutputStream fos = new FileOutputStream("E://学生表.xls");
			wb.write(fos);
			fos.close();
			wb.close();
			JOptionPane.showMessageDialog(null, "导出成功！");
			
			
			
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 整列删除
	 * @param sheet
	 * @param delStartColumn  删除开始列
	 * @param delEndColumn	删除结束列
	 */
	private void deleteColumn(Sheet sheet, int delStartColumn, int delEndColumn){
		int maxColumn = 0;
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			
			//row不存在，跳过
			if (row == null) {
				continue;
			}
			
			int lastColumn = row.getLastCellNum();
			if (lastColumn > maxColumn) {
				maxColumn = lastColumn;
			}
			
			if (lastColumn < delStartColumn || lastColumn < delEndColumn) {
				continue;
			}
			
			int cur = 0;
			for (int j = delEndColumn+1; j < lastColumn; j++) {
				cur = j-1-(delEndColumn-delStartColumn);
				Cell oldCell = row.getCell(cur);
				if (oldCell != null) {
					row.removeCell(oldCell);
				}
				
			}
			
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
