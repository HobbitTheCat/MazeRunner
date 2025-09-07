import java.util.ArrayList;

public class Cell {
    private int row, column;
    private double probabilityOfMine, probabilityOfTool;
    private Map currentMap;
    private Mine mine;
    private EnergySource energySource;
    private GrenadeSource grenadeSource;
    private ExtraTool tool;
    private boolean[] walls = new boolean[] {true,true,true,true};
    private boolean containPlayer, isVisible;

    /**
     *Accessors
     */
    public int getRow() {return this.row;}
    public int getColumn() {return this.column;}

    public boolean isContainsMine(){return this.mine != null;}
    public Mine getMine(){return this.mine;}
    public double getProbabilityOfMine(){return this.probabilityOfMine;}

    public boolean isContainsTool(){return this.tool != null;}
    public ExtraTool getTool(){return this.tool;}
    public double getProbabilityOfTool() {return this.probabilityOfTool;}

    public boolean isContainsEnergy(){return  this.energySource != null;}
    public EnergySource getEnergySource(){return this.energySource;}

    public boolean isContainsGrenade(){return this.grenadeSource != null;}
    public GrenadeSource getGrenadeSource(){return this.grenadeSource;}

    public boolean isContainsCollectible(){
        return this.isContainsEnergy() || this.isContainsMine() || this.isContainsTool() || this.isContainsGrenade();
    }
    public Map getCurrentMap() {return this.currentMap;}

    public boolean[] getWalls() {return this.walls;}

    public boolean isContainPlayer(){return  this.containPlayer;}
    public boolean isVisible(){return this.isVisible;}

    public void setCoordinates(int row, int column){
        this.row = row;
        this.column = column;
    }
    public void setMine(Mine mine){this.mine = mine;}
    public void setProbabilityOfMine(double probability){this.probabilityOfMine = probability;}

    public void setEnergySource(EnergySource energySource){this.energySource = energySource;}
    public void setGrenadeSource(GrenadeSource grenadeSource){this.grenadeSource = grenadeSource;}
    public void setTool(ExtraTool tool){this.tool = tool;}
    public void setProbabilityOfTool(double probability){this.probabilityOfTool = probability;}
    private void setCurrentMap(Map map){this.currentMap = map;}
    public void deleteWall(int index){this.getWalls()[index] = false;}
    public void setContainPlayer(boolean containPlayer){
        this.containPlayer = containPlayer;
        if(containPlayer) this.setVisible(true);
    }
    public void setVisible(boolean isVisible){this.isVisible = isVisible;}

    /**
     *Constructor
     */
    public Cell(int row, int column, Map map){
        this.setCoordinates(row, column);
        this.setCurrentMap(map);

        int distRow = this.getRow() - map.getCenterRow();
        int distColumn = this.getColumn() - map.getCenterColumn();
        double max_probability = (100.0/(map.getHeight()* map.getWidth()))/100;
//        z = a(x^2+y^2)/(b+x^2+y^2)
        double mine_prob = (max_probability*(distRow*distRow+distColumn*distColumn))/(Math.max(map.getHeight(),map.getWidth()) + distRow*distRow + distColumn*distColumn);
        double tool_prob = -1 * mine_prob + 2*max_probability;
//        double tool_prob = Math.exp(0.5 * this.getDistanceToCenter() * this.getDistanceToCenter());
        this.setProbabilityOfMine(mine_prob+ max_probability);
        this.setProbabilityOfTool(tool_prob);
    }

    /**
     *To String Methods
     */

    public String getContain() {
        if(this.isContainsEnergy()){
            if(this.getEnergySource().getAmountEnergy() > 0) {
                return "E";}
        } else if (this.isContainsGrenade()) {
            if(this.getGrenadeSource().getAmountGrenade() > 0){
            return "G";}
        } else if (this.isContainsMine()){
            return this.getMine().toString();
        } else if (this.isContainsTool()) {
            return this.getTool().toString();
        }
        return " ";
    }

        /**
     * Methods
     */

    public ArrayList<Cell> getAdjacentCells(){
        int row = this.getRow();
        int column = this.getColumn();
        ArrayList<Cell> adjacentCell = new ArrayList<Cell>();
        if(this.currentMap.isExisting(row+1, column)){
            adjacentCell.add(this.getCurrentMap().getCell(row+1, column));
        }
        if(this.currentMap.isExisting(row-1, column)){
            adjacentCell.add(this.getCurrentMap().getCell(row-1, column));
        }
        if(this.currentMap.isExisting(row, column+1)){
            adjacentCell.add(this.getCurrentMap().getCell(row,column+1));
        }
        if(this.currentMap.isExisting(row, column-1)){
            adjacentCell.add(this.getCurrentMap().getCell(row,column-1));
        }
        return adjacentCell;
    }
    public boolean isAdjacent(Cell other){
        return this.getAdjacentCells().contains(other);
    }

