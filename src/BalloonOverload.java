import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// Visual Layer
public class BalloonOverload extends Canvas implements Runnable {
    private final JFrame frame;
    private final List<Balloon> ballList = new ArrayList<>();

    public BalloonOverload(JFrame frame) {
        this.frame = frame;
        for (int i = 1; i <= 100; i++) {
            ballList.add(new Balloon(BalloonColor.get((int) (Math.random() * BalloonColor.values().length)),
                    (int) (Math.random() * (frame.getWidth() * 10)),
                    (int) (Math.random() * (frame.getHeight() + frame.getHeight())), frame));
        }
        new Thread(this).start();
    }

    public void update(Graphics g) {
        Image image = createImage(getWidth(), getHeight());
        Graphics imageG = image.getGraphics();

        for (Balloon ball : ballList) {
            ball.draw(imageG);
        }
        imageG.setColor(Color.red);
        imageG.setFont(new Font(this.getFont().getName(), Font.BOLD, 30));
        imageG.drawString("BALLOON OVERLOAD", (int) (frame.getWidth() / 2.5), frame.getHeight() / 2);

        g.drawImage(image, 0, 0, this);
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            repaint();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
