# ElemenationBackOff
## Overviiew
backoff stack uses the lock-free stack as a framework but has an altered backoff scheme.
It uses elimination, a method to reduce contention at the top of the stack, based upon the
notion that push and pop operations are able to cancel each other out. The correctness
of elimination revolves around the fact that if two threads push and pop sequentially, the
state of the stack is not altered. Therefore they do not need the stack and can simply
exchange values. When a thread backoffs from the central stack due to contention, it will
try to exchange values in an elimination array, an array implementation consisting of lockfree exchangers. A lock-free exchanger is an object that provides a way for two threads to
exchange values. When two threads meet in a lock-free exchanger an event occurs which
is referred to as a collision. If both colliding threads perform opposite operations (one is a
pop and the other a push), the popping thread returns the pusher’s value and the pushing
thread gets a dummy value signalling a successful elimination. Since neither of the threads
needs the central stack anymore, contention at the top of the stack is reduced. Since
this algorithm only alters the backoff mechanic, it maintains good throughput under low
workloads of the lock-free stack. At the time of publication, this feature was not achieved
by other concurrent stack implementations performing well under high workloads, making
this the first robust and scalable solution with relatively low overhead per operation
