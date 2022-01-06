package Benchmarking;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.caliper.ConfiguredBenchmark;
import com.google.caliper.Scenario;
import com.google.caliper.SimpleBenchmark;
import eliminationBackoffStack.*;
public class StackBenchmark extends SimpleBenchmark {
boolean[]randomBools=new boolean[100];
	@Override
	public double bytesToUnits(long bytes) {
		// TODO Auto-generated method stub
		return super.bytesToUnits(bytes);
	}
	public void timeBackoffstack() {
		EliminationBackoffStack<Integer> backStack=new EliminationBackoffStack<>(1000);
		Random rd = new Random();
		for(int j=0;j<100;j++) {
			int value=rd.nextInt();
			if(randomBools[j]) {
		
					backStack.push(value);
				
			}else {
				
					backStack.pop();
				
			}
			
		}
		
		
	}
	public void timeImprovedBackoffstack() {
		ImprovedEliminationBackoffStack<Integer> backStack=new ImprovedEliminationBackoffStack<>();
		Random rd = new Random();
		for(int j=0;j<100;j++) {
			int value=rd.nextInt();
			if(randomBools[j]) {
		
					backStack.push(value);
				
			}else {
				
					backStack.pop();
				
			}
			
		}
	}
	public void timeLockFreeStack() {
		LockFreeStack<Integer> lockStack=new LockFreeStack<>();
		Random rd = new Random();
		for(int j=0;j<100;j++) {
			int value=rd.nextInt();
			if(randomBools[j]) {
		
				try {
					lockStack.push(value);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else {
				
				try {
					lockStack.pop();
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
	}
	@Override
	public ConfiguredBenchmark createBenchmark(Map<String, String> arg0) {
		// TODO Auto-generated method stub
		return super.createBenchmark(arg0);
	}

	@Override
	public Map<String, Integer> getInstanceUnitNames() {
		// TODO Auto-generated method stub
		return super.getInstanceUnitNames();
	}

	@Override
	public Map<String, Integer> getMemoryUnitNames() {
		// TODO Auto-generated method stub
		return super.getMemoryUnitNames();
	}

	@Override
	public Map<String, Integer> getTimeUnitNames() {
		// TODO Auto-generated method stub
		return super.getTimeUnitNames();
	}

	@Override
	public double instancesToUnits(long instances) {
		// TODO Auto-generated method stub
		return super.instancesToUnits(instances);
	}

	@Override
	public double nanosToUnits(double nanos) {
		// TODO Auto-generated method stub
		return super.nanosToUnits(nanos);
	}

	@Override
	public Scenario normalizeScenario(Scenario arg0) {
		// TODO Auto-generated method stub
		return super.normalizeScenario(arg0);
	}

	@Override
	public Set<String> parameterNames() {
		// TODO Auto-generated method stub
		return super.parameterNames();
	}

	@Override
	public Set<String> parameterValues(String arg0) {
		// TODO Auto-generated method stub
		return super.parameterValues(arg0);
	}

	@Override
	protected void setUp() throws Exception {
		Random rd = new Random();
     for(int i=0;i<randomBools.length;i++) {
    	 randomBools[i]=rd.nextBoolean();
}
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

}
