package net.catae.messagehub;

public class Scenario {	

	private String name;
	private Thread thread;
    private boolean actionFinished = false;
	private Exception lastException = null;

	public Scenario(String name, Action action) {
		this.name = name;
		this.thread = createThread(action);
	}

	private Thread createThread(Action action) {
		return new Thread(new Runnable(){		
			@Override
			public void run() {
                try {
                    action.run();
                    done(null);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    done(ex);
                }				
			}
		});
	}

	public static Scenario create(String name, Action action) {
		return new Scenario(name, action);	
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void start() {
		thread.start();
	}
	
	public Object getStats() {
		return null;
	}
	
	public boolean hasFinished() {
		return this.actionFinished;
	}
	
	public String getId() {
		return "0001";
	}

	private void done(Exception ex) {
        if(this.lastException != null && ex != null) {
            throw new IllegalStateException("exception already set");
        }

		this.lastException = ex;
		this.actionFinished = true;
	}
	
	public interface Action {
		void run();
	}
}
