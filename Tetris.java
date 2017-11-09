// tester
import tester.*;

// utility
import java.util.*;

// colors
import java.awt.Color;

// big bang stuff
import javalib.impworld.*;
import javalib.worldimages.*;

// a square unit of a tetra
class Cell {
    
    // the length of one side of a cell
    static int SIZE = 16;
    
    // the position of the cell in the world
    Posn cpos;
    
    // the cell's color
    Color color;
    
    // construct Cell
    Cell(Posn cpos, Color color) {
        this.cpos = cpos;
        this.color = color;
    }
    
    // is this cell above the ceiling of the world?
    boolean atCeiling() {
        return this.cpos.y == -1;
    }
    
    // rotate this cell 90 degrees clockwise around that center position
    void rotateCell(Posn center) {
        if (this.cpos.x == center.x) {
            this.cpos = new Posn(this.cpos.x - this.cpos.y + center.y,
                    center.y);
        }
        else if (this.cpos.y == center.y) {
            this.cpos = new Posn(center.x,
                    this.cpos.y + this.cpos.x - center.x);
        }
        else if (this.cpos.x < center.x && this.cpos.y > center.y) {
            this.cpos = new Posn(center.x - this.cpos.y + center.y,
                    center.y - center.x + this.cpos.x);
        }
        else if (this.cpos.x < center.x && this.cpos.y < center.y) {
            this.cpos = new Posn(center.x + center.y - this.cpos.y,
                    center.y - center.x + this.cpos.x);
        }
        else if (this.cpos.x > center.x && this.cpos.y > center.y) {
            this.cpos = new Posn(center.x - this.cpos.y + center.y, 
                    center.y + this.cpos.x - center.x);
        }
        else {
            this.cpos = new Posn(center.x + center.y - this.cpos.y,
                    center.y + this.cpos.x - center.x);
        }
    }
    
    // return this cell's new position after rotation around that center position
    Posn newPosn(Posn center) {
        if (this.cpos.x == center.x) {
            return new Posn(this.cpos.x - this.cpos.y + center.y, center.y);
        }
        else if (this.cpos.y == center.y) {
            return new Posn(center.x, this.cpos.y + this.cpos.x - center.x);
        }
        else if (this.cpos.x < center.x && this.cpos.y > center.y) {
            return new Posn(center.x - this.cpos.y + center.y,
                    center.y - center.x + this.cpos.x);
        }
        else if (this.cpos.x < center.x && this.cpos.y < center.y) {
            return new Posn(center.x + center.y - this.cpos.y,
                    center.y - center.x + this.cpos.x);
        }
        else if (this.cpos.x > center.x && this.cpos.y > center.y) {
            return new Posn(center.x - this.cpos.y + center.y, 
                    center.y + this.cpos.x - center.x);
        }
        else {
            return new Posn(center.x + center.y - this.cpos.y,
                    center.y + this.cpos.x - center.x);
        }
    }
    
    // this cell after falling for one tick
    void fall() {
        this.cpos = new Posn(this.cpos.x, this.cpos.y + 1);
    }
    
    // slide this cell to the left
    void slideLeft() {
        this.cpos = new Posn(this.cpos.x - 1, this.cpos.y);
    }
    
    // slide this cell to the right
    void slideRight() {
        this.cpos = new Posn(this.cpos.x + 1, this.cpos.y);
    }
    
    // has this cell hit the floor?
    boolean hitFloor() {
        return this.cpos.y == TetrisWorld.VERTICAL;
    }
    
    // has this cell hit the left wall?
    boolean hitLeftWall() {
        return this.cpos.x == 0;
    }
    
    // has this cell hit the right wall?
    boolean hitRightWall() {
        return this.cpos.x == TetrisWorld.HORIZONTAL;
    }
    
    // is this cell on top of that cell?
    boolean onTop(Cell that) {
        return this.cpos.x == that.cpos.x &&
               this.cpos.y == that.cpos.y - 1;
    }
    
