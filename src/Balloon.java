import javax.swing.*;
import java.awt.*;

// Logic Layer
public class Balloon {
    private static long count = 1;
    private long number;
    private int x, y;
    private final int height = 100;
    private final Color color;
    private JFrame frame;

    public Balloon(BalloonColor color, int x, int y, JFrame frame) {
        this.x = x;
        this.y = y;
        this.color = new Color(color.r, color.g, color.b);
        this.frame = frame;
        this.number = -1;
        start();
    }

    public Balloon(BalloonColor color) {
        this.color = new Color(color.r, color.g, color.b);
        this.number = count++;
    }

    public long getNumber() {
        return number;
    }

    public Color getColor() {
        return color;
    }

    public void start() {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                move();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.start();
    }

    private void move() {
        if (y < -height) {
            y = (int) (Math.random() * frame.getHeight()) + frame.getHeight();
        } else {
            y -= 5;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, 80, height);
    }

    public void setNumber(int i) {
        this.number = i;
    }
}
