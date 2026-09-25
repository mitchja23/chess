package chess;

import java.util.Collection;
import java.util.Iterator;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public  TeamColor currentColor;
    private  ChessBoard board;

    public ChessGame() {
        currentColor = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return currentColor;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard board = getBoard();
        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null){
            return  null;
        }

        Collection<ChessMove> validMoves = piece.pieceMoves(board, startPosition);
        Iterator<ChessMove> possibleMoves = validMoves.iterator();
        while (possibleMoves.hasNext()){
            ChessMove move = possibleMoves.next();
            ChessBoard currentState = board.simulateBoard();
            ChessPiece movingPiece = currentState.getPiece(startPosition);
            currentState.addPiece(move.getEndPosition(), movingPiece);
            currentState.addPiece(startPosition, null);

        }
        return validMoves;


    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if(!moves.contains(move)){
            throw new InvalidMoveException("Illegal Move");
        }
        ChessPiece piece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), piece);
        board.addPiece(move.getStartPosition(), null);


        if(piece.getPieceType() == ChessPiece.PieceType.PAWN){
            int promote = (piece.getTeamColor() == TeamColor.WHITE) ? 8 : 1;
            if(move.getEndPosition().getRow() == promote){
                ChessPiece.PieceType upgrade = ChessPiece.PieceType.QUEEN;
                ChessPiece newPiece = new ChessPiece(piece.getTeamColor(), upgrade);
                board.addPiece(move.getEndPosition(), newPiece);
            }
        }


    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = null;
        for(int row  = 1; row <= 8; row++){
            for(int col = 1; col <= 8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                        && piece.getTeamColor() == teamColor){
                    king = position;
                    break;
                }
            }
        }
        for(int row  = 1; row <= 8; row++){
            for(int col = 1; col <= 8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position):

                if (piece != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> opponentMoves = piece.pieceMoves(board, position);

                    for (ChessMove move : opponentMoves){
                        if (move.getEndPosition().equals(king)){
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        if (this.board == null){
            throw new IllegalArgumentException("Already Set");
        }
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
