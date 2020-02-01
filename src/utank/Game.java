package utank;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game extends JFrame {
    final static int WIDTH = 500, HEIGHT = 500;

    List<Thing> everyThing = new ArrayList<>();
    List<Wall> walls = new ArrayList<>();
    Player player1 = new Player();
    Player player2 = new Player();
    List<Shot> shotsInTheAir = new ArrayList<>();

    public Game() {
        this.setSize(Game.WIDTH, Game.HEIGHT);

         Wall leftEdge = new Wall(20, 20, Game.HEIGHT, true);
         this.everyThing.add(leftEdge);
         this.walls.add(leftEdge);
//         Wall topEdge = new Wall(20, 20, Game.WIDTH, false);
//         this.everyThing.add(topEdge);
//         this.walls.add(topEdge);
//         // other walls
//        Wall leftEdge = new Wall(0, 0, Game.HEIGHT, true);
//        this.everyThing.add(leftEdge);
//        this.walls.add(leftEdge);
//        Wall topEdge = new Wall(0, 20, Game.WIDTH, false);
//        this.everyThing.add(topEdge);
//        this.walls.add(topEdge);
//        Wall rightEdge = new Wall(600, 0, Game.HEIGHT, true);
//        this.everyThing.add(rightEdge);
//        this.walls.add(rightEdge);
//        Wall bottomEdge = new Wall(0, 600, Game.WIDTH, false);
//        this.everyThing.add(bottomEdge);
//        this.walls.add(bottomEdge);
        this.player1.newRound(false, (int) Math.round(Math.random() * Game.WIDTH), (int) Math.round(Math.random() * Game.HEIGHT));
        this.player2.newRound(false, (int) Math.round(Math.random() * Game.WIDTH), (int) Math.round(Math.random() * Game.HEIGHT));
        this.everyThing.add(player1.getTank());
        this.everyThing.add(player2.getTank());
    }

    public void updateState() {
        Tank p1Tank = (Tank) this.player1.getTank();
        Tank p2Tank = (Tank) this.player2.getTank();
        for (Shot shot : this.shotsInTheAir) {
            for (Wall wall : this.walls) {
                if (wall.contacts(shot)) {
                    shot.bounceAgainst(wall);
                }
                shot.step();
            }
            if (p1Tank.contacts(shot)) {
                this.everyThing.remove(p1Tank);
                this.everyThing.remove(p2Tank);
                this.player1.newRound(false, (int) Math.round(Math.random() * Game.WIDTH), (int) Math.round(Math.random() * Game.HEIGHT));
                this.player2.newRound(true, (int) Math.round(Math.random() * Game.WIDTH), (int) Math.round(Math.random() * Game.HEIGHT));
                this.everyThing.add(player1.getTank());
                this.everyThing.add(player2.getTank());
                // ... code to handle new round
            }
            if (p2Tank.contacts(shot)) {
                this.everyThing.remove(p2Tank);
                this.everyThing.remove(p1Tank);
                this.player1.newRound(true, (int) Math.round(Math.random() * Game.WIDTH), (int) Math.round(Math.random() * Game.HEIGHT));
                this.player2.newRound(false, (int) Math.round(Math.random() * Game.WIDTH), (int) Math.round(Math.random() * Game.HEIGHT));
                this.everyThing.add(player1.getTank());
                this.everyThing.add(player2.getTank());
                // code to handle new round
            }
        }
        this.shotsInTheAir.forEach(Shot::growOld);
        List<Shot> tempShotsInTheAir = new ArrayList<>(shotsInTheAir);
        for (Shot shot : tempShotsInTheAir) {
            if (shot.isDead()) {
                this.shotsInTheAir.remove(shot);
                this.everyThing.remove(shot);
            }
        }


        GameActionListener listener = (GameActionListener) this.getKeyListeners()[0];
        if (listener.p1Move && this.walls.stream().noneMatch(wall -> wall.contacts(p1Tank))) {
            p1Tank.step();
        }
        if (listener.p1Left)
            p1Tank.turnLeft();
        if (listener.p1Right)
            p1Tank.turnRight();
        if (listener.p1Fire) {
            Shot shotP1 = new Shot(p1Tank.getGunX(), p1Tank.getGunY(), (float) p1Tank.getDirection());
            this.shotsInTheAir.add(shotP1);
            this.everyThing.add(shotP1); ////////////
            shotP1.step();
            shotP1.growOld();

        }
        if (listener.p2Move && this.walls.stream().noneMatch(wall -> wall.contacts(p2Tank))) {
            p2Tank.step();
        }
        if (listener.p2Left)
            p2Tank.turnLeft();
        if (listener.p2Right)
            p2Tank.turnRight();
        if (listener.p2Fire) {
            Shot shotP2 = new Shot(p2Tank.getGunX(), p2Tank.getGunY(), (float) p2Tank.getDirection());
            this.shotsInTheAir.add(shotP2);
            this.everyThing.add(shotP2);
            shotP2.step();
        }
    }


    public void paint(Graphics graphics) {
        super.paint(graphics);
        this.everyThing.forEach(thing -> thing.draw(graphics));
        Toolkit.getDefaultToolkit().sync();
    }
}
