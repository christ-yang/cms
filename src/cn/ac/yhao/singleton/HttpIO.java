package cn.ac.yhao.singleton;

/**
 * 饿汉式单例模式（一开始就创建对象，线程安全）
 * @author yhao
 * 但当获取其中一个全局变量时，在类加载时还是会对HttpIO实例进行初始化，导致时间比较久
 */
public class HttpIO {
	public static final String TYPE_MP3 = ".mp3";
	private static final HttpIO INSTANCE = new HttpIO();
	
	private HttpIO() {
	}
	public static HttpIO getInstance(){
		return INSTANCE;
	}
}
/**
 * 静态内部类的单例模式
 * @author yhao
 * 当执行getInstance()方法的时候就去调用FileIOHolder内部类里面的INSTANCE实例，
 * 此时FileIOHolder内部类会被加载到内存里，在类加载的时候就对INSTANCE实例进行初始化
 */
class HttpIO1{
	
	private static final class HttpIO1Holder{
		private static final HttpIO1 INSTANCE = new HttpIO1(); 
	}
	
	public HttpIO1() {
	}
	public static HttpIO1 getInstance(){
		return HttpIO1Holder.INSTANCE;
	}
}