    // is this cell on the right of that cell
    boolean onRight(Cell that) {
        return this.cpos.x == that.cpos.x + 1 &&
               this.cpos.y == that.cpos.y;
    }
    
    // is this cell on the left of that cell
    boolean onLeft(Cell that) {
        return this.cpos.x == that.cpos.x - 1 &&
               this.cpos.y == that.cpos.y;
    }
    
    // is this cell at that position?
    boolean atPosn(Posn that) {
        return that.x == this.cpos.x &&
               that.y == this.cpos.y;
    }
    
    // is this cell overlayed over or under that cell?
    boolean overlayed(Cell cell) {
        return this.atPosn(cell.cpos);
    }
    
    // draw this Cell
    public WorldImage cellImage() {
        return new RectangleImage(new Posn(
                this.cpos.x * Cell.SIZE + Cell.SIZE / 2,
                this.cpos.y * Cell.SIZE + Cell.SIZE / 2), Cell.SIZE, Cell.SIZE,
                this.color);
    }
}

// a tetra is composed of four cells
class Tetra {
    
    // position of the tetra in the world
    Posn tpos;
    
    // color of the tetra
    Color color;
    
    // four cells making up a tetra
    Cell first;
    Cell second;
    Cell third;
    Cell fourth;
    
    // construct Tetra
    Tetra(Posn tpos) {
        this.tpos = tpos;
        int random = new Random().nextInt(7);
        if (random == 0) {
            this.color = Color.cyan;
        }
        else if (random == 1) {
            this.color = Color.orange;
        }
        else if (random == 2) {
            this.color = Color.yellow;
        }
        else if (random == 3) {
            this.color = Color.green;
        }
        else if (random == 4) {
            this.color = Color.red;
        }
        else if (random == 5) {
            this.color = Color.blue;
        }
        else {
            this.color = Color.magenta;
        }
        if (random == 0) {
            this.first = new Cell(new Posn(tpos.x, tpos.y - 1), this.color);
            this.second = new Cell(new Posn(tpos.x + 1, tpos.y - 1),
                    this.color);
            this.third = new Cell(new Posn(tpos.x + 2, tpos.y - 1),
                    this.color);
            this.fourth = new Cell(new Posn(tpos.x + 3, tpos.y - 1),
                    this.color);
        }
        else if (random == 1) {
            this.first = new Cell(new Posn(tpos.x, tpos.y - 1), this.color);
            this.second = new Cell(new Posn(tpos.x + 1, tpos.y - 1),
                    this.color);
            this.third = new Cell(new Posn(tpos.x + 2, tpos.y - 1),
                    this.color);
            this.fourth = new Cell(new Posn(tpos.x + 2, tpos.y - 2),
                    this.color);
        }
        else if (random == 2) {
            this.first = new Cell(new Posn(tpos.x, tpos.y - 1), this.color);
            this.second = new Cell(new Posn(tpos.x + 1, tpos.y - 1),
                    this.color);
            this.third = new Cell(new Posn(tpos.x + 1, tpos.y - 2),
                    this.color);
            this.fourth = new Cell(new Posn(tpos.x + 2, tpos.y - 1),
                    this.color);
        }
        else if (random == 3) {
            this.first = new Cell(new Posn(tpos.x + 1, tpos.y - 1),
                    this.color);
            this.second = new Cell(new Posn(tpos.x + 2, tpos.y - 1),
                    this.color);
            this.third = new Cell(new Posn(tpos.x, tpos.y - 2), this.color);
            this.fourth = new Cell(new Posn(tpos.x + 1, tpos.y - 2),
                    this.color);
        }
        else if (random == 4) {
            this.first = new Cell(new Posn(tpos.x, tpos.y - 1),
                    this.color);
            this.second = new Cell(new Posn(tpos.x + 1, tpos.y - 1),
                    this.color);
            this.third = new Cell(new Posn(tpos.x, tpos.y - 2), this.color);
            this.fourth = new Cell(new Posn(tpos.x + 1, tpos.y - 2),
                    this.color);
        }
        else if (random == 5) {
            this.first = new Cell(new Posn(tpos.x, tpos.y - 2), this.color);
            this.second = new Cell(new Posn(tpos.x, tpos.y - 1),
                    this.color);
            this.third = new Cell(new Posn(tpos.x + 1, tpos.y - 1),
                    this.color);
            this.fourth = new Cell(new Posn(tpos.x + 2, tpos.y - 1),
                    this.color);
        }
        else {
            this.first = new Cell(new Posn(tpos.x + 1, tpos.y - 2),
                    this.color);
            this.second = new Cell(new Posn(tpos.x + 2, tpos.y - 2),
                    this.color);
            this.third = new Cell(new Posn(tpos.x, tpos.y - 1), this.color);
            this.fourth = new Cell(new Posn(tpos.x + 1, tpos.y - 1),
                    this.color);
        }
    }
    
