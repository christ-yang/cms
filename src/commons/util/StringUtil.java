package commons.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class StringUtil {

	public static String iso2utf8(String src) {
		try {
			if (isEmpty(src))
				return "";
			return new String(src.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			return "?";
		}
	}

	public static Integer getStringLength(String str) {
		if (isEmpty(str))
			return 0;
		return str.length();
	}

	public static String getString4XXX(String str) {
		if (isEmpty(str))
			return "";
		if (str.length() <= 4)
			return str;
		String ss = str.substring(0, str.length() - 4);
		String result = "";
		for (int i = 0; i < ss.length(); i++) {
			result += "*";
		}
		str = str.substring(str.length() - 4, str.length());
		result += str;
		return result;
	}

	public static String getString4X(String str) {
		if (isEmpty(str))
			return "";
		if (str.length() <= 10)
			return str;
		String result = "";
		result = str.substring(0, 10) + "...";
		return result;
	}

	public static String getTimeForZN(String src) {
		if (isEmpty(src))
			return "0秒";
		double db = Double.parseDouble(src);
		// long time = Long.parseLong(src);
		long time = (long) db;
		long s = time % 60;// 秒数
		long fa = time / 60;// 总分钟数
		long f = fa % 60;// 分钟数
		long h = time / (60 * 60);// 小时数
		StringBuffer sb = new StringBuffer();
		if (h != 0) {
			sb.append(h + "小时");
		}
		if (f != 0) {
			sb.append(f + "分钟");
		}
		sb.append(s + "秒");
		return sb.toString();
	}

	public static String iso2gbk(String src) {
		try {
			if (isEmpty(src))
				return "";
			return new String(src.getBytes("iso-8859-1"), "gbk");
		} catch (UnsupportedEncodingException e) {
			return "?";
		}
	}

	/**
	 * <li>判断字符串是否为空值</li> <li>NULL、空格均认为空值</li>
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEmpty(String value) {
		return null == value || "".equals(value.trim());
	}

	/**
	 * 内容不为空
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNotEmpty(String value) {
		return null != value && !"".equals(value.trim());
	}

	public static String getValue(String value) {
		return null == value || "".equals(value) ? "" : value.trim();
	}

	/**
	 * 重复字符串 如 repeatString("a",3) ==> "aaa"
	 * 
	 * @author uke
	 * @param src
	 * @param repeats
	 * @return
	 */
	public static String repeatString(String src, int repeats) {
		if (null == src || repeats <= 0) {
			return src;
		} else {
			StringBuffer bf = new StringBuffer();
			for (int i = 0; i < repeats; i++) {
				bf.append(src);
			}
			return bf.toString();
		}
	}

	/**
	 * 左对齐字符串 * lpadString("X",3); ==>" X" *
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static String lpadString(String src, int length) {
		return lpadString(src, length, " ");
	}

	/**
	 * 以指定字符串填补空位，左对齐字符串 * lpadString("X",3,"0"); ==>"00X"
	 * 
	 * @param src
	 * @param byteLength
	 * @param temp
	 * @return
	 */
	public static String lpadString(String src, int length, String single) {
		if (src == null || length <= src.getBytes().length) {
			return src;
		} else {
			return repeatString(single, length - src.getBytes().length) + src;
		}
	}

	/**
	 * 右对齐字符串 * rpadString("9",3)==>"9 "
	 * 
	 * @param src
	 * @param byteLength
	 * @return
	 */
	public static String rpadString(String src, int byteLength) {
		return rpadString(src, byteLength, " ");
	}

	/**
	 * 以指定字符串填补空位，右对齐字符串 rpadString("9",3,"0")==>"900"
	 * 
	 * @param src
	 * @param byteLength
	 * @param single
	 * @return
	 */
	public static String rpadString(String src, int length, String single) {
		if (src == null || length <= src.getBytes().length) {
			return src;
		} else {
			return src + repeatString(single, length - src.getBytes().length);
		}
	}

	/**
	 * 去除,分隔符，用于金额数值去格式化
	 * 
	 * @param value
	 * @return
	 */
	public static String decimal(String value) {
		if (null == value || "".equals(value.trim())) {
			return "0";
		} else {
			return value.replaceAll(",", "");
		}
	}

	/**
	 * 在数组中查找字符串
	 * 
	 * @param params
	 * @param name
	 * @param ignoreCase
	 * @return
	 */
	public static int indexOf(String[] params, String name, boolean ignoreCase) {
		if (params == null)
			return -1;
		for (int i = 0, j = params.length; i < j; i++) {
			if (ignoreCase && params[i].equalsIgnoreCase(name)) {
				return i;
			} else if (params[i].equals(name)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 将字符转数组
	 * 
	 * @param str
	 * @return
	 */
	public static String[] toArr(String str) {
		String inStr = str;
		String a[] = null;
		try {
			if (null != inStr) {
				StringTokenizer st = new StringTokenizer(inStr, ",");
				if (st.countTokens() > 0) {
					a = new String[st.countTokens()];
					int i = 0;
					while (st.hasMoreTokens()) {
						a[i++] = st.nextToken();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * 字符串数组包装成字符串
	 * 
	 * @param ary
	 * @param s
	 *            包装符号如 ' 或 "
	 * @return
	 */
	public static String toStr(String[] ary, String s) {
		if (ary == null || ary.length < 1)
			return "";
		StringBuffer bf = new StringBuffer();
		bf.append(s);
		bf.append(ary[0]);
		for (int i = 1; i < ary.length; i++) {
			bf.append(s).append(",").append(s);
			bf.append(ary[i]);
		}
		bf.append(s);
		return bf.toString();
	}

	/**
	 * 设置MESSAGE中的变量{0}...{N}
	 * 
	 * @param msg
	 * @param vars
	 * @return
	 */
	public static String getMessage(String msg, String[] vars) {
		for (int i = 0; i < vars.length; i++) {
			msg = msg.replaceAll("\\{" + i + "\\}", vars[i]);
		}
		return msg;
	}

	/**
	 * @param msg
	 * @param var
	 * @return
	 */
	public static String getMessage(String msg, String var) {
		return getMessage(msg, new String[] { var });
	}

	/**
	 * @param msg
	 * @param var
	 * @param var2
	 * @return
	 */
	public static String getMessage(String msg, String var, String var2) {
		return getMessage(msg, new String[] { var, var2 });
	}

	/**
	 * 取整数值
	 * 
	 * @param map
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Map map, String key, int defValue) {
		if (null != map && isNotEmpty(key)) {
			try {
				return (Integer) getMapIntValue(map, key);
			} catch (Exception e) {
			}
		}
		return defValue;
	}

	/**
	 * 取字符串
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static String get(Map map, String key) {
		return getString(map, key, "");
	}

	public static String getMapValue(Map map, String key) {
		return getString(map, key, "");
	}

	/**
	 * 从Map中取Integer类型值
	 * 
	 * @param map
	 * @param key
	 * @return
	 */
	public static Object getMapIntValue(Map map, Object key) {
		if (null == map || null == key)
			return new Integer(0);
		Object value = map.get(key);
		if (null == value) {
			return new Integer(0);
		}
		try {
			return Integer.valueOf(value.toString());
		} catch (NumberFormatException e) {
			return new Integer(0);
		}
	}

	/**
	 * 取字符串
	 * 
	 * @param map
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Map map, String key, String defValue) {
		if (null != map && isNotEmpty(key)) {
			Object val = map.get(key);
			if (null == val) {
				return defValue;
			} else {
				return val.toString();
			}
		}
		return defValue;
	}

	public static String appLikeCondition(String elem, boolean preFlag,
			boolean nextFlag) {
		StringBuffer bf = new StringBuffer();
		if (preFlag) {
			bf.append("%");
		}
		if (isNotEmpty(elem)) {
			bf.append(elem);
		}
		if (nextFlag) {
			bf.append("%");
		}
		return bf.toString();
	}

	/**
	 * 将key=value;key2=value2……转换成Map
	 * 
	 * @param params
	 * @return
	 */
	public static Map gerneryParams(String params) {
		Map args = new HashMap();
		if (!isEmpty(params)) {
			try {
				String map, key, value;
				StringTokenizer st = new StringTokenizer(params, ";");
				StringTokenizer stMap;
				while (st.hasMoreTokens()) {
					map = st.nextToken();
					if (isEmpty(map.trim()))
						break;
					stMap = new StringTokenizer(map, "=");
					key = stMap.hasMoreTokens() ? stMap.nextToken() : "";
					if (isEmpty(key.trim()))
						continue;
					value = stMap.hasMoreTokens() ? stMap.nextToken() : "";
					args.put(key, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return args;
	}

	/**
	 * 页面格式化日期:yyyyMMdd ---> yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
//	public static String formatDate(String date) {
//		return isEmpty(date) ? "" : DateUtil.format(date, "yyyyMMdd",
//				"yyyy-MM-dd");
//	}

	public static String formatDate1(String date) {
		return isEmpty(date) ? "" : date.substring(0, date.lastIndexOf("."));
	}

	public static String formatDateZh(String date) {
		if (isEmpty(date)) {
			return "";
		}
		String[] strs = date.split("-");
		StringBuffer sb = new StringBuffer();
		sb.append(strs[0] + " 年 " + strs[1] + " 月 " + strs[2] + " 日 ");
		return sb.toString();
	}

	/**
	 * 解析文件的扩展名
	 * 
	 * @param oldName
	 * @return
	 */
	public static String getFileExtName(String oldName) {
		String ext = "";
		int lastIndex = oldName.lastIndexOf(".");
		if (lastIndex > 0) {
			ext = oldName.substring(lastIndex);
		}
		return ext;
	}

	public static void generyXmlEntry(StringBuffer bf, String entry,
			Object value) {
		bf.append("<").append(entry).append(">");
		if (null != value)
			bf.append(value.toString().trim());
		bf.append("</").append(entry).append(">");
	}

	public static void generyXmlEntryCData(StringBuffer bf, String entry,
			Object value) {
		bf.append("<").append(entry).append("><![CDATA[");
		if (null != value)
			bf.append(value);
		bf.append("]]></").append(entry).append(">");
	}

	/**
	 * 生成图片访问全路径
	 * 
	 * @param rootUrl
	 *            图片服务器根目录
	 * @param dir
	 *            分类目录
	 * @param imgId
	 *            图片ID
	 * @param imgType
	 *            图片类型
	 * @return
	 */
	public static String generyImgUrl(Object rootUrl, Object dir, Object imgId,
			Object imgType) {
		StringBuffer bf = new StringBuffer();
		try {
			bf.append(rootUrl).append("/");
			bf.append(dir).append("/");
			bf.append(imgId).append(".").append(imgType);
		} catch (Exception e) {
			bf.append("");
		}
		return bf.toString();
	}

	public static int string2Int(String s) {
		if (s == null || "".equals(s) || "undefined".equals(s))
			return 0;
		int result = 0;
		try {
			if (s.indexOf(",") != -1) {
				s = s.substring(0, s.indexOf(","));
			} else if (s.indexOf(".") != -1) {
				s = s.substring(0, s.indexOf("."));
			} else if (s.indexOf("|") != -1) {
				s = s.substring(0, s.indexOf("|"));
			}
			result = Integer.parseInt(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean string2boolean(String s) {
		return "true".equals(s);
	}

	public static String formatAccount(String acctNo, String format) {
		if ("#### ####".equals(format)) {
			if (isNotEmpty(acctNo) && acctNo.length() > 4) {
				StringBuffer bf = new StringBuffer();
				int max = acctNo.length() - 4;
				int i = 0;
				while (i < max) {
					bf.append(acctNo.substring(i, i + 4)).append(" ");
					i += 4;
				}
				bf.append(acctNo.substring(i));
				return bf.toString();
			}
		}
		return acctNo;
	}

	/**
	 * 格式化xml数据
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static String formatXmlStr(String xmlStr) {
		return formatXmlStr(xmlStr, "utf-8");
	}

	/**
	 * 格式化xml数据
	 * 
	 * @param stringWriter
	 * @param doc
	 * @throws IOException
	 */
	public static String formatXmlStr(String xmlStr, String encoding) {
		Document doc;
		try {
			doc = DocumentHelper.parseText(xmlStr);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return xmlStr;
		}
		doc.setXMLEncoding(encoding);
		Writer stringWriter = new StringWriter();
		OutputFormat of = new OutputFormat();
		of.setIndent(true);
		of.setNewlines(true);
		XMLWriter xmlWriter = new XMLWriter(stringWriter, of);
		try {
			xmlWriter.write(doc);
			xmlWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return xmlStr;
		}
		return stringWriter.toString();
	}

	public static String replacePwd(String log) {
		log = log.replaceAll("\\\"Password\":\".*?\"", "\"Password\":\"\"");
		log = log.replaceAll("\\\"FundTransPwd\":\".*?\"",
				"\"FundTransPwd\":\"\"");
		log = log.replaceAll("\\\"MobilePasswd\":\".*?\"",
				"\"MobilePasswd\":\"\"");
		log = log.replaceAll("\\\"Passwd\":\".*?\"", "\"Passwd\":\"\"");
		return log;
	}

	public static String replacePwdFormXMl(String log) {
		log = log.replaceAll("<Password>.*?</Password>",
				"<Password></Password>");
		log = log.replaceAll("<FundTransPwd>.*?</FundTransPwd>",
				"<FundTransPwd></FundTransPwd>");
		log = log.replaceAll("<MobilePasswd>.*?</MobilePasswd>",
				"<MobilePasswd></MobilePasswd>");
		return log;
	}

	public static String replaceXMLPwd(String log) {
		log = log.replaceAll("<Password>.*?</Password>",
				"<Password></Password>");
		log = log.replaceAll("<TrsPassword>.*?</TrsPassword>",
				"<TrsPassword></TrsPassword>");
		log = log.replaceAll("<TrsPwd>.*?</TrsPwd>", "<TrsPwd></TrsPwd>");
		log = log.replaceAll("<OldPassword>.*?</OldPassword>",
				"<OldPassword></OldPassword>");
		log = log.replaceAll("<AcountPassword>.*?</AcountPassword>",
				"<AcountPassword></AcountPassword>");
		return log;
	}

	/**
	 * 返回一个20位的交易流水号
	 * 
	 * @return String
	 */
	public static String getUniqueMTRANS_ID() {
		return "0000000" + (new Date().getTime() + "");
	}

	/**
	 * 查询条件中不能包含空格、分号、引号等特殊关键字符<br>
	 * <b>防SQL注入</b>
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	public static boolean checkQueryParam(Object paramMap) {
		if (paramMap == null)
			return true;
		String content = "";
		if (paramMap instanceof Map) {
			Map<String, Object> params = (Map<String, Object>) paramMap;
			if (params == null || params.isEmpty()) {
				return true;
			}
			StringBuffer bf = new StringBuffer();
			for (String key : params.keySet()) {
				if (params.get(key) instanceof List) {
					List<String> list = (List<String>) params.get(key);
					for (String string : list) {
						bf.append(string);
					}
				} else {
					bf.append(params.get(key));
				}
			}
			content = bf.toString();
		} else {
			content = paramMap.toString();
		}
		if (StringUtil.isEmpty(content)) {
			return true;
		}
		// 防SQL注入
		return content.matches("[^\\s|^;|^\\'|^\"]+");
	}

	public static void parseRequest(HttpServletRequest request, Map params) {
		Enumeration<String> enumes = request.getParameterNames();

		while (enumes.hasMoreElements()) {
			String name = enumes.nextElement();
			params.put(name, request.getParameter(name));
		}
	}

	/**
	 * 显示参数文本
	 * 
	 * @param key
	 * @param itemType
	 * @param language
	 * @return
	 */
//	public static String showLabel(String key, String itemType) {
//		if (isEmpty(key) || isEmpty(itemType)) {
//			return "";
//		}
//		String text = ServerInit.disgramCaches.loadValue(itemType, null,
//				key.trim());
//		return StringUtil.isEmpty(text) ? key : text;
//	}

	/**
	 * @param itemType
	 * @return
	 */
//	public static List showItems(String itemType) {
//		List datas = new ArrayList();
//		if (isEmpty(itemType)) {
//			return datas;
//		}
//		Map<String, String> map = ServerInit.disgramCaches.getCaches(itemType,
//				null);
//		if (map == null || map.isEmpty()) {
//			return datas;
//		}
//		for (String key : map.keySet()) {
//			Map tmp = new HashMap();
//			tmp.put("VALUE", key);
//			tmp.put("LABEL", map.get(key));
//			datas.add(tmp);
//		}
//		return datas;
//	}

	public static String showPageNumbar(Integer totalCount, Integer totalPage,
			Integer curNo, String form) {
		StringBuffer bf = new StringBuffer();
		if (totalCount > 0) {
			totalPage = Math.max(1, totalPage);
		}
		int min = Math.max(1, (curNo - 2));
		int max = Math.min(totalPage, min + 4);
		bf.append("<ul data-totalCount='");
		bf.append(totalCount);
		bf.append("' data-curPageNo='").append(curNo);
		bf.append("' data-maxPageNo='").append(max);
		bf.append("' data-minPageNo='").append(min);
		bf.append("' data-totalPageNo='").append(totalPage);
		bf.append("'>");

		bf.append("<li ");
		if (min > 1) {
			bf.append("onclick='showMorePrev(this)'");
		} else {
			bf.append(" class='disabled'");
		}
		bf.append("><a onclick='return false;'>&lt;&lt;</a></li>");
		for (int i = min; i <= max; i++) {
			bf.append("<li");
			if (i == curNo) {
				bf.append(" class='active'");
			}
			bf.append("><a onclick='gotoPageList(");
			bf.append(form).append("," + i + ");return false;'>");
			bf.append(i).append("</a>");
		}
		bf.append("<li ");
		if (max < totalPage) {
			bf.append(" onclick='showMoreNext(this)'");
		} else {
			bf.append(" class='disabled'");
		}
		bf.append(" ><a onclick='return false;'>&gt;&gt;</a></li>");
		bf.append("</ul>");
		return bf.toString();
	}

	/**
	 * 格式化xml数据
	 * 
	 * @param stringWriter
	 * @param doc
	 * @throws IOException
	 */
	public static void formateXMLStr(Writer stringWriter, Element elem)
			throws IOException {
		OutputFormat of = new OutputFormat();
		of.setIndent(true);
		of.setNewlines(true);
		XMLWriter xmlWriter = new XMLWriter(stringWriter, of);
		xmlWriter.write(elem);
		xmlWriter.close();
	}

	/**
	 * 格式化xml数据
	 * 
	 * @param stringWriter
	 * @param doc
	 * @throws IOException
	 */
	public static void formateXMLStr(Writer stringWriter, Document doc)
			throws IOException {
		OutputFormat of = new OutputFormat();
		of.setIndent(true);
		of.setEncoding("UTF-8");
		of.setNewlines(true);
		XMLWriter xmlWriter = new XMLWriter(stringWriter, of);
		xmlWriter.write(doc);
		xmlWriter.close();
	}

	public static boolean isNumber(String s) {
		return s.matches("[0-9\\.]+");
	}

	/**
	 * 金额格式化
	 * 
	 * @param s
	 *            金额
	 * @param len
	 *            小数位数
	 * @return 格式后的金额
	 */
	public static String formatDouble(String num, Integer length) {
		if (StringUtil.isEmpty(num)) {
			return "-";
		}
		NumberFormat format = null;
		double numD = Double.parseDouble(num);
		if (length == 0) {
			format = new DecimalFormat("###,###");
		} else {
			StringBuffer buff = new StringBuffer();
			buff.append("###,##0.");
			for (int i = 0; i < length; i++) {
				buff.append("0");
			}
			format = new DecimalFormat(buff.toString());
		}
		return format.format(numD);
	}

	public static void main(String[] args) {
		// Map param = new HashMap();
		// param.put("id", "fdsafdsa");
		// System.out.println(checkQueryParam(param));
		System.out.println(formatDouble("0.000", 0));
	}
}
