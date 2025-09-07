public class EnergySource {
    private int amountEnergy;

    private void setAmountEnergy(int amountEnergy){
        if(amountEnergy >= 0)
            this.amountEnergy = amountEnergy;
        else throw new RuntimeException("Energy must by positive");
    }
    public int getAmountEnergy(){return this.amountEnergy;}

    public EnergySource(int amountEnergy){
        setAmountEnergy(amountEnergy);
    }

    public int takeEnergy(int amount){
        if (amount <= this.amountEnergy && amount > 0){
            this.setAmountEnergy(this.getAmountEnergy()-amount);
            return amount;
        }
        else if(amount > this.amountEnergy){
            int energy = this.getAmountEnergy();
            this.setAmountEnergy(0);
            return energy;
        }
        return 0;
    }
}
