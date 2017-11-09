// tester
import tester.*;

// utility
import java.util.*;

// colors
import java.awt.Color;

// big bang stuff
import javalib.impworld.*;
import javalib.worldimages.*;

// a single square unit of the game
class Cell {
    
    // length of one side of a Cell
    public static int SIZE = 8;
    
    // position of the cell in the world
    Posn cpos;
    
    // the color of the cell
    Color color;
    
    // construct Cell
    Cell(Posn cpos, Color color) {
        this.cpos = cpos;
        this.color = color;
    }
    
    // draw this Cell
    WorldImage cellImage() {
        return new RectangleImage(new Posn(
                this.cpos.x * Cell.SIZE + Cell.SIZE / 2,
                this.cpos.y * Cell.SIZE + Cell.SIZE / 2), Cell.SIZE, Cell.SIZE,
                this.color);
    }
}

// a player character (whose avatar is a snake)
class Character {
    
    // position of the head of the snake
    Posn hpos;
    
    // the snake's entire body
    LinkedList<Cell> body;
    
    // the snake's body, not including its head
    LinkedList<Cell> body2;
    
    // the snake's color
    Color color;
    
    // is this character facing north, south, east, or west?
    String direction;
    
    // construct Character
    Character(Posn hpos, Color color, String direction) {
        this.hpos = hpos;
        this.body = new LinkedList<Cell>();
        this.body2 = new LinkedList<Cell>();
        this.color = color;
        this.direction = direction;
        this.body.addFirst(new Cell(hpos, color));
    }
    
    // grow this Character
    void growCharacter() {
        this.body.addFirst(new Cell(this.hpos, this.color));
        this.body2.addFirst(this.body.get(1));
    }
    
    // is this character colliding with its own body?
    boolean collision() {
        boolean helper = false;
        for(Cell cell : this.body2) {
            helper = this.hpos.x == cell.cpos.x &&
                     this.hpos.y == cell.cpos.y || helper;
        }
        return helper;
    }
    
    // change the color of the Cell at that Posn in this Character's body
    void animateExplosion(Posn posn) {
        for(Cell cell : this.body) {
            if(cell.cpos.x == posn.x && cell.cpos.y == posn.y) {
                cell.color = Color.orange;
            }
            else {
                //do nothing
            }
        }
    }
    
    // draw this Character
    WorldImage characterImage() {
        WorldImage helper = new CircleImage(new Posn(
                this.hpos.x * Cell.SIZE + Cell.SIZE / 2,
                this.hpos.y * Cell.SIZE + Cell.SIZE / 2), 1, this.color);
        for(Cell cell : this.body) {
            helper = new OverlayImages(helper, cell.cellImage());
        }
        return helper;
    }
}

// the world characters live in
class CharacterWorld extends World {
    
    // horizontal width of the world
    public static int HORIZONTAL = 32;
    
    // horizontal length of the world
    public static int VERTICAL = 32;
    
    // SpeedSnake is a multiplayer game with two players
    Character player1;
    Character player2;
    
    // player1's score
    int score1;
    
    // player2's score
    int score2;
    
    // construct CharacterWorld
    CharacterWorld() {
        this.player1 = new Character(new Posn(0, CharacterWorld.VERTICAL),
                Color.cyan, "east");
        this.player2 = new Character(new Posn(CharacterWorld.HORIZONTAL, 0),
                Color.green, "west");
        this.score1 = this.player1.body.size() * 10;
        this.score2 = this.player2.body.size() * 10;
    }
    
    // does that player collide with the walls or with itself?
    boolean collision1(Character player) {
        return player.hpos.x < 0 ||
               player.hpos.x > CharacterWorld.HORIZONTAL ||
               player.hpos.y < 0 ||
               player.hpos.y > CharacterWorld.VERTICAL ||
               player.collision();
    }
    
    // does player1 collide with player2 or the wall?
    boolean collision2() {
        boolean helper = false;
        for(Cell cell : this.player2.body2) {
            helper = this.player1.hpos.x == cell.cpos.x &&
                     this.player1.hpos.y == cell.cpos.y || helper;
        }
        helper = this.collision1(this.player1) || helper;
        return helper;
    }
    
    // does player2 collide with player1 or the wall?
    boolean collison3() {
        boolean helper = false;
        for(Cell cell : this.player1.body2) {
            helper = this.player2.hpos.x == cell.cpos.x &&
                     this.player2.hpos.y == cell.cpos.y || helper;
        }
        helper = this.collision1(this.player2) || helper;
        return helper;
    }
    
