import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


//if the view gameloop calls repaint for 60 fps, then
//most repaints here are not necessary

public class CameraTest extends JPanel  implements MouseListener, MouseMotionListener {

    // Panel size
    private int SCALE = 2;
    private static final int PANEL_SIZE_X = 1280;
    private static final int PANEL_SIZE_Y = 720;
    private int drawoffsetx;
    private int drawoffsety;
    private int camerax;
    private int cameray;
    private int cameraoffsetx;
    private int cameraoffsety;

    private boolean dragging = false;
    private boolean locked = false;

    MapPartitionTest map;

    CameraTest(MapPartitionTest map) {
        this.map = map;

        JFrame frame = new JFrame("Circle in Square");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(PANEL_SIZE_X, PANEL_SIZE_Y);
        frame.add(this);
        frame.setVisible(true);

        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        camerax = map.player.x;
        cameray = map.player.y;
        drawoffsetx = calculateXOffset();
        drawoffsety = calculateYOffset();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse pressed");
        dragging = true;
        cameraoffsetx = e.getX() - camerax;
        cameraoffsety = e.getY() - cameray;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse released");
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        System.out.println("Mouse dragged");
        if (dragging && !locked) {
            camerax = e.getX() - cameraoffsetx;
            camerax = camerax < 0? 0 : (camerax > map.MAP_SIZE? map.MAP_SIZE : camerax);
            cameray = e.getY() - cameraoffsety;
            cameray = cameray < 0? 0 : (cameray > map.MAP_SIZE? map.MAP_SIZE : cameray);
            repaint();
        }
    }

    // Unused event methods
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    protected void paintComponent(Graphics g) {

        MapPartitionTest.Champion c = map.player;
        if (locked) {
            camerax = c.x;
            cameray = c.y;
        }
        //bake SCALE into draw offsets
        drawoffsetx = calculateXOffset();
        drawoffsety = calculateYOffset();

        System.out.println("Camera is at: " + camerax + ", " + cameray);
        super.paintComponent(g);

        // Draw the square (positioned at (20,20) for padding)
        g.drawRect(drawoffsetx, drawoffsety, map.MAP_SIZE * SCALE, map.MAP_SIZE * SCALE);

        for (MapPartitionTest.Minion m : map.minions) {
            drawMinion(g, (m.x * SCALE) + drawoffsetx, (m.y * SCALE) + drawoffsety, m.size * SCALE);
        }
        drawChampion(g, (c.x * SCALE) + drawoffsetx, (c.y * SCALE) + drawoffsety, map.player.size * SCALE);

    }

    public void drawMinion(Graphics g, int centerX, int centerY, int radius) {
        int diameter = 2 * radius;
        int topLeftX = centerX - radius;
        int topLeftY = centerY - radius;
        g.drawOval(topLeftX, topLeftY, diameter, diameter);
    }

    public void drawChampion(Graphics g, int centerX, int centerY, int radius) {
        int diameter = 2 * radius;
        int topLeftX = centerX - radius;
        int topLeftY = centerY - radius;
        g.drawOval(topLeftX, topLeftY, diameter, diameter);
    }

    public int calculateXOffset() {
        return (PANEL_SIZE_X / 2) - (camerax * SCALE);
    }

    public int calculateYOffset() {
        return (PANEL_SIZE_Y / 2) - (cameray * SCALE);
    }

    public void setSCALE(int SCALE) {
        this.SCALE = SCALE;
        repaint();
    }

    public void update() {
        repaint();
    }

}