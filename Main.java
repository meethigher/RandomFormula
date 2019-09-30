package calculator;

import java.util.HashSet;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		int numberTotal;// ����������
		int formulaTotal;// ����ʽ����
		int numberRange;// ���ַ�Χ
		int maxResult;//���������ֵ
		boolean includeMulAndDiv;// �����˳�
		boolean includeNegNum;// ��������
		boolean printFormulas;//�Ƿ������ʽ
		boolean printAnswers;//�Ƿ������
		RandomFormula randomFormula;
		Scanner input=new Scanner(System.in);
		System.out.print("��ѡ��ʹ��Ĭ�����ã������Զ������ã�1(Ĭ������) or 0(�Զ�������) ");
		if(input.nextInt()==1) {
			randomFormula=new RandomFormula();
		}else {
			System.out.print("\n������������������ ");
			numberTotal=input.nextInt();
			System.out.print("\n��������ʽ������ ");
			formulaTotal=input.nextInt();
			System.out.print("\n���������ַ�Χ ");
			numberRange=input.nextInt();
			System.out.print("\n��������������������ֵ ");
			maxResult=input.nextInt();
			System.out.print("\n�������������1(�Ӽ��˳�) or 0(�Ӽ�) ");
			includeMulAndDiv=input.nextInt()==1?true:false;
			System.out.print("\n�������Ƿ����������1(��) or 0(��) ");
			includeNegNum=input.nextInt()==1?true:false;
			randomFormula=new RandomFormula(numberTotal, formulaTotal, numberRange, maxResult,includeMulAndDiv,
			includeNegNum);
		}
		HashSet<String> set=randomFormula.generateFormulas();
		System.out.println("\n���ɵ������������£�");
		for(String value:set) {
			System.out.println(value+"=");
		}
		System.out.print("\n�������Ƿ��ӡ��ʽ��1(��) or 0(��) ");
		printFormulas=input.nextInt()==1?true:false;
		if(printFormulas) {
			System.out.println("��ʽresult.txt�Ѿ����浽"+randomFormula.outputFormulas(set));
		}
		System.out.print("\n�������Ƿ��ӡ�𰸣�1(��) or 0(��)");
		printAnswers=input.nextInt()==1?true:false;
		if(printAnswers) {
			int[] arr=randomFormula.generateAnswers(set);
			System.out.println("��answer.txt�Ѿ����浽"+randomFormula.outputAnswers(arr));
		}
		System.out.println("\n��������==== ���������====��������");
		input.close();
	}
}
