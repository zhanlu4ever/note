# Effective Java
> 摘取精髓，助我腾飞

## 第二章 创建和销毁对象

### 第1条 考虑用静态工厂方法代替构造器
* 传统做法存在的问题
	* 构造函数不直观
	* 多参数时对每个参数的使用不够详细
* 作法&建议:
	* 提供一个公有的静态工厂方法返回类的实例
* 事例:Boolean类静态方法valueOf

		public static Boolean valueOf(boolean b) {
        	return (b ? TRUE : FALSE);
    	}
* 优点:
	* 静态方法有名称，可以顾名思义，便于记忆
	* 不必每次调用都创建一个新对象
	* 可以返回原类型的任何子类型的对象
	* 创建参数化类型实例时，使代码变得更加简洁
* 缺点
	* 类如果不含公有的或者受保护的构造器，就不能被子类化
	* 工厂方法与其他静态方法没有任何区别
		* Javadoc中不能和其他static方法区分开，没有构造函数的集中显示优点；但可以通过公约的命名规则来改善
	* 当存在大量的构造参数时不能很好的进行扩展,解决方案是使用构建器,第2条
* 补充:阅读服务提供者框架

-------------------------------------------------------------

### 第2条 遇到多个构造器参数时要考虑用构建器
* 传统做法存在的问题
	* 叠加构造器
		* 构造器中会包含很多不需要的参数
		* 当参数增多时客户端代码很难编写,参数颠倒等时会引起操作
		* 阅读困难
	* JavaBeans的setter方式
		* 构造过程被分配到了很多个调用中会导致对象状态不一致
		* 线程安全无法保证
* 作法&建议:
	* 不直接生成想要的对象，让客户端利用所有必要的参数调用构造器，得到builder对象
	* 客户端在builder对象上调用类似于setter的方法，设置每个相关的可选参数
	* 最后客户端调用build()方法来生成不可变对象
* 事例:
	
		/**
		 * 建造器模式
		 * 实例域字段为随机写的几个
		 * 代码遵照 effective java 书中进行编写
		 * @author haoc
		 *
		 */
		public class BuilderDemo {
			private int size;
			private int fat;
			private int sodium;
			private int settings;
			private int servings;
			private int calors;
			
			private BuilderDemo(Builder builder){
				int size = builder.size;
				int fat = builder.fat;
				int sodium = builder.sodium;
				int settings = builder.settings;
				int servings = builder.servings;
				int calors = builder.calors;
			}
			
			public static class Builder{
				private int size;
				private int fat;
				
				private int sodium = 0;
				private int settings = 0;
				private int servings = 0;
				private int calors = 0;
				
				public Builder(int size,int fat){
					this.size = size;
					this.fat = fat;
				}
				
				public Builder sodium(int val){
					sodium = val;
					return this;
				}
				
				public Builder settings(int val){
					settings = val;
					return this;
				}
				
				public Builder servings(int val){
					servings = val;
					return this;
				}
				
				public Builder calors(int val){
					calors = val;
					return this;
				}
				
				public BuilderDemo build(){
					return new BuilderDemo(this);
				}
			}
			
			public static void main(String[] args) {
				// 只有默认参数的建造器
				BuilderDemo builderDemo1 = new BuilderDemo.Builder(23, 34).build();
				
				// 加两个参数的建造器
				BuilderDemo builderDemo2 = new BuilderDemo.Builder(23, 34).servings(2).settings(3).build();
			}
		}
* 优点
	* 代码容易编写，易于阅读，编写灵活
	* 参数检查最好放在要build的对象的构造函数中，而非builder的构建过程中
	* 可以对参数加强约束条件,出错后抛出IllegalStateException并标识清哪里出错
* 缺点
	* 需要先创建构建器，在一些重性能的场景下会有些影响
	* 比重叠构造器要冗长,需要针对每个参数进行编写一个方法
* 补充
	* 如果类的构造器或静态工厂中具有多个参数，设计这种类时，Builder模式是不错的选择
	* 扩展阅读生成器设计模式

-------------------------------------------------------------

### 第3条 用私有构造器或者枚举类型强化Singleton属性
* 传统做法存在的问题
	* 需要自己保证线程安全，反序列化导致重新生成实例对象等问题
* 作法&建议:
	* 用enum实现单例
* 事例:
	
		public enum Singleton1 {
			INSTANCE;
			
			public void test(){
				System.out.println("test");
			}
		}

	* 调用，Singleton1.INSTANCE.test();
* 优点
	* 功能上与公有域方法相近,但是更加简洁
	* 无偿提供了序列化机制,可以防止多次实例化，在面对复杂的序列化和反射攻击时也依然可以保证
* 缺点
	* 
* 补充
	* 

-------------------------------------------------------------

### 第4条 通过私有构造器强化不可实例化的能力
* 作法:
	* 将类的构造函数私有化,让外界无法访问

