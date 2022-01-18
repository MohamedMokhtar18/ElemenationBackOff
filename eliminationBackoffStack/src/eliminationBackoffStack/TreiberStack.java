package eliminationBackoffStack;

import java.util.concurrent.atomic.AtomicReference;

public class TreiberStack<T> implements ConcurrentStack<T> {
    AtomicReference<Node<T>> top = new AtomicReference<Node<T>>();

	@Override
	public void push(T item) throws InterruptedException {
        Node<T> newHead = new Node<T>(item);
        Node<T> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));		
	}

	@Override
	public T pop() throws NullPointerException, InterruptedException {
		Node<T> oldHead;
        Node<T> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null)
                return null;
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.value;
	}

}
