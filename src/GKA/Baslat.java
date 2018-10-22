package GKA;

import javax.swing.*;

public class Baslat {

    public static void main(String[] args) {
        Oyun oyun=new Oyun();


        JFrame pencere=new JFrame("2048");
        pencere.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pencere.setResizable(false);
        pencere.add(oyun);
        pencere.pack();
        pencere.setLocationRelativeTo(null);
        pencere.setVisible(true);

        oyun.baslat();
    }


}
