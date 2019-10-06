package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommandButtonView extends JFrame {

    private JButton[] commandButtons;

    public CommandButtonView(int steps) {
        this.commandButtons = new JButton[steps+1];

        for(int i = 0; i <= steps; ++i) {
            this.commandButtons[i] = new JButton(String.valueOf(i));
            this.add(this.commandButtons[i]);
            this.setSize(new Dimension(50, 50));
            this.commandButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Utilisateur cabine demande l'étage " + ((JButton)e.getSource()).getText() );
                }
            });
        }
        this.setSize(new Dimension(200, 300));
        this.setLocation(new Point(500, 200));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new GridLayout(3, 3));
        this.setTitle("Clavier numérique de la cabine d'ascenceur");
    }

    public static void main(String[] args) {
        CommandButtonView view = new CommandButtonView(5);
        view.show();
    }
}
