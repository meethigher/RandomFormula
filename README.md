### **要求：使用C或Java语言完成一个自动生成四则运算试题的程序**

软件基本功能如下。

1. 自动生成10道100以内的2个操作数的四则运算算式(+ - * /)，要求运算结果也在100以内
2. 剔除重复算式。2+3 和 2+3 是重复算式，2+3 和 3+2 不属于重复算式
3. 题目数量可定制
4. 相关参数可控制
   1. 是否包含乘法和除法
   2. 操作数数值范围可控
   3. 操作数是否含负数
5. 生成的运算题存储到外部文件result.txt中



#### **1. 需求分析**

	某小学里，老师让家长每天出30道加减法题目给孩子做。于是，想写一个小程序完成这件事。

#### **2. 功能设计**

- 基本功能
  - 算式去重可控
  - 题目数量可控
  - 乘除可控
  - 操作数范围可控
  - 操作数正负可控
  - 输出内容
- 扩展功能
  - 输出答案可控
  - 判断负数加括号
  - 可实现多数字加减乘除运算
  - 打印算式或者答案时，输出文件保存的绝对路径
  - 算式运算结果范围限制

#### **3. 设计实现**

java语言，单类完成所有功能。

##### 3.1 设置默认配置：

`RandomFormula`类的无参构造方法，用来设置默认值。`numberTotal`运算数数量设置为2，`formulaToal`运算式数量设置为10，`numberRange`数字范围设置为100，`maxResult`运算结果的最大范围限制，`includeMulAndDiv`包含乘除设置为false，`includeNegNum`设置为false。

```
public RandomFormula() {
		this.numberTotal = 2;
		this.formulaTotal = 10;
		this.numberRange = 100;
		this.maxResult = 10000;
		this.includeMulAndDiv = false;
		this.includeNegNum = false;
}
```



##### 3.2 设置自定义配置：

`RandomFormula`类的有参构造方法，用来根据需求初始化。

```
public RandomFormula(int numberTotal, int formulaTotal, int numberRange, int maxResult, boolean includeMulAndDiv,
			boolean includeNegNum) {
		this.numberTotal = numberTotal;
		this.formulaTotal = formulaTotal;
		this.numberRange = numberRange;
		this.maxResult = maxResult;
		this.includeMulAndDiv = includeMulAndDiv;
		this.includeNegNum = includeNegNum;
}
```



##### 3.3 产生随机运算数和随机运算符：

`getRandomNumber`方法用来产生随机数，`getRandomOperator`方法用来产生随机运算符。

```
/**
* 获取随机数
* 
* @return 返回一个指定范围内的数字
*/
public int getRandomNumber() {
	Random rand = new Random();
	if (this.includeNegNum) {
		return (rand.nextInt(this.numberRange) + 1) * (rand.nextDouble() > 0.5 ? 1 : -1);
	} else {
		return rand.nextInt(this.numberRange) + 1;
	}
}
/**
* 得到一个随机的运算符
* 
* @return返回运算符
*/
public String getRandomOperator() {
	Random rand = new Random();
	String[] operations = { "+", "-", "*", "/" };
	return operations[rand.nextInt((this.includeMulAndDiv == true) ? 4 : 2)];
}
```



##### 3.4 生成四则运算：

根据配置，`generateFormula`方法生成一道四则运算，如果`isNegNum`方法判断包含负数，则负数加括号。`generateFormulas`生成指定数量的四则运算，用HashSet来存储，可以自动去重算式。

