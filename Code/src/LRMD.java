public class LRMD extends ExtraTool{
    public LRMD(){super("Long range mass detector","Lm", true);}
    private final boolean directionIsNeeded = true;
    public String useTool(Player player, String direction){
        if(player.addEnergy(-2)) {

            Map map = player.getCurrentMap();
            int currentRow = player.getCurrentPosition().getRow();
            int currentColumn = player.getCurrentPosition().getColumn();
            int count = 0;
            Cell processingCell = player.getCurrentPosition();
            boolean end = false;
            do {
                switch (direction){
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
                if (processingCell.isContainsCollectible() && !end) {
                    count += 1;
                }
            } while (!end);
//        return count;
            int quantity = (int) (Math.random() * (0.2 * count + 1) + 0.9 * count);
            switch (direction){
                case "top": if(quantity != 1) return "There are " + quantity + " objects at the top ";
                            else return "There is 1 object at the top";
                case "bottom": if (quantity != 1) return "There are " + quantity + " objects at the bottom";
                               else return "There is 1 object at the bottom";
                case "left": if(quantity != 1) return "There are "+ quantity +" objects on the left";
                             else  return "There is 1 object on the left";
                case "right": if(quantity != 1) return "There are " + quantity + " objects on the right";
                              else return "There is 1 object on the right";
                default: return "The direction is incorrect";
            }
        }else return "Not enough energy";
    }
}
