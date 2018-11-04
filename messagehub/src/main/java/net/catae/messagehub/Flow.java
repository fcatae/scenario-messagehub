package net.catae.messagehub;

public class Flow {   
    
    private Thread thread;
    private boolean actionStarted = false;
    private boolean actionFinished = false;
    private Exception lastException = null;

    public Flow(Branch branch, Flow.Action action) {
        thread = createThread(branch, action);
    }

    private Thread createThread(Branch branch, Flow.Action action) {
        return new Thread(new Runnable(){        
            @Override
            public void run() {
                // safe execution
                try {
                    action.run(branch);
                    done(null);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    done(ex);
                }
            }
        });
    }

    public void start() {
        if(this.actionStarted == true) {
            throw new IllegalStateException("flow already started");
        }
        this.actionStarted = true;
        this.thread.start();
    }

    public void join() {
        if(this.actionStarted == false) {
            throw new IllegalStateException("flow has not started yet");
        }
        try {
            this.thread.join();            
        } catch (InterruptedException e) {
            this.thread.interrupt();
            done(e);
        }

        if(!hasFinished()) {
            throw new IllegalStateException("thread finished");
        }
    }

    public boolean hasFinished() {
        return this.actionFinished;
    }

    public boolean success() {
        if(this.actionFinished == false) {
            throw new IllegalStateException("flow hasn't finished");
        }

        return this.lastException != null;
    }

    private void done(Exception ex) {
        if(this.lastException != null && ex != null) {
            throw new IllegalStateException("exception already set");
        }

        if(this.actionFinished == false) {
            this.lastException = ex;
            this.actionFinished = true;
        }
    }

    public interface Action {
        void run(Branch b);
    }
}
