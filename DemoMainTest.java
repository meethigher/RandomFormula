//package calculator;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.util.HashSet;
//import java.util.Random;
//import java.util.Scanner;
//import java.util.Stack;
//
//class RandomFormula {
//	private int numberTotal;// ����������
//	private int formulaTotal;// ����ʽ����
//	private int numberRange;// ���ַ�Χ
//	private boolean includeMulAndDiv;// �����˳�
//	private boolean includeNegNum;// ��������
//
//	public RandomFormula() {
//		this.numberTotal = 2;
//		this.formulaTotal = 10;
//		this.numberRange = 100;
//		this.includeMulAndDiv = false;
//		this.includeNegNum = false;
//	}
//
//	public RandomFormula(int numberTotal, int formulaTotal, int numberRange, boolean includeMulAndDiv,
//			boolean includeNegNum) {
//		this.numberTotal = numberTotal;
//		this.formulaTotal = formulaTotal;
//		this.numberRange = numberRange;
//		this.includeMulAndDiv = includeMulAndDiv;
//		this.includeNegNum = includeNegNum;
//	}
//
//	/**
//	 * ��������������
//	 * 
//	 * @param num
//	 * @return
//	 */
//	public String isNegNum(int num) {
//		if (num < 0) {
//			return "(" + num + ")";
//		}
//		return "" + num;
//	}
//
//	/**
//	 * ��ȡ�����
//	 * 
//	 * @return ����һ��ָ����Χ�ڵ�����
//	 */
//	public int getRandomNumber() {
//		Random rand = new Random();
//		if (this.includeNegNum) {
//			return (rand.nextInt(this.numberRange) + 1) * (rand.nextDouble() > 0.5 ? 1 : -1);
//		} else {
//			return rand.nextInt(this.numberRange) + 1;
//		}
//	}
//
//	/**
//	 * �õ�һ������������
//	 * 
//	 * @return���������
//	 */
//	public String getRandomOperator() {
//		Random rand = new Random();
//		String[] operations = { "+", "-", "*", "/" };
//		return operations[rand.nextInt((this.includeMulAndDiv == true) ? 4 : 2)];
//	}
//
//	/**
//	 * ��ʽ��ֵ
//	 * 
//	 * @return
//	 */
//	public int compute(Stack<String> opOperators, Stack<Integer> opNumbers) {
//		int num2 = opNumbers.pop();
//		int num1 = opNumbers.pop();
//		String _op = opOperators.pop();
//		int result = 0;
//		switch (_op) {
//		case "+":
//			result = num1 + num2;
//			break;
//		case "-":
//			result = num1 - num2;
//			break;
//		case "*":
//			result = num1 * num2;
//			break;
//		case "/":
//			result = num1 / num2;
//			break;
//		}
//		return result;
//	}
//
//	/**
//	 * �Ƚ��������ȼ�
//	 * 
//	 * @return
//	 */
//	public int compare(String operator1, String operator2) {
//		int res = 0;
//		switch (operator1) {
//		case "+":
//		case "-":
//			if (operator2.equals("+") || operator2.equals("-") || operator2.equals("*") || operator2.equals("/")) {
//				res = 1;
//			} else {
//				res = -1;
//			}
//			break;
//		case "*":
//		case "/":
//			if (operator2.equals("*") || operator2.equals("/")) {
//				res = 1;
//			} else {
//				res = -1;
//			}
//			break;
//		}
//		return res;
//	}
//
//	/**
//	 * ������ʽ
//	 * 
//	 * @return ������ʽ
//	 */
//	public String generateFormula() {
//		String formula = "";
//		for (int i = 0; i < this.numberTotal; i++) {
//			if (i >= this.numberTotal - 1) {
//				formula += isNegNum(this.getRandomNumber());
//				continue;
//			}
//			formula += isNegNum(this.getRandomNumber()) + " " + this.getRandomOperator() + " ";
//		}
//		return formula;
//	}
//
//	/**
//	 * ������ʽ����
//	 * 
//	 * @return
//	 */
//	public HashSet<String> generateFormulas() {
//		HashSet<String> set = new HashSet<String>();
//		while (set.size() <= this.formulaTotal) {
//			set.add(this.generateFormula());
//		}
//		return set;
//	}
//
//	
//
//	/**
//	 * ������ʽ���
//	 * 
//	 * @param formula
//	 * @return
//	 */
//	public int generateAnswer(String formula) {
//		int length = 0;
//		String[] formulaArr = formula.split(" ");
//		String operators = "+-*/";
//		Stack<Integer> opNumbers = new Stack<Integer>();
//		Stack<String> opOperators = new Stack<String>();
//		opOperators.add("#");
//		while (length < formulaArr.length) {
//			String op = formulaArr[length++];
//			if (operators.indexOf(op) > -1) {// ���������,�ж����ȼ�
//				String sign = opOperators.peek();
//				int priority = compare(op, sign);// Ҫ��ջ�ĸ�ջ�������
//				if (priority >= 0) {// ���Ҫ��ջ��������߻������,��ջ��������,��֮ǰ�������,�����,��������ջ,���ַ���ջ
//					opNumbers.add(compute(opOperators, opNumbers));
//					opOperators.add(op);
//				} else {// ��ջ��������ȼ��ͣ�ֱ����ջ
//					opOperators.add(op);
//				}
//				continue;
//			}
//			// ��������,����ջ
//			opNumbers.add(Integer.parseInt(op.replace("(", "").replace(")", "")));
//		}
//		while (opOperators.peek() != "#") {
//			opNumbers.add(compute(opOperators, opNumbers));
//		}
//		return opNumbers.pop();
//	}
//
//	/**
//	 * ������ʽ�������
//	 * 
//	 * @param set
//	 * @return
//	 */
//	public int[] generateAnswers(HashSet<String> set) {
//		int[] arr = new int[set.size()];
//		int i = 0;
//		for (String str : set) {
//			arr[i++] = generateAnswer(str);
//		}
//		return arr;
//	}
//	
//	
//	/** �����ʽ���ļ�
//	 * @param set
//	 * @return
//	 */
//	public String outputFormulas(HashSet<String> set) {
//		File file=new File("result.txt");
//		try {
//			FileWriter fw = new FileWriter(file);
//			for (String str : set) {
//				fw.write(str + "\n");
//			}
//			fw.close();
//		} catch (Exception e) {
//			System.out.println("Error" + e.getMessage());
//			System.exit(0);
//		}
//		return file.getAbsolutePath();
//	}
//	
//	/** ����𰸵��ļ�
//	 * @param arr
//	 * @return
//	 */
//	public String outputAnswers(int[] arr) {
//		File file=new File("answer.txt");
//		try {
//			FileWriter fw = new FileWriter(file);
//			for (int i = 0; i < arr.length; i++) {
//				fw.write(arr[i]+"\n");
//			}
//			fw.close();
//		} catch (Exception e) {
//			System.out.println("Error" + e.getMessage());
//			System.exit(0);
//		}
//		return file.getAbsolutePath();	
//	}
//}
//
//public class DemoMainTest {
//	public static void main(String[] args) {
//		int numberTotal;// ����������
//		int formulaTotal;// ����ʽ����
//		int numberRange;// ���ַ�Χ
//		boolean includeMulAndDiv;// �����˳�
//		boolean includeNegNum;// ��������
//		boolean printFormulas;//�Ƿ������ʽ
//		boolean printAnswers;//�Ƿ������
//		RandomFormula randomFormula;
//		Scanner input=new Scanner(System.in);
//		System.out.print("��ѡ��ʹ��Ĭ�����ã������Զ������ã�1(Ĭ������) or 0(�Զ�������) ");
//		if(input.nextInt()==1) {
//			randomFormula=new RandomFormula();
//		}else {
//			System.out.print("\n������������������ ");
//			numberTotal=input.nextInt();
//			System.out.print("\n��������ʽ������ ");
//			formulaTotal=input.nextInt();
//			System.out.print("\n���������ַ�Χ ");
//			numberRange=input.nextInt();
//			System.out.print("\n�������������1(�Ӽ��˳�) or 0(�Ӽ�) ");
//			includeMulAndDiv=input.nextInt()==1?true:false;
//			System.out.print("\n�������Ƿ����������1(��) or 0(��) ");
//			includeNegNum=input.nextInt()==1?true:false;
//			randomFormula=new RandomFormula(numberTotal, formulaTotal, numberRange, includeMulAndDiv,
//			includeNegNum);
//		}
//		HashSet<String> set=randomFormula.generateFormulas();
//		System.out.println("\n���ɵ������������£�");
//		for(String value:set) {
//			System.out.println(value+"=");
//		}
//		System.out.print("\n�������Ƿ��ӡ��ʽ��1(��) or 0(��) ");
//		printFormulas=input.nextInt()==1?true:false;
//		if(printFormulas) {
//			System.out.println("��ʽresult.txt�Ѿ����浽"+randomFormula.outputFormulas(set));
//		}
//		System.out.print("\n�������Ƿ��ӡ�𰸣�1(��) or 0(��)");
//		printAnswers=input.nextInt()==1?true:false;
//		if(printAnswers) {
//			int[] arr=randomFormula.generateAnswers(set);
//			System.out.println("��answer.txt�Ѿ����浽"+randomFormula.outputAnswers(arr));
//		}
//		input.close();
//	}
//}
