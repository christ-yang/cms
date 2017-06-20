package me.yhao.单例模式;

/**
 * 通过枚举实现单例模式
 * @author yhao
 * 枚举的特点保证只会有一个实例，
 * 同时保证了线程安全、反射安全和反序列化安全。
 */
public enum FileIOe {
	INSTENCE;
	//.....
	public void openFile(String fileName){
		//.....
	}
}
