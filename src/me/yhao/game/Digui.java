package me.yhao.game;

import java.math.BigInteger;

public class Digui {

	public static BigInteger fun(long num, BigInteger result){
		if (num == 1) {
			return result;
		} else {
			return fun(num-1, result.multiply(BigInteger.valueOf(num)));
		}
	}
	
	public static long fu(long num){
		if (num > 2) {
			return num*fu(num-1);
		}
		return num;
	}
	
	public static void main(String[] args) {
		long a = 1493;
		System.out.println(a+"!="+fun(a,BigInteger.valueOf(1)));
		
	}
}