-------------------------------------------------------------

### 第5条 避免创建不必要的对象
* 传统做法存在的问题
	* 频繁的创建对象,浪费性能
* 作法&建议:
	* 优先使用基本类型而不是装箱基本类型,当心无意识的自动装箱,会影响性能
	* 自行维护一个对象池(object pool)来避免创建对象并不是一种好做法,除非池中的对象非常重量级
* 事例:
	* 问题代码
	
			public class DateDemo2 {
	
				private final Date birthDate = new Date();
				
				public boolean isBadyBoomer(){
					Calendar calender = Calendar.getInstance(TimeZone.getTimeZone("CMT"));
					calender.set(1946, Calendar.JANUARY, 1,0,0,0);
					Date start = calender.getTime();
					calender.set(1965, Calendar.JANUARY, 1,0,0,0);
					Date end = calender.getTime();
					
					return birthDate.compareTo(start) >= 0 && birthDate.compareTo(end) < 0;
				}			
			}
		* isBadyBoomer()每次被调用时都会新建一个Calendar一个TimeZone和两个Date实例,这是没必要的

	* 改进后代码

			public class DateDemo2 {
	
				private final Date birthDate = new Date();
				
				private final static Date start;
				
				private final static Date end;
				
				static{
					Calendar calender = Calendar.getInstance(TimeZone.getTimeZone("CMT"));
					calender.set(1946, Calendar.JANUARY, 1,0,0,0);
					start = calender.getTime();
					calender.set(1965, Calendar.JANUARY, 1,0,0,0);
					end = calender.getTime();
				}
				
				public boolean isBadyBoomer(){
					return birthDate.compareTo(start) >= 0 && birthDate.compareTo(end) < 0;
				}							
			}
		* 改进后的代码做性能对比,在调用10000000次时优化后的时间是7,未优化的时间是69315
* 优点
	* 提升性能
* 补充
	* 扩展阅读适配器设计模式

-------------------------------------------------------------

### 第6条 消除过期的对象引用 
* 传统做法存在的问题
	* 持有着过期对象的引用,导致gc无法进行回收
	* 使用数组容易导致内存泄漏
	* 使用缓存容易导致内存泄漏
	* 监听器和回调容易导致内存泄漏
* 作法&建议:
	* 一旦对象引用过期,需要清空这些引用
	* 清除引用最好的作法是让包含该引用的变量结束其生命周期
	* 警惕:只要类是自己管理内存就需要警惕内存泄漏问题,只要元素被释放则该元素中包含的任何对象应用都应该被清除
	* 使用弱引用
* 事例:
	* 问题代码

			class Stack {
				private Object[] elements;
				private int size = 0;
				private static final int DEFAULT_INITAL_CAPACITY = 16;
				
				public Stack(){
					elements = new Object[DEFAULT_INITAL_CAPACITY];
				}
				
				public void push(Object e){
					ensureCapacity();
					elements[size ++] = e;
				}
				
				public Object pop(){
					if(size == 0){
						throw new EmptyStackException();
					}
					
					return elements[-- size];
				}
				
				private void ensureCapacity(){
					if(elements.length == size){
						elements = Arrays.copyOf(elements, 2 * size + 1);
					}
				}
			}
		* 问题原因:
			* 当栈先push后在pop,从栈中弹出来的对象将不会被当作垃圾回收,即使使用栈的程序不再引用这些对象
			* 栈内部维护着对这些对象的过期引用,即永远都不会被解除的引用
			* pop出一个元素之后,其实只是数组下标的移动,但是元素依然存在

	* 改进代码

			public Object pop(){
				if(size == 0){
					throw new EmptyStackException();
				}
				
				Object result = elements[-- size];
				
				// 将不用的对象置空
				elements[size] = null;
				
				return result;
			}
* 优点
	* 可以避免内存泄漏
	* 后期再被错误的使用时会抛出空指针
* 补充
	* 阅读一些常见的内存泄漏的写法

-------------------------------------------------------------

### 第7条 避免使用终结方法
* 终结方法存在的问题
	* 终结方法(finalizer)通常是不可预测的,也是很危险的,一般情况下是不必要的,会导致行为不稳定,降低性能,以及可移植性问题
	* 无法保证被及时执行,即何时执行是由JVM控制,会导致对象生存周期变长,甚至根本不会释放
	* Java语言规范不仅不保证终结方法会被及时执行,而且根本就不保证它们会被执行
	* 在终结方法中执行的代码抛出异常后,终结方法被中止,对象的状态将会被改变,其他程序使用时会导致不可预知的问题
	* 使用终结方法会降低性能
* 作法:
	* 不要依赖终结方法来更新重要的之久状态
	* 需要使用终结方法的时候自己提供一个显示的终止方法
	* try finally