    // rotate this Tetra 90 degrees clockwise
    Tetra rotateTetra() {
        this.first.rotateCell(new Posn(this.tpos.x, this.tpos.y - 1));
        this.second.rotateCell(new Posn(this.tpos.x, this.tpos.y - 1));
        this.third.rotateCell(new Posn(this.tpos.x, this.tpos.y - 1));
        this.fourth.rotateCell(new Posn(this.tpos.x, this.tpos.y - 1));
        return this;
    }
    
    // return list of new Cell Posn's after rotation of this Tetra
    ArrayList<Posn> newPosn() {
        ArrayList<Posn> helper = new ArrayList<Posn>();
        helper.add(this.first.newPosn(new Posn(this.tpos.x, this.tpos.y - 1)));
        helper.add(this.second.newPosn(new Posn(this.tpos.x, this.tpos.y - 1)));
        helper.add(this.third.newPosn(new Posn(this.tpos.x, this.tpos.y - 1)));
        helper.add(this.fourth.newPosn(new Posn(this.tpos.x, this.tpos.y - 1)));
        return helper;
    }
    
    // has this Tetra hit the floor?
    boolean hitFloor() {
        return this.first.hitFloor() || this.second.hitFloor() ||
               this.third.hitFloor() || this.fourth.hitFloor();
    }
    
    // has this Tetra hit the left wall?
    boolean hitLeftWall() {
        return this.first.hitLeftWall() || this.second.hitLeftWall() ||
               this.third.hitLeftWall() || this.fourth.hitLeftWall();
    }
    
    // has this Tetra hit the right wall?
    boolean hitRightWall() {
        return this.first.hitRightWall() || this.second.hitRightWall() ||
               this.third.hitRightWall() || this.fourth.hitRightWall();
    }
    
    // is this Tetra on top of that Tetra?
    boolean onTop(Tetra that) {
        return this.first.onTop(that.first) ||
               this.first.onTop(that.second) ||
               this.first.onTop(that.third) ||
               this.first.onTop(that.fourth) ||
               this.second.onTop(that.first) ||
               this.second.onTop(that.second) ||
               this.second.onTop(that.third) ||
               this.second.onTop(that.fourth) ||
               this.third.onTop(that.first) ||
               this.third.onTop(that.second) ||
               this.third.onTop(that.third) ||
               this.third.onTop(that.fourth) ||
               this.fourth.onTop(that.first) ||
               this.fourth.onTop(that.second) ||
               this.fourth.onTop(that.third) ||
               this.fourth.onTop(that.fourth);
    }
    
