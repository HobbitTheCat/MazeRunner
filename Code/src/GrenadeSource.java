public class GrenadeSource {
    private int amountGrenade;

    private void setAmountGrenade(int amountGrenade){
        if(amountGrenade >= 0){
            this.amountGrenade = amountGrenade;
        }else throw new RuntimeException("Grenade must by positive");
    }
    public int getAmountGrenade(){return this.amountGrenade;}

    public GrenadeSource(int amountGrenade){
        setAmountGrenade(amountGrenade);
    }

    public int takeGrenade(int amount){
        if (amount <= this.amountGrenade && amount > 0){
            this.setAmountGrenade(this.getAmountGrenade() - amount);
            return amount;
        }
        else if(amount > this.amountGrenade){
            int grenade = this.getAmountGrenade();
            this.setAmountGrenade(0);
            return grenade;
        }
        return 0;
    }
}
