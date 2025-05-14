import java.awt.*;

abstract class DrawnObject {
    protected int x;
    protected int y;
    protected int id;

    public DrawnObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public abstract void draw(Graphics2D g2d);
}
