import javax.swing.*;
import java.awt.*;

public class BankerAlgorithm extends JFrame {
    public BankerAlgorithm() {
        this.setLayout(new BorderLayout(10, 5));
        MainShow mainShow = new MainShow();
        this.add(mainShow);
        this.setTitle("银行家算法");
        this.setResizable(true);
        this.setSize(800, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public static void main(String[] args) {
        new BankerAlgorithm();
    }
}
