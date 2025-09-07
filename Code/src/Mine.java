public class Mine {
    private boolean charged;

    private void setCharged(boolean charged){this.charged = charged;}
    public boolean isCharged(){return this.charged;}
    public Mine(){
        setCharged(true);
    }
    public void discharge(){
        setCharged(false);
    }

    public String toString(){
        if(this.isCharged()){
            return "Mc";
        }else return "Md";
    }
}
