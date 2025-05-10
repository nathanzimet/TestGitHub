//This is 100% ChatGPT

//Prompt:
//Can you write a Java program that:
//  Draws a small red square in a JPanel
//The user can click and drag to move the square

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DraggableSquare extends JPanel implements MouseListener, MouseMotionListener {

    private int squareX = 100; // initial x position
    private int squareY = 100; // initial y position
    private final int SQUARE_SIZE = 30;
    private boolean dragging = false;
    private int offsetX, offsetY;

    public DraggableSquare() {
        this.setPreferredSize(new Dimension(400, 400));
        this.setBackground(Color.WHITE);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(squareX, squareY, SQUARE_SIZE, SQUARE_SIZE);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        if (mx >= squareX && mx <= squareX + SQUARE_SIZE &&
                my >= squareY && my <= squareY + SQUARE_SIZE) {
            dragging = true;
            offsetX = mx - squareX;
            offsetY = my - squareY;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            squareX = e.getX() - offsetX;
            squareY = e.getY() - offsetY;
            repaint();
        }
    }

    // Unused event methods
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draggable Red Square");
        DraggableSquare panel = new DraggableSquare();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