```
/**
* 生成算式
* 
* @return 返回算式
*/
public String generateFormula() {
	String formula = "";
	for (int i = 0; i < this.numberTotal; i++) {
		if (i >= this.numberTotal - 1) {
			formula += isNegNum(this.getRandomNumber());
			continue;
		}
		formula += isNegNum(this.getRandomNumber()) + " " + this.getRandomOperator() + " ";
	}
	return formula;
}
/**
* 生成算式集合
* 
* @return
*/
public HashSet<String> generateFormulas() {
	HashSet<String> set = new HashSet<String>();
	while (set.size() <= this.formulaTotal) {
		String formula=this.generateFormula();
		if(this.maxResult>=this.generateAnswer(formula))
		set.add(formula);
	}
	return set;
}
/**
* 若负数，加括号
* 
* @param num
* @return
*/
public String isNegNum(int num) {
	if (num < 0) {
		return "(" + num + ")";
	}
	return "" + num;
}
```



##### 3.5 计算算式的值：

`generateAnswer`遍历HashSet的算式，`compare`判断运算符的优先级。根据**栈的“先进后出”的特性**，若是数字，则入栈；若是字符，判断优先级，优先级高则直接入栈；优先级低，则出栈数字和字符，进入`compute`计算，然后将结果入栈。如此，来计算算式的值。

```
/**
* 生成算式结果
* 
* @param formula
* @return
*/
public int generateAnswer(String formula) {
	int length = 0;
	String[] formulaArr = formula.split(" ");
	String operators = "+-*/";
	Stack<Integer> opNumbers = new Stack<Integer>();
	Stack<String> opOperators = new Stack<String>();
	opOperators.add("#");//字符栈中存储个#号，防止栈空
	while (length < formulaArr.length) {
		String op = formulaArr[length++];
		if (operators.indexOf(op) > -1) {// 若是运算符,判断优先级
			String sign = opOperators.peek();
			int priority = compare(op, sign);// 要入栈的跟栈顶的相比
			if (priority >= 0) {// 如果要入栈的运算符高或者相等,出栈两个数字,和之前的运算符,计算后,将数字入栈,将字符入栈
				opNumbers.add(compute(opOperators, opNumbers));
				opOperators.add(op);
			} else {// 入栈运算符优先级低，直接入栈
				opOperators.add(op);
			}
			continue;
		}
		// 若是数字,则入栈
		opNumbers.add(Integer.parseInt(op.replace("(", "").replace(")", "")));
	}
	while (opOperators.peek() != "#") {
		opNumbers.add(compute(opOperators, opNumbers));
	}
	return opNumbers.pop();
}
/**
* 比较运算优先级
* 
* @return
 */
public int compare(String operator1, String operator2) {
	int res = 0;
	switch (operator1) {
	case "+":
	case "-":
		if (operator2.equals("+") || operator2.equals("-") || operator2.equals("*") || operator2.equals("/")) {
			res = 1;
		} else {
			res = -1;
		}
		break;
	case "*":
	case "/":
		if (operator2.equals("*") || operator2.equals("/")) {
			res = 1;
		} else {
			res = -1;
		}
		break;
	}
	return res;
}
/**
* 算式求值
* 
* @return
*/
public int compute(Stack<String> opOperators, Stack<Integer> opNumbers) {
	int num2 = opNumbers.pop();
	int num1 = opNumbers.pop();
	String _op = opOperators.pop();
	int result = 0;
	switch (_op) {
	case "+":
		result = num1 + num2;
		break;
	case "-":
		result = num1 - num2;
		break;
	case "*":
		result = num1 * num2;
		break;
	case "/":
		result = num1 / num2;
		break;
	}
	return result;
}
```



##### 3.6 生成答案：

`generateAnswers`用来生成相应算式数量的值，用一维数组来存储。

```
/**
* 生成算式结果数组
* 
* @param set
* @return
*/
public int[] generateAnswers(HashSet<String> set) {
	int[] arr = new int[set.size()];
	int i = 0;
	for (String str : set) {
		arr[i++] = generateAnswer(str);
	}
	return arr;
}
```



##### 3.7 打印算式和答案：

`outputFormulas`和`outputAnswers`将算式和答案，分别保存到result.txt和answer.txt下，并且输出文件的绝对路径。

