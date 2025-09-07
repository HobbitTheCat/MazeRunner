import java.util.ArrayList;
import java.util.List;

public class Player {
    private Cell currentPosition;
    private Map currentMap;
    private int energy, grenade;
    private int maxEnergy = 20, maxGrenade = 10;
    private boolean victory, death, takeEnergy, takeGrenade, takeTool;
    private ArrayList<ExtraTool> tools = new ArrayList<>(List.of(new LRUN(), new MineDetector()));
    private String name;

    public Cell getCurrentPosition() {return this.currentPosition;}
    public int getEnergy() {return this.energy;}
    public String getName(){return this.name;}
    public int getGrenade() {return this.grenade;}
    private int getMaxEnergy(){return this.maxEnergy;}
    private int getMaxGrenade(){return this.maxGrenade;}
    public Map getCurrentMap() {return this.currentMap;}
    public boolean getVictory() {return this.victory;}
    public boolean getDeath() {return this.death;}
    public boolean canTakeEnergy() {return this.takeEnergy;}
    public boolean canTakeGrenade() {return this.takeGrenade;}
    public boolean canTakeTool() {return this.takeTool;}
    private ArrayList<ExtraTool> getTools(){return this.tools;}

    private void setCurrentPosition(Cell newPosition) {
        this.currentPosition.setContainPlayer(false);
        this.currentPosition = newPosition;
        this.currentPosition.setContainPlayer(true);
    }
    private void setCurrentMap(Map map) {this.currentMap = map;}
    private void setMaxEnergy(int maxEnergy){if(maxEnergy > 0){this.maxEnergy = maxEnergy;}else throw new RuntimeException("Max energy must be positive");}
    private void setMaxGrenade(int maxGrenade){if(maxGrenade > 0){this.maxGrenade = maxGrenade;}else throw new RuntimeException("Max grenade must be positive");}
    private void setEnergy(int energy) {
        if (energy >= 0) {
            this.energy = energy;
        } else throw new RuntimeException("Energy can't be negative");
    }
    private void setGrenade(int grenade) {
        if (grenade > 0) {
            this.grenade = grenade;
        } else throw new RuntimeException("Grenade can't be negative");
    }
    public boolean addEnergy(int energy) {
        if (this.energy + energy >= 0 && this.energy + energy <= this.getMaxEnergy()) {
            this.energy += energy;
            return true;
        } else return false;
    }
    public boolean addGrenade(int grenade) {
        if (this.grenade + grenade >= 0 && this.grenade + grenade <= this.getMaxGrenade()) {
            this.grenade += grenade;
            return true;
        } else return false;
    }
    public boolean addTool(ExtraTool tool){
        if(!this.tools.contains(tool)){
            this.tools.add(tool);
            return true;
        }else return false;
    }
    public void setName(String name){this.name = name;}
    private void setVictory() {this.victory = true;}
    private void setDeath() {
        this.getCurrentPosition().setContainPlayer(false);
        this.death = true;
    }

    private void setTakeEnergy(boolean takeEnergy) {this.takeEnergy = takeEnergy;}
    private void setTakeGrenade(boolean takeGrenade) {this.takeGrenade = takeGrenade;}
    private void setTakeTool(boolean takeTool) {this.takeTool = takeTool;}
    private void resetCanTake(){
        if(this.getCurrentPosition().isContainsCollectible()){
            if(this.getCurrentPosition().isContainsEnergy())
                this.setTakeEnergy(this.getCurrentPosition().getEnergySource().getAmountEnergy() > 0);
            else this.setTakeEnergy(false);

            if (this.getCurrentPosition().isContainsGrenade())
                this.setTakeGrenade(this.getCurrentPosition().getGrenadeSource().getAmountGrenade() > 0);
            else this.setTakeGrenade(false);

            this.setTakeTool(this.getCurrentPosition().isContainsTool());
        }else {
            this.setTakeTool(false);
            this.setTakeGrenade(false);
            this.setTakeEnergy(false);
        }
    }

    public Player(Map currentMap, int maxEnergy, int maxGrenade) {
        this(currentMap);
        this.setMaxEnergy(maxEnergy);
        this.setMaxGrenade(maxGrenade);
        this.setEnergy(maxEnergy);
        this.setGrenade(maxGrenade);

    }

    public Player(Map currentMap) {
        this.setName(name);
        this.setCurrentMap(currentMap);
        this.currentPosition = this.getCurrentMap().getCell(currentMap.getCenterRow(), currentMap.getCenterColumn());
        this.getCurrentPosition().setContainPlayer(true);

        this.setEnergy(this.getMaxEnergy());
        this.setGrenade(this.getMaxGrenade());
    }

/**
*To String Methods
*/

