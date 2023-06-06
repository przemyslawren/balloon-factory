//Visual layer
public enum BalloonColor {
    WHITE(255, 255, 255), BLACK(0, 0, 0), RED(255, 0, 0),
    BLUE(0, 0, 255), YELLOW(250, 218, 94), GRAY(220, 220, 220),
    GREEN(0, 255, 0), PURPLE(230, 230, 250), PINK(255, 192, 203),
    ORANGE(255, 140, 0), BROWN(255, 248, 220), CYAN(0, 255, 255),
    VIOLET(230, 230, 250), DARK_GREEN(0, 150, 0), DARK_RED(150, 0, 0),
    DARK_BLUE(0, 0, 150);

    public final int r, g, b;

    BalloonColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    private static final BalloonColor[] colors = new BalloonColor[16];

    static {
        int i = 0;
        for (BalloonColor e : values()) {
            colors[i++] = e;
        }
    }

    public static BalloonColor get(int num) {
        return colors[num];
    }
}
