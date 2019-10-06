package views;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.SliderUI;
import java.awt.*;

public class ElevatorSliderView extends JFrame {

    private JSlider slider;

    public ElevatorSliderView(int steps) {
        this.slider = new JSlider(JSlider.VERTICAL, 0, steps, 0);

        this.add(this.slider);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("Etage : " + slider.getValue());
            }
        });

        this.setSize(new Dimension(200, 300));
        this.setLocation(new Point(300,200));
        this.setTitle("Simulateur ascenseur");

    }
}