    public String toString(){
        String player = "Name: " + this.getName();
        player += "\nEnergy: ";
        for(int i =0; i != this.getEnergy(); i++){player+="#";}
        for(int i =this.getEnergy(); i < this.getMaxEnergy(); i++){player += " ";}
        player += "  " + this.getEnergy();
        player+="\nGrenade: ";
        for(int i =0; i < this.getGrenade(); i++){player += "#";}
        for(int i =this.getGrenade(); i < this.getMaxGrenade(); i++){player += " ";}
        player += "  " + this.getGrenade();
        player += "\nDeath: " + this.getDeath();
        player += "\nVictory: " + this.getVictory();
        return player;
    }
    public String[] getAvailableToolString(){
        ArrayList<String> availableToolString = new ArrayList<>();
        for (ExtraTool tool:this.getTools()){
            availableToolString.add(tool.getName());
        }
        return availableToolString.toArray(new String[0]);
    }
    public String[] getAvailableActionsString(){
        ArrayList <String> option = new ArrayList<>();
        option.add("Go");
        if(this.getGrenade() > 0) option.add("Throw grenade");
        option.add("Use tool");
        if(this.canTakeEnergy()) option.add("Take energy");
        if(this.canTakeGrenade()) option.add("Take grenade");
        if(this.canTakeTool()) option.add("Take tool");
        option.add("Show map");
        return option.toArray(new String[0]);
    }


    public String go(String direction) {
        this.resetCanTake();
        Cell currentPosition = this.getCurrentPosition();
        if (!currentPosition.existWall(direction)) {
            Cell newPosition = currentPosition.getCellByDirection(direction);
            if (newPosition == null) {
                this.setVictory();
                this.getCurrentPosition().setContainPlayer(false);
                return "Victory";
            }
            if (newPosition == this.getCurrentPosition()) return "Error input";

            this.setCurrentPosition(newPosition);
            if (this.getCurrentPosition().isContainsMine() && this.getCurrentPosition().getMine().isCharged()) {
                this.explode();
                return "You exploded";
            }
            this.resetCanTake();
            return "Success";
        } else return "Wall " + direction;
    }


    public void explode(){this.setDeath();}

    public String throwGrenade(String direction) {
        if (this.addGrenade(-1)){
            if (this.getCurrentPosition().existWall(direction)) {
                String status = this.getCurrentPosition().breakWall(direction);
                if(status.equals("success")) {
                    if (this.getCurrentPosition().getCellByDirection(direction).isContainsMine()) {
                        if (this.getCurrentPosition().getCellByDirection(direction).getMine().isCharged()) {
                            this.setDeath();
                            return "You exploded";
                        }
                    } else return "Success";
                }else return status;
            } else return "Grenade was lost";
        }return "Not enough grenades";
    }


    public String takeEnergy(){
        if(this.canTakeEnergy()){
            if(!(this.getEnergy() == this.getMaxEnergy())) {
                if (this.addEnergy(this.getCurrentPosition().getEnergySource().takeEnergy(this.getMaxEnergy() - this.getEnergy()))) {
                    this.resetCanTake();
                    return "Success";
                }
            }else return "Already full energy";
        }
        return "Can't take energy";
    }
    public String takeGrenade(){
        if(this.canTakeGrenade()){
            if(!(this.getGrenade() == this.getMaxGrenade())) {
                if (this.addGrenade(this.getCurrentPosition().getGrenadeSource().takeGrenade(this.getMaxGrenade() - this.getGrenade()))) {
                    this.resetCanTake();
                    return "Success";
                }
            }else return "Already full grenade";
        }
        return "Can't take grenade";
    }
    public String takeTool(){
        if(this.canTakeTool()){
            boolean alreadyContain = false;
            for(int i = 0; i!= this.getTools().size(); i++) if(this.getCurrentPosition().getTool().equals(this.getTools().get(i))) alreadyContain = true;

            if(!alreadyContain) {
                if (this.addTool(this.getCurrentPosition().getTool())) {
                    this.getCurrentPosition().setTool(null);
                    this.resetCanTake();
                    return "Success";
                }
            } return "Already contain " + this.getCurrentPosition().getTool().getName();
        }
        return "Can't take tool";
    }

    public String useTool(int index){
        ExtraTool tool = this.getTools().get(index);
        if(tool.toString().equals("Md")){
            MineDetector mineDetector = (MineDetector) tool;
            return mineDetector.useTool(this);
        }
        if(tool.toString().equals("Sh")){
            ShRos shRos = (ShRos) tool;
            return shRos.useTool(this);
        }

        throw new RuntimeException("Unknown tool" + tool.getName());
    }

    public String useTool(int index, String direction){
        ExtraTool tool = this.getTools().get(index);
        if(tool.toString().equals("Du")){
            DischargeUnit dischargeUnit = (DischargeUnit) tool;
            return dischargeUnit.useTool(this, direction);}
        if(tool.toString().equals("Ex")){
            Excavator excavator = (Excavator) tool;
            return excavator.useTool(this, direction);}
        if(tool.toString().equals("Lm")){
            LRMD lrmd = (LRMD) tool;
            return lrmd.useTool(this, direction);}
        if(tool.toString().equals("Lr")){
            LRUN lrun = (LRUN) tool;
            return lrun.useTool(this, direction);}

        throw new RuntimeException("Unknown tool" + tool.getName());
    }

    public boolean directionIsNeeded(int index){
        ExtraTool tool = this.tools.get(index);
        return tool.directionIsNeeded();
    }
}