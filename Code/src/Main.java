public class Main {
    public static void main(String[] args) {
        Main.forPresentation();
//        Map m1 = new Map(9,11, 00, 20, 15, 10 ,10);
//        Player p1 = new Player(m1, 20, 10);
//        Game g1 = new Game(p1, m1);
////        System.out.println(m1.toString());
////        g1.testMode();
//        g1.play(true);
    }

    public static void forPresentation(){
        Map m1 = new Map(5,5, 10, 4, 4, 10 ,10);
        Player p1 = new Player(m1, 20, 10);
        Game g1 = new Game(p1, m1);
        g1.play(false);
    }
}