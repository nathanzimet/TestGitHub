import java.awt.*;

class BlueSquare extends DrawnObject {
    private static final int SIZE = 50;

    public BlueSquare(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillRect(x, y, SIZE, SIZE);
    }
}
