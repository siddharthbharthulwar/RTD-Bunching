package liveFeed;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class ScheduledExecutor {
    public void scheduleRegularly(Runnable task, LocalDateTime firstTime,
            Function<LocalDateTime, LocalDateTime> nextTime) {
        pendingTask = task;
        scheduleRegularly(firstTime, nextTime);
    }

    protected void scheduleRegularly(LocalDateTime firstTime,
            Function<LocalDateTime, LocalDateTime> nextTime) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                scheduleRegularly(nextTime.apply(firstTime), nextTime);
                pendingTask.run();
            }
        }, Date.from(firstTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private volatile Runnable pendingTask = null;
    
     
    public static void main(String[] args) throws IOException {
    	
  
    	new ScheduledExecutor().scheduleRegularly(() -> {
    		
        	FeedPoller poller;
			try {
				poller = new FeedPoller(840, 60);
	    		poller.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}, LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).withHour(6), thisTime -> thisTime.plusDays(1));
    }
}