    // is this Tetra on the right of that Tetra?
    boolean onRight(Tetra that) {
        return this.first.onRight(that.first) ||
               this.first.onRight(that.second) ||
               this.first.onRight(that.third) ||
               this.first.onRight(that.fourth) ||
               this.second.onRight(that.first) ||
               this.second.onRight(that.second) ||
               this.second.onRight(that.third) ||
               this.second.onRight(that.fourth) ||
               this.third.onRight(that.first) ||
               this.third.onRight(that.second) ||
               this.third.onRight(that.third) ||
               this.third.onRight(that.fourth) ||
               this.fourth.onRight(that.first) ||
               this.fourth.onRight(that.second) ||
               this.fourth.onRight(that.third) ||
               this.fourth.onRight(that.fourth);
    }
    
    // is this Tetra on the left of that Tetra?
    boolean onLeft(Tetra that) {
        return this.first.onLeft(that.first) ||
               this.first.onLeft(that.second) ||
               this.first.onLeft(that.third) ||
               this.first.onLeft(that.fourth) ||
               this.second.onLeft(that.first) ||
               this.second.onLeft(that.second) ||
               this.second.onLeft(that.third) ||
               this.second.onLeft(that.fourth) ||
               this.third.onLeft(that.first) ||
               this.third.onLeft(that.second) ||
               this.third.onLeft(that.third) ||
               this.third.onLeft(that.fourth) ||
               this.fourth.onLeft(that.first) ||
               this.fourth.onLeft(that.second) ||
               this.fourth.onLeft(that.third) ||
               this.fourth.onLeft(that.fourth);
    }
    
    // is this Tetra overlayed over or under that Tetra?
    boolean overlayed(Tetra that) {
        return this.first.overlayed(that.first) ||
               this.first.overlayed(that.second) ||
               this.first.overlayed(that.third) ||
               this.first.overlayed(that.fourth) ||
               this.second.overlayed(that.first) ||
               this.second.overlayed(that.second) ||
               this.second.overlayed(that.third) ||
               this.second.overlayed(that.fourth) ||
               this.third.overlayed(that.first) ||
               this.third.overlayed(that.second) ||
               this.third.overlayed(that.third) ||
               this.third.overlayed(that.fourth) ||
               this.fourth.overlayed(that.first) ||
               this.fourth.overlayed(that.second) ||
               this.fourth.overlayed(that.third) ||
               this.fourth.overlayed(that.fourth);
    }
    
    // this tetra after falling for one tick
    void fall() {
        if (!this.hitFloor()) {
            this.tpos = new Posn(this.tpos.x, this.tpos.y + 1);
            this.first.fall();
            this.second.fall();
            this.third.fall();
            this.fourth.fall();
        }
    }
    
    // slide this tetra to the left
    void slideLeft() {
        this.tpos = new Posn(this.tpos.x - 1, this.tpos.y);
        this.first.slideLeft();
        this.second.slideLeft();
        this.third.slideLeft();
        this.fourth.slideLeft();
    }
    
    // slide this tetra to the right
    void slideRight() {
        this.tpos = new Posn(this.tpos.x + 1, this.tpos.y);
        this.first.slideRight();
        this.second.slideRight();
        this.third.slideRight();
        this.fourth.slideRight();
    }
    
    // draw this Tetra
    WorldImage tetraImage() {
        return new OverlayImages(new OverlayImages(this.first.cellImage(),
                this.second.cellImage()),
                new OverlayImages(this.third.cellImage(),
                        this.fourth.cellImage()));
    }
}

// world of Tetris
class TetrisWorld extends World {
    
    // horizontal width of the world
    static int HORIZONTAL = 15;
    
    // vertical length of the world
    static int VERTICAL = 31;
    
    // whichever tetra that is currently falling
    Tetra tetra;
    
    // the pile of cells on the floor of the world
    LinkedList<Cell> cellList;
    
    // is the world paused?
    boolean isPaused;
    
    // construct this TetrisWorld
    TetrisWorld() {
        this.tetra = new Tetra(new Posn(new Random().nextInt(
                TetrisWorld.HORIZONTAL), 0));
        this.cellList = new LinkedList<Cell>();
        this.isPaused = false;
    }
    
