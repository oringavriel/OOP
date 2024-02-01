import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;

public class GameLogic implements PlayableLogic {

    Stack<Position> undo = new Stack<Position>();
    ArrayList<ArrayList<Position>> attackerHistory = new ArrayList<ArrayList<Position>>();

    ArrayList<ArrayList<Position>> defenderHistory = new ArrayList<ArrayList<Position>>();
    private final ConcretePlayer player1 = new ConcretePlayer(true);
    private final ConcretePlayer player2 = new ConcretePlayer(false);
    private Position[][] p;
    private ConcretePiece[][] pieces;

    private Position[][] positions = new Position[11][11];
    private boolean Turn = true;
    private boolean isGameFinished = false;
    private int piecesLeftPlayerOne = 24;


    public GameLogic() {
        //this.p = new Position[11][11];
        this.reset();
    }

    @Override
    public boolean move(Position a, Position b) {
        if ((!(this.isSecondPlayerTurn())) && (!(getPieceAtPosition(a).getOwner().isPlayerOne()))) {
            return false;
        }
        if ((this.isSecondPlayerTurn()) && (getPieceAtPosition(a).getOwner().isPlayerOne())) {
            return false;
        }
        if (this.pieces[b.getRow()][b.getCol()] != null || this.pieces[a.getRow()][a.getCol()] == null) {
            return false;
        }
        if (b.getRow() != a.getRow() && b.getCol() != a.getCol()) {
            return false;
        }
        if (this.pieces[a.getRow()][a.getCol()] instanceof Pawn) {
            if (b.getCol() == 0 || b.getCol() == 10) {
                if (b.getRow() == 0 || b.getRow() == 10) {
                    return false;
                }
            }
        }
        int i;
        if (a.getCol() < b.getCol()) {
            for (i = a.getCol() + 1; i < b.getCol() + 1; i++) {
                if (this.pieces[a.getRow()][i] != null) {
                    return false;
                }
            }
        }
        if (a.getCol() > b.getCol()) {
            for (i = a.getCol() - 1; i > b.getCol() - 1; i--) {
                if (this.pieces[a.getRow()][i] != null) {
                    return false;
                }
            }
        }
        if (a.getRow() < b.getRow()) {
            for (i = a.getRow() + 1; i < b.getRow() + 1; i++) {
                if (this.pieces[i][a.getCol()] != null) {
                    return false;
                }
            }
        }
        if (a.getRow() > b.getRow()) {
            for (i = a.getRow() - 1; i > b.getRow() - 1; i--) {
                if (this.pieces[i][a.getCol()] != null) {
                    return false;
                }
            }
        }

        ConcretePiece newp = this.pieces[a.getRow()][a.getCol()];
        this.pieces[b.getRow()][b.getCol()] = newp;
        this.pieces[a.getRow()][a.getCol()] = null;
        this.p[b.getRow()][b.getCol()].setNumOfPieces();

        if(a.getCol()==b.getCol()){
            if(a.getRow()>b.getRow()){
                this.pieces[b.getRow()][b.getCol()].setNumOfSteps(a.getRow()-b.getRow());
            }
            if(a.getRow()<b.getRow()){
                this.pieces[b.getRow()][b.getCol()].setNumOfSteps(b.getRow()-a.getRow());
            }
        }
        if(a.getRow()==b.getRow()){
            if(a.getCol()>b.getCol()){
                this.pieces[b.getRow()][b.getCol()].setNumOfSteps(a.getCol()-b.getCol());
            }
            if(a.getRow()<b.getRow()){
                this.pieces[b.getRow()][b.getCol()].setNumOfSteps(b.getCol()-a.getCol());
            }
        }


        Kill(b.getRow(), b.getCol());
        kingVictory(b.getRow(), b.getCol());
        if (piecesLeftPlayerOne == 0) {
            this.player2.addWin();
            isGameFinished = true;
        }
        Position pTemp = new Position(b.getRow(), b.getCol());
        this.pieces[b.getRow()][b.getCol()].setPositionHistory(pTemp);
        undo.push(b);
        undo.push(a);
        Turn = !Turn;

        return true;

    }

    public void kingVictory(int r, int c) {
        if (this.pieces[r][c] instanceof King && isCorner(r, c)) {
            this.player2.addWin();
            isGameFinished = true;
        }
    }