    // player controls
    public void onKeyEvent(String key) {
        if(key.equals("left")) {
            this.player1.direction = "west";
        }
        else if(key.equals("up")) {
            this.player1.direction = "north";
        }
        else if(key.equals("right")) {
            this.player1.direction = "east";
        }
        else if(key.equals("down")) {
            this.player1.direction = "south";
        }
        else if(key.equals("a")) {
            this.player2.direction = "west";
        }
        else if(key.equals("w")) {
            this.player2.direction = "north";
        }
        else if(key.equals("d")) {
            this.player2.direction = "east";
        }
        else if(key.equals("s")) {
            this.player2.direction = "south";
        }
        else {
            //do nothing
        }
    }
    
    // something happens ever tick
    public void onTick() {
        
        // for player1
        if(this.collision2()) {
            this.player1.animateExplosion(this.player1.body.getFirst().cpos);
        }
        else if(this.player1.direction.equals("west")) {
            this.player1.hpos = new Posn(this.player1.hpos.x - 1,
                    this.player1.hpos.y);
            this.player1.growCharacter();
        }
        else if(this.player1.direction.equals("north")) {
            this.player1.hpos = new Posn(this.player1.hpos.x,
                    this.player1.hpos.y - 1);
            this.player1.growCharacter();
        }
        else if(this.player1.direction.equals("east")) {
            this.player1.hpos = new Posn(this.player1.hpos.x + 1,
                    this.player1.hpos.y);
            this.player1.growCharacter();
        }
        else {
            this.player1.hpos = new Posn(this.player1.hpos.x,
                    this.player1.hpos.y + 1);
            this.player1.growCharacter();
        }
        
        // for player2
        if(this.collison3()) {
            this.player1.animateExplosion(this.player2.body.getFirst().cpos);
            this.player2.animateExplosion(this.player2.body.getFirst().cpos);
        }
        else if(this.player2.direction.equals("west")) {
            this.player2.hpos = new Posn(this.player2.hpos.x - 1,
                    this.player2.hpos.y);
            this.player2.growCharacter();
        }
        else if(this.player2.direction.equals("north")) {
            this.player2.hpos = new Posn(this.player2.hpos.x,
                    this.player2.hpos.y - 1);
            this.player2.growCharacter();
        }
        else if(this.player2.direction.equals("east")) {
            this.player2.hpos = new Posn(this.player2.hpos.x + 1,
                    this.player2.hpos.y);
            this.player2.growCharacter();
        }
        else {
            this.player2.hpos = new Posn(this.player2.hpos.x,
                    this.player2.hpos.y + 1);
            this.player2.growCharacter();
        }
        
        // update scores
        this.score1 = this.player1.body.size() * 10;
        this.score2 = this.player2.body.size() * 10;
    }
    
    // draw world
    public WorldImage makeImage() {
        WorldImage helper = new RectangleImage(new Posn(
                CharacterWorld.HORIZONTAL * Cell.SIZE / 2 + Cell.SIZE / 2,
                CharacterWorld.VERTICAL * Cell.SIZE / 2 + Cell.SIZE / 2),
                CharacterWorld.HORIZONTAL * Cell.SIZE + Cell.SIZE,
                CharacterWorld.VERTICAL * Cell.SIZE + Cell.SIZE,
                Color.darkGray);
        helper = new OverlayImages(new OverlayImages(helper,
                this.player2.characterImage()), this.player1.characterImage());
        helper = new OverlayImages(helper, new TextImage(new Posn(
                CharacterWorld.HORIZONTAL * Cell.SIZE * 7 / 8,
                CharacterWorld.VERTICAL * Cell.SIZE * 7 / 8),
                "player 1: " + this.score1, Color.magenta));
        helper = new OverlayImages(helper, new TextImage(new Posn(
                CharacterWorld.HORIZONTAL * Cell.SIZE / 8,
                CharacterWorld.VERTICAL * Cell.SIZE / 8),
                "player 2: " + this.score2, Color.magenta));
        return helper;
    }
}

// examples
class ExamplesSpeedSnake {
    
    // world for characters to play in
    CharacterWorld w1;
    
    // initial state
    void initial() {
        this.w1 = new CharacterWorld();
    }
    
    // run animation
    void testRunAnimation(Tester t) {
        this.initial();
        this.w1.bigBang(CharacterWorld.HORIZONTAL * Cell.SIZE + Cell.SIZE,
                CharacterWorld.VERTICAL * Cell.SIZE + Cell.SIZE, 0.2);
    }
}
