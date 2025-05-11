import java.awt.*;

class RedTriangle extends DrawnObject {
    private static final int SIZE = 60;

    public RedTriangle(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        int[] xPoints = {x, x + SIZE/2, x + SIZE};
        int[] yPoints = {y + SIZE, y, y + SIZE};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
}
