package commons.export;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import commons.util.StringUtil;

/**
 * @author hoye
 *
 */
public class ExcelExportConfig {
	private Logger logger = Logger.getLogger(this.getClass());

	// /**
	// * 文件流加载
	// */
	// private static final DefaultResourceLoader defaultResourceLoader = new
	// DefaultResourceLoader();

	/**
	 * 模板文件路径
	 */
	private String modelFile;

	/**
	 * 模板文件对应的资源
	 */
	private FileSystemResource modelResource;

	/**
	 * 导出配置
	 */
	private List<SheetExportConfig> sheetExportConfigs;

	/**
	 * 待导出的Excel
	 */
	private HSSFWorkbook wb;

	/**
	 * 初始化
	 * 
	 * @param modelFile
	 */
	public ExcelExportConfig(String modelFile) {
		this.modelFile = modelFile;
		// this.modelResource = defaultResourceLoader.getResource(modelFile);
		this.modelResource = new FileSystemResource(modelFile);
		Assert.isTrue(modelResource.exists(), "模板文件不存在：" + modelFile);
		init();
	}

	/**
	 * 初始化模板信息数据
	 */
	private void init() {
		InputStream inputStream = null;
		try {
			inputStream = modelResource.getInputStream();
			wb = new HSSFWorkbook(inputStream);
		} catch (Exception e) {
			logger.error("HSSFWorkbook 初始化失败", e);
		}
		Assert.notNull(wb, "读取模板文件失败：" + modelFile);
		HSSFSheet sheet = wb.getSheet("TemplateSetting");
		Assert.notNull(sheet, "TemplateSetting Sheet页没有找到，请检查模板配置");
		final int sheetMaxIdx = wb.getSheetIndex(sheet);
		Set<Integer> idxs = new HashSet<Integer>();
		sheetExportConfigs = new ArrayList<SheetExportConfig>();
		SheetExportConfig cur = null;
		final int lastRowNum = sheet.getLastRowNum();
		for (int i = 0; i <= lastRowNum; i++) {
			final HSSFRow row = sheet.getRow(i);
			final HSSFCell cell = row.getCell(0);
			final HSSFCell valCell = row.getCell(1);
			if (null == cell || null == valCell) {
				continue;
			}
			cell.setCellType(CellType.STRING);
			valCell.setCellType(CellType.STRING);
			final String keyCellValue = cell.getStringCellValue();
			String valueCellValue = valCell.getStringCellValue();
			if (StringUtil.isEmpty(keyCellValue)
					|| StringUtil.isEmpty(keyCellValue)) {
				continue;
			}
			valueCellValue = valueCellValue.trim();
			if ("sheets.exportIdx".equals(keyCellValue)) {
				int idx = Integer.valueOf(valueCellValue) - 1;
				if (idxs.contains(idx)) {
					cur = null;
					continue;
				}
				idxs.add(idx);
				cur = new SheetExportConfig(idx);
				sheetExportConfigs.add(cur);
				continue;
			}
			if (null == cur) {
				continue;
			}
			if ("rows.formatRowIdx".equals(keyCellValue)) {
				cur.setFormatRowIdx(Integer.valueOf(valueCellValue) - 1);
			} else if ("rows.formatColStartIdx".equals(keyCellValue)) {
				cur.setFormatColStartIdx(valueCellValue.charAt(0) - 'A');
			} else if ("rows.formatColEndIdx".equals(keyCellValue)) {
				cur.setFormatColEndIdx(valueCellValue.charAt(0) - 'A');
			} else if ("rows.varFormatRowIdx".equals(keyCellValue)) {
				cur.setVarFormatRowIdx(Integer.valueOf(valueCellValue) - 1);
			} else if ("rows.varName".equals(keyCellValue)) {
				cur.setVarName(valueCellValue);
			} else if ("rows.mergedRegion".equals(keyCellValue)) {// 获取合并单元格相关信息数据
				cur.setMergedRegions(parseMergedRegion(valueCellValue));
			} else if ("rows.titleRowStartIdx".equals(keyCellValue)) {
				cur.setTitleRowStartIdx(Integer.valueOf(valueCellValue) - 1);
			} else if ("rows.titleRowEndIdx".equals(keyCellValue)) {
				cur.setTitleRowEndIdx(Integer.valueOf(valueCellValue) - 1);
			} else if ("rows.titleColStartIdx".equals(keyCellValue)) {
				cur.setTitleColStartIdx(valueCellValue.charAt(0) - 'A');
			} else if ("rows.titleColEndIdx".equals(keyCellValue)) {
				cur.setTitleColEndIdx(valueCellValue.charAt(0) - 'A');
			} else if ("rows.footRowStartIdx".equals(keyCellValue)) {
				cur.setFootRowStartIdx(Integer.valueOf(valueCellValue) - 1);
			} else if ("rows.footRowEndIdx".equals(keyCellValue)) {
				cur.setFootRowEndIdx(Integer.valueOf(valueCellValue) - 1);
			} else if ("rows.footColStartIdx".equals(keyCellValue)) {
				cur.setFootColStartIdx(valueCellValue.charAt(0) - 'A');
			} else if ("rows.footColEndIdx".equals(keyCellValue)) {
				cur.setFootColEndIdx(valueCellValue.charAt(0) - 'A');
			}
		}

		for (SheetExportConfig config : sheetExportConfigs) {
			sheet = wb.getSheetAt(config.getSheetIdx());
			final HSSFRow formatRow = sheet.getRow(config.getFormatRowIdx());
			final HSSFRow varFormatRow = sheet.getRow(config
					.getVarFormatRowIdx());
			Assert.notNull(formatRow, "rows.formatRowIdx配置项错误");
			Assert.notNull(varFormatRow, "rows.varFormatRowIdx配置项错误");
			if (-1 == config.getFormatColStartIdx()) {
				config.setFormatColEndIdx(formatRow.getLastCellNum());
			}
			final int start = config.getFormatColStartIdx();
			final int length = config.getFormatColEndIdx()
					- config.getFormatColStartIdx() + 1;
			HSSFCellStyle[] cellStyles = new HSSFCellStyle[length];
			String[] varExps = new String[length];
			for (int i = 0; i < length; i++) {
				final HSSFCell varFormatRowCell = varFormatRow.getCell(start
						+ i);
				varExps[i] = null != varFormatRowCell ? varFormatRowCell
						.getStringCellValue().replaceAll("row\\.?", "") : null;
				if (null != varFormatRowCell) {
					varFormatRow.removeCell(varFormatRowCell);
				}
				final HSSFCell formatRowCell = formatRow.getCell(start + i);
				cellStyles[i] = null != formatRowCell ? formatRowCell
						.getCellStyle() : null;
				if (null != formatRowCell) {
					formatRow.removeCell(formatRowCell);
				}
			}
			config.setColVarExps(varExps);
			config.setDataCellStyles(cellStyles);

			final HSSFCellStyle rowStyle = formatRow.getRowStyle();
			config.setDataRowStyle(rowStyle);

			// 表头数据格式获取
			final int titleRowStart = config.getTitleRowStartIdx();
			final int titleRowEnd = config.getTitleRowEndIdx();
			final int titleRowLength = titleRowEnd - titleRowStart + 1;
			final int titleColStart = config.getTitleColStartIdx();
			final int titleColEnd = config.getTitleColEndIdx();
			final int titleColLength = titleColEnd - titleColStart + 1;
			if (titleRowStart > -1 && titleRowEnd > -1 && titleColStart > -1
					&& titleColEnd > -1 && titleRowLength > 0
					&& titleColLength > 0) {
				String[][] titleVarExps = new String[titleRowLength][titleColLength];
				HSSFCellStyle[][] titleCellStyles = new HSSFCellStyle[titleRowLength][titleColLength];
				for (int i = 0; i < titleRowLength; i++) {
					final HSSFRow titleRow = sheet.getRow(titleRowStart + i);// 获取表头内容行数据
					if (null == titleRow) {
						continue;
					}
					for (int j = 0; j < titleColLength; j++) {
						final HSSFCell titleRowCell = titleRow
								.getCell(titleColStart + j);
						titleVarExps[i][j] = null != titleRowCell ? titleRowCell
								.getStringCellValue().replaceAll("row\\.?", "")
								: null;
						titleCellStyles[i][j] = null != titleRowCell ? titleRowCell
								.getCellStyle() : null;
						if (null != titleRowCell
								&& null != titleRowCell.getStringCellValue()
								&& titleRowCell.getStringCellValue()
										.startsWith("row\\.")) {
							titleRow.removeCell(titleRowCell);
						}
					}
				}
				config.setTitleVarExps(titleVarExps);
				config.setTitleCellStyles(titleCellStyles);
			}
			// 表尾数据格式获取
		}

		// 删除多余的sheet页
		List<Integer> needDelSheet = new LinkedList<Integer>();
		for (int i = 0; i <= sheetMaxIdx; i++) {
			final HSSFSheet sheetAt = wb.getSheetAt(i);
			if (null == sheetAt) {
				break;
			}
			if (!idxs.contains(i)) {
				needDelSheet.add(i);
			}
		}
		for (int i = needDelSheet.size() - 1; i >= 0; i--) {
			wb.removeSheetAt(needDelSheet.get(i));
		}
	}