    public boolean isCorner(int r, int c) {
        if ((r == 0 && c == 0) || (r == 10 && c == 10) || (r == 0 && c == 10) || (r == 10 && c == 0)) {
            return true;
        }
        return false;
    }

    public void killKing(int r, int c) {
        if ((r - 1) == -1) {
            if (this.pieces[r + 1][c] != null && this.pieces[r][c + 1] != null && this.pieces[r][c - 1] != null &&
                    this.pieces[r + 1][c].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r][c + 1].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r][c - 1].getOwner() != this.pieces[r][c].getOwner()) {
                this.player1.addWin();
                isGameFinished = true;
            }
        } else if ((r + 1) == 11) {
            if (this.pieces[r - 1][c] != null && this.pieces[r][c + 1] != null && this.pieces[r][c - 1] != null &&
                    this.pieces[r - 1][c].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r][c + 1].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r][c - 1].getOwner() != this.pieces[r][c].getOwner()) {
                this.player1.addWin();
                isGameFinished = true;
            }
        } else if ((c - 1) == -1) {
            if (this.pieces[r + 1][c] != null && this.pieces[r - 1][c] != null && this.pieces[r][c + 1] != null &&
                    this.pieces[r + 1][c].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r - 1][c].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r][c + 1].getOwner() != this.pieces[r][c].getOwner()) {
                this.player1.addWin();
                isGameFinished = true;
            }
        } else if ((c + 1) == 11) {
            if (this.pieces[r + 1][c] != null && this.pieces[r - 1][c] != null && this.pieces[r][c - 1] != null &&
                    this.pieces[r + 1][c].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r - 1][c].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r][c - 1].getOwner() != this.pieces[r][c].getOwner()) {
                this.player1.addWin();
                isGameFinished = true;

            }
        } else {
            if (this.pieces[r + 1][c] != null && this.pieces[r - 1][c] != null && this.pieces[r][c + 1] != null && this.pieces[r][c - 1] != null &&
                    this.pieces[r + 1][c].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r - 1][c].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r][c + 1].getOwner() != this.pieces[r][c].getOwner() &&
                    this.pieces[r][c - 1].getOwner() != this.pieces[r][c].getOwner()) {
                this.player1.addWin();
                isGameFinished = true;
            }
        }
    }

    public void Kill(int r, int c) {
        if (this.pieces[r][c] instanceof Pawn) {
            //left
            if ((r - 1) >= 0 && this.pieces[r - 1][c] != null && this.pieces[r][c].getOwner() != this.pieces[r - 1][c].getOwner()) {
                if (!(this.pieces[r - 1][c] instanceof King)) {
                    if ((r - 2) == -1) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r - 1][c] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    } else if (this.pieces[r - 2][c] != null && this.pieces[r][c].getOwner() == this.pieces[r - 2][c].getOwner() && (!(this.pieces[r - 2][c] instanceof King))) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r - 1][c] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    } else if (isCorner((r - 2), c)) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r - 1][c] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    }
                } else {
                    killKing((r - 1), c);
                }
            }
            //right
            if ((r + 1) <= 10 && this.pieces[r + 1][c] != null && this.pieces[r][c].getOwner() != this.pieces[r + 1][c].getOwner()) {
                if (!(this.pieces[r + 1][c] instanceof King)) {
                    if ((r + 2) == 11) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r + 1][c] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    } else if (this.pieces[r + 2][c] != null && this.pieces[r][c].getOwner() == this.pieces[r + 2][c].getOwner() && (!(this.pieces[r + 2][c] instanceof King))) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r + 1][c] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    } else if (isCorner((r + 2), c)) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r + 1][c] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    }
                } else {
                    killKing((r + 1), c);
                }
            }
            //up
            if ((c - 1) >= 0 && this.pieces[r][c - 1] != null && this.pieces[r][c].getOwner() != this.pieces[r][c - 1].getOwner()) {
                if (!(this.pieces[r][c - 1] instanceof King)) {
                    if ((c - 2) == -1) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r][c - 1] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    } else if (this.pieces[r][c - 2] != null && this.pieces[r][c].getOwner() == this.pieces[r][c - 2].getOwner() && (!(this.pieces[r][c - 2] instanceof King))) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r][c - 1] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    } else if (isCorner(r, (c - 2))) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r][c - 1] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    }
                } else {
                    killKing(r, (c - 1));
                }
            }
            //down
            if ((c + 1) <= 10 && this.pieces[r][c + 1] != null && this.pieces[r][c].getOwner() != this.pieces[r][c + 1].getOwner()) {
                if (!(this.pieces[r][c + 1] instanceof King)) {
                    if ((c + 2) == 11) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r][c + 1] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    } else if (this.pieces[r][c + 2] != null && this.pieces[r][c].getOwner() == this.pieces[r][c + 2].getOwner() && (!(this.pieces[r][c + 2] instanceof King))) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r][c + 1] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    } else if (isCorner(r, (c + 2))) {
                        if (this.pieces[r][c].getOwner().isPlayerOne() == false) {
                            piecesLeftPlayerOne--;
                        }
                        this.pieces[r][c + 1] = null;
                        ((Pawn) this.pieces[r][c]).setKill();
                    }
                } else {
                    killKing(r, (c + 1));
                }

            }
        }
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
        return this.pieces[position.getRow()][position.getCol()];
    }

    @Override
    public Player getFirstPlayer() {
        return this.player1;
    }

    @Override
    public Player getSecondPlayer() {
        return this.player2;
    }

    @Override
    public boolean isGameFinished() {
        if (isGameFinished) {
            compareByPositions();
            compareByKills();
            compareBySteps();
            compareByPieces();
        }
        return this.isGameFinished;
    }

    public void stepsHistory() {
        for (int i = 0; i < 11; i++) {
            for (int z = 0; z < 11; z++) {
                if (this.pieces[i][z] != null) {
                    if (this.pieces[i][z].getOwner().isPlayerOne()) {
                        attackerHistory.add(this.pieces[i][z].getPositionHistory());
                    } else {
                        defenderHistory.add(this.pieces[i][z].getPositionHistory());
                    }
                }
            }
        }
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return (!(this.Turn));
    }

    @Override
    public void reset() {
        this.pieces = new ConcretePiece[11][11];
        this.p = new Position[11][11];
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                p[i][j] = new Position(i, j);
            }
        }
        this.isGameFinished = false;
        Turn = true;
        int i;
        int count = 7;
        for (i = 3; i < 8; i++) {

                this.pieces[0][i] = new Pawn(this.player1);
                this.pieces[0][i].setNumber(count);
                String s = "A";
                this.pieces[0][i].setNameOfPiece(s);
                this.pieces[0][i].setPositionHistory(this.p[0][i]);
                this.p[0][i].setNumOfPieces();

            if(i==5){
                count=count+4;
            }
            else{
                count=count+2;
            }
        }


        this.pieces[1][5] = new Pawn(this.player1);
        this.pieces[1][5].setNumber(12);
        this.pieces[1][5].setNameOfPiece("A");
        this.pieces[1][5].setPositionHistory(this.p[1][5]);
        this.p[1][5].setNumOfPieces();

        count = 1;
        for (i = 3; i < 8; ++i) {
            this.pieces[i][0] = new Pawn(this.player1);
            this.pieces[i][0].setNumber(count);
            String s = "A";
            this.pieces[i][0].setNameOfPiece(s);
            count = count + 1;
            this.pieces[i][0].setPositionHistory(this.p[i][0]);
            this.p[i][0].setNumOfPieces();
        }

        this.pieces[5][1] = new Pawn(this.player1);
        this.pieces[5][1].setNumber(6);
        this.pieces[5][1].setNameOfPiece("A");
        this.pieces[5][1].setPositionHistory(this.p[5][1]);
        this.p[5][1].setNumOfPieces();

        count = 20;
        for (i = 3; i < 8; ++i) {
            this.pieces[i][10] = new Pawn(this.player1);
            this.pieces[i][10].setNumber(count);
            String s = "A";
            this.pieces[i][10].setNameOfPiece(s);
            this.pieces[i][10].setPositionHistory(this.p[i][10]);
            this.p[i][10].setNumOfPieces();
            count=count+1;
        }

        this.pieces[5][9] = new Pawn(this.player1);
        this.pieces[5][9].setNumber(19);
        this.pieces[5][9].setNameOfPiece("A");
        this.pieces[5][9].setPositionHistory(this.p[5][9]);
        this.p[5][9].setNumOfPieces();

        count = 8;
        for (i = 3; i < 8; ++i) {
            this.pieces[10][i] = new Pawn(this.player1);
            this.pieces[10][i].setNumber(count);
            String s = "A";
            this.pieces[10][i].setNameOfPiece(s);
            this.pieces[10][i].setPositionHistory(this.p[10][i]);
            this.p[10][i].setNumOfPieces();
            if (i == 4) {
                count = count + 4;
            } else {
                count = count + 2;
            }
        }

        this.pieces[9][5] = new Pawn(this.player1);
        this.pieces[9][5].setNumber(13);
        this.pieces[9][5].setNameOfPiece("A");
        this.pieces[9][5].setPositionHistory(this.p[9][5]);
        this.p[9][5].setNumOfPieces();

        count = 2;
        for (i = 4; i < 7; ++i) {
            this.pieces[4][i] = new Pawn(this.player2);
            this.pieces[4][i].setNumber(count);
            String s = "D";
            this.pieces[4][i].setNameOfPiece(s);
            count = count + 4;
            this.pieces[4][i].setPositionHistory(this.p[4][i]);
            this.p[4][i].setNumOfPieces();
        }

        count = 4;
        for (i = 4; i < 7; ++i) {

            this.pieces[6][i] = new Pawn(this.player2);
            this.pieces[6][i].setNumber(count);
            String s = "D";
            this.pieces[6][i].setNameOfPiece(s);
            count = count + 4;
            this.pieces[6][i].setPositionHistory(this.p[6][i]);
            this.p[6][i].setNumOfPieces();
        }

        this.pieces[3][5] = new Pawn(this.player2);
        this.pieces[3][5].setNumber(5);
        this.pieces[3][5].setNameOfPiece("D");
        this.pieces[3][5].setPositionHistory(this.p[3][5]);
        this.p[3][5].setNumOfPieces();

        this.pieces[7][5] = new Pawn(this.player2);
        this.pieces[7][5].setNumber(9);
        this.pieces[7][5].setNameOfPiece("D");
        this.pieces[7][5].setPositionHistory(this.p[7][5]);
        this.p[7][5].setNumOfPieces();

        count = 1;
        for (i = 3; i < 8; ++i) {
            if (i == 5) {
                count = count + 6;
            }
            if (i != 5) {
                this.pieces[5][i] = new Pawn(this.player2);
                this.pieces[5][i].setNumber(count);
                String s = "D";
                this.pieces[5][i].setNameOfPiece(s);
                this.pieces[5][i].setPositionHistory(this.p[5][i]);
                this.p[5][i].setNumOfPieces();
                count = count + 2;
            }

        }
        this.pieces[5][5] = new King(this.player2);
        this.pieces[5][5].setNumber(7);
        this.pieces[5][5].setNameOfPiece("K");
        this.pieces[5][5].setPositionHistory(this.p[5][5]);
        this.p[5][5].setNumOfPieces();

        this.undo = new Stack<Position>();

    }

    @Override
    public void undoLastMove() {
        if (!undo.empty()) {
            Position a = new Position(undo.pop());
            Position b = new Position(undo.pop());
            ConcretePiece newp = this.pieces[b.getRow()][b.getCol()];
            this.pieces[a.getRow()][a.getCol()] = newp;
            this.pieces[b.getRow()][b.getCol()] = null;

        }
    }

    @Override
    public int getBoardSize() {
        return 11;
    }


    public void compareByPieces(){
     ArrayList <Position> arr= new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            for (int z = 0; z < 11; z++) {
                arr.add(this.p[i][z]);
            }
        }
        Comparator<Position> byPiece = Comparator.comparing(Position::getNumOfPieces);;
        Comparator<Position> byPieceReversed = byPiece.reversed().thenComparing(Position::getRow).thenComparing(Position::getCol);
        arr.sort(byPieceReversed);

        for (int i=0; i<arr.size(); i++){
            System.out.println(arr.get(i).toString() + arr.get(i).getNumOfPieces()+" pieces");
        }
        System.out.println("***************************************************************************");

    }
    public void compareBySteps(){
        ArrayList<ConcretePiece> pawnPiece = new ArrayList<>();
        ArrayList<Pawn> attackers = new ArrayList<>();
        ArrayList<ConcretePiece> defenders = new ArrayList<>();

        for (int i = 0; i < 11; i++) {
            for (int z = 0; z < 11; z++) {
                if (this.pieces[i][z] != null ){
                    if (this.pieces[i][z].getOwner().isPlayerOne()){
                        attackers.add( (Pawn)this.pieces[i][z]);}
                    else{
                        defenders.add(this.pieces[i][z]);}
                }
            }
        }
        if (player1.getWins()==1){
            pawnPiece.addAll(attackers);
            pawnPiece.addAll(defenders);
        }
        if (player2.getWins()==1){
            pawnPiece.addAll(defenders);
            pawnPiece.addAll(attackers);
        }
        Comparator<ConcretePiece> bySteps = Comparator.comparing(ConcretePiece::getNumOfSteps);
        Comparator<ConcretePiece> byStepsReversed = bySteps.reversed().thenComparing(ConcretePiece::getNumber);
        pawnPiece.sort(byStepsReversed);

        for (int i=0; i<pawnPiece.size(); i++){
            System.out.println(pawnPiece.get(i).toString() + pawnPiece.get(i).getNumOfSteps()+" squares");
        }

        System.out.println("***************************************************************************");


    }

    public void compareByKills(){
        ArrayList<Pawn> pawnPiece = new ArrayList<>();
        ArrayList<Pawn> attackers = new ArrayList<>();
        ArrayList<Pawn> defenders = new ArrayList<>();


        for (int i = 0; i < 11; i++) {
            for (int z = 0; z < 11; z++) {
                if (this.pieces[i][z] != null && this.pieces[i][z] instanceof Pawn ){
                    if (this.pieces[i][z].getOwner().isPlayerOne()){
                        attackers.add( (Pawn)this.pieces[i][z]);}
                else{
                        defenders.add( (Pawn)this.pieces[i][z]);}
                }
            }
        }
        if (player1.getWins()==1){
            pawnPiece.addAll(attackers);
            pawnPiece.addAll(defenders);
        }
        if (player2.getWins()==1){
            pawnPiece.addAll(defenders);
            pawnPiece.addAll(attackers);
        }
        Comparator<Pawn> byKill = Comparator.comparing(Pawn::getKills);
        Comparator<Pawn> byKillReversed = byKill.reversed().thenComparing(Pawn::getNumber);
        pawnPiece.sort(byKillReversed);

        for (int i=0; i<pawnPiece.size(); i++){
            System.out.println(pawnPiece.get(i).toString() + pawnPiece.get(i).getKills()+" kills");
        }

        System.out.println("***************************************************************************");


    }
    public void compareByPositions() {

        ArrayList<ConcretePiece> attackers = new ArrayList<>();

        ArrayList<ConcretePiece> defenders = new ArrayList<>();


        for (int i = 0; i < 11; i++) {
            for (int z = 0; z < 11; z++) {
                if (this.pieces[i][z] != null) {
                    if (this.pieces[i][z].getOwner().isPlayerOne()) {
                        attackers.add(this.pieces[i][z]);
                    } else {
                        defenders.add(this.pieces[i][z]);
                    }
                }
            }
        }

        Comparator<ConcretePiece> byPosition = Comparator.comparing(ConcretePiece::positionHistorySize).thenComparing(ConcretePiece::getNumber);

        attackers.sort(byPosition);
        defenders.sort(byPosition);

        if (player1.getWins() == 1) {

            for (int i = 0; i < attackers.size(); i++) {
                System.out.println(attackers.get(i).toString() + attackers.get(i).getPositionHistory());
            }
            for (int i = 0; i < defenders.size(); i++) {
                System.out.println(defenders.get(i).toString() + defenders.get(i).getPositionHistory());
            }
        }
        else{
            for (int i = 0; i < defenders.size(); i++) {
                System.out.println(defenders.get(i).toString() + defenders.get(i).getPositionHistory());
            }

            for (int i = 0; i < attackers.size(); i++) {
                System.out.println(attackers.get(i).toString() + attackers.get(i).getPositionHistory());
            }
        }
        System.out.println("***************************************************************************");
    }
}