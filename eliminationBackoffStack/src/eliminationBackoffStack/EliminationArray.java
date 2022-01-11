package eliminationBackoffStack;

import java.util.*;
import java.util.concurrent.*;

public class EliminationArray<T> {
	private static final int duration = 1;
	LockFreeExchanger<T>[] exchanger;
	Random random;

	public EliminationArray(int capacity) {
		exchanger = (LockFreeExchanger<T>[]) new LockFreeExchanger[capacity];
		for (int i = 0; i < capacity; i++) {
			exchanger[i] = new LockFreeExchanger<T>();
		}
		random = new Random();
	}

	public T visit(T value, int range) throws TimeoutException {
		int slot = random.nextInt(range);
		return (exchanger[slot].exchange(value, duration, TimeUnit.MILLISECONDS));
	}
}