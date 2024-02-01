public class Position {

    private int row;
    private int column;
    private int numOfPieces=0;

    public Position (int row, int column ) {
        this.row = row;
        this.column = column;

    }
   public Position( Position p){
        this.row=p.getRow();
        this.column= p.getCol();
   }

   public void setNumOfPieces(){
        this.numOfPieces++;
   }
    public int getNumOfPieces(){
        return this.numOfPieces;
    }

    public int getRow() {
        return this.row;
    }


    public int getCol(){
        return this.column;
    }

    public String toString () {
        return ( "("+ this.row + "," + this.column + ")" );
    }

}
