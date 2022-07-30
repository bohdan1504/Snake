import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GameFrame(){

        this.setTitle("Snake!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);


        this.add(new GamePanel());
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}
