package eliminationBackoffStack;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;



class ImprovedLockFreeExchanger<T> {
	static final int EMPTY = 0 , WAITINGPOPPER = 1 , WAITINGPUSHER = 2 , BUSY =3 ;
	 AtomicStampedReference<T> slot = new AtomicStampedReference<>(null , 0 ) ;
  // slot: stores value and stamp
  // EMPTY: slot has no value.
  // WAITING: slot has 1st value, waiting for 2nd.
  // BUSY: slot has 2nd value, waiting to be empty.

  public ImprovedLockFreeExchanger() {
  }


  public T exchange(T myItem , long timeout , TimeUnit unit)
    throws TimeoutException {
	int status = myItem == null ? WAITINGPOPPER : WAITINGPUSHER;
	long nanos = unit.toNanos (timeout) ; 
	long timeBound = System . nanoTime ( ) + nanos ;
    int[] stampHolder = {EMPTY};
    while ( true ) {
    	if ( System . nanoTime ( ) > timeBound )
    	throw new TimeoutException ( ) ;
    T yrItem = slot.get ( stampHolder ) ;
    int stamp = stampHolder [ 0 ] ;
    switch ( stamp ) {
    case EMPTY:
    	if ( slot.compareAndSet ( yrItem , myItem , EMPTY, status ) ) {
    		while ( System.nanoTime ( ) < timeBound ) {
    			yrItem = slot.get ( stampHolder ) ;
    			if ( stampHolder [ 0 ] == BUSY) {
    				 slot.set( null , EMPTY) ;
    				 return yrItem ;
    			}
    		}
    		if ( slot. compareAndSet (myItem , null , status , EMPTY) ) {
    			throw new TimeoutException ( ) ;
    		}else {
    			yrItem = slot.get ( stampHolder ) ;
    			 slot.set (null , EMPTY) ;
    			 return yrItem ;
    		}
    	}
    	break ;
    case WAITINGPOPPER:
    	if (status != WAITINGPOPPER && slot.compareAndSet(yrItem,myItem ,WAITINGPOPPER, BUSY)) {
    		return yrItem ;
    	}
    	break ;
    case WAITINGPUSHER:
    	if ( status != WAITINGPUSHER && slot . compareAndSet ( yrItem , myItem ,WAITINGPUSHER, BUSY) ) {
    		return yrItem ;
    	}
    	break ;
    case BUSY:
    	break ;
    	}
    	
    }

    }

  

/*
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
  */
}