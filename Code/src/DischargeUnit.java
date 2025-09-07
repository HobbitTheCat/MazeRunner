public class DischargeUnit extends ExtraTool{
    private final boolean directionIsNeeded = true;
    public DischargeUnit(){super("DischargeUnit","Du", true);}

    public String useTool(Player player, String direction){
        if(player.addEnergy(-2)) {
            if(player.getCurrentPosition().getCellByDirection(direction)!=null){
                if(!player.getCurrentPosition().existWall(direction)){
                    if(player.getCurrentPosition().getCellByDirection(direction).isContainsMine()){
                        if(player.getCurrentPosition().getCellByDirection(direction).getMine().isCharged()){
                            player.getCurrentPosition().getCellByDirection(direction).getMine().discharge();
                            return "The mine was successfully discharged";
                        } else return "The mine has already been discharged";
                    } else return "Cell does not contain mine";
                } else return "Cell is separated by a wall";
            } else return "Cell does not exist";
        }
        else return "Not enough energy";
    }
}
