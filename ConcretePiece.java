import java.util.ArrayList;

public abstract class ConcretePiece implements Piece {

    protected Player owner;
    private ArrayList<Position> positionHistory = new ArrayList<>();

    private String nameOfPiece;
    private int num;

    private int numOfSteps=0;

    public ConcretePiece() {
    }

    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public abstract String getType();

    public ArrayList<Position> getPositionHistory() {
        return this.positionHistory;
    }

    public void setNumOfSteps(int n){
        this.numOfSteps= this.numOfSteps+n;
    }
    public int getNumOfSteps(){
        return this.numOfSteps;
    }
    public void setNameOfPiece(String s) {
        this.nameOfPiece = s;
    }

    public String getNameOfPiece() {
        return this.nameOfPiece;
    }

    public int positionHistorySize() {
        return this.positionHistory.size();
    }

    public void setPositionHistory(Position p) {
        this.positionHistory.add(p);

    }

    public String toString() {
        return this.nameOfPiece + this.num+ ": " ;
    }

    public void setNumber(int n) {
        this.num = n;
    }
    public int getNumber() {
        return this.num ;
    }
}