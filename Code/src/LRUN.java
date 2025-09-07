public class LRUN extends ExtraTool{
    private final boolean directionIsNeeded = true;
    public LRUN(){super("Long-range unidirectional scanner", "Lr", true);}
    public String useTool(Player player, String direction){
        if(player.addEnergy(-2)) {

            Map map = player.getCurrentMap();
            int currentRow = player.getCurrentPosition().getRow();
            int currentColumn = player.getCurrentPosition().getColumn();
            int count = 0;
            Cell processingCell = player.getCurrentPosition();
            boolean end = false;
            do {
                switch (direction) {
                    case "top":
                        currentRow -= 1;
                        if (map.isExisting(currentRow, currentColumn)) {
                            processingCell = map.getCell(currentRow, currentColumn);
                        } else end = true;
                        break;
                    case "bottom":
                        currentRow += 1;
                        if (map.isExisting(currentRow, currentColumn)) {
                            processingCell = map.getCell(currentRow, currentColumn);
                        } else end = true;
                        break;
                    case "left":
                        currentColumn -= 1;
                        if (map.isExisting(currentRow, currentColumn)) {
                            processingCell = map.getCell(currentRow, currentColumn);
                        } else end = true;
                        break;
                    case "right":
                        currentColumn += 1;
                        if (map.isExisting(currentRow, currentColumn)) {
                            processingCell = map.getCell(currentRow, currentColumn);
                        } else end = true;
                        break;
                }
                if (!processingCell.isContainsCollectible() && !end) {
                    count += 1;
                }
            } while (!processingCell.isContainsCollectible() && !end);
//        return count;
            int distance = (int) (Math.random() * (0.4 * count + 1) + 0.8 * count);
            switch (direction){
                case "top": if (distance != 1) return "From above, the nearest object/world wall is at a distance of "+ distance +" cells";
                            else return "From above, the nearest object/world wall is 1 cell away";
                case "bottom": if(distance != 1) return "From below, the nearest object/world wall is at a distance of "+ distance +" cells";
                               else return "From below, the nearest object/world wall is 1 cell away";
                case "left": if(distance != 1) return "To the left, the nearest object/world wall is at a distance of " + distance+ " cells";
                             else return "To the left, the nearest object/world wall is 1 cell away";
                case "right": if(distance != 1) return  "To the right, the nearest object/world wall is at a distance of "+ distance+ " cells";
                              else return "To the right, the nearest object/world wall is 1 cell away";
                default: return "The direction is incorrect";
            }
        }else return "Not enough energy";
    }
}
