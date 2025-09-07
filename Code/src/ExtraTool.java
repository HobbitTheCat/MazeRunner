public class ExtraTool {
    private String mapSymbol, name;
    private boolean directionIsNeeded;

    private void setName(String name){this.name = name;}
    private void setMapSymbol(String mapSymbol){this.mapSymbol = mapSymbol;}
    private void setDirectionIsNeeded(boolean directionIsNeeded){this.directionIsNeeded = directionIsNeeded;}

    public String getName(){return this.name;}
    private String getMapSymbol(){return this.mapSymbol;}

    public ExtraTool(String name, String mapSymbol, boolean directionIsNeeded){this.setName(name); this.setMapSymbol(mapSymbol); this.setDirectionIsNeeded(directionIsNeeded);}

    public String toString() {return this.getMapSymbol();}

    public boolean equals(ExtraTool other){
        return this.getClass() == other.getClass();
    }
    public boolean directionIsNeeded(){return this.directionIsNeeded;}
}
