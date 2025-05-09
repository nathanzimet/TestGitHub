//this is from chatgpt lmfao

import javax.swing.*;
import java.awt.*;

public class CircleDrawer extends JPanel {

    // Panel size
    private static final int PANEL_SIZE = 300; // Slightly larger than the square
    private static final int SQUARE_SIZE = 256;
    private static int xoffset = 20;
    private static int yoffset = 20;

    MapPartitionTest map;

    CircleDrawer(MapPartitionTest map) {
        this.map = map;

        JFrame frame = new JFrame("Circle in Square");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(PANEL_SIZE, PANEL_SIZE);
        frame.add(this);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the square (positioned at (20,20) for padding)
        g.drawRect(xoffset, yoffset, SQUARE_SIZE, SQUARE_SIZE);

        // Draw the circle inside the square
        //drawCircle(g, circleX + squareX, circleY + squareY, radius);

        for (MapPartitionTest.Minion m : map.minions) {
            drawCircle(g, m.x + xoffset, m.y + yoffset, m.size);
        }

    }

    // Method to draw a circle based on center and radius
    public void drawCircle(Graphics g, int centerX, int centerY, int radius) {
        int diameter = 2 * radius;
        int topLeftX = centerX - radius;
        int topLeftY = centerY - radius;
        g.drawOval(topLeftX, topLeftY, diameter, diameter);
    }

//    // Optional: setters if you want to dynamically update circle parameters
//    public void setCircle(int centerX, int centerY, int radius) {
//        this.circleX = centerX;
//        this.circleY = centerY;
//        this.radius = radius;
//        repaint(); // Repaint the panel to reflect changes
//    }

    public void update() {
        repaint();
    }
}

