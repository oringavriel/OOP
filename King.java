public class King extends ConcretePiece{


    public King(Player defender) {
        this.owner=defender;
    }

    @Override
    public String getType() {
        return "♔";
    }
}