```
/** 输出算式到文件
* @param set
* @return
*/
public String outputFormulas(HashSet<String> set) {
	File file=new File("result.txt");
	try {
		FileWriter fw = new FileWriter(file);
		for (String str : set) {
			fw.write(str + "\n");
		}
		fw.close();
	} catch (Exception e) {
		System.out.println("Error" + e.getMessage());
		System.exit(0);
	}
	return file.getAbsolutePath();
}
	
/** 输出答案到文件
* @param arr
* @return
*/
public String outputAnswers(int[] arr) {
	File file=new File("answer.txt");
	try {
		FileWriter fw = new FileWriter(file);
		for (int i = 0; i < arr.length; i++) {
			fw.write(arr[i]+"\n");
		}
		fw.close();
	} catch (Exception e) {
		System.out.println("Error" + e.getMessage());
		System.exit(0);
	}
	return file.getAbsolutePath();	
}
```



#### **4. 测试运行**

##### 4.1 测试加减运算（true）

###### 4.1.1 测试两位加减运算（true）

![](https://img2018.cnblogs.com/blog/1791022/201909/1791022-20190929185434340-569607246.png)


###### 4.1.2 测试多位加减运算（true）

![](https://img2018.cnblogs.com/blog/1791022/201909/1791022-20190929185455646-1744954958.png)



##### 4.2 测试加减乘除运算（true）

![](https://img2018.cnblogs.com/blog/1791022/201909/1791022-20190929185506485-479991316.png)



##### 4.3 测试去重（false）

如果组合的算式足够多的话，生成的算式是可以去重的。（true）

**但是**，如果输出20个加减算式，最大数为2的话，还要不重复，出现矛盾，待解决ing。（false）

##### 4.4 测试括号（true）

![](https://img2018.cnblogs.com/blog/1791022/201909/1791022-20190929185517268-1360019889.png)



##### 4.5 测试结果的最大范围（false）

正常的生成指定结果之内（图中是生成不超过300的运算式）的算式功能实现了。（true）

![](https://img2018.cnblogs.com/blog/1791022/201909/1791022-20190929185526885-533692959.png)



**但是**，如果要输出的算式结果最大数有限制，去重也有限制的话，在某些极限情况下，会出现矛盾。待解决ing（false）

#### **5. 总结**

**第一版总结**：感觉写的程序比较复杂，所有的功能在一个循环里完成了。感觉好多地方还可以封装一下，这样代码也会比较美观。

还有我感觉自己变量用的太多了，有点冗余。还有个问题，如果最大操作数是1，输出10个式子的话，肯定重复了啊。。。

算了，这个是第一版，之后慢慢改进。

**第二版总结**：第一版是用for循环执行一遍，执行完所有功能的。第二版将代码重新封装，优化了一下，界面上更加美观，而且也没有了那么多冗余的变量。每个要实现的功能都在各自的方法里实现的。main方法里实现的功能就是单纯调用方法，输入输出的功能。但是，还有好多bug未解决。

虽然自己编程还是很菜，但是我不会放弃的。努力ing。。。

![](https://img2018.cnblogs.com/blog/1791022/201909/1791022-20190910222129678-1977310389.gif)



#### **6. PSP**

|               PSP2.1                |      任务内容      | 计划完成需要时间（min) | 实际完成需要时间（min) |
| :---------------------------------: | :----------------: | :--------------------: | :--------------------: |
|            **Planning**             |      **计划**      |         **5**          |         **10**         |
|              Estimate               | 估计时间，规划步骤 |           5            |           10           |
|           **Development**           |      **开发**      |         **30**         |        **145**         |
|              Analysis               |      需求分析      |           5            |           20           |
|               Coding                |      具体编码      |           20           |          120           |
|                Test                 |        测试        |           5            |           5            |
|            **Reporting**            |      **报告**      |         **20**         |         **40**         |
| Postmortem&Process Improvement Plan |      总结改进      |           20           |           40           |