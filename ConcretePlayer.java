public class ConcretePlayer implements Player{

    protected boolean player1;

    protected int wins=0;

    public ConcretePlayer (boolean player1){
        this.player1=player1;
    }

    @Override
    public boolean isPlayerOne() {
        return player1;
    }

    @Override
    public int getWins() {
        return this.wins;
    }

    public void addWin(){
    this.wins++;
    }

}