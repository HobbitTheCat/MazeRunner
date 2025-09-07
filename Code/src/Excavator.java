public class Excavator extends ExtraTool{

    public Excavator(){
        super("Excavator","Ex", true);
    }
    public String useTool(Player player, String direction){
        if(player.addEnergy(-8)) {
            String response = player.getCurrentPosition().breakWall(direction);
            if(response.equals(direction + " wall is already broken")){
                try {
                    if (player.getCurrentPosition().getCellByDirection(direction).isContainsMine()) {
                        if (player.getCurrentPosition().getCellByDirection(direction).getMine().isCharged()) {
                            player.explode();
                            return "You exploded";
                        }
                    }
                }catch (NullPointerException ignore){}
            }
            return response;
        }else return "Not enough energy";
    }
}
