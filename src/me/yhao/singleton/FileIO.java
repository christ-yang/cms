package me.yhao.singleton;

/**
 * @author yhao
 * 懒汉式单例模式（需要的时候才创建,线程不安全）
 * 当多线程并发执行getInstance()时，可能会产生多个实例
 */
public class FileIO {
	
	private static FileIO fileIO;
	
	private FileIO() {
	}
	public static FileIO getInstance(){
		if (fileIO == null) {
			fileIO = new FileIO();
		}
		return fileIO;
	}
}

/**
 * 
 * @author yhao
 *线程安全-双重检验锁
 *
 *当有多个线程通过第一次检验时，假设线程拿到锁进入synchronized语句块，对fileIO实例进行初始化，
 *释放FileIO.class锁之后，线程二持有这个锁进入synchronized语句块，此时又对fileIO对象就行初始化。
 *所以在这里进行第二次检验防止这种意外发生。
 *
 *volatile关键字防止JVM优化，保证已完成初始化
 */
class FileIO1{
	
	private static volatile FileIO1 fileIO1;
	private FileIO1(){}
	public static FileIO1 getInstance(long maxFileSize){
		if (fileIO1 == null) {
			synchronized (FileIO1.class) {
				if (fileIO1 == null) {
					fileIO1 = new FileIO1();
				}
			}
		}
		return fileIO1;
	}
}