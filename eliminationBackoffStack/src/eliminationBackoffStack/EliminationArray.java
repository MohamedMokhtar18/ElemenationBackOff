package eliminationBackoffStack;

import java.util.*;
import java.util.concurrent.*;



class EliminationArray<T> {
  Exchanger<T>[] exchangers;
  final long TIMEOUT;
  final TimeUnit UNIT;
  Random random;
  // exchangers: array of exchangers
  // TIMEOUT: exchange timeout number
  // UNIT: exchange timeout unit
  // random: random number generator

  public EliminationArray(int capacity, long timeout, TimeUnit unit) {
    exchangers = new Exchanger[capacity];
    for (int i=0; i<capacity; i++)
      exchangers[i] = new Exchanger<>();
    random = new Random();
    TIMEOUT = timeout;
    UNIT = unit;
  }

  public T visit(T x) throws TimeoutException {
    int i = random.nextInt(exchangers.length);
    return exchangers[i].exchange(x, TIMEOUT, UNIT);
  }
}