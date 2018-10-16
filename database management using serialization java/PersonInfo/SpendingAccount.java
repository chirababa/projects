package PersonInfo;

public class SpendingAccount extends Account {

    public SpendingAccount() {
        super();
        this.setTip(getClass().getSimpleName());
    }

    @Override
    public int withdraw(int money) {
        if(this.getMoney()>=money&&money>0) {
            this.setMoney(this.getMoney()-money);
            setChanged();
            notifyObservers("S-au extras din contul cu nrCont "+this.getNumarCont()+" suma de "+money+" lei");
            return 1;
        }
    return 0;
    }

    @Override
    public int deposit(int money) {
        if(money>0) {
            this.setMoney(this.getMoney() + money);
            setChanged();
            notifyObservers("S-au adaugat in contul cu nrCont " + this.getNumarCont() + " suma de " + money + " lei");
            return 1;
        }
        return 0;
    }

}
