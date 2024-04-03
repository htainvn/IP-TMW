import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public Main() {
        Player[] players = new Player[10];
        players[0] = new Player(1, "Alice", 100);
        players[1] = new Player(2, "Bob", 90);
        players[2] = new Player(3, "Charlie", 80);
        players[3] = new Player(4, "David", 70);
        players[4] = new Player(5, "Eve", 60);
        players[5] = new Player(6, "Frank", 50);
        players[6] = new Player(7, "Grace", 40);
        players[7] = new Player(8, "Heidi", 30);
        players[8] = new Player(9, "Ivan", 20);
        players[9] = new Player(10, "Judy", 10);
//        new Client(30);
        new Leaderboard(players);
    }

    public static void main(String[] args) {
        new Main();
    }
}