* finalize存在的意义
	* 充当“safety net”的角色，避免对象的使用者忘记调用显式termination方法，尽管finalize方法的执行时间没有保证，但是晚释放资源好过不释放资源；此处输出log警告有利于排查bug
	* 用于释放native peer，但是当native peer持有必须要释放的资源时，应该定义显式termination方法 

## 第三章 对于所有对象都通用的对象

### 第8条 覆盖equals时请遵守通用约定
* 能不重写equals时就不要重写
	* 当对象表达的不是值，而是可变的状态时
	* 对象不需要使用判等时
	* 父类已重写，且满足子类语义
* 重写equals时需要遵循的语义
	* Reflexive（自反性）: x.equals(x)必须返回true（x不为null）
	* Symmetric（对称性）: x.equals(y) == y.equals(x)
	* Transitive（传递性）: x.equals(y) && y.equals(z) ==> x.equals(z)
	* Consistent（一致性）: 当对象未发生改变时，多次调用应该返回同一结果
	* x.equals(null)必须返回false
* 实现建议
	* 先用==检查是否引用同一对象，提高性能
	* 用instanceof再检查是否同一类型
	* 再强制转换为正确的类型
	* 再对各个域进行equals检查，遵循同样的规则
	* 确认其语义正确，编写测例
	* 重写equals时，同时也重写hashCode
	* ！重写equals方法，传入的参数是Object

-------------------------------------------------------------

### 第9条 覆盖equals时总要覆盖hashCode 
* 不覆盖带来的问题
	* 在基于hash的集合中使用时出错
* 语义
	* 一致性
	* 当两个对象equals返回true时，hashCode方法的返回值也要相同
* hashCode的计算方式
	* 要求：equals的两个对象hashCode一样，但是不equals的对象hashCode不一样
	* 取一个素数，例如17，result = 17
* 计算hashCode时，不重要的field（未参与equals判断）不要参与计算

-------------------------------------------------------------

### 第10条 始终要覆盖toString()
* 优点
	* 可读性，简洁，信息丰富

-------------------------------------------------------------

### 第11条 谨慎地覆盖clone
* 通则
	* 永远不要让客户去做任何类库能够替客户完成的事情 

-------------------------------------------------------------

### 第12条 考虑实现 Comparable
* 实现Comparable接口可以利用其有序性特点，提高集合使用/搜索/排序的性能
* Contact
	* sgn(x.compareTo(y)) == - sgn(y.compareTo(x))，当类型不对时，应该抛出ClassCastException，抛出异常的行为应该是一致的
	* transitive: x.compareTo(y) > 0 && y.compareTo(z) > 0 ==> x.compareTo(z) > 0
	* x.compareTo(y) == 0 ==> sgn(x.compareTo(z)) == sgn(y.compareTo(z))
	* 建议，但非必须：与equals保持一致，即 x.compareTo(y) == 0 ==> x.equals(y)，如果不一致，需要在文档中明确指出
* TreeSet, TreeMap等使用的就是有序保存，而HashSet, HashMap则是通过equals + hashCode保存
* 当要为一个实现了Comparable接口的类增加成员变量时，不要通过继承来实现，而是使用组合，并提供原有对象的访问方法，以保持对Contract的遵循

## 第四章 类和接口

### 第13条 使类和成员的可访问性最小化 
* 信息隐藏的重要原因
	* 可以有效地接触组成系统的各模块之间的耦合关系,使模块可以独立开发,测试,优化,使用理解和修改
* 作法:
	* 尽可能地使每个类或者成员不被外界访问
	* 若一个类可以被做成包级私有的就尽量做成是包级私有的,此时此类属于包的实现的一部分.
	* 实例域不能是公有的
* 注意
	* 暴漏final域要么是基本类型的值,要么是指向不可变对象的引用.若包含可变对象会导致灾难性的后果
	* 不可提供final的数组对外暴漏,存在安全漏洞

-------------------------------------------------------------

### 第14条 在公有类中使用访问方法而非公有域

-------------------------------------------------------------

### 第15条 使可变性最小化
* 使类不可变的五条规则
	* 不要提供任何会修改改变对象状态的方法
	* 保证类不会被扩展
	* 使所有的域都是final
	* 使所有的域都成为私有的
	* 确保对于任何可变组件的互斥访问

-------------------------------------------------------------

### 第16条 复合优先于继承

-------------------------------------------------------------

### 第17条 要么为继承设计,并提供文档说明,要么就禁止继承

-------------------------------------------------------------

### 第18条 接口优于抽象类

-------------------------------------------------------------

### 第19条 接口只用于定义类型
* 在接口中定义各种常量是对接口的不良使用模式

-------------------------------------------------------------

### 第20条 类层次优于标签类

-------------------------------------------------------------

### 第21条 用函数对象表示策略

-------------------------------------------------------------

### 第22条 优先考虑静态成员类

## 第五章

### 第23条 不要在新代码中使用原生态类型
* 
