# An improved implementation of the elimination-backoff stack algorithm
## The elimination-backoff stack
Danny Hendler, Nir Shavit and Lena Yerushalmi, also proposed a solution to the sequential
bottleneck problem by introducing the elimination-backoff stack . The eliminationbackoff stack uses the lock-free stack as a framework but has an altered backoff scheme.
It uses elimination, a method to reduce contention at the top of the stack, based upon the
notion that push and pop operations are able to cancel each other out. The correctness
of elimination revolves around the fact that if two threads push and pop sequentially, the
state of the stack is not altered. Therefore they do not need the stack and can simply
exchange values. When a thread backoffs from the central stack due to contention, it will
try to exchange values in an elimination array, an array implementation consisting of lockfree exchangers. A lock-free exchanger is an object that provides a way for two threads to
exchange values. When two threads meet in a lock-free exchanger an event occurs which
is referred to as a collision. If both colliding threads perform opposite operations (one is a
pop and the other a push), the popping thread returns the pusher’s value and the pushing
thread gets a dummy value signalling a successful elimination. Since neither of the threads
needs the central stack anymore, contention at the top of the stack is reduced. Since
this algorithm only alters the backoff mechanic, it maintains good throughput under low
workloads of the lock-free stack. At the time of publication, this feature was not achieved
by other concurrent stack implementations performing well under high workloads, making
this the first robust and scalable solution with relatively low overhead per operation 

## Improvements to the elimination-backoff stack
The implementation of the elimination-backoff stack by Danny Hendler, Nir Shavit and
Lena Yerushalmi is not highly optimised with regards to performance , because of its
modular nature. Major areas of improvement are:
* Handling unequal distributions of push and pop operations. The elimination-backoff
stack’s performance decays rapidly as the number of pops and pushes unbalances,
this is due to successful eliminations requiring a pop and a push operation. This
shortcoming is addressed by Gal Bar-Nissan, Danny Hendler and Adi Suissa in [2]
where they present the Dynamic Elimination-Combining Stack algorithm (DECS).
This algorithm uses a mechanic which also allows the elimination of operations of

the same type, reducing the contention on the top of the stack even further and
outperforming the elimination-backoff stack by a significant margin.
* Designing an efficient policy to manage access to the elimination array. This is given
by Danny Hendler, Nir Shavit and Lena Yerushalmi in , as a topic that needs
further research and is yet to be addressed.
* Operations of the same type colliding (e.g. two push operations colliding). This
point of improvement is where this paper will be focusing on. When operations of
the same type collide the values are exchanged and only when the threads are done
executing the exchange operation, it is verified that the collision was performed
correctly. If an incorrect collision occurs, the entire operation has to be reversed,
wasting clock cycles. In this thesis, an altered implementation of the eliminationbackoff stack is introduced, an implementation in which threads executing the same
operation can not collide.
