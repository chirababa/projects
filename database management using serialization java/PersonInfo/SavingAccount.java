package PersonInfo;

public class SavingAccount extends Account {
    public SavingAccount() {
        super();
        this.setTip(getClass().getSimpleName());
    }

    @Override
    public int withdraw(int money) {
        if(this.getMoney()==money&&money>0) {
            this.setMoney((this.getMoney()-money));
            setChanged();
            notifyObservers("S-au extras din contul cu nrCont "+this.getNumarCont()+" suma de "+money+" lei");
            return 1;
        }
        return 0;
    }

    @Override
    public int deposit(int money) {
        if(this.getMoney()==0&&money>0) {
            int interest = 16 * money / 100;
            this.setMoney(money + interest);
            setChanged();
            notifyObservers("S-au adaugat in contul cu nrCont " + this.getNumarCont() + " suma de " + money + " lei");
            return 1;
        }
        return 0;
    }

}
