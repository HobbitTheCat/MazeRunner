import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {
    private Map map;
    private Player player;
    private boolean error;
    private String path = "Output.txt";

    public Map getMap(){return this.map;}
    public Player getPlayer(){return this.player;}
    private boolean getError(){return this.error;}
    public String getPath(){return this.path;}

    private void setMap(Map map){this.map = map;}
    private void setPlayer(Player player){this.player = player;}
    private void setError(boolean error){this.error = error;}
    private void setPath(String path){this.path = path;}

    public Game(Player player, Map map, String pathOutputFile){
        this.setMap(map);
        this.setPlayer(player);
        this.setPath(pathOutputFile);
    }
    public Game(Player player, Map map){
        this.setMap(map);
        this.setPlayer(player);
    }

    public void play(boolean notepad){
        long startTime = System.currentTimeMillis();
        Scanner Lire = new Scanner(System.in);
        Output output = new Output(this);
        output.outputConsole("Enter your name: ");
        this.getPlayer().setName(Lire.next());
        while (!this.getPlayer().getVictory()  && !this.getPlayer().getDeath()){
            this.setError(false);
            if(notepad) output.outputMap(false);
            else output.outputMapToConsole(false);

            output.outputConsole(this.getPlayer().toString());
            String [] option = this.getPlayer().getAvailableActionsString();

            for (int index = 0; index != option.length; index++){
                if(option[index].equals("Take tool"))
                    output.outputConsole((index+1) + ". Take " + this.getPlayer().getCurrentPosition().getTool().getName());
                else
                    output.outputConsole((index+1) + ". " + option[index]);
            }
            String action = Lire.next();

            try{
                int index = Integer.parseInt(action);
                index -= 1;
                try {
                    action = option[index];
                }catch (ArrayIndexOutOfBoundsException e){
                    output.outputConsole("Unexpected index");
                    this.setError(true);}
            }catch (NumberFormatException ignored){}

            if(!this.getError()) {
                action = action.toLowerCase();
                switch (action) {
                    case "go":
                        String status1 = this.getPlayer().go(Game.getDirection());
                        output.outputConsole(status1);
                        if(status1.equals("You exploded")) output.animationKab00m(this.getPlayer().getCurrentPosition());
                        break;
                    case "use tool":
                        String [] tools = this.getPlayer().getAvailableToolString();
                        for(int i = 0; i!= tools.length; i++){
                            output.outputConsole((i+1) +". " + tools[i]);
                        }
                        try{
                            int index = Lire.nextInt()-1;
                            if(index >= 0 && index < tools.length){
                                if(this.getPlayer().directionIsNeeded(index)){
                                    String direction = Game.getDirection();
                                    String status2 = this.getPlayer().useTool(index,direction);
                                    if(status2.equals("success") && notepad){output.animationBreak(direction, this.getPlayer().getCurrentPosition());}
                                    else if(status2.equals("You exploded") && notepad){output.animationKab00m(direction, this.getPlayer().getCurrentPosition());}
                                    else output.outputConsole(status2);
                                }else output.outputConsole(this.getPlayer().useTool(index));
                            }
                            else output.outputConsole("Unexpected index");
                        }catch (InputMismatchException e){
                            output.outputConsole("Unexpected index");
                        }
                        break;
                    case "throw grenade":
                        String direction = Game.getDirection();
                        String status = this.getPlayer().throwGrenade(direction);
                        output.outputConsole(status);
                        if(status.equals("Success") && notepad){output.animationBreak(direction, this.getPlayer().getCurrentPosition());}
                        else if(status.equals("You exploded") && notepad){output.animationKab00m(direction, this.getPlayer().getCurrentPosition());}
                        break;
                    case "take energy":
                        output.outputConsole(this.getPlayer().takeEnergy());
                        break;
                    case "take grenade":
                        output.outputConsole(this.getPlayer().takeGrenade());
                        break;
                    case "take tool":
                        output.outputConsole(this.getPlayer().takeTool());
                        break;
                    case "show map":
                        if(notepad) {
                            output.outputMap(true);
                            output.outputMapToConsole(true);
                            try {TimeUnit.SECONDS.sleep(3);} catch (InterruptedException ignore) {}
                        }else output.outputMapToConsole(true);
                        break;
                }
            }
            output.outputConsole("");
        }
        output.outputMap(true);
        long endTime = System.currentTimeMillis();
        int minutes = (int)(endTime - startTime) / (1000 * 60);
        int seconds = (int)((endTime - startTime) / 1000) % 60;
        if(this.getPlayer().getVictory()){
            output.saveScore(minutes, seconds);
            String prevScore = output.getPreviousScore();
            if(notepad) output.output(prevScore);
            else output.outputConsole(prevScore);

        }

    }

    public void testMode(){
        Excavator ex = new Excavator();
        Output output = new Output(this);
        while(!this.getPlayer().getVictory()){
            output.outputMap(false);
            player.addEnergy(8);
            String direction = Game.getDirection();
            output.outputConsole(ex.useTool(player, direction));
            output.outputConsole(player.go(direction));
        }
        output.outputMap(false);
    }

    public static String getDirection(){
        Scanner Lire = new Scanner(System.in);
        System.out.println("Choose a direction: \n1. Top \n2. Bottom \n3. Left \n4. Right");
        String direction = Lire.next();
        direction = direction.toLowerCase();
        return switch (direction) {
            case "1", "top" -> "top";
            case "2", "bottom" -> "bottom";
            case "3", "left" -> "left";
            case "4", "right" -> "right";
            default -> "error";
        };
    }
    public void secretMode(){
        if(this.getMap().getHeight() == 7 && this.getMap().getWidth() == 15){
            Output output = new Output(this);
            Excavator ex = new Excavator();
            String[] list = new String[]{"top", "left", "right","top","top","right","right","bottom","bottom","top","right","top","right","right",
            "right","right","bottom","bottom","bottom","bottom","bottom","bottom","left","top","top","top","top","top","top","left",
            "bottom","bottom","left","bottom","right","bottom","left","left","bottom","right","right","bottom",
            "left","left","left","left","top","top","top","left","bottom","left","right","bottom","bottom","left","left",
            "left","top","top","top","bottom","bottom","bottom","left","left","top","top","bottom","bottom","left","left",
            "top","top","top","top","top","top","right","right","bottom","bottom","top","top","right","right","bottom","bottom",
            "top","top","right","right"};
            for(int i = 0; i < list.length; i++){
                output.outputMap(false);
                player.addEnergy(8);
                String direction = list[i];
                System.out.println(ex.useTool(player, direction));
                System.out.println(player.go(direction));
                try {
                    TimeUnit.MILLISECONDS.sleep(120);
                }catch (InterruptedException ignored){}
            }
            output.outputMap(false);
        }
    }
}
