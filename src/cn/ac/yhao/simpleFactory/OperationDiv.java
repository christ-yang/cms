package cn.ac.yhao.simpleFactory;

public class OperationDiv extends Operation {

	@Override
	public double getResult() throws Exception {
		double result = 0;
		if (NumberB == 0) {
			throw new Exception("除数不能为0！");
		}
		result = NumberA / NumberB;
		return result;
	}

}
