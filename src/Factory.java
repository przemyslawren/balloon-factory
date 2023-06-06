// Logic Layer
public class Factory extends Thread {
    private int frequency = 1000;
    private final Storage storage;
    private int produced;

    private volatile boolean paused = false;
    private final Object pauseLock = new Object();

    public Factory(Storage storage) {
        this.storage = storage;
        start();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (pauseLock) {
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            }
            if (storage.getStorage().size() < storage.getMaxCapacity()) {
                Balloon balloon = new Balloon(BalloonColor.get((int) (Math.random() * BalloonColor.values().length)));
                storage.add(balloon);
                produced++;
            }
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }

    public void pauseThread() {
        paused = true;
    }

    public void resumeThread() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public int getProduced() {
        return produced;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
