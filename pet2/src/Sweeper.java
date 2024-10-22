import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import sweeper.Box;
import sweeper.Coord;
import sweeper.Game;
import sweeper.Ranges;


public class Sweeper extends JFrame {
    private Game game;

    private JPanel panel;
    private JLabel label;
    private final int COLS = 9;
    private final int ROWS = 9;
    private final int BOMBS = 10;
    private final int IMAGE_SIZE = 50;


    public static void main(String[]args){
        new Sweeper();
    }

    private Sweeper(){
        game = new Game(COLS,ROWS,BOMBS);
        game.start();
        setImages();
        initLabel();
        initPanel();
        initFrame();
    }
    private void initLabel(){
        label = new JLabel("Welcome!");
        add(label,BorderLayout.SOUTH);
    }
    private void initPanel() {
        panel = new JPanel()
        {

            @Override
            protected void paintComponent(Graphics g){
                super.paintComponents(g);
                for(Coord coord: Ranges.getAllCoords()) {
                    g.drawImage((Image) game.getBox(coord).image,
                            coord.x*IMAGE_SIZE, coord.y*IMAGE_SIZE, this);
                }
            }
        };
        //мышечный адаптер
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX()/IMAGE_SIZE;
                int y = e.getY()/IMAGE_SIZE;
                Coord coord = new Coord(x,y);
                if(e.getButton() == MouseEvent.BUTTON1)
                    game.pressLeftButton(coord);
                if(e.getButton() == MouseEvent.BUTTON3)
                    game.pressRightButton(coord);
                if(e.getButton() == MouseEvent.BUTTON2)
                    game.start();
                label.setText(getMessage());
                panel.repaint();
            }
        });
        panel.setPreferredSize(new Dimension(
                Ranges.getSize().x*IMAGE_SIZE,
                Ranges.getSize().y*IMAGE_SIZE));
        add(panel);
    }
    private String getMessage(){
        switch (game.getState()){
            case PLAYED : return "Think twice";
            case BOMBED : return "You lose";
            case WINNER : return "You win";
            default: return "Welcome";
        }
    }
    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//Завершает программу если нажать на крестик
        setTitle("JavaSweeper");//создаем подпись вверху
        setVisible(true);
        setResizable(false);
        pack();//распаковываем до размера 500*300
        setLocationRelativeTo(null);//устанавливаем ее слева
        setIconImage(getImage("icon"));
    }
    private void setImages(){//подгрузка всех картинок
        for(Box box: Box.values())
            box.image = getImage(box.name().toLowerCase());
    }

    private Image getImage(String name){
        String fname = "img/"+name+".png";
        ImageIcon icon = new ImageIcon(getClass().getResource(fname));
        return icon.getImage();
    }

}