    public Cell getCellByDirection(String direction){
        Map map = this.getCurrentMap();
        int row = this.getRow();
        int column = this.getColumn();
        switch (direction){
            case "top": row -= 1; break;
            case "bottom": row += 1; break;
            case "left": column -= 1; break;
            case "right": column +=1; break;
        }
        if(map.isExisting(row,column)) return map.getCell(row, column);
        else return null;
    }
    public boolean existWall(String direction){
        return switch (direction) {
            case "top" -> this.getWalls()[0];
            case "bottom" -> this.getWalls()[2];
            case "left" -> this.getWalls()[3];
            case "right" -> this.getWalls()[1];
            default -> false;
        };
    }

    public String breakWall(String direction){
        int row = this.getRow();
        int column = this.getColumn();
        switch (direction){
            case "top":
                if(this.walls[0]){
                    row-=1;
                    if(this.getCurrentMap().isExisting(row,column)) {
                        this.walls[0] = false;
                        this.getCurrentMap().getCell(row,column).breakWall("bottom");
                        this.setVisible(true);
                    }else return direction + " is a border wall";
                }else return direction + " wall is already broken";
                break;

            case "bottom":
                if(this.walls[2]){
                    row+=1;
                    if(this.getCurrentMap().isExisting(row,column)) {
                        this.walls[2] = false;
                        this.getCurrentMap().getCell(row,column).breakWall("top");
                        this.setVisible(true);
                    }else return direction + " is a border wall";
                }else return direction + " wall is already broken";
                break;

            case "left":
                if(this.walls[3]){
                    column -=1;
                    if(this.getCurrentMap().isExisting(row,column)) {
                        this.walls[3] = false;
                        this.getCurrentMap().getCell(row,column).breakWall("right");
                        this.setVisible(true);
                    }else return direction + " is a border wall";
                }else return direction + " wall is already broken";
                break;

            case "right":
                if(this.walls[1]){
                    column +=1;
                    if(this.getCurrentMap().isExisting(row,column)) {
                        this.walls[1] = false;
                        this.getCurrentMap().getCell(row,column).breakWall("left");
                        this.setVisible(true);
                    }else return direction + " is a border wall";
                }else return direction + " wall is already broken";
                break;
            default: return "Error input";
        }

        return "success";
    }

    public String repairWall(String direction){
        int row = this.getRow();
        int column = this.getColumn();
        switch (direction){
            case "top":
                if(!this.walls[0]){
                    row-=1;
                    if(this.getCurrentMap().isExisting(row,column)) {
                        this.walls[0] = true;
                        if(this.walls[1] && this.walls[2] && this.walls[3]) this.setVisible(false);
                    }else return direction + " is a border wall";
                }else return " wall is intact";
                break;
            case "bottom":
                if(!this.walls[2]){
                    row+=1;
                    if(this.getCurrentMap().isExisting(row,column)) {
                        this.walls[2] = true;
                        if(this.walls[0] && this.walls[1] &&  this.walls[3]) this.setVisible(false);
                    }else return direction + " is a border wall";
                }else return direction + " wall is intact";
                break;
            case "left":
                if(!this.walls[3]){
                    column -=1;
                    if(this.getCurrentMap().isExisting(row,column)) {
                        this.walls[3] = true;
                        if(this.walls[0] && this.walls[1] && this.walls[2]) this.setVisible(false);
                    }else return direction + " is a border wall";
                }else return direction + " wall is intact";
                break;
            case "right":
                if(!this.walls[1]){
                    column +=1;
                    if(this.getCurrentMap().isExisting(row,column)) {
                        this.walls[1] = true;
                        if(this.walls[0] && this.walls[2] && this.walls[3]) this.setVisible(false);
                    }else return direction + " is a border wall";
                }else return direction + " wall is intact";
                break;
            default: return "Error input";
        }
        return "success";
    }
    public boolean isVisibleWithWallOn(String direction){
        boolean [] walls = this.getWalls().clone();
        switch (direction){
            case "top": walls[0] = true; break;
            case "bottom": walls[2] = true; break;
            case "left": walls[3] = true; break;
            case "right": walls[1] = true; break;
        }
        for (boolean wall : walls) if (!wall) return true;
        return false;
    }