	/**
	 * 解析文件合并单元格信息数据
	 * 
	 * @param mergedRegions
	 * @return
	 */
	private Set<Integer> parseMergedRegion(String mergedRegions) {
		Set<Integer> mrs = new HashSet<Integer>();
		if (StringUtil.isEmpty(mergedRegions)) {
			return mrs;
		}
		String[] strs = mergedRegions.split("-");
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			int mrIndex = str.charAt(0) - 'A';
			mrs.add(mrIndex);
		}
		return mrs;
	}

	public String getModelFile() {
		return modelFile;
	}

	public Resource getModelResource() {
		return modelResource;
	}

	public List<SheetExportConfig> getSheetExportConfigs() {
		return sheetExportConfigs;
	}

	public HSSFWorkbook getWorkbook() {
		return wb;
	}

	public void setSheetExportConfigs(List<SheetExportConfig> sheetExportConfigs) {
		this.sheetExportConfigs = sheetExportConfigs;
	}

	/**
	 * 用于记录导出表格参数配置信息
	 * 
	 * @author hoye
	 *
	 */
	public static class SheetExportConfig {
		private int sheetIdx = 0;
		private int formatRowIdx = 1;
		private int formatColStartIdx = 0;
		private int formatColEndIdx = -1;
		private int varFormatRowIdx = 2;
		private String varName = "this";
		private Set<Integer> mergedRegions = new HashSet<Integer>();// 记录合并单元格信息数据信息
		private String[] colVarExps;
		private HSSFCellStyle[] dataCellStyles;
		private HSSFCellStyle dataRowStyle;

		private int titleRowStartIdx = -1;
		private int titleRowEndIdx = -1;
		private int titleColStartIdx = -1;
		private int titleColEndIdx = -1;
		private int footRowStartIdx = -1;
		private int footRowEndIdx = -1;
		private int footColStartIdx = -1;
		private int footColEndIdx = -1;
		private String[][] titleVarExps;
		private HSSFCellStyle[][] titleCellStyles;
		private HSSFCellStyle[][] footVarExps;
		private HSSFCellStyle[][] footCellStyles;

		public HSSFCellStyle[][] getFootVarExps() {
			return footVarExps;
		}

		public void setFootVarExps(HSSFCellStyle[][] footVarExps) {
			this.footVarExps = footVarExps;
		}

		public HSSFCellStyle[][] getFootCellStyles() {
			return footCellStyles;
		}

		public void setFootCellStyles(HSSFCellStyle[][] footCellStyles) {
			this.footCellStyles = footCellStyles;
		}

		public int getTitleRowStartIdx() {
			return titleRowStartIdx;
		}

		public void setTitleRowStartIdx(int titleRowStartIdx) {
			this.titleRowStartIdx = titleRowStartIdx;
		}

		public int getTitleColStartIdx() {
			return titleColStartIdx;
		}

		public void setTitleColStartIdx(int titleColStartIdx) {
			this.titleColStartIdx = titleColStartIdx;
		}

		public int getTitleColEndIdx() {
			return titleColEndIdx;
		}

		public void setTitleColEndIdx(int titleColEndIdx) {
			this.titleColEndIdx = titleColEndIdx;
		}

		public int getFootRowStartIdx() {
			return footRowStartIdx;
		}

		public void setFootRowStartIdx(int footRowStartIdx) {
			this.footRowStartIdx = footRowStartIdx;
		}

		public int getTitleRowEndIdx() {
			return titleRowEndIdx;
		}

		public void setTitleRowEndIdx(int titleRowEndIdx) {
			this.titleRowEndIdx = titleRowEndIdx;
		}

		public int getFootRowEndIdx() {
			return footRowEndIdx;
		}

		public void setFootRowEndIdx(int footRowEndIdx) {
			this.footRowEndIdx = footRowEndIdx;
		}

		public int getFootColStartIdx() {
			return footColStartIdx;
		}

		public void setFootColStartIdx(int footColStartIdx) {
			this.footColStartIdx = footColStartIdx;
		}

		public int getFootColEndIdx() {
			return footColEndIdx;
		}

		public void setFootColEndIdx(int footColEndIdx) {
			this.footColEndIdx = footColEndIdx;
		}

		public String[][] getTitleVarExps() {
			return titleVarExps;
		}

		public void setTitleVarExps(String[][] titleVarExps) {
			this.titleVarExps = titleVarExps;
		}

		public HSSFCellStyle[][] getTitleCellStyles() {
			return titleCellStyles;
		}

		public void setTitleCellStyles(HSSFCellStyle[][] titleCellStyles) {
			this.titleCellStyles = titleCellStyles;
		}

		public SheetExportConfig(int sheetIdx) {
			this.sheetIdx = sheetIdx;
		}

		public Set<Integer> getMergedRegions() {
			return mergedRegions;
		}

		public void setMergedRegions(Set<Integer> mergedRegions) {
			this.mergedRegions = mergedRegions;
		}

		public int getSheetIdx() {
			return sheetIdx;
		}

		public int getFormatRowIdx() {
			return formatRowIdx;
		}

		public void setFormatRowIdx(int formatRowIdx) {
			this.formatRowIdx = formatRowIdx;
		}

		public int getFormatColStartIdx() {
			return formatColStartIdx;
		}

		public void setFormatColStartIdx(int formatColStartIdx) {
			this.formatColStartIdx = formatColStartIdx;
		}

		public int getFormatColEndIdx() {
			return formatColEndIdx;
		}

		public void setFormatColEndIdx(int formatColEndIdx) {
			this.formatColEndIdx = formatColEndIdx;
		}

		public int getVarFormatRowIdx() {
			return varFormatRowIdx;
		}

		public void setVarFormatRowIdx(int varFormatRowIdx) {
			this.varFormatRowIdx = varFormatRowIdx;
		}

		public String getVarName() {
			return varName;
		}

		public void setVarName(String varName) {
			this.varName = varName;
		}

		public String[] getColVarExps() {
			return colVarExps;
		}

		public void setColVarExps(String[] colVarExps) {
			this.colVarExps = colVarExps;
		}

		public HSSFCellStyle[] getDataCellStyles() {
			return dataCellStyles;
		}

		public void setDataCellStyles(HSSFCellStyle[] dataCellStyles) {
			this.dataCellStyles = dataCellStyles;
		}

		public HSSFCellStyle getDataRowStyle() {
			return dataRowStyle;
		}

		public void setDataRowStyle(HSSFCellStyle dataRowStyle) {
			this.dataRowStyle = dataRowStyle;
		}
	}
}
