## 1 并发

### 1.1 Semaphore (信号量)

信号量的提出是为了解决同步不同执行线程问题.信号量是具有非负整数值的全局变量，只能由两种特殊的操作来处理，这两种操作称为P和V：

- $P(s)$: 如果s是非零的，那么P将s减一，并且立即返回。如果s为零，那么就挂起这个线程，直到s变为非零，而一个V操作会重启这个线程。在重启之后，P操作将s减1，并将控制返回给调用者。

- $V(s)$: V操作将s加1.如果有任何线程柱塞在P操作等待s变成非零，那么V操作会重启这些线程中的一个，然后该线程将s减1，完成它的P操作。

P和V的定义确保了一个正在运行的程序绝不可能进入这样一种状态, 也就是一个正确初始化了的信号量一个负值。这个属性称为信号量不变性(semaphore invariant).

### 1.2 使用信号量来实现互斥

信号量提供了一种很方便的方法来确保对共享变量的互斥访问。基本思想是将每个共享变量（或者一组相关的共享变量）与一个信号量s（初始为1）联系起来，然后用$P(s)$和$V(s)$操作将相应的临界区包围起来。


以这种方式来保护共享变量的信号量叫做二元信号量(binary semaphore) , 因为它的值总是0或者1 。以提供互斥为目的的二元信号量常常也称为互斥锁( mutex ) 。在一个互斥锁上执行P操作称为对互斥锁加 锁。类似地, 执行V操作称为对互斥锁解锁 对一个互斥锁加了锁但是还没有解锁的线程称为占用这个互斥锁。一个被用作一组可用资源的计数器的信号量被称为计数信号量。

### 1.3 生产者-消费者问题

生产者和消费者线程共享一个有n个槽的有限缓冲区。生产者线程反复地生成新地项目（item），并把它们插入到缓冲区中。消费者不断地从缓冲区取出这些项目，然后消费（使用）它们。也可能有多个生产者和消费者的变种。

因为插入和取出项目都涉及更新共享变扯, 所以我们必须保证对缓冲区的访问是互斥的。但是只保证互斥访问是不够的, 我们还需要调度对缓冲区的访问 。如果缓冲区是满的(没有空的槽位), 那么生产者必须等待直到有一个槽位变为可用。与之相似, 如果缓冲区是空的(没有可取用的项目), 那么消费者必须等待直到有一个项目变为可用。