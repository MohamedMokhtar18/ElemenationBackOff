package eliminationBackoffStack;

import java.util.concurrent.TimeoutException;

public class EliminationBackoffStack<T> extends LockFreeStack<T> implements
ConcurrentStack<T>{
	EliminationArray<T> eliminationArray ;
	static ThreadLocal<RangePolicy> policy ;

public EliminationBackoffStack ( int capacity) {
	eliminationArray = new EliminationArray <>(capacity - 1 ) ;
	policy = new ThreadLocal <>() {
 protected synchronized RangePolicy initialValue ( ) {
 return new RangePolicy ( capacity-1 ) ;
}
 } ;
 }

@Override
 public void push (T value ) {
 RangePolicy rangePolicy = policy . get ( ) ;
 Node<T> node = new Node<>(value ) ;
 while ( true ) {
 if ( tryPush ( node ) ) {
 return ;
 } else try {
 T otherValue = eliminationArray . visit
 ( value , rangePolicy . getRange ( ) ) ;
 if ( otherValue == null ) {
	 rangePolicy .recordEliminationSuccess();
 return ; // exchanged w i t h pop
 }
 } catch ( TimeoutException ex ) {
	 rangePolicy.recordEliminationTimeout();
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
 T otherValue =eliminationArray . visit ( null , rangePolicy . getRange ( ) ) ;
 if ( otherValue != null ) {
	 rangePolicy .recordEliminationSuccess();
 return otherValue ;
 }
 } catch ( TimeoutException ex ) {
	 rangePolicy.recordEliminationTimeout();
 }
 }
 }

}
