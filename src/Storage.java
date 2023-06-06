import java.util.ArrayList;
import java.util.List;

// Logic Layer
class Storage {
    private final List<Balloon> storage = new ArrayList<>();
    private int balloonCount = 1;

    public synchronized Balloon takeFirst() {
        if (storage.isEmpty()) {
            return null;
        }
        return storage.remove(0);
    }

    public synchronized void add(Balloon balloon) {
        storage.add(balloon);
        balloon.setNumber(balloonCount++);
    }

    public synchronized List<Balloon> getStorage() {
        return new ArrayList<>(storage);
    }

    public synchronized int getMaxCapacity() {
        return 99;
    }

    public synchronized void clear() {
        storage.clear();
        balloonCount = 1;
    }
}
