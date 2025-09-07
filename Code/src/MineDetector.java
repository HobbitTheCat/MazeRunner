import java.util.ArrayList;

public class MineDetector extends ExtraTool {
    private final boolean directionIsNeeded = false;
    public MineDetector() {
        super("Mine detector","Md", false);
    }

    public String useTool(Player player) {
        if (player.addEnergy(-3)) {
            ArrayList<Cell> adjacentCells = player.getCurrentPosition().getAdjacentCells();
            int countOfMine = 0;
            for (Cell cell : adjacentCells) {
                if (cell.isContainsMine()) {
                    countOfMine += 1;
                }
            }
            if(countOfMine != 1) return "There are " + countOfMine + " mines around";
            return "There is 1 mine around";
        }else return "Not enough energy";
    }
}