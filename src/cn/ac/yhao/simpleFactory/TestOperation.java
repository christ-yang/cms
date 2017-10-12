package cn.ac.yhao.simpleFactory;

import org.junit.Test;

public class TestOperation {

	@Test
	public void test(){
		Operation oper = null;
		oper = OperationFactory.createOperate("/");
		oper.NumberA = 6;
		oper.NumberB = 0;
		double result = 0;
		try {
			result = oper.getResult();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		System.out.println(result);
	}
}
