package cn.ac.yhao.simpleFactory;

public class OperationSub extends Operation {

	@Override
	public double getResult() {
		double result = 0;
		result = NumberA - NumberB;
		return result;
	}

}
