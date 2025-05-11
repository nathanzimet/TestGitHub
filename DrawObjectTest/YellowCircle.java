import java.awt.*;

class YellowCircle extends DrawnObject {
    private static final int DIAMETER = 50;

    public YellowCircle(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(x, y, DIAMETER, DIAMETER);
    }
}