    public  String[] getLayers(Boolean visible){
        if(visible){
            ArrayList<Cell> adjacentCells = this.getAdjacentCells();
            boolean [] adjacentVisibility = new boolean[adjacentCells.size()];
            for(int count = 0; count < adjacentCells.size(); count++){
                adjacentVisibility[count] = adjacentCells.get(count).isVisible();
                adjacentCells.get(count).setVisible(true);
            }
            boolean visibility = this.isVisible();
            this.setVisible(true);

            String [] layers = this.getLayers();
            this.setVisible(visibility);
            for(int count = 0; count < adjacentCells.size(); count++){
                adjacentCells.get(count).setVisible(adjacentVisibility[count]);
            }
            return layers;
        }
        return this.getLayers();
    }
    public String[] getLayers(){
        ArrayList<String> layers = new ArrayList<>();
        boolean[] visibility = new boolean[]{this.isVisibleByDirection("left"), this.isVisibleDiagonal(), this.isVisibleByDirection("top")};
        String first, second, third;

        if(this.isVisible()) {
            first = "       ";
            third = "       ";

            if(!this.isContainPlayer()) {
                second = "   " + this.getContain();
                if (this.getContain().length() > 1) second += "  ";
                else second += "   ";
            }else {
                first = "  ,";
                second = "  |" + this.getContain();
                if (this.getContain().length() > 1) {
                    second += "| ";
                    first += "__, ";
                } else {
                    first += "_,  ";
                    second += "|  ";
                }
            }

            if (visibility[0] && visibility[1] && visibility[2]) if(!this.existWall("top")) layers.add("┼       "); else layers.add("┼───────");
            if (visibility[0] && visibility[1] && !visibility[2]) layers.add("┼───────");
            if (visibility[0] && !visibility[1] && visibility[2]) if(!this.existWall("top")) layers.add("┼       "); else layers.add("┼───────");
            if (visibility[0] && !visibility[1] && !visibility[2]) layers.add("┬───────");
            if (!visibility[0] && visibility[1] && visibility[2]) if(!this.existWall("top")) layers.add("┼       "); else layers.add("┼───────");
            if (!visibility[0] && visibility[1] && !visibility[2]) layers.add("┼───────");
            if (!visibility[0] && !visibility[1] && visibility[2]) if(!this.existWall("top")) layers.add("├       "); else layers.add("├───────");
            if (!visibility[0] && !visibility[1] && !visibility[2]) layers.add("┌───────");
            if(!this.existWall("left")){
                layers.add(" " + first);
                layers.add(" " + second);
                layers.add(" " + third);
            }else {
                layers.add("│" + first);
                layers.add("│" + second);
                layers.add("│" + third);
            }


            if(this.getCellByDirection("right") == null ){
                if(this.getCellByDirection("top") != null)
                    if(visibility[2]) layers.set(0, layers.get(0) + "┤");
                    else layers.set(0, layers.get(0) + "┐");
                else layers.set(0, layers.get(0) + "┐");
                if(!this.existWall("right")){
                    layers.set(1,layers.get(1) + " ");
                    layers.set(2,layers.get(2) + " ");
                    layers.set(3,layers.get(3) + " ");
                }else {
                    layers.set(1, layers.get(1) + "│");
                    layers.set(2, layers.get(2) + "│");
                    layers.set(3, layers.get(3) + "│");
                }
            }
            if(this.getCellByDirection("bottom") == null){
                if(this.getCellByDirection("right") != null){
                  if(this.getCellByDirection("left") != null && visibility[0]) layers.add("┴───────");
                  else layers.add("└───────");
                } else {
                    if (visibility[0]) layers.add("┴───────┘");
                    else layers.add("└───────┘");
                }
            }else layers.add(null);
        }else {
            first = "███████";
            second = "███████";
            third = "███████";

            if (visibility[0] && visibility[1] && visibility[2]) layers.add("┼───────");
            if (visibility[0] && visibility[1] && !visibility[2]) layers.add("┤███████");
            if (visibility[0] && !visibility[1] && visibility[2]) layers.add("┼───────");
            if (visibility[0] && !visibility[1] && !visibility[2]) layers.add("┐███████");
            if (!visibility[0] && visibility[1] && visibility[2]) layers.add("┴───────");
            if (!visibility[0] && visibility[1] && !visibility[2]) layers.add("┘███████");
            if (!visibility[0] && !visibility[1] && visibility[2]) layers.add("└───────");
            if (!visibility[0] && !visibility[1] && !visibility[2]) layers.add("████████");
            if(visibility[0]){
                layers.add("│" + first);
                layers.add("│" + second);
                layers.add("│" + third);
            }else {
                layers.add("█" + first);
                layers.add("█" + second);
                layers.add("█" + third);
            }

            if(this.getCellByDirection("right") == null ){
                if (this.getCellByDirection("top") != null && visibility[2]) layers.set(0, layers.get(0) + "┘");
                else layers.set(0, layers.get(0) + "█");
                layers.set(1,layers.get(1) + "█");
                layers.set(2,layers.get(2) + "█");
                layers.set(3,layers.get(3) + "█");
            }

            if(this.getCellByDirection("bottom") == null){

                if(visibility[0]) layers.add("┘███████");
                else layers.add("████████");
                if(this.getCellByDirection("right") == null) layers.set(4, layers.get(4) + "█");
            }else layers.add(null);

        }
        return layers.toArray(new String[0]);
    }

    private boolean isVisibleDiagonal(){
        return this.getCurrentMap().getCell(this.getRow()-1, this.getColumn()-1) != null && this.getCurrentMap().getCell(this.getRow()-1, this.getColumn()-1).isVisible();
    }
    public boolean isVisibleByDirection(String direction){return this.getCellByDirection(direction) != null && this.getCellByDirection(direction).isVisible();}
}