    // is the first tetra in a list of tetras on top of any tetra in the list of
    // tetras?
    boolean onTop() {
        boolean helper = false;
        for (Cell cell : this.cellList) {
            helper = this.tetra.first.onTop(cell) ||
                     this.tetra.second.onTop(cell) ||
                     this.tetra.third.onTop(cell) ||
                     this.tetra.fourth.onTop(cell) || helper;
        }
        return helper;
    }
    
    // has the first tetra in the list of tetras hit the left wall?
    boolean hitLeftWall() {
        return this.tetra.hitLeftWall();
    }
    
    // has the first tetra in the list of tetras hit the right wall?
    boolean hitRightWall() {
        return this.tetra.hitRightWall();
    }
    
    // is the first tetra in a list of tetras on the right of any tetra in the
    // list of tetras?
    boolean onRight() {
        boolean helper = false;
        for (Cell cell : this.cellList) {
            helper = this.tetra.first.onRight(cell) ||
                     this.tetra.second.onRight(cell) ||
                     this.tetra.third.onRight(cell) ||
                     this.tetra.fourth.onRight(cell) || helper;
        }
        return helper;
    }
    
    // is the first tetra in a list of tetras on the left of any tetra in the
    // list of tetras?
    boolean onLeft() {
        boolean helper = false;
        for (Cell cell : this.cellList) {
            helper = this.tetra.first.onLeft(cell) ||
                     this.tetra.second.onLeft(cell) ||
                     this.tetra.third.onLeft(cell) ||
                     this.tetra.fourth.onLeft(cell) || helper;
        }
        return helper;
    }
    
    // if the Tetra is rotated, will any Cell in the tetra be overlayed or
    // underlayed on another cell in the list of Cells?
    boolean overlayed() {
        boolean helper = false;
        for (Cell cell : this.cellList) {
            for (Posn posn : this.tetra.newPosn()) {
                helper = cell.cpos.x == posn.x &&
                         cell.cpos.y == posn.y ||
                         helper;
            }
        }
        return helper;
    }
    
    // if the Tetra is rotated, will any Cell in the tetra be out of the world?
    boolean outOfWorld() {
        boolean helper = false;
        for (Posn posn : this.tetra.newPosn()) {
            helper = posn.x < 0 || posn.x > TetrisWorld.HORIZONTAL ||
                     posn.y > TetrisWorld.VERTICAL || helper;
        }
        return helper;
    }
    
    // move Tetra
    public void onKeyEvent(String key) {
        if (key.equals("left") && !this.onRight() && !this.hitLeftWall() &&
            !this.isPaused) {
            this.tetra.slideLeft();
        }
        else if (key.equals("right") && !this.onLeft() &&
                 !this.hitRightWall() && !this.isPaused) {
            this.tetra.slideRight();
        }
        else if (key.equals("down") && !this.tetra.hitFloor() &&
                 !this.onTop() && !this.isPaused) {
            this.tetra.fall();
        }
        else if (key.equals("s") && !this.isPaused && !this.overlayed() &&
                 !this.outOfWorld()) {
            this.tetra.rotateTetra();
        }
        else if (key.equals("p") && !this.isPaused) {
            this.isPaused = true;
        }
        else if (key.equals("p") && this.isPaused) {
            this.isPaused = false;
        }
    }
    
