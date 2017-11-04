//tester
import tester.*;
//utility
import java.util.*;
//colors
import java.awt.Color;
//big bang stuff
import javalib.impworld.*;
import javalib.worldimages.*;
//a square unit of snake's body
class Cell {
    //fields
    public static int SIZE = 8;
    public static Color COLOR = Color.green;
    Posn cpos;
    //construct Cell
    Cell(Posn cpos) {
        this.cpos = new Posn(cpos.x * Cell.SIZE + Cell.SIZE / 2,
                cpos.y * Cell.SIZE + Cell.SIZE / 2);
    }
    //draw this Cell
    WorldImage cellImage() {
        return new RectangleImage(this.cpos, Cell.SIZE, Cell.SIZE, Cell.COLOR);
    }
}
//snake food
class Food {
    //fields
    public static Color COLOR = Color.red;
    Posn fpos;
    //construct Food
    Food() {
        this.fpos = new Posn(
                new Random().nextInt(
                        SnakeWorld.HORIZONTAL) * Cell.SIZE + Cell.SIZE / 2,
                new Random().nextInt(
                        SnakeWorld.VERTICAL) * Cell.SIZE + Cell.SIZE / 2);
    }
    //construct Food
    Food(Posn fpos) {
        this.fpos = new Posn(fpos.x * Cell.SIZE + Cell.SIZE / 2,
                fpos.y * Cell.SIZE + Cell.SIZE / 2);
    }
    //draw this Food
    WorldImage foodImage() {
        return new RectangleImage(this.fpos, Cell.SIZE, Cell.SIZE, Food.COLOR);
    }
}
//a snake
class Snake {
    //fields
    LinkedList<Cell> body;
    LinkedList<Cell> body2;
    Posn spos;
    //construct Snake
    Snake() {
        this.body = new LinkedList<Cell>();
        this.body2 = new LinkedList<Cell>();
        this.spos = SnakeWorld.CENTER;
        this.body.addFirst(new Cell(SnakeWorld.CENTER));
    }
    //is this snake out of bounds?
    boolean outOfBounds() {
        return this.spos.x > SnakeWorld.HORIZONTAL ||
               this.spos.x < 0 ||
               this.spos.y > SnakeWorld.VERTICAL ||
               this.spos.y < 0;
    }
    //does this snake collide with itself?
    boolean collision() {
        boolean result = false;
        for(Cell cell : this.body2) {
            result = this.spos.x * Cell.SIZE + Cell.SIZE / 2 == cell.cpos.x &&
                     this.spos.y * Cell.SIZE + Cell.SIZE / 2 == cell.cpos.y ||
                     result;
        }
        return result;
    }
    //draw this Snake
    WorldImage snakeImage() {
        WorldImage result = new RectangleImage(new Posn(
                SnakeWorld.CENTER.x * Cell.SIZE + Cell.SIZE / 2,
                SnakeWorld.CENTER.y * Cell.SIZE + Cell.SIZE / 2),
                SnakeWorld.HORIZONTAL * Cell.SIZE + Cell.SIZE / 2,
                SnakeWorld.VERTICAL * Cell.SIZE + Cell.SIZE / 2, Color.orange);
        for(Cell cell : this.body) {
            result = new OverlayImages(result, cell.cellImage());
        }
        return result;
    }
}
//world snake lives in
class SnakeWorld extends World {
    //fields
    public static int HORIZONTAL = 64;
    public static int VERTICAL = 64;
    public static Posn CENTER = new Posn(SnakeWorld.HORIZONTAL / 2,
            SnakeWorld.VERTICAL / 2);
    Food food;
    Snake snake;
    String direction;
    String direction2;
    int score;
    //construct SnakeWorld
    SnakeWorld() {
        this.food = new Food();
        this.snake = new Snake();
        int random = new Random().nextInt(3);
        if(random == 0) {
            this.direction = "left";
        }
        else if(random == 1) {
            this.direction = "up";
        }
        else if(random == 2) {
            this.direction = "right";
        }
        else {
            this.direction = "down";
        }
        this.direction2 = "stop";
        this.score = 0;
    }
    //is the food eaten?
    boolean foodEaten() {
        return this.food.fpos.x / Cell.SIZE == this.snake.spos.x &&
               this.food.fpos.y / Cell.SIZE == this.snake.spos.y;
    }
    //do stuff when key is pressed
    void growSnake() {
        if(this.foodEaten()) {
            this.snake.body.addFirst(new Cell(this.snake.spos));
            this.snake.body2.addFirst(this.snake.body.get(1));
            this.food = new Food();
            this.score = this.score + 10;
        }
        else {
            this.snake.body.addFirst(new Cell(this.snake.spos));
            this.snake.body2.addFirst(this.snake.body.get(1));
            this.snake.body.removeLast();
            this.snake.body2.removeLast();
        }
    }
    //move the snake
    public void onKeyEvent(String key) {
        if(key.equals("left") && !this.direction.equals("stop")) {
            this.snake.spos = new Posn(this.snake.spos.x - 1,
                    this.snake.spos.y);
            this.direction = "left";
            this.growSnake();
        }
        else if(key.equals("up") && !this.direction.equals("stop")) {
            this.snake.spos = new Posn(this.snake.spos.x,
                    this.snake.spos.y - 1);
            this.direction = "up";
            this.growSnake();
        }
        else if(key.equals("right") && !this.direction.equals("stop")) {
            this.snake.spos = new Posn(this.snake.spos.x + 1,
                    this.snake.spos.y);
            this.direction = "right";
            this.growSnake();
        }
        else if(key.equals("down") && !this.direction.equals("stop")) {
            this.snake.spos = new Posn(this.snake.spos.x,
                    this.snake.spos.y + 1);
            this.direction = "down";
            this.growSnake();
        }
        //pause or resume the game
        else if(key.equals("p")){
            String temp = this.direction;
            this.direction = this.direction2;
            this.direction2 = temp;
        }
        //easy
        else if(key.equals("e")) {
            this.bigBang(SnakeWorld.HORIZONTAL * Cell.SIZE + Cell.SIZE,
                    SnakeWorld.VERTICAL * Cell.SIZE + Cell.SIZE, 0.2);
        }
        //normal
        else if(key.equals("n")) {
            this.bigBang(SnakeWorld.HORIZONTAL * Cell.SIZE + Cell.SIZE,
                    SnakeWorld.VERTICAL * Cell.SIZE + Cell.SIZE, 0.1);
        }
        //hard
        else if(key.equals("h")) {
            this.bigBang(SnakeWorld.HORIZONTAL * Cell.SIZE + Cell.SIZE,
                    SnakeWorld.VERTICAL * Cell.SIZE + Cell.SIZE, 0.05);
        }
        else {
            //do nothing
        }
    }
    //how snake moves each tick
    public void onTick() {
        if(this.direction.equals("left")) {
            this.snake.spos = new Posn(this.snake.spos.x - 1,
                    this.snake.spos.y);
            this.direction = "left";
            this.growSnake();
        }
        else if(this.direction.equals("up")) {
            this.snake.spos = new Posn(this.snake.spos.x,
                    this.snake.spos.y - 1);
            this.direction = "up";
            this.growSnake();
        }
        else if(this.direction.equals("right")) {
            this.snake.spos = new Posn(this.snake.spos.x + 1,
                    this.snake.spos.y);
            this.direction = "right";
            this.growSnake();
        }
        else if(this.direction.equals("stop")) {
            //pause game
        }
        else {
            this.snake.spos = new Posn(this.snake.spos.x,
                    this.snake.spos.y + 1);
            this.direction = "down";
            this.growSnake();
        }
    }
    //ends the game
    public WorldEnd worldEnds() {
        if(this.snake.outOfBounds() || this.snake.collision()) {
            return new WorldEnd(true, this.lastImage(""));
        }
        else {
            return new WorldEnd(false, this.makeImage());
        }
    }
    //draw this snake world
    public WorldImage makeImage() {
        return new OverlayImages(new OverlayImages(this.snake.snakeImage(),
                this.food.foodImage()), new TextImage(new Posn(
                        SnakeWorld.HORIZONTAL * Cell.SIZE * 3 / 4
                        + Cell.SIZE / 2,
                        SnakeWorld.VERTICAL * Cell.SIZE * 3 / 4
                        + Cell.SIZE / 2),
                        "score: " + this.score, Color.blue));
    }
}
//examples
class ExamplesSnake {
    //cells
    Cell c1, c2, c3, c4;
    //food
    Food f1, f2, f3, f4;
    //snakes
    Snake s1, s2, s3, s4;
    //world of snake
    SnakeWorld w1;
    //initial state
    void initial() {
        //cells
        this.c1 = new Cell(new Posn(0, 0));
        this.c2 = new Cell(new Posn(1, 0));
        this.c3 = new Cell(new Posn(0, 1));
        this.c4 = new Cell(new Posn(1, 1));
        //food
        this.f1 = new Food(new Posn(0, 0));
        this.f2 = new Food(new Posn(1, 0));
        this.f3 = new Food(new Posn(0, 1));
        this.f4 = new Food(new Posn(1, 1));
        //snakes
        this.s1 = new Snake();
        this.s2 = new Snake();
        //world of snake
        this.w1 = new SnakeWorld();
    }
    //run animation
    void testRunAnimation(Tester t) {
        this.initial();
        this.w1.bigBang(SnakeWorld.HORIZONTAL * Cell.SIZE + Cell.SIZE,
                SnakeWorld.VERTICAL * Cell.SIZE + Cell.SIZE, 0.05);
    }
}
