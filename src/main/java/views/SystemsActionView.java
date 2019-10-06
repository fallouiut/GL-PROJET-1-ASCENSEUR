package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SystemsActionView extends JFrame {

    private JButton orderUp, orderDown, orderNextStep, orderEmergency;

    public SystemsActionView() {
        this.orderDown = new JButton("Descendre");
        this.orderUp = new JButton("Monter");
        this.orderNextStep = new JButton("Arreter prochain niveau");
        this.orderEmergency = new JButton("Arret d'urgence");


        this.orderUp.setSize(new Dimension(100, 250));
        this.orderDown.setSize(new Dimension(100, 250));
        this.orderEmergency.setSize(new Dimension(100, 250));
        this.orderNextStep.setSize(new Dimension(100, 250));

        this.enableListeners();

        this.add(this.orderUp);
        this.add(this.orderDown);
        this.add(this.orderNextStep);
        this.add(this.orderEmergency);

        this.setLayout(new FlowLayout());

        this.setSize(new Dimension(200, 300));
        this.setLocation(new Point(1000, 200));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Simulateur système");
    }

    public void enableListeners() {
        this.orderUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action système: montée");
            }
        });
        this.orderDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action système: descente");
            }
        });
        this.orderNextStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action système: arret prochaine niveau");
            }
        });
        this.orderEmergency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action système: arret urgence");
            }
        });
    }

    public static void main(String[] args) {
        SystemsActionView systemActionView = new SystemsActionView();
        systemActionView.show();
    }
}
