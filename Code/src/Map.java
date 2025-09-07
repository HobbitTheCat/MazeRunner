import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {
    private Cell [][] cells;
    private int centerRow, centerColumn;
    private int height, width;
    private int amountEnergy, amountGrenade;
    private int numberOfMine, numberOfEnergySource, numberOfGrenadeSource;
    private ArrayList<Cell> alreadyUsed = new ArrayList<Cell>();
    public ArrayList<ExtraTool> typeOfTool = new ArrayList<ExtraTool>(List.of(new ShRos(), new LRMD(), new Excavator(), new DischargeUnit()));

/**
*Accessors
*/
    public Cell getCell(int row, int column){
        if(this.isExisting(row, column))
            return this.cells[row][column];
        else return null;
    }
    public int getCenterRow(){return this.centerRow;}
    public int getCenterColumn(){return this.centerColumn;}
    public int getWidth(){return this.width;}
    public int getHeight(){return this.height;}
    private int getNumberOfGrenadeSource(){return  this.numberOfGrenadeSource;}
    private int getNumberOfEnergySource(){return this.numberOfEnergySource;}
    private int getAmountEnergy(){return  this.amountEnergy;}
    private int getAmountGrenade(){return this.amountGrenade;}

    public void setCell(Cell cell){
        if(cell.getRow() < this.cells.length && cell.getColumn() < this.cells[cell.getRow()].length) this.cells[cell.getRow()][cell.getColumn()] = cell;
        else throw new RuntimeException("Index out of range");
    }
    private void setCenterRow(int centerRow){this.centerRow = centerRow;}
    private void setCenterColumn(int centerColumn){this.centerColumn = centerColumn;}
    private void setHeight(int height){this.height = height;}
    private void setWidth(int width){this.width = width;}
    private void setNumberOfMine(int numberOfMine){this.numberOfMine = numberOfMine;}
    private void addToAlreadyUsed(Cell cell){this.alreadyUsed.add(cell);}

    private void setNumberOfEnergySource(int numberOfEnergySource){this.numberOfEnergySource = numberOfEnergySource;}
    private void setNumberOfGrenadeSource(int numberOfGrenadeSource){this.numberOfGrenadeSource = numberOfGrenadeSource;}
    private void setAmountEnergy(int amountEnergy){this.amountEnergy = amountEnergy;}
    private void setAmountGrenade(int amountGrenade){this.amountGrenade = amountGrenade;}

    /**
     *Constructor
     */
    public Map(int height, int width, int numberOfMine, int numberOfGrenadeSource, int numberOfEnergySource, int amountEnergy, int amountGrenade){
        if(height % 2 == 1 && width % 2 == 1) {
            if(height > 3 && width > 3 ) {
                if(numberOfMine >= 0 && numberOfEnergySource >= 0 && numberOfGrenadeSource>=0 && amountEnergy >= 0 && amountGrenade >= 0){
                    if(numberOfMine+numberOfGrenadeSource+numberOfEnergySource+this.typeOfTool.size()+1 <= (height*width)-1) {
                        this.setAmountGrenade(amountGrenade);
                        this.setAmountEnergy(amountEnergy);

                        this.setHeight(height);
                        this.setWidth(width);
                        this.setNumberOfMine(numberOfMine);
                        this.setNumberOfEnergySource(numberOfEnergySource);
                        this.setNumberOfGrenadeSource(numberOfGrenadeSource);

                        this.cells = new Cell[height][width];
                        this.setCenterRow(height / 2);
                        this.setCenterColumn(width / 2);
                        double totalProbabilityMine = 0;
                        double totalProbabilityTool = 0;
                        for (int row = 0; row < height; row++) {
                            for (int column = 0; column < width; column++) {
                                this.setCell(new Cell(row, column, this));
                                totalProbabilityMine += this.getCell(row, column).getProbabilityOfMine();
                                totalProbabilityTool += this.getCell(row, column).getProbabilityOfTool();
                            }
                        }

                        for (int row = 0; row < height; row++) {
                            for (int column = 0; column < width; column++) {
                                this.getCell(row, column).setProbabilityOfMine(this.getCell(row, column).getProbabilityOfMine() / totalProbabilityMine);
                                this.getCell(row, column).setProbabilityOfTool(this.getCell(row, column).getProbabilityOfTool() / totalProbabilityTool);
                            }
                        }

                        this.addToAlreadyUsed(this.getCell(this.getCenterRow(), this.getCenterColumn()));

    //                  Generate
                        this.generateToolLayout();
                        this.generateEnergySourceLayout();
                        this.generateGrenadeSourceLayout();
                        this.generateMines();

                        Cell centralCell = this.getCell(this.getCenterRow(),this.getCenterColumn());
                        if(centralCell.getCellByDirection("top").isContainsMine() && centralCell.getCellByDirection("bottom").isContainsMine() && centralCell.getCellByDirection("left").isContainsMine() && centralCell.getCellByDirection("right").isContainsMine()){
                            this.regenerateMineLayout();
                        }

                        Cell[] corner = new Cell[]{this.getCell(0, 0), this.getCell(0, this.getWidth() - 1), this.getCell(this.getHeight() - 1, 0), this.getCell(this.getHeight() - 1, this.getWidth() - 1)};
                        int index = (int) (Math.random() * 4);
                        if (!this.isExisting(corner[index].getRow(), corner[index].getColumn() - 1)) {
                            corner[index].deleteWall(3);
                        } else corner[index].deleteWall(1);

                    }else throw new RuntimeException("Too many collectibles");
                }else throw new RuntimeException("One of the entered numbers is negative");
            }else throw new RuntimeException("Height or Width is too small");
        }else throw new RuntimeException("Height or Width is an even number");
    }

    public Map(int height, int width, int numberOfMine, int numberOfGrenade, int numberOfEnergy){
        this(height,width,numberOfMine,numberOfGrenade,numberOfEnergy,10, 10);
    }

    /**
     *To String Methods
     */
    public String toStringProbabilityOfMine(){
        StringBuilder probabilityArray = new StringBuilder();
        for (int row = 0; row < this.getHeight(); row++){
            probabilityArray.append("[");
            for (int column = 0; column < this.getWidth(); column++){
                probabilityArray.append(this.getCell(row, column).getProbabilityOfMine());
                probabilityArray.append(", ");
            }
            probabilityArray.append("]\n");
        }
        return probabilityArray.toString();
    }
    public String toStringProbabilityOfTool(){
        StringBuilder probabilityArray = new StringBuilder();
        for(int row = 0; row < this.getHeight(); row++){
            probabilityArray.append("[");
            for(int column = 0; column < this.getWidth(); column++){
                probabilityArray.append(this.getCell(row, column).getProbabilityOfTool());
                probabilityArray.append(", ");
            }
            probabilityArray.append("]\n");
        }
        return probabilityArray.toString();
    }
    public String toStringMineLayout(){
        StringBuilder mineLayout = new StringBuilder();
        for(int row = 0; row < this.getHeight(); row++){
            mineLayout.append("[");
            for(int column = 0; column < this.getWidth(); column++){
                if(this.getCell(row, column).isContainsMine()){
                    mineLayout.append("1");
                }
                else mineLayout.append("0");
                mineLayout.append(", ");
            }
            mineLayout.append("]\n");
        }
        return mineLayout.toString();
    }

    public String toStringCollectibleLayout(){
        StringBuilder collectibleLayout = new StringBuilder();
        for(int row = 0; row < this.getHeight(); row++){
            collectibleLayout.append("[");
            for(int column = 0; column < this.getWidth();column++){
                if(this.getCell(row,column).isContainsGrenade()){
                    collectibleLayout.append("G");
                }
                else if(this.getCell(row,column).isContainsEnergy()){
                    collectibleLayout.append("E");
                }
                else collectibleLayout.append("0");
                collectibleLayout.append(", ");
            }
            collectibleLayout.append("]\n");
        }
        return collectibleLayout.toString();
    }

    public String toString(){
        StringBuilder layout = new StringBuilder();

        for(int column = 0; column < this.getWidth();column++){
            if (this.getCell(0, column).existWall("top")) layout.append(",_______");
            else layout.append("         ");
        }layout.append(",\n");

        for(int row = 0; row < this.getHeight(); row++){
            for(int column = 0; column < this.getWidth();column++){
                if(this.getCell(row, column).existWall("left")) layout.append("|");
                else layout.append(" ");
                if(!this.getCell(row,column).isContainPlayer()) layout.append("       ");
                else{
                    if(this.getCell(row,column).getContain().length() > 1) layout.append("  ,__, ");
                    else layout.append("  ,_,  ");
                }
            }
            if(this.getCell(row, this.getWidth()-1).existWall("right")) layout.append("|");
            else layout.append(" ");
            layout.append("\n");

            for(int column = 0; column < this.getWidth(); column++){
                if(this.getCell(row, column).existWall("left")) layout.append("|");
                else layout.append(" ");

                if(!this.getCell(row,column).isContainPlayer()) {
                    layout.append("   ");
                    layout.append(this.getCell(row, column).getContain());
                    if (this.getCell(row, column).getContain().length() > 1) {
                        layout.append("  ");
                    } else {
                        layout.append("   ");
                    }
                }
                else {
                    layout.append("  |");
                    layout.append(this.getCell(row,column).getContain());
                    if (this.getCell(row, column).getContain().length() > 1) {
                        layout.append("| ");
                    } else {
                        layout.append("|  ");
                    }
                }
            }
            if(this.getCell(row, this.getWidth()-1).existWall("right")) layout.append("|");
            else layout.append(" ");
            layout.append("\n");

            for(int column = 0; column < this.getWidth(); column++){
                if(this.getCell(row, column).existWall("left")) layout.append("|");
                else layout.append(",");
                if(this.getCell(row, column).existWall("bottom")) layout.append("_______");
                else layout.append("       ");
            }

            if(this.getCell(row, this.getWidth()-1).existWall("right")) layout.append("|");
            else layout.append(",");
            layout.append("\n");
        }
        return layout.toString();
    }

    /**
     * Methods
     */
    private void generateMines(){
        this.resetLayout("mine");
        int mine = 0;
        Random random = new Random();
        while (mine < this.numberOfMine){
            double rand = random.nextDouble();
            double cumulativeProbability = 0;
            boolean objectPlaced = false;
            for (int row = 0; row < this.getHeight() && !objectPlaced; row++){
                for (int column = 0; column < this.getWidth() && !objectPlaced; column++){
                    cumulativeProbability += this.getCell(row, column).getProbabilityOfMine();
                    if(rand <= cumulativeProbability && !this.alreadyUsed.contains(this.getCell(row, column))){
                        this.getCell(row,column).setMine(new Mine());
                        this.addToAlreadyUsed(this.getCell(row,column));
                        objectPlaced = true;
                        mine+=1;
                    }
                }
            }
        }
    }


    public void regenerateMineLayout(){this.generateMines();}

    private void generateToolLayout(){
        this.resetLayout("tool");
        int tool = 0;
        Random random = new Random();
        int size = this.typeOfTool.size();
        while(tool < size){
            double rand = random.nextDouble();
            double cumulativeProbability = 0;
            boolean objectPlaced = false;
            for(int row = 0; row < this.getHeight() && !objectPlaced; row++){
                for(int column = 0; column < this.getWidth() && !objectPlaced; column++){
                    cumulativeProbability += this.getCell(row, column).getProbabilityOfTool();
                    if(rand <= cumulativeProbability && !this.alreadyUsed.contains(this.getCell(row,column))){
                        int randomIndex = random.nextInt(this.typeOfTool.size());
                        ExtraTool selectedTool = this.typeOfTool.get(randomIndex);
                        this.typeOfTool.remove(randomIndex);
                        this.getCell(row,column).setTool(selectedTool);
                        this.addToAlreadyUsed(this.getCell(row,column));
                        objectPlaced = true;
                        tool += 1;
                    }
                }
            }
        }
    }
    public void regenerateToolLayout(){this.generateToolLayout();}

    private void generateGrenadeSourceLayout(){
        this.resetLayout("grenade");
        int grenade = 0;
        double probability = (100.0/(this.getHeight()*this.getWidth()))/100;
        Random random = new Random();
        while(grenade < this.getNumberOfGrenadeSource()){
            double rand = random.nextDouble();
            double cumulativeProbability = 0;
            boolean objectPlaced = false;
            for(int row = 0; row < this.getHeight() && !objectPlaced; row++){
                for(int column = 0; column < this.getWidth() && !objectPlaced;column++){
                    cumulativeProbability += probability;
                    if(rand <=cumulativeProbability && !this.alreadyUsed.contains(this.getCell(row,column))){
                        this.getCell(row,column).setGrenadeSource(new GrenadeSource(this.getAmountGrenade()));
                        this.addToAlreadyUsed(this.getCell(row,column));
                        objectPlaced = true;
                        grenade += 1;
                    }
                }
            }
        }
    }
    public void regenerateGrenadeSourceLayout(){this.generateGrenadeSourceLayout();}
    private void generateEnergySourceLayout(){
        this.resetLayout("energy");
        int energy = 0;
        double probability = (100.0/(this.getHeight()*this.getWidth()))/100;
        Random random = new Random();
        while(energy < this.getNumberOfEnergySource()){
            double rand = random.nextDouble();
            double cumulativeProbability = 0;
            boolean objectPlaced = false;
            for(int row = 0; row < this.getHeight() && !objectPlaced; row++){
                for(int column = 0; column < this.getWidth() && !objectPlaced;column++){
                    cumulativeProbability += probability;
                    if(rand <=cumulativeProbability && !this.alreadyUsed.contains(this.getCell(row,column))){
                        this.getCell(row,column).setEnergySource(new EnergySource(this.getAmountEnergy()));
                        this.addToAlreadyUsed(this.getCell(row,column));
                        objectPlaced = true;
                        energy += 1;
                    }
                }
            }
        }
    }
    public void regenerateEnergyLayout(){this.generateEnergySourceLayout();}
    private void resetLayout(String type){
        for(int row = 0; row < this.getHeight(); row++){
            for(int column = 0; column < this.getWidth(); column++){
                Cell currentCell = this.getCell(row,column);
                if(currentCell.isContainsMine() && type.equals("mine")){
                    currentCell.setMine(null);
                    this.alreadyUsed.remove(currentCell);
                }
                if(currentCell.isContainsEnergy() && type.equals("energy")){
                    currentCell.setEnergySource(null);
                    this.alreadyUsed.remove(currentCell);
                }
                if(currentCell.isContainsGrenade() && type.equals("grenade")){
                    currentCell.setGrenadeSource(null);
                    this.alreadyUsed.remove(currentCell);
                }
                if(currentCell.isContainsTool() && type.equals("tool")){
                    currentCell.setTool(null);
                    this.alreadyUsed.remove(currentCell);
                }
            }
        }
    }

    public boolean isExisting(int row, int column){
        if(row >= 0 && row < this.getHeight()){
            return column >= 0 && column < this.getWidth();
        }
        return false;
    }


}
