package views;

import views.parts.CallsButtonPannel;

import javax.swing.*;
import java.awt.*;

public class CallsButtonView extends JFrame {

    private CallsButtonPannel[] callButtonsView;

    public CallsButtonView(int steps) {
        this.callButtonsView = new CallsButtonPannel[steps+1];

        this.getContentPane().setLayout(new FlowLayout());
        this.initCallButtons(steps);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(new Point(100, 200));
        this.setSize(new Dimension(200, 300));
        this.setTitle("Buttons for each floor");
    }

    public  static void main(String[] args) {
        CallsButtonView view = new CallsButtonView(5);
        view.setVisible(true);
    }

    public void initCallButtons(int steps) {
        for(int i = steps; i >= 0; --i) {
            this.callButtonsView[i] = new CallsButtonPannel(i);
            this.add(this.callButtonsView[i]);
        }
    }
}