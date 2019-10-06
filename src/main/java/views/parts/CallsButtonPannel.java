package views.parts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CallsButtonPannel extends JPanel {

    private int step;
    private JLabel stepLabel;
    private JButton up;
    private JButton down;

    public CallsButtonPannel(final int step) {
        this.step = step;

        ImageIcon iconUp = this.resize(new ImageIcon("icon_up.png"));
        ImageIcon iconDown = this.resize(new ImageIcon("icon_down.png"));

        this.stepLabel = new JLabel(String.valueOf(step));
        this.up = new JButton(iconUp);
        this.down = new JButton(iconDown);

        this.add(stepLabel, 0);
        this.add(up, 1);
        this.add(down, 2);

        this.up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(step + " - demande la montée");
            }
        });

        this.down.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(step + " - demande a descendre");
            }
        });
    }

    public ImageIcon resize(ImageIcon imageIcon) {
        Image image = imageIcon.getImage(); // transform it
        Image newImg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(newImg);  // transform it back
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();

        for (int i = 5; i >= 0; --i) {
            frame.add(new CallsButtonPannel(i));
        }

        frame.getContentPane().setLayout(new FlowLayout());
        frame.setSize(300, 300);
        frame.show();

    }
}
