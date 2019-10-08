package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SystemsActionView extends JFrame {

    private JButton orderUp, orderDown, orderNextStep, orderEmergency;

    public SystemsActionView() {
        this.orderDown = new JButton("Down");
        this.orderUp = new JButton("Up");
        this.orderNextStep = new JButton("Stop at next floor");
        this.orderEmergency = new JButton("Emergency Stop");


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
        this.setTitle("System Simulator");
    }

    public void enableListeners() {
        this.orderUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("System Action : Up");
            }
        });
        this.orderDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("System Action : Down");
            }
        });
        this.orderNextStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("System Action : Stop at next floor");
            }
        });
        this.orderEmergency.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("System Action : Emergency Stop");
            }
        });
    }

    public static void main(String[] args) {
        SystemsActionView systemActionView = new SystemsActionView();
        systemActionView.setVisible(true);
    }
}
