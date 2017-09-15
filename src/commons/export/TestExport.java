package commons.export;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestExport {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void test(){
		String modelPath = "D:/git/cms/src/commons/export/djysgl-014-028.xls";
		Map title = new HashMap<>();
		title.put("title", "测试");
		Map params = new HashMap<>();
		params.put("ORG_JGJB", "3");
		params.put("DEP_TXJB", "3");
		ExcelExportHandler excelExportHandler = new ExcelExportHandler(modelPath);
		try {
			OutputStream os = new FileOutputStream(new File("E://测试.xls"));
			excelExportHandler.process(null, title, null, params).export(os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
		
	

}
