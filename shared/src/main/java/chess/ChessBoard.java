package chess;


import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() -1 ] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() -1 ];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col ++){
                squares[row][col] = null;
            }
        }
        for (int col = 0; col < 8; col++){
            squares[1][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        ChessPiece.PieceType[] backRow = {
                ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK
        };
        for (int col = 0; col < 8; col++){
            squares[0][col] = new ChessPiece(ChessGame.TeamColor.WHITE, backRow[col]);
            squares[7][col] = new ChessPiece(ChessGame.TeamColor.BLACK, backRow[col]);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece thisPiece = this.squares[row][col];
                ChessPiece thatPiece = that.squares[row][col];
                if (!Objects.equals(thisPiece, thatPiece)) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        int result = 1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                result = 31 * result + (squares[row][col] != null ? squares[row][col].hashCode() : 0);
            }
        }
        return result;
    }
}
