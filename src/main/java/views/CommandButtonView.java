package views;

import system.ElevatorSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommandButtonView extends JFrame {

    private JButton[] commandButtons;
    private ElevatorSystem system;

    public CommandButtonView(int steps, ElevatorSystem system) {
        this.commandButtons = new JButton[steps + 1];

        this.system = system;

        for (int i = 0; i <= steps; ++i) {
            this.commandButtons[i] = new JButton(String.valueOf(i));
            this.add(this.commandButtons[i]);
            this.setSize(new Dimension(50, 50));
            this.commandButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                System.out.println("User inside elevator demands floor " + ((JButton) e.getSource()).getText());
                }
            });
        }
        this.setSize(new Dimension(200, 300));
        this.setLocation(new Point(500, 200));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout(3, 3));
        this.setTitle("Elevator's numeric keyboard");
    }
/*
    public static void main(String[] args) {
        CommandButtonView view = new CommandButtonView(5);
        view.setVisible(true);
    }
*/
}
