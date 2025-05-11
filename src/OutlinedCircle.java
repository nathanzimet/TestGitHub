import java.awt.*;

class OutlinedCircle extends DrawnObject {
    private static final int DIAMETER = 70;

    public OutlinedCircle(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x, y, DIAMETER, DIAMETER);
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawOval(x, y, DIAMETER, DIAMETER);
    }
}
