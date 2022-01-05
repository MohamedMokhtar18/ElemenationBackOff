package eliminationBackoffStack;


public interface ConcurrentStack <T> {

    public void push(T item) throws InterruptedException ;

    public T pop() throws NullPointerException, InterruptedException ;
    }
    
