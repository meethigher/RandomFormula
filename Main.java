package calculator;

import java.util.HashSet;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		int numberTotal;// 运算数数量
		int formulaTotal;// 运算式数量
		int numberRange;// 数字范围
		int maxResult;//运算结果最大值
		boolean includeMulAndDiv;// 包含乘除
		boolean includeNegNum;// 包含负数
		boolean printFormulas;//是否输出算式
		boolean printAnswers;//是否输出答案
		RandomFormula randomFormula;
		Scanner input=new Scanner(System.in);
		System.out.print("请选择使用默认配置，还是自定义配置？1(默认配置) or 0(自定义配置) ");
		if(input.nextInt()==1) {
			randomFormula=new RandomFormula();
		}else {
			System.out.print("\n请输入运算数的数量 ");
			numberTotal=input.nextInt();
			System.out.print("\n请输入算式的数量 ");
			formulaTotal=input.nextInt();
			System.out.print("\n请输入数字范围 ");
			numberRange=input.nextInt();
			System.out.print("\n请输入运算结果允许的最大值 ");
			maxResult=input.nextInt();
			System.out.print("\n请输入运算规则，1(加减乘除) or 0(加减) ");
			includeMulAndDiv=input.nextInt()==1?true:false;
			System.out.print("\n请输入是否包含负数，1(是) or 0(否) ");
			includeNegNum=input.nextInt()==1?true:false;
			randomFormula=new RandomFormula(numberTotal, formulaTotal, numberRange, maxResult,includeMulAndDiv,
			includeNegNum);
		}
		HashSet<String> set=randomFormula.generateFormulas();
		System.out.println("\n生成的四则运算如下：");
		for(String value:set) {
			System.out.println(value+"=");
		}
		System.out.print("\n请输入是否打印算式，1(是) or 0(否) ");
		printFormulas=input.nextInt()==1?true:false;
		if(printFormulas) {
			System.out.println("算式result.txt已经保存到"+randomFormula.outputFormulas(set));
		}
		System.out.print("\n请输入是否打印答案，1(是) or 0(否)");
		printAnswers=input.nextInt()==1?true:false;
		if(printAnswers) {
			int[] arr=randomFormula.generateAnswers(set);
			System.out.println("答案answer.txt已经保存到"+randomFormula.outputAnswers(arr));
		}
		System.out.println("\n《《《《==== 程序结束！====》》》》");
		input.close();
	}
}
