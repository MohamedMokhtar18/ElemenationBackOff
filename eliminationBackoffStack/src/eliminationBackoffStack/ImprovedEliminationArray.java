package eliminationBackoffStack;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ImprovedEliminationArray<T> {
	private static final int duration = 1;
	ImprovedLockFreeExchanger<T>[] exchanger;
	Random random;

	public ImprovedEliminationArray(int capacity) {
		exchanger = (ImprovedLockFreeExchanger<T>[]) new ImprovedLockFreeExchanger[capacity];
		for (int i = 0; i < capacity; i++) {
			exchanger[i] = new ImprovedLockFreeExchanger<T>();
		}
		random = new Random();
	}

	public T visit(T value, int range) throws TimeoutException {
		int slot = random.nextInt(range);
		return (exchanger[slot].exchange(value, duration, TimeUnit.MILLISECONDS));
	}
}