    // move tetra every tick
    // have a new tetra enter the world if the current one has hit the floor
    // if a row is formed then remove it from the world
    public void onTick() {
        if (this.isPaused) {
            //do nothing
        }
        else if (this.tetra.hitFloor() || this.onTop()) {
            this.cellList.add(this.tetra.first);
            this.cellList.add(this.tetra.second);
            this.cellList.add(this.tetra.third);
            this.cellList.add(this.tetra.fourth);
            this.tetra = new Tetra(new Posn(new Random().nextInt(
                    TetrisWorld.HORIZONTAL), 0));
            this.tetra.fall();
        }
        else if (this.rowFormed2()) {
            for (int i = 0; i < TetrisWorld.VERTICAL + 2; i++) {
                if (this.rowFormed(i)) {
                    this.removeRow(i);
                    for (Cell cell : this.cellList) {
                        if (i > cell.cpos.y) {
                            cell.fall();
                        }
                    }
                }
            }
        }
        else {
            this.tetra.fall();
        }
    }
    
    // has a row been formed at y?
    boolean rowFormed(int y) {
        LinkedList<Cell> helper = new LinkedList<Cell>();
        for (Cell cell : this.cellList) {
            if (cell.cpos.y == y) {
                helper.add(cell);
            }
        }
        return helper.size() == TetrisWorld.HORIZONTAL + 1;
    }
    
    // has a row been formed anywhere in this tetris world?
    boolean rowFormed2() {
        boolean helper = false;
        for (int i = 0; i < TetrisWorld.VERTICAL + 2; i++) {
            helper = this.rowFormed(i) || helper;
        }
        return helper;
    }
    
    // remove the row formed at y from this tetris world
    void removeRow(int y) {
        LinkedList<Cell> helper = new LinkedList<Cell>();
        for (Cell cell : this.cellList) {
            if (cell.cpos.y != y) {
                helper.add(cell);
            }
        }
        this.cellList = helper;
    }
    
    // has a column been formed at x?
    boolean columnFormed(int x) {
        LinkedList<Cell> helper = new LinkedList<Cell>();
        for (Cell cell : this.cellList) {
            if (cell.cpos.x == x) {
                helper.add(cell);
            }
        }
        return helper.size() == TetrisWorld.VERTICAL + 1;
    }
    
    // has a column been formed anywhere in this tetris world?
    boolean columnFormed2() {
        boolean helper = false;
        for (int i = 0; i < TetrisWorld.HORIZONTAL + 2; i++) {
            helper = this.columnFormed(i) || helper;
        }
        return helper;
    }
    
    // is any cell in cellList at the ceiling of the world?
    boolean atCeiling() {
        boolean helper = false;
        for (Cell cell : this.cellList) {
            helper = cell.atCeiling() || helper;
        }
        return helper;
    }
    
    // ends the game
    public WorldEnd worldEnds() {
        if (this.atCeiling()) {
            return new WorldEnd(true, this.lastImage(""));
        }
        else {
            return new WorldEnd(false, this.makeImage());
        }
    }
    
    // draw this TetrisWorld
    public WorldImage makeImage() {
        WorldImage helper = new RectangleImage(new Posn(
                TetrisWorld.HORIZONTAL * Cell.SIZE / 2 + Cell.SIZE / 2,
                TetrisWorld.VERTICAL * Cell.SIZE / 2 + Cell.SIZE / 2),
                TetrisWorld.HORIZONTAL * Cell.SIZE,
                TetrisWorld.VERTICAL * Cell.SIZE, Color.white);
        if (this.cellList.isEmpty()) {
            helper = new OverlayImages(helper, this.tetra.tetraImage());
        }
        else {
            for (Cell cell : this.cellList) {
                helper = new OverlayImages(helper,
                        new OverlayImages(this.tetra.tetraImage(),
                        cell.cellImage()));
            }
        }
        return helper;
    }
}

// examples
class ExamplesTetris {
    
    // tetris worlds
    TetrisWorld w1;
    
    // initial state
    void initial() {
        this.w1 = new TetrisWorld();
    }
    
    // run animation
    void testRunAnimation(Tester t) {
        this.initial();
        this.w1.bigBang(TetrisWorld.HORIZONTAL * Cell.SIZE + Cell.SIZE,
                TetrisWorld.VERTICAL * Cell.SIZE + Cell.SIZE, 0.2);
    }
}
