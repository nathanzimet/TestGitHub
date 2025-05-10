import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


//if the view gameloop calls repaint for 60 fps, then
//most repaints here are not necessary, such as in setScale

public class CameraTest extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Panel size
    private int scale = 8;
    private static final int PANEL_SIZE_X = 1280;
    private static final int PANEL_SIZE_Y = 720;

    //position of cam as if in game
    private int cameraX;
    private int cameraY;

    //offset for drawing objects in JPanel
    private int drawOffsetX;
    private int drawOffsetY;

    //amount dragged when click and drag to move cam (unlocked cam)
    private int cameraDragX;
    private int cameraDragY;

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
        this.addKeyListener(this);
        this.setFocusable(true);

        cameraX = map.player.x;
        cameraY = map.player.y;
        drawOffsetX = calculateXOffset();
        drawOffsetY = calculateYOffset();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Mac cannot detect control + click
        //ChatGPT:  On macOS, Control + Click is interpreted by the OS as a right-click after processing the event.
        //          But Java sees it as a left-click with the Control key held down
        //the solution is to use isControlDown() with left click
        if (SwingUtilities.isRightMouseButton(e) || (SwingUtilities.isLeftMouseButton(e) && e.isControlDown())) {

            //debug stuff
            System.out.println("Right Mouse pressed");
            int x = e.getX();
            int y = e.getY();
            System.out.println("Clicked on window at: (" + x + ", " + y + ")");

            //the real stuff, not sure clamping is necessary ??
            //also, clamps to 255 not 256 ermmm
            int clickedX = (e.getX() - drawOffsetX) / scale;
            clickedX = clickedX < 0? 0 : (clickedX >= map.MAP_SIZE? map.MAP_SIZE - 1 : clickedX);
            int clickedY = (e.getY() - drawOffsetY) / scale;
            clickedY = clickedY < 0? 0 : (clickedY >= map.MAP_SIZE? map.MAP_SIZE - 1 : clickedY);
            System.out.println("Which in game is: (" + clickedX + ", " + clickedY + ")");
        }
        else if (SwingUtilities.isLeftMouseButton(e)) {
            System.out.println("Left Mouse pressed");
            dragging = true;
            cameraDragX = e.getX() - cameraX;
            cameraDragY = e.getY() - cameraY;
        }
        else {
            System.out.println("you used middle click probably");
        }
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
            //does not clamp to MAP_SIZE - 1 because Model doesn't know it exists
            cameraX = e.getX() - cameraDragX;
            cameraX = cameraX < 0? 0 : (cameraX > map.MAP_SIZE? map.MAP_SIZE : cameraX);
            cameraY = e.getY() - cameraDragY;
            cameraY = cameraY < 0? 0 : (cameraY > map.MAP_SIZE? map.MAP_SIZE : cameraY);
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        System.out.println("a key was pressed");
        if (code == KeyEvent.VK_W) {
            Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mouseLocation, this);
            System.out.println("Mouse in panel: " + mouseLocation);
            System.out.println("which in game is: " + (mouseLocation.getX() - drawOffsetX) / scale + ", " + (mouseLocation.getY() - drawOffsetY) / scale);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    // Unused event methods
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    @Override
    protected void paintComponent(Graphics g) {

        MapPartitionTest.Champion c = map.player;
        if (locked) {
            cameraX = c.x;
            cameraY = c.y;
        }
        //bake scale into draw offsets
        //scale is not baked into position (x,y) or size
        drawOffsetX = calculateXOffset();
        drawOffsetY = calculateYOffset();

        //System.out.println("Camera is at: " + camerax + ", " + cameray);

        //I don't know what this does
        super.paintComponent(g);

        //Draw: map, minions, champion
        g.drawRect(drawOffsetX, drawOffsetY, map.MAP_SIZE * scale, map.MAP_SIZE * scale);
        for (MapPartitionTest.Minion m : map.minions) {
            drawMinion(g, (m.x * scale) + drawOffsetX, (m.y * scale) + drawOffsetY, m.size * scale);
        }
        drawChampion(g, (c.x * scale) + drawOffsetX, (c.y * scale) + drawOffsetY, map.player.size * scale);

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

    //calculate drawoffsetx
    public int calculateXOffset() {
        return (PANEL_SIZE_X / 2) - (cameraX * scale);
    }

    //calculate drawoffsety
    public int calculateYOffset() {
        return (PANEL_SIZE_Y / 2) - (cameraY * scale);
    }

    public void setScale(int scale) {
        this.scale = scale;
        repaint();
    }

    public void update() {
        repaint();
    }


}