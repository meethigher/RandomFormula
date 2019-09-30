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
//	private int numberTotal;// 运算数数量
//	private int formulaTotal;// 运算式数量
//	private int numberRange;// 数字范围
//	private boolean includeMulAndDiv;// 包含乘除
//	private boolean includeNegNum;// 包含负数
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
//	 * 若负数，加括号
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
//	 * 获取随机数
//	 * 
//	 * @return 返回一个指定范围内的数字
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
//	 * 得到一个随机的运算符
//	 * 
//	 * @return返回运算符
//	 */
//	public String getRandomOperator() {
//		Random rand = new Random();
//		String[] operations = { "+", "-", "*", "/" };
//		return operations[rand.nextInt((this.includeMulAndDiv == true) ? 4 : 2)];
//	}
//
//	/**
//	 * 算式求值
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
//	 * 比较运算优先级
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
//	 * 生成算式
//	 * 
//	 * @return 返回算式
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
//	 * 生成算式集合
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
//	 * 生成算式结果
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
//			if (operators.indexOf(op) > -1) {// 若是运算符,判断优先级
//				String sign = opOperators.peek();
//				int priority = compare(op, sign);// 要入栈的跟栈顶的相比
//				if (priority >= 0) {// 如果要入栈的运算符高或者相等,出栈两个数字,和之前的运算符,计算后,将数字入栈,将字符入栈
//					opNumbers.add(compute(opOperators, opNumbers));
//					opOperators.add(op);
//				} else {// 入栈运算符优先级低，直接入栈
//					opOperators.add(op);
//				}
//				continue;
//			}
//			// 若是数字,则入栈
//			opNumbers.add(Integer.parseInt(op.replace("(", "").replace(")", "")));
//		}
//		while (opOperators.peek() != "#") {
//			opNumbers.add(compute(opOperators, opNumbers));
//		}
//		return opNumbers.pop();
//	}
//
//	/**
//	 * 生成算式结果数组
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
//	/** 输出算式到文件
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
//	/** 输出答案到文件
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
//		int numberTotal;// 运算数数量
//		int formulaTotal;// 运算式数量
//		int numberRange;// 数字范围
//		boolean includeMulAndDiv;// 包含乘除
//		boolean includeNegNum;// 包含负数
//		boolean printFormulas;//是否输出算式
//		boolean printAnswers;//是否输出答案
//		RandomFormula randomFormula;
//		Scanner input=new Scanner(System.in);
//		System.out.print("请选择使用默认配置，还是自定义配置？1(默认配置) or 0(自定义配置) ");
//		if(input.nextInt()==1) {
//			randomFormula=new RandomFormula();
//		}else {
//			System.out.print("\n请输入运算数的数量 ");
//			numberTotal=input.nextInt();
//			System.out.print("\n请输入算式的数量 ");
//			formulaTotal=input.nextInt();
//			System.out.print("\n请输入数字范围 ");
//			numberRange=input.nextInt();
//			System.out.print("\n请输入运算规则，1(加减乘除) or 0(加减) ");
//			includeMulAndDiv=input.nextInt()==1?true:false;
//			System.out.print("\n请输入是否包含负数，1(是) or 0(否) ");
//			includeNegNum=input.nextInt()==1?true:false;
//			randomFormula=new RandomFormula(numberTotal, formulaTotal, numberRange, includeMulAndDiv,
//			includeNegNum);
//		}
//		HashSet<String> set=randomFormula.generateFormulas();
//		System.out.println("\n生成的四则运算如下：");
//		for(String value:set) {
//			System.out.println(value+"=");
//		}
//		System.out.print("\n请输入是否打印算式，1(是) or 0(否) ");
//		printFormulas=input.nextInt()==1?true:false;
//		if(printFormulas) {
//			System.out.println("算式result.txt已经保存到"+randomFormula.outputFormulas(set));
//		}
//		System.out.print("\n请输入是否打印答案，1(是) or 0(否)");
//		printAnswers=input.nextInt()==1?true:false;
//		if(printAnswers) {
//			int[] arr=randomFormula.generateAnswers(set);
//			System.out.println("答案answer.txt已经保存到"+randomFormula.outputAnswers(arr));
//		}
//		input.close();
//	}
//}
