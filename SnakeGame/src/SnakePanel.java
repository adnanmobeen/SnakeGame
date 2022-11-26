import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;
public class SnakePanel extends JPanel implements ActionListener {
    //fixed sizes of the canvas and unit
    static final int width = 1200;
    static final int height = 600;
    static final int unit_size = 50;

    // random variable for random food spawns
    Random random;

    //coordinates of the food
    int foodX;
    int foodY;

    boolean game_flag = false;
    char dir='R';
    int body = 4;

    int foodeaten=0;

    //number of units
    static final int size = (width*height)/(unit_size*unit_size);

    final int x_snake[] = new int[size];
    final int y_snake[] = new int[size];

    // to decide how frequently we need to update the snake
    Timer timer;
    static final int DELAY = 160;

    SnakePanel(){
        this.setPreferredSize(new Dimension(width,height));
        this.setBackground(Color.black);
        this.addKeyListener(new MyKey());
        // to make sure we get the keyboard input
        this.setFocusable(true);
        random = new Random();

        Game_Start();

    }

    public void Game_Start(){
        game_flag=true;
        timer=new Timer(DELAY,this);
        timer.start();
        spawnfood();
    }

    public void spawnfood(){
        // determining the random x and y coordinates of the food
        foodX=random.nextInt((int)(width/unit_size))*unit_size;
        foodY=random.nextInt((int)(height/unit_size))*unit_size;
    }

    public void paintComponent(Graphics graphic){
        super.paintComponent(graphic);
        draw(graphic);
    }

    public void draw(Graphics graphic){
        if(game_flag){
            // setting the graphics for the food block
            graphic.setColor(Color.red);
            graphic.fillOval(foodX,foodY,unit_size,unit_size);

            // setting the graphics for the snake's body
            for(int i=0;i<body;i++){
                //to fill the head of the snake
                if(i==0){
                    graphic.setColor(Color.orange);
                    graphic.fillRect(x_snake[i],y_snake[i],unit_size,unit_size);
                }
                //to fill the rest of the body
                else{
                    graphic.setColor(Color.green);
                    graphic.fillRect(x_snake[i],y_snake[i],unit_size,unit_size);
                }
            }
          // to display the score
            graphic.setColor(Color.cyan);
            graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
            FontMetrics font_me = getFontMetrics(graphic.getFont());
            graphic.drawString("Score:"+ foodeaten,(width-font_me.stringWidth("Score"+foodeaten))/2,graphic.getFont().getSize());

        }
        else{
            gameOver(graphic);
        }
    }

    public void gameOver(Graphics graphic){
        // to display the score
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics font_me = getFontMetrics(graphic.getFont());
        graphic.drawString("Score:"+ foodeaten,(width-font_me.stringWidth("Score"+foodeaten))/2,graphic.getFont().getSize());

        // to display the gameover text
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,80));
        FontMetrics font_me1 = getFontMetrics(graphic.getFont());
        graphic.drawString("GAME OVER!",(width-font_me1.stringWidth("GAME OVER!"))/2,height/2);

        // prompt to replay
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Comic Sans",Font.BOLD,40));
        FontMetrics font_me2 = getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to replay",(width-font_me2.stringWidth("Press R to replay"))/2,height/2-150);
    }

    public void move(){
        // updating the whole apart from it's head
        for(int i=body;i>0;i--) {
            x_snake[i] = x_snake[i - 1];
            y_snake[i] = y_snake[i - 1];
        }
  //updating the head of the snake according to the direction
            switch (dir){
                case'U':
                    y_snake[0]=y_snake[0]-unit_size;
                    break;
                case'L':
                    x_snake[0]=x_snake[0]-unit_size;
                    break;
                case'D':
                    y_snake[0]=y_snake[0]+unit_size;
                    break;
                case'R':
                    x_snake[0]=x_snake[0]+unit_size;
                    break;
            }
        }


    public void checkhit(){
        //to check if the snake has hit itself or the walls
        for(int i=body;i>0;i--){
         if(x_snake[0]==x_snake[i]&&y_snake[0]==y_snake[i]){
             game_flag=false;
         }
        }

        if(x_snake[0]<0){
            game_flag=false;
        }
        else if(x_snake[0]>width){
            game_flag=false;
        }
        else if(y_snake[0]<0){
            game_flag=false;
        }
        else if(y_snake[0]>height){
            game_flag=false;
        }
        if(game_flag==false){
            timer.stop();
        }
    }

    public void eat(){
        if(x_snake[0]==foodX &&y_snake[0]==foodY){
            body++;
            foodeaten++;
            spawnfood();
        }
    }

     public class MyKey extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
           switch (e.getKeyCode()){
               case KeyEvent.VK_LEFT:
                   if(dir!='R'){
                       dir='L';
                   }
                   break;
               case KeyEvent.VK_RIGHT:
                   if(dir!='L'){
                       dir='R';
                   }
                   break;
               case KeyEvent.VK_UP:
                   if(dir!='D'){
                       dir='U';
                   }
                   break;
               case KeyEvent.VK_DOWN:
                   if(dir!='u'){
                       dir='D';
                   }
                   break;
               case KeyEvent.VK_R:
                   if(!game_flag){
                       foodeaten=0;
                       body=4;
                       dir='R';
                       Arrays.fill(x_snake,0);
                       Arrays.fill(y_snake,0);
                       Game_Start();
                   }
                   break;
           }
        }
     }
     //to make sure we use this function instead of any with the same name in the parent class(JPanel in this case)
     @Override
     public  void actionPerformed(ActionEvent arg0){
            if(game_flag){
                move();
                eat();
                checkhit();
            }
            repaint();
    }
}
