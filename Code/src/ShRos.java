import java.util.ArrayList;

public class ShRos extends ExtraTool{
    private final boolean directionIsNeeded = false;
    public ShRos(){
        super("Short-range omnidirectional scanner","Sh", false);
    }

    public String useTool(Player player) {
        if (player.addEnergy(-1)) {
            int countOfObjects = 0;
            ArrayList<Cell> adjacentCells = player.getCurrentPosition().getAdjacentCells();
            for (Cell cell : adjacentCells) {
                if (cell.isContainsMine() || cell.isContainsTool() || cell.isContainsGrenade() || cell.isContainsEnergy()) {
                    countOfObjects += 1;
                }
            }
            if(countOfObjects != 1) return "There are " + countOfObjects + " objects around";
            else return "There is 1 object around";
        }else return "Not enough energy";
    }
}
