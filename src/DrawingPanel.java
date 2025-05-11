import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

class DrawingPanel extends JPanel {
    private final List<Message<DrawnObject>> drawObjects;

    public DrawingPanel() {
        this.drawObjects = new ArrayList<>();
        setBackground(Color.WHITE);
    }

    public void addDrawObject(Message<DrawnObject> message) {
        drawObjects.add(message);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Message<DrawnObject> message : drawObjects) {
            DrawnObject obj = message.getObject();
            obj.draw(g2d);
        }
    }
}
