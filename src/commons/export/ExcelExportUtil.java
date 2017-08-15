package commons.export;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 报表数据导出
 * 
 * @author hoye
 *
 */
public class ExcelExportUtil {

	private static final Map<String, ExcelExportHandler> excels = new HashMap<String, ExcelExportHandler>();

	/**
	 * 获取功能所信息数据
	 * 
	 * @param modelFile
	 * @return
	 */
	private static synchronized ExcelExportHandler getExcelExportHandler(
			String modelFile) {
		if (excels.get(modelFile) != null) {
			return excels.get(modelFile);
		} else {
			ExcelExportHandler excelExportHandler = new ExcelExportHandler(
					modelFile);
			excels.put(modelFile, excelExportHandler);
			return excelExportHandler;
		}
	}

	/**
	 * 导出表格
	 * 
	 * @param modelPath
	 *            模板名称
	 * @param response
	 *            返回
	 * @param fileName
	 *            生成文件名称
	 * @param dataList
	 *            数据列表
	 * @throws IOException
	 *             异常信息
	 */
	public static void export(HttpServletRequest request,
			HttpServletResponse response, String modelPath, String fileName,
			List dataList, Map title, Map foot) throws IOException {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {// IE浏览器以及以IE为内核的浏览器
			fileName = URLEncoder.encode(fileName, "UTF-8");
		} else {// 其他浏览器
			fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
		}
		// String root = request.getContextPath();
		String root = request.getSession().getServletContext().getRealPath("/");
		// String tplPath = "classpath:exceltempl/" + modelPath;
		String tplPath = root + "/WEB-INF/exceltempl/" + modelPath;
		// ExcelExportHandler excelExportHandler =
		// getExcelExportHandler(tplPath);
		ExcelExportHandler excelExportHandler = new ExcelExportHandler(tplPath);
		excelExportHandler.process(dataList, title, foot).export(response,
				fileName);
	}
}
