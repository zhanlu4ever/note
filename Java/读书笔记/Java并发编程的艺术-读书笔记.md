# Java并发编程的艺术-读书笔记

---

## 第一章：并发编程的挑战
#### 1.1 上下文切换
* 任务从保存到再加载的过程是一次上下文切换
* 多线程只有在达到一定的数量级时才会表现出效率高效
* 因为切换上下文本身也是很耗时的操作，因此量级小时反而会比单线程速度要慢上很多

**vmstat**
**Lmbench3**

* 减少上下文切换的方法
	* 无锁并发编程
	* CAS算法
	* 使用最少线程
	* 协程：在单线程里实现多任务的调度，并在单线程里维持多个任务间的切换

* 有一个减少上下文切换的分析
#### 1.2 死锁
* 死锁代码演示

---
	public class DeadLockDemo {
	
		private static Object lock1 = new Object();
		private static Object lock2 = new Object();
	
		public static void main(String[] args) {
			deadLock();
		}
	
		public static void deadLock() {
			Thread t1 = new Thread(new Runnable() {
				public void run() {
					synchronized (lock1) {
						try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
						synchronized (lock2) {
							System.out.println("t1");
						}
					}
				}
			});
	
			Thread t2 = new Thread(new Runnable() {
				public void run() {
					synchronized (lock2) {
						try {Thread.sleep(2000);} catch (InterruptedException e) {e.printStackTrace();}
						synchronized (lock1) {
							System.out.println("t2");
						}
					}
				}
			});
			
			t1.start();
			t2.start();
		}
	}

