public class Pawn extends ConcretePiece{

    private int kills;

    public Pawn(Player owner){

        this.owner=owner;
        this.kills=0;

    }
    public void setKill (){
        this.kills++;
    }
    public int getKills (){
        return this.kills;
    }

    @Override
    public String getType() {
        if (owner.isPlayerOne()) {
            return ("♟");
        }
        else {
            return ("♙");
        }
    }
}


