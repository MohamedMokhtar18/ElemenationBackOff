package eliminationBackoffStack;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;



class ImprovedEliminationBackoffStack<T> extends LockFreeStack<T> {
	 static final int capacity = 8 ;
	EliminationArray<T> eliminationArray = new EliminationArray <>(capacity -1 ) ;
	 static ThreadLocal<RangePolicy> policy = new ThreadLocal <>() {
	 protected synchronized RangePolicy initialValue ( ) {
	 return new RangePolicy ( capacity - 1 ) ;
	 }
	 };
	 @Override
	  public void push (T value ) {
	  RangePolicy rangePolicy = policy . get ( ) ;
	  Node<T> node = new Node<>(value ) ;
	 while ( true ) {
	  if ( tryPush ( node ) ) {
	  break ;
	  } else try {
		  eliminationArray . visit ( value , rangePolicy. getRange ( ) ) ;
		  rangePolicy . recordEliminationSuccess ( ) ;
	  break ;
	  } catch ( TimeoutException ex ) {
		  rangePolicy .recordEliminationTimeout(); ;
	  }
	  }
	  }
	 @Override
	  public T pop ( ) throws NullPointerException {
	 RangePolicy rangePolicy = policy . get ( ) ;
	  while ( true ) {
	  Node<T> returnNode = tryPop ( ) ;
	  if ( returnNode != null ) {
	  return returnNode . value ;
	  } else try {
	  T otherValue = eliminationArray . visit ( null , rangePolicy . getRange ( ) ) ;
	  rangePolicy . recordEliminationSuccess ( ) ;
	  return otherValue ;
	  } catch ( TimeoutException ex ) {
		  rangePolicy .recordEliminationTimeout(); ;
	  }
	   }
	   }
	   }