import java.awt.*;

abstract class DrawnObject {
    protected int x;
    protected int y;

    public DrawnObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Graphics2D g2d);
}
