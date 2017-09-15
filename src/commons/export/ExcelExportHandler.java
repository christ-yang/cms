package commons.export;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import commons.util.StringUtil;



/**
 * Excel导出处理器 1.用于读取表格导出的模板
 * 
 * 2.按照模板进行生成报表
 * 
 * @author hoye
 *
 */
public class ExcelExportHandler {
	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 表格导出参数
	 */
	private ExcelExportConfig excelExportConfig;
	private volatile boolean hasInit = false;

	/**
	 * @param tplFile
	 */
	public ExcelExportHandler(String modelFile) {
		this.excelExportConfig = new ExcelExportConfig(modelFile);
	}

	public <T> ExcelExportHandler process(T data, Map title, Map foot, Map params) {
		if (hasInit) {
			logger.warn("重复处理导出数据，直接忽略");
			return this;
		}
		if (null == data) {
			logger.warn("处理导出数据为null，直接忽略");
//			return this;
		}
		hasInit = true;
		final HSSFWorkbook wb = excelExportConfig.getWorkbook();
		for (ExcelExportConfig.SheetExportConfig config : excelExportConfig
				.getSheetExportConfigs()) {
			final HSSFSheet sheet = wb.getSheetAt(config.getSheetIdx());
			
			
			final String varName = config.getVarName();
			Object listObj = data;
			if (!"this".equalsIgnoreCase(varName)) {
				try {
					listObj = PropertyUtils.getProperty(data, varName);
				} catch (Exception e) {
					logger.warn("读取导出数据错误", e);
					return this;
				}
			}
			Iterator<Object> iterator = null;
			if (listObj instanceof List) {
				iterator = ((List) listObj).iterator();
			} else {
				iterator = Arrays.asList(listObj).iterator();
			}
			final int rowIndex = config.getFormatRowIdx();
			final int colStartIdx = config.getFormatColStartIdx();
			final CellStyle rowStyle = config.getDataRowStyle();
			final CellStyle[] cellStyles = config.getDataCellStyles();
			final String[] varExps = config.getColVarExps();
			Object row = null;
			for (int i = 0; iterator.hasNext(); i++) {
				row = iterator.next();
				HSSFRow newRow = sheet.getRow(rowIndex + i);
				if (null == newRow) {
					newRow = sheet.createRow(rowIndex + i);
				}
				if (null != rowStyle) {
					newRow.setRowStyle(rowStyle);
				}
				for (int j = 0; j < cellStyles.length; j++) {
					final HSSFCell cell = newRow.createCell(colStartIdx + j);
					if (null != cellStyles[j]) {
						cell.setCellStyle(cellStyles[j]);
					}
					Object value = null;
					if (null != varExps[j]) {
						try {
							value = PropertyUtils.getProperty(row, varExps[j]);
						} catch (Exception e) {
							logger.warn(String.format("读取属性{%s}错误", ""
									+ varExps[j]));
						}
					}
					if (null == value) {
						continue;
					}
					if (value instanceof Date) {
						cell.setCellValue((Date) value);
					} else if (value instanceof Calendar) {
						cell.setCellValue((Calendar) value);
					} else if (value instanceof Boolean) {
						cell.setCellValue((Boolean) value);
					} else if (value instanceof BigDecimal) {
						cell.setCellValue(((BigDecimal) value).doubleValue());
					} else {
						cell.setCellValue(value.toString());
					}

				}
			}

			for (int j = 0; j < cellStyles.length; j++) {
				try {
					sheet.autoSizeColumn(colStartIdx + j, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// 处理单元格合并信息数据
			Set<Integer> mergedRegions = config.getMergedRegions();
			Iterator<Integer> mrIterator = mergedRegions.iterator();
			while (mrIterator.hasNext()) {
				int index = mrIterator.next();
				handleSpan(sheet, index, config);
			}

			// TODO 表头内容填充
			if (null != title) {
				final int titlerowIndex = config.getTitleRowStartIdx();
				final int titlecolStartIdx = config.getTitleColStartIdx();
				HSSFCellStyle[][] titleCellStyles = config.getTitleCellStyles();
				String[][] titleVarExps = config.getTitleVarExps();
				for (int i = 0; i < titleVarExps.length; i++) {
					String[] varExp = titleVarExps[i];
					HSSFRow newRow = sheet.getRow(titlerowIndex + i);
					for (int j = 0; j < varExp.length; j++) {
						final HSSFCell cell = newRow
								.createCell(titlecolStartIdx + j);
						String key = varExp[j];
						if (StringUtil.isEmpty(key)) {
							continue;
						}
						Object value = title.get(key);
						if (null == value) {
							continue;
						}
						if (value instanceof Date) {
							cell.setCellValue((Date) value);
						} else if (value instanceof Calendar) {
							cell.setCellValue((Calendar) value);
						} else if (value instanceof Boolean) {
							cell.setCellValue((Boolean) value);
						} else if (value instanceof BigDecimal) {
							cell.setCellValue(((BigDecimal) value)
									.doubleValue());
						} else {
							cell.setCellValue(value.toString());
						}
						if (null != titleCellStyles[i][j]) {
							cell.setCellStyle(titleCellStyles[i][j]);
						}
					}
				}
			}
			// TODO 表尾内容填充
			if (null != foot) {
			}

			String jgjb = (String) params.get("ORG_JGJB");
			if (StringUtil.isNotEmpty(jgjb)) {
				int orgColStartIdx = config.getOrgColStartIdx();
				int orgColEndIdx = config.getOrgColEndIdx();
				int colNum = (Integer.valueOf(jgjb)+1)/2;
				this.deleteColumn(sheet, orgColStartIdx+colNum, orgColEndIdx);
			}
			String txjb = (String) params.get("DEP_TXJB");
			if (StringUtil.isNotEmpty(txjb)) {
				int orgColStartIdx = config.getOrgColStartIdx();
				int orgColEndIdx = config.getOrgColEndIdx();
				int colNum = Integer.valueOf(txjb);
				this.deleteColumn(sheet, orgColStartIdx+colNum, orgColEndIdx);
			}
			
		}
		return this;
	}
	
	/**
	 * 整列删除
	 * @param sheet
	 * @param delStartColumn  删除开始列
	 * @param delEndColumn	删除结束列
	 */
	private void deleteColumn(HSSFSheet sheet, int delStartColumn, int delEndColumn){
		int delColNum = delEndColumn - delStartColumn + 1;
		if (delEndColumn > delStartColumn) {
			logger.warn("删除列错误！");
			return;
		}
		
		//处理合并的单元格
		List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
		//删除单元格
		int numMergedRegions = sheet.getNumMergedRegions();
		for (int i = 0; i < numMergedRegions; i++) {
			sheet.removeMergedRegion(i);
		}
		
		//删除列
		for (int i = 0; i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			
			//row不存在，跳过
			if (row == null) {
				continue;
			}
			
			int lastColumn = row.getLastCellNum();
			if (lastColumn < delStartColumn || lastColumn < delEndColumn) {
				continue;
			}
			for (int j = delStartColumn; j <= lastColumn; j++) {
				HSSFCell ocell =row.getCell(j);
				row.removeCell(ocell);
				HSSFCell ncell = row.getCell(j+delColNum);
				row.moveCell(ncell, (short) j);
			}
		}
		
		//合并单元格
		CellRangeAddress cellRangeAddress = null;
		int fColumn = 0;
		int lColumn = 0;
		for (int i = 0; i < mergedRegions.size(); i++) {
			cellRangeAddress = mergedRegions.get(i);
			fColumn = cellRangeAddress.getFirstColumn();
			lColumn = cellRangeAddress.getLastColumn();
			if (fColumn > delEndColumn) {
				cellRangeAddress.setFirstColumn(fColumn-delColNum);
				cellRangeAddress.setLastColumn(lColumn-delColNum);
				sheet.addMergedRegion(cellRangeAddress);
			} else if (lColumn < delStartColumn) {
				sheet.addMergedRegion(cellRangeAddress);
			}
		}
		
	}

	
	
	/**
	 * 合并值相同的行
	 * 
	 * @param sheet
	 * @param col
	 *            处理列
	 */
	private void handleSpan(HSSFSheet sheet, int col,
			ExcelExportConfig.SheetExportConfig config) {
		HSSFCell firstCell = null, currentCell = null;
		int spannum = 0;
		for (int i = config.getFormatRowIdx(); i <= sheet.getLastRowNum(); i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(col);
			if (i == config.getFormatRowIdx()) {
				firstCell = cell;
				spannum = 0;
			} else {
				currentCell = cell;
				if (firstCell.getStringCellValue().equals(
						currentCell.getStringCellValue())) {
					String value = cell.getStringCellValue();
					if ("-".equals(value)) {
						continue;
					}
					spannum++;
				} else {
					CellRangeAddress cra = new CellRangeAddress(
							firstCell.getRowIndex(), firstCell.getRowIndex()
									+ spannum, col, col);
					sheet.addMergedRegion(cra);
					firstCell = cell;
					spannum = 0;
				}
			}
			if (i == sheet.getLastRowNum()) {
				CellRangeAddress cra = new CellRangeAddress(
						firstCell.getRowIndex(), firstCell.getRowIndex()
								+ spannum, col, col);
				sheet.addMergedRegion(cra);
			}
		}
	}

	/**
	 * 导出表格
	 * 
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	public ExcelExportHandler export(OutputStream outputStream)
			throws IOException {
		final HSSFWorkbook workbook = excelExportConfig.getWorkbook();
		try {
			workbook.write(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			outputStream.flush();
			IOUtils.closeQuietly(outputStream);
		}
		return this;
	}

	/**
	 * 导出表格
	 * 
	 * @param response
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public ExcelExportHandler export(HttpServletResponse response,
			String fileName) throws IOException {
		response.reset();
		response.setContentType("application/octet-stream; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		export(response.getOutputStream());
		return this;
	}
}
