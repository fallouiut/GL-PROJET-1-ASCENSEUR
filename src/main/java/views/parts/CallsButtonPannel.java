package views.parts;

import system.Elevator;
import system.ElevatorSystem;
import system.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class CallsButtonPannel extends JPanel implements Observer {

    private int step;
    private JLabel stepLabel;
    private JButton up;
    private JButton down;
    private ElevatorSystem system;

    public CallsButtonPannel(final int step, final ElevatorSystem system) {
        this.step = step;
        this.system = system;
        this.system.addObserver(this);

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
                system.takeRequestOnFloor(step, Elevator.Direction.TRACT_UP);
            }
        });

        this.down.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                system.takeRequestOnFloor(step, Elevator.Direction.TRACT_DOWN);
            }
        });
    }

    public ImageIcon resize(ImageIcon imageIcon) {
        Image image = imageIcon.getImage(); // transform it
        Image newImg = image.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        return new ImageIcon(newImg);  // transform it back
    }

    public void colore(Elevator.Direction direction) {
        if (direction == Elevator.Direction.TRACT_DOWN) {
            this.down.setBackground(Color.cyan);
        } else {
            this.up.setBackground(Color.cyan);
        }
    }

    public void delecore() {
        this.up.setBackground(new JButton().getBackground());
        this.down.setBackground(new JButton().getBackground());
    }

    @Override
    public void update(Observable o, Object arg) {
        Notification action = (Notification) arg;

        if (action.getStageConcerned() == this.step) {
            switch (action.getType()) {
                case REQUEST_FLOOR_TAKEN_BY_SYSTEM:
                    this.colore(action.getDirection());
                    break;
                case STAGE_REACHED:
                    this.delecore();
                    break;
            }
        }
    }
}
