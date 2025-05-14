import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DrawObjectTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Draw Object Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLayout(new BorderLayout());

            DrawingPanel panel = new DrawingPanel();
            frame.add(panel, BorderLayout.CENTER);

            // Create control panel for user input
            JPanel controlPanel = new JPanel(new GridLayout(2, 5, 5, 5));

            JComboBox<String> shapeCombo = new JComboBox<>(new String[]{
                    "Yellow Circle", "Blue Square",
                    "Red Triangle", "Outlined Circle"
            });

            JTextField xField = new JTextField("100");
            JTextField yField = new JTextField("100");
            JButton addButton = new JButton("Add Shape");
            JButton removeButton = new JButton("Remove Shape");
            final int[] currentID = {0};
            ArrayList<Message<DrawnObject>> objectList = new ArrayList<>();

            controlPanel.add(new JLabel("Shape Type:"));
            controlPanel.add(shapeCombo);
            controlPanel.add(new JLabel("X Coordinate:"));
            controlPanel.add(xField);
            controlPanel.add(new JLabel("Y Coordinate:"));
            controlPanel.add(yField);
            controlPanel.add(addButton);
            controlPanel.add(removeButton);


            frame.add(controlPanel, BorderLayout.SOUTH);

            // Add button action
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        int x = Integer.parseInt(xField.getText());
                        int y = Integer.parseInt(yField.getText());
                        String shapeType = (String) shapeCombo.getSelectedItem();

                        DrawnObject obj = null;
                        switch (shapeType) {
                            case "Yellow Circle":
                                obj = new YellowCircle(x, y);
                                break;
                            case "Blue Square":
                                obj = new BlueSquare(x, y);
                                break;
                            case "Red Triangle":
                                obj = new RedTriangle(x, y);
                                break;
                            case "Outlined Circle":
                                obj = new OutlinedCircle(x, y);
                                break;
                        }

                        if (obj != null) {
                            obj.setID(currentID[0]);
                            Message<DrawnObject> drawObject = new Message<>(obj);
                            objectList.add(drawObject);
                            currentID[0]++;
                            panel.addDrawObject(drawObject);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame,
                                "Please enter valid coordinates (numbers only)",
                                "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Message<DrawnObject> toRemove = objectList.get(objectList.size() - 1);
                    panel.removeDrawObject(toRemove);
                    objectList.remove(objectList.size() - 1);
                }
            });

            frame.setVisible(true);
        });
    }
}
