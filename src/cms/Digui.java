package cms;

import java.math.BigInteger;

public class Digui {

	public static BigInteger f(long num){
		if (num-1>0) {
			return f(num-1).multiply(BigInteger.valueOf(num));
		}
		return BigInteger.valueOf(num);
	}
	
	public static void main(String[] args) {
		long a = 1493;
		System.out.println(a+"!="+f(a));
	}
}
