import java.util.ArrayList;
import java.util.List;

// Logic Layer
public class Transporter extends Thread {
    private final List<Balloon> transport = new ArrayList<>();
    private final Storage storage;
    private String status = "None";

    private boolean paused = false;
    private final Object pauseLock = new Object();

    public Transporter(Storage storage) {
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
            for (int i = 0; i < 10; i++) {
                if (paused) {
                    break;
                }

                if (transport.size() < 10 && !storage.getStorage().isEmpty()) {
                    status = "Loading";
                    Balloon balloon = storage.takeFirst();
                    if (balloon != null) {
                        transport.add(balloon);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                } else if (storage.getStorage().isEmpty()) {
                    status = "Waiting";
                    i--;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if (!paused) {
                status = "Delivering";
                transport.clear();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public synchronized void pauseThread() {
        paused = true;
    }

    public synchronized void resumeThread() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
