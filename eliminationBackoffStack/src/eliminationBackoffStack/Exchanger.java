package eliminationBackoffStack;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;



class Exchanger<T> {
  AtomicStampedReference<T> slot;
  static final int EMPTY = 0;
  static final int WAITING = 1;
  static final int BUSY = 2;
  // slot: stores value and stamp
  // EMPTY: slot has no value.
  // WAITING: slot has 1st value, waiting for 2nd.
  // BUSY: slot has 2nd value, waiting to be empty.

  public Exchanger() {
    slot = new AtomicStampedReference<>(null, 0);
  }


  public T exchange(T y, long timeout, TimeUnit unit)
    throws TimeoutException {
    long w = unit.toNanos(timeout); 
    long W = System.nanoTime() + w; 
    int[] stamp = {EMPTY};
    while (System.nanoTime() < W) { 
      T x = slot.get(stamp); 
      switch (stamp[0]) {    
        case EMPTY:    
        if (addA(y)) { 
          while (System.nanoTime() < W)            
            if ((x = removeB()) != null) return x; 
          throw new TimeoutException(); 
        }
        break;
        case WAITING:   
        if (addB(x, y)) 
          return x;     
        break;
        case BUSY: 
        break;     
        default:
      }
    }
    throw new TimeoutException(); 
  }


  private boolean addA(T y) { 
    return slot.compareAndSet(null, y, EMPTY, WAITING);
  }


  private boolean addB(T x, T y) { 
    return slot.compareAndSet(x, y, WAITING, BUSY);
  }


  private T removeB() {
    int[] stamp = {EMPTY};
    T x = slot.get(stamp);             
    if (stamp[0] != BUSY) return null; 
    slot.set(null, EMPTY); 
    return x;              
  }
}