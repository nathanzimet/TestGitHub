package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class PanelSwitcher {
        private static JFrame frame;
        private static ButtonPanel panel1;
        private static OvalPanel panel2;
        private static boolean isPanel1Active = true;

        public static void main(String[] args) {
            // Set up the frame and panels
            frame = new JFrame("Panel Switcher");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            panel1 = new ButtonPanel();
            panel2 = new OvalPanel();

            // Start with panel1
            frame.add(panel1);
            frame.setVisible(true);

            // Scanner for console input (run in a separate thread)
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Type 'switch' to toggle panels or 'quit' to exit");
                while (true) {
                    String input = scanner.nextLine().trim().toLowerCase();
                    if (input.equals("switch")) {
                        switchPanels();
                    } else if (input.equals("quit")) {
                        System.exit(0);
                    }
                }
            }).start();
        }

        // Handles panel switching (called from the scanner thread)
        private static void switchPanels() {
            SwingUtilities.invokeLater(() -> {
                frame.getContentPane().removeAll();
                if (isPanel1Active) {
                    frame.add(panel2);
                } else {
                    frame.add(panel1);
                }
                isPanel1Active = !isPanel1Active; // Toggle state
                frame.revalidate();
                frame.repaint();
                System.out.println("Panel switched!");
            });
        }

        // Panel with a button
        static class ButtonPanel extends JPanel {
            public ButtonPanel() {
                setBackground(Color.WHITE);
                JButton button = new JButton("Click Here!");
                button.addActionListener(e -> System.out.println("Button clicked!"));
                add(button);
            }
        }

        // Panel with a red oval
        static class OvalPanel extends JPanel {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                g.fillOval(100, 50, 200, 100); // Draw oval at (x=100, y=50, width=200, height=100)
            }
        }
    }