package views;

import system.ElevatorSystem;
import system.Notification;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class CommandButtonView extends JFrame implements Observer {

    private JButton[] commandButtons;
    private ElevatorSystem system;

    public CommandButtonView(int steps, ElevatorSystem system) {
        this.commandButtons = new JButton[steps + 1];

        this.system = system;
        this.system.addObserver(this);

        for (int i = 0; i <= steps; ++i) {
            this.commandButtons[i] = new JButton(String.valueOf(i));
            this.add(this.commandButtons[i]);
            this.setSize(new Dimension(50, 50));

            this.commandButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton pressed =  ((JButton) e.getSource());
                    transmitRequest(Integer.parseInt(pressed.getText()));
                }
            });
        }
        this.setSize(new Dimension(200, 300));
        this.setLocation(new Point(500, 200));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout(3, 3));
        this.setTitle("Elevator's numeric keyboard");
    }

    public void transmitRequest(int stage) {
        this.system.takeRequest(stage);
    }

    public void  colore(int stage) {
        if(this.commandButtons[stage] != null) {
            this.commandButtons[stage].setBackground(Color.cyan);
        }
    }

    public void delecore(int stage) {

    }

    @Override
    public void update(Observable o, Object arg) {
        Notification action = (Notification) arg;

        switch (action.getType()) {
            case REQUEST_TAKEN_BY_SYSTEM:
                this.colore(action.getStageConcerned());
                break;

        }
    }

    /*
    public static void main(String[] args) {
        CommandButtonView view = new CommandButtonView(5);
        view.setVisible(true);
    }
*/
}