* 分析死锁
	> jps

	> jstack process_num

	![](http://i.imgur.com/KSUkuXW.png)

* 避免死锁的几种方式
	* 避免一个线程同时获取多个锁
	* 避免一个线程在锁内同时占用多个资源，尽量保证每个锁只占用一个资源
	* 尝试使用定时锁，使用lock.tryLock(timeout)来替代使用内部锁机制
	* 对于数据库锁，加锁和解锁必须在一个数据库连接里，否则会出现解锁失败的情况

#### 1.3 资源限制
* 并发编程时需要考虑的系统资源
	* 硬件方面
		* 带宽上传和下载
		* 硬盘读写速度
		* CPU处理速度
	* 软件方面
		* 数据库连接数
		* socket连接数
* 资源限制所带来的问题
	* 由于资源限制而使并发程序串行执行时，会增加系统负担，使程序低效，因为上下文切换和资源调度需要消耗时间
* 如何解决
	* 硬件方面可以使用集群，不同的计算分配到不同的机器上去执行，数据ID%机器数
	* 软件方面可以使用数据库连接池，socket连接复用
#### 1.4 个人想法
* 不需要使用并发时就不要使用并发，不要为了并发而并发
* 在生成并发数时，是否可以获取系统相关资源情况来动态合理地给出一个并发数
* 避免出现因为资源不够而导致的无效并发
* 需要一个公式或策略，如多少颗CPU，每个CPU几核，来计算并发线程数
* 需要搜索相关公式

## 第二章：Java并发机制的底层实现原理
> Java的并发机制依赖于JVM的实现和CPU指令

#### 2.1 volatile的应用
* 可见性：当一个线程修改一个**共享变量**时，另外一个线程能读到这个修改的值
* volatile使用得当其效率要高于synchronized，因为不需要线程切换上下文和调度
* 一个**字段**被声明成volatile，Java线程内存模型确保所有线程看到这个变量的值是**一致的**
* volatile如何保证的内存可见性
	* 使用volatile修饰的变量在编译时会多加一条指令
	* lock add1 $0x0
	* lock指令在多核处理器下会引发两件事情
		* 将当前处理器缓存行的数据写回到系统内存
		* 写回内存的操作会使在其他CPU里缓存了该内存地址的数据无效
	* 多处理器下，为保证各个处理器的缓存是一致的，会实现**缓存一致性**的协议，**每个处理器通过嗅探在总线上传播的数据**来检查自己缓存的值是不是过期了，当处理器发现自己缓存行对应的内存地址被修改，就会将当前处理器的缓存行设置成无效状态，因此当处理器对这个数据进行修改操作的时候，会重新从系统内存中把数据读到处理器缓存里
* volatile两条实现原则
	* Lock前缀指令会引起处理器缓存回写到内存
	* 一个处理器的缓存回写到内存会导致其他处理器的缓存无效

#### 2.2 synchronized的实现原理与应用
* synchronized实现原理
	* JVM基于进入和退出Monitor对象来实现方法同步和代码块同步，两者实现细节不一致
	* 代码块同步使用**monitorenter**和**monitorexit**指令实现
	* **monitorenter**指令是在编译后插入到**同步代码块的开始位置**
	* **monitorexit**是插入到**方法结束处和异常处**
	* JVM保证每个monitorenter必须有对应的monitorexit与之配对
	* 任何对象都有一个**Monitor对象与之关联**，且当一个**Monitor被持有后将处于锁定状态**
	* 线程执行到monitor指令时将会尝试获取对象所对应的monitor的所有权，即尝试获得对象的锁
* Java对象头
	* synchronized使用的锁存储在Java对象头中
	* 若对象是数组类型，JVM使用3个字宽存储对象头
	* 若对象是非数组类型，JVM使用2个字宽存储对象头,1个字宽等于4个字节，32bit
	* 对象头包含Mark Word和Class Metadata Address，如果是数组类型还包含Array Length
	* Mark Word包含信息如下
		* 对象的HashCode
		* 分代年龄
		* 锁标记位
#### 2.3 锁的升级与对比
* JDK1.6中锁有4种状态，级别从低到高分别是,无锁状态，偏向锁状态，轻量级锁状态，重量级锁状态
* 锁可以升级但是不能降级
* 偏向锁
	* 为让线程获得锁的代价更低
	* 线程访问同步块获得锁时会在对象头和帧栈中的锁记录里存储锁偏向的线程id
	* 以后该线程在进入和退出同步块时不需要进行CAS操作来加锁和解锁
	
	* 偏向锁使用一种等到竞争出现才释放锁的机制
* 轻量级锁

#### 2.4 原子操作的实现原理
* 多处理器实现原子操作的原理
	* 使用缓存加锁或总线加锁来实现多处理器之间的原子操作
	* 处理器自动保证基本的内存操作的原子性，处理器保证从系统内存中读取或者写入一个字节是原子的，即当一个处理器读取一个字节时其他处理器不能访问这个字节的内存地址
* 使用总线锁保证原子性
	* 使用处理器提供的一个LOCK # 信号，当一个处理器在总线上输出此信号时，其他处理器的请求将被阻塞住，那么该处理器可以独占共享内存
	* 把CPU和内存之间的通信锁住了，使得锁定期间其他处理器不能操作其他内存地址的数据，开销比较大
* 使用缓存锁保证原子性
	* 同一时刻只需保证对某个内存地址的操作是原子性即可
	* 缓存锁定是指内存区域如果被缓存在处理器的缓存行中，并且在LOCK操作期间被锁定，在执行锁操作回写到内存时，处理器不再总线上声言LOCK #信号，而是修改内部的内存地址，并允许它的缓存一致性机制来保证操作的原子性
* CAS

## 第三章：Java内存模型
* 并发编程处理的两个关键问题，线程之间如何通信，线程之间如何同步
* Java采用共享内存模型
* Java中所有实例域，静态域和数组元素都存储在堆内存中，堆内存在线程之间共享
* 局部变量，方法定义参数，异常处理器参数不会在线程之间共享，不会有内存可见性问题且不受内存模型的影响
#### 3.1 指令重排序
* 有三种重排序
	* 编译器优化的重排序
	* 指令级并行的重排序
	* 内存系统的重排序
* 执行顺序
	* 源代码->编译器优化重排->指令级并行重排->内存系统重排->最终执行的指令序列
* 插入内存屏障可以组织一定的重排

## 第四章：Java并发编程基础

#### 4.1 获取线程信息
	public static void main(String[] args) {
		// 获取Java线程管理MXBean
		ThreadMXBean mxBean = ManagementFactory.getThreadMXBean();

		// 不需要获取同步的monitor和synchronized信息,仅获取线程和线程堆栈信息
		ThreadInfo[] threadInfos = mxBean.dumpAllThreads(false, false);

		for (ThreadInfo threadInfo : threadInfos) {
			System.out.println(threadInfo.getThreadId() + ":" + threadInfo.getThreadName());
		}
	}
#### 4.2 线程的状态
* 线程状态表
	<table>
		<tr>
			<td>状态名称</td>
			<td>说明</td>
		</tr>
		<tr>
			<td>NEW</td>
			<td>初始状态，线程被构建，但是还没有调用start()方法</td>
		</tr>
		<tr>
			<td>RUNNABLE</td>
			<td>运行状态，Java线程将操作系统中的就绪和运行两种状态笼统地称作“运行中”</td>
		</tr>
		<tr>
			<td>BLOCKED</td>
			<td>阻塞状态，表示线程阻塞于锁</td>
		</tr>
		<tr>
			<td>WAITING</td>
			<td>等待状态，表示线程进入等待状态，即当前线程需要等待其他线程做出一些特定动作(通知或中断)</td>
		</tr>
		<tr>
			<td>TIME_WAITING</td>
			<td>超时等待状态，该状态不同于WAITING，它是可以在指定的时间自行返回的</td>
		</tr>
		<tr>
			<td>TERMINATED</td>
			<td>终止状态，表示当前线程已经执行完毕</td>
		</tr>
	</table>

#### 4.3 管道输入/输出流
* PipedOutputStream
* PipedInputStream
* PipedReader
* PipedWriter

* 管道主要用于线程之间的数据传输，传输的媒介是内存
* Piped类型的流在使用时必须先绑定，通过connect()方法，不进行绑定会抛出异常

---
	public class PipedDemo1 {
	
		public static void main(String[] args) throws IOException {
			PipedWriter out = new PipedWriter();
			PipedReader in = new PipedReader();
	
			// 不进行连接会抛出 		java.io.IOException: Pipe not connected
			out.connect(in);
	
			Thread printThread = new Thread(new PrintTask(in), "print-task");
			printThread.start();
	
			int receive = 0;
	
			try {
				while ((receive = System.in.read()) != -1) {
					out.write(receive);
				}
			} finally {
				out.close();
			}
		}
	
		static class PrintTask implements Runnable {
			private PipedReader in;
	
			public PrintTask(PipedReader in) {
				this.in = in;
			}
	
			public void run() {
				int receive = 0;
				try {
					while ((receive = in.read()) != -1) {
						System.out.println((char) receive);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}