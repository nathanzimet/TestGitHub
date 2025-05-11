import javax.swing.*;
import java.awt.*;

public class PathfindingDrawer extends JPanel {

    // Panel size
    private static final int PANEL_SIZE = 512; // Slightly larger than the square
    private static int SQUARE_SIZE; //use this as scale as well
    private static int SCALE;

    TerrainMap terrain;

    PathfindingDrawer(TerrainMap terrain) {
        this.terrain = terrain;
        SQUARE_SIZE = PANEL_SIZE / terrain.TERRAIN_MAP_SIZE;
        SCALE = PANEL_SIZE / terrain.MAP_SIZE;

        JFrame frame = new JFrame("Line On Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(PANEL_SIZE, PANEL_SIZE + 20);
        frame.add(this);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draw all terrain map rectangles
        //TODO: are i and j in the correct order?

        for (int i = 0; i < terrain.TERRAIN_MAP_SIZE; i++) {
            for (int j = 0; j < terrain.TERRAIN_MAP_SIZE; j++) {
                if (terrain.terrainMap[i][j] == 0) g.setColor(Color.WHITE);
                else g.setColor(Color.LIGHT_GRAY);
                //The x, y must be swapped for drawing in the JPanel coordinates
                g.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }

        g.setColor(Color.BLACK);
        for (TerrainMap.Line l : terrain.lines) {
            drawLine(g, l);
        }
        for (TerrainMap.Point p : terrain.points) {
            drawPoint(g, p);
        }

    }

    public void drawLine(Graphics g, TerrainMap.Line l) {
        g.drawLine((int)(l.p1.x * SCALE), (int)(l.p1.y * SCALE), (int)(l.p2.x * SCALE), (int)(l.p2.y * SCALE));
    }

    public void drawPoint(Graphics g, TerrainMap.Point p) {
        g.fillOval((int)(p.x * SCALE - 3), (int)(p.y * SCALE -3), 6, 6);
    }

    public void update() {
        repaint();
    }

}
