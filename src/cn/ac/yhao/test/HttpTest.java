package cn.ac.yhao.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;

public class HttpTest {

	public static void before(String url, Map<String, String> params) {
		String jsonStr = JSON.toJSONString(params);
		System.out.println(jsonStr);
		try {
			HttpClient httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(jsonStr, "UTF-8");
			entity.setContentEncoding("UTF-8");
			httpPost.setEntity(entity);
			HttpResponse resp = httpClient.execute(httpPost);
			if (resp.getStatusLine().getStatusCode() == 200) {
				HttpEntity reEnetity = resp.getEntity();
				String respContent = EntityUtils.toString(reEnetity, "UTF-8");
				System.out.println(respContent);
			} else {
				System.out.println("StatusCode"
						+ resp.getStatusLine().getStatusCode());
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	// @Test
	// public void testAppService004() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00004.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("CURR_ISO", "CNY");
	// params.put("RATE_LLLX", "01");
	// // params.put("RATE_LLZL", "01002");
	// params.put("DATE", "2015-10-24");
	// params.put("PAGE_NO", "1");
	// params.put("PAGE_SIZE", "2");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService005() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00005.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("CURR_ISO", "CNY");
	// params.put("RATE_LLLX", "01");
	// params.put("RATE_LLZL", "01002");
	// params.put("DATE", "2015-10-24");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService006() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00006.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("CURR_ISO", "CNY");
	// params.put("RATE_LLLX", "01");
	// params.put("RATE_LLZL", "01002");
	// params.put("PRD_TS", "365");
	// params.put("DATE", "2015-10-24");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService007() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00007.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("CURR_ISO", "CNY");
	// params.put("PAGE_NO", "1");
	// params.put("PAGE_SIZE", "2");
	// params.put("FSI_LLZL", "1101");
	// params.put("DATE", "2016-01-05");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService008() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00008.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("CURR_ISO", "CNY");
	// params.put("FSI_LLZL", "1101");
	// params.put("DATE", "2016-01-05");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService009() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00009.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("CURR_ISO", "CNY");
	// params.put("FSI_LLZL", "1101");
	// params.put("DATE", "2016-10-05");
	// params.put("PRD_TS", "30");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService010() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00010.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("PRD_TYPE", "01");
	// params.put("PAGE_NO", "2");
	// // params.put("PAGE_SIZE", "5");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService011() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00011.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService012() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00012.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService013() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00013.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService014() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00014.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("PRD_CPLB", "1");
	// params.put("PRD_LVL", "2");
	// params.put("PRD_SJCPDM", "01");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService015() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00015.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// // params.put("PRD_SJCPDM", "01");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService016() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00016.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("RATE_LLLX", "01");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService017() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00017.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// // params.put("RATE_LLLX", "01");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService018() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00018.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("DEP_TXJB", "1");
	// params.put("PRD_CAL_TYPE", "1");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService019() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00019.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("DEP_TXBM", "1011101");
	// params.put("PRD_CAL_TYPE", "1");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService020() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00020.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService021() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00021.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("PRD_CPDM", "10111010101");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService022() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00022.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("PRD_CPDM", "10111010101");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService023() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00023.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// // params.put("PRD_CPDM", "10111010101");
	// before(url, params);
	// }
	//
	// @Test
	// public void testAppService024() {
	// String url = "http://127.0.0.1:8080/pdcs/app/PDCS00024.shtml";
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("ORG_LVL", "7");
	// params.put("ORG_PAR_ID", "0201");
	// before(url, params);
	// }

	// @Test
	// public void test() {
	// int pageCount = 10;
	// int size = 9;
	// pageCount = pageCount % size == 0 ? pageCount / size : pageCount / size
	// + 1;
	// System.out.println(pageCount);
	// }

	@Test
	public void testAppService001() {
		String url = "http://127.0.0.1:8080/pdcs/app/pdcs0001.shtml";
		Map<String, String> params = new HashMap<String, String>();
		params.put("USER_ID", "heyi001");
		params.put("DEV_TYPE", "1");
		params.put("USER_PSW", "E10ADC3949BA59ABBE56E057F20F883E");
		params.put("DEVICE_ID", "123123123");
		before(url, params);
	}

	@Test
	public void testAppService003() {
		String url = "http://127.0.0.1:8080/pdcs/app/pdcs0003.shtml";
		Map<String, String> params = new HashMap<String, String>();
		params.put("USER_ID", "heyi001");
		params.put("USER_TEL", "18605511921");
		before(url, params);
	}

	@Test
	public void testAppService004() {
		String url = "http://127.0.0.1:8080/pdcs/app/pdcs0004.shtml";
		Map<String, String> params = new HashMap<String, String>();
		params.put("TOKEN", "heyi001");
		params.put("CODE_MSG", "123123");
		before(url, params);
	}

	 @Test
	 public void testAppService002() {
	 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0002.shtml";
	 Map<String, String> params = new HashMap<String, String>();
	 params.put("USER_ID", "heyi001");
	 params.put("HEAD_IMG", "2131");
//	 params.put("USER_ADDR", "红庙北里67");
	 before(url, params);
	 }
	 
	 @Test
	 public void testAppService005() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0005.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "heyi001");
		 params.put("USER_TEL", "18605511955");
		 params.put("DEV_TYPE", "2");
		 params.put("DEVICE_ID", "123456");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService006() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0006.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "heyi001");
		 params.put("USER_PSW", "E10ADC3949BA59ABBE56E057F20F883E");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService007() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0007.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("ROLE_ID", "1");
		 params.put("USER_ID", "heyi001");
		 params.put("PAGE_NO", "1");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService008() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0008.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "heyi001");
		 params.put("NOTICE_ID", "3");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService009() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0009.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "yhao1234");
		 params.put("USER_PSW", "123456789");
		 params.put("USER_PSW_NEW", "123467898");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService033() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0033.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "yhao1234");
		 params.put("ROLE_ID", "123456789");
		 params.put("PRD_TYPE", "02");
		 params.put("PRD_BJRQ", "2017-06-29");
		 params.put("PAGE_NO", "1");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService046() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0046.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "heyi001");
		 params.put("ROLE_ID", "1");
		 params.put("PRD_CPDM", "25354");
		 params.put("PRD_JGDM", "0001");
		 params.put("PRD_BZ", "CNY");
		 params.put("CXLB", "1");
		 params.put("CUST_LVL", "2555");
		 params.put("CZRQ", "2017-06-29");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService047() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0047.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "heyi001");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService048() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0048.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "heyi001");
		 params.put("TYPE", "4");
		 params.put("MENU_ID", "1001");
		 params.put("MENU_NAME", "19802");
		 params.put("MENU_PAR_ID", "103");
		 params.put("MENU_URL", "10704");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService049() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0049.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "heyi001");
		 params.put("TYPE", "3");
		 params.put("ROLE_ID", "1001");
		 params.put("ROLE_NAME", "0000");
		 params.put("ROLE_BZ", "0000");
		 before(url, params);
	 }
	 
	 @Test
	 public void testAppService050() {
		 String url = "http://127.0.0.1:8080/pdcs/app/pdcs0050.shtml";
		 Map<String, String> params = new HashMap<String, String>();
		 params.put("USER_ID", "heyi001");
		 params.put("TYPE", "1");
		 params.put("ROLE_ID", "1000002");
		 params.put("MENU_ID", "04000,01000,02000,03000,01001,01002,01003,02001,02002,03001,03002");
		 before(url, params);
	 }
	
}
