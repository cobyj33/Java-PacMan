package utilities;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
	private static boolean initialized = false;
	static ScheduledExecutorService scheduler;
	private Scheduler() {}
	
	public static void init() {
		if (!initialized) {
			initialized = true;
			scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
		}
	}
	
	public static void scheduleAtFixedRate(Runnable run, Runnable end, int delay, int loopTime, int cancelTime, TimeUnit unit) {
		Future<?> task = scheduler.scheduleAtFixedRate(run, delay, loopTime, unit);
		
		scheduler.schedule(() -> { 
			task.cancel(false);
			end.run();
		}
		, cancelTime, unit);
	}
	
	public static void scheduleAtFixedRate(Runnable run, int delay, int loopTime, int cancelTime, TimeUnit unit) {
		Future<?> task = scheduler.scheduleAtFixedRate(run, delay, loopTime, unit);
		
		scheduler.schedule(() -> { 
			task.cancel(false);
		}, cancelTime, unit);
	}
	
	public static void scheduleAtFixedDelay(Runnable run, Runnable end, int initialdelay, int delayLoopTime, int cancelTime, TimeUnit unit) {
		Future<?> task = scheduler.scheduleWithFixedDelay(run, initialdelay, delayLoopTime, unit);
		
		scheduler.schedule(() -> { 
			task.cancel(false);
			end.run();
		}, cancelTime, unit);
	}
	
	public static void scheduleAtFixedDelay(Runnable run, int initialdelay, int delayLoopTime, int cancelTime, TimeUnit unit) {
		Future<?> task = scheduler.scheduleWithFixedDelay(run, initialdelay, delayLoopTime, unit);
		
		scheduler.schedule(() -> { 
			task.cancel(false);
		}, cancelTime, unit);
	}
	
	public static void schedule(Runnable run, int time, TimeUnit unit) {
		scheduler.schedule(run, time, unit);
	}
	
	public static ScheduledExecutorService getThreadPool() {
		return scheduler;
	}
	
	
	
	
}
