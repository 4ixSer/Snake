import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by qny4i on 08.01.2017.
 */
public class Room {
    public static void main(String[] args) {

        game= new Room(20,20, new Snake(5,5));
        game.getSnake().setDirection(SnakeDirection.DOWN);
        game.createMouse();
        game.run();

    }

    public static Room game;

    private int width;
    private int height;
    private Snake snake;
    private Mouse mouse;

    public Room(int width, int height, Snake snake) {
        this.width = width;
        this.height = height;
        this.snake = snake;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }

    public void createMouse(){
        mouse = new Mouse((int) (Math.random() * width),(int) (Math.random() * height));
    }

    public void eatMouse(){
        createMouse();
    }

    //Массив "пауз" в зависимости от уровня.
    private static int[] levelDelay = {500, 480, 460, 440, 420, 400, 380, 360, 340, 320,  300};

    /**
     * Прогрмма делает паузу, длинна которой зависит от длинны змеи.
     */
    public void sleep()
    {
        try
        {
            int level = snake.getSections().size();
            int delay = level < 11 ? levelDelay[level] : 200;
            Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {
        }
    }

    /**
     *  Основной цикл программы.
     *  Тут происходят все важные действия
     */
    public void run()
    {
        //Создаем объект "наблюдатель за клавиатурой" и стартуем его.
        KeyboardObserver keyboardObserver = new KeyboardObserver();
        keyboardObserver.start();

        //пока змея жива
        while (snake.isAlive())
        {
            //"наблюдатель" содержит события о нажатии клавиш?
            if (keyboardObserver.hasKeyEvents())
            {
                KeyEvent event = keyboardObserver.getEventFromTop();
                //Если равно символу 'q' - выйти из игры.
                if (event.getKeyChar() == 'q') return;

                //Если "стрелка влево" - сдвинуть фигурку влево
                if (event.getKeyCode() == KeyEvent.VK_LEFT)
                    snake.setDirection(SnakeDirection.LEFT);
                    //Если "стрелка вправо" - сдвинуть фигурку вправо
                else if (event.getKeyCode() == KeyEvent.VK_RIGHT)
                    snake.setDirection(SnakeDirection.RIGHT);
                    //Если "стрелка вверх" - сдвинуть фигурку вверх
                else if (event.getKeyCode() == KeyEvent.VK_UP)
                    snake.setDirection(SnakeDirection.UP);
                    //Если "стрелка вниз" - сдвинуть фигурку вниз
                else if (event.getKeyCode() == KeyEvent.VK_DOWN)
                    snake.setDirection(SnakeDirection.DOWN);
            }

            snake.move();   //двигаем змею
            print();        //отображаем текущее состояние игры
            sleep();        //пауза между ходами
        }

        //Выводим сообщение "Game Over"
        System.out.println("Game Over!");
    }

    public void print(){
        //Создаем массив, куда будем "рисовать" текущее состояние игры
        int[][] matrix = new int[height][width];

        //Рисуем все кусочки змеи
        ArrayList<SnakeSection> sections = new ArrayList<SnakeSection>(snake.getSections());
        for (SnakeSection snakeSection : sections)
        {
            matrix[snakeSection.getY()][snakeSection.getX()] = 1;
        }

        //Рисуем голову змеи (4 - если змея мертвая)
        matrix[snake.getY()][snake.getX()] = snake.isAlive() ? 2 : 4;

        //Рисуем мышьsdasd
        matrix[mouse.getY()][mouse.getX()] = 3;

        //Выводим все это на экран
        String[] symbols = {" . ", " x ", " W ", " o ", "RIP"};
        System.out.println("_____________________________________________________________");
        for (int y = 0; y < height; y++)
        {
            System.out.print("|");
            for (int x = 0; x < width; x++)
            {
                System.out.print(symbols[matrix[y][x]]);
            }
            System.out.println("|");
        }
        System.out.println("_____________________________________________________________");

    }



}
