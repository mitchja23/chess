package ui;

import chess.ChessGame;
import chess.ChessPiece;

public class BoardUI {
    private static String icon(ChessPiece chessPiece){
        if (chessPiece != null){
            var color = chessPiece.getTeamColor();
            var type = chessPiece.getPieceType();
            if (color == ChessGame.TeamColor.WHITE){
                if(type == ChessPiece.PieceType.PAWN){
                    return EscapeSequences.WHITE_PAWN;
                }
                if(type == ChessPiece.PieceType.ROOK){
                    return EscapeSequences.WHITE_ROOK;
                }
                if(type == ChessPiece.PieceType.KNIGHT){
                    return EscapeSequences.WHITE_KNIGHT;
                }
                if(type == ChessPiece.PieceType.BISHOP){
                    return EscapeSequences.WHITE_BISHOP;
                }
                if(type == ChessPiece.PieceType.QUEEN){
                    return EscapeSequences.WHITE_QUEEN;
                }
                if(type == ChessPiece.PieceType.KING){
                    return EscapeSequences.WHITE_KING;
                }
            }else{
                if(type == ChessPiece.PieceType.PAWN){
                    return EscapeSequences.BLACK_PAWN;
                }
                if(type == ChessPiece.PieceType.ROOK){
                    return EscapeSequences.BLACK_ROOK;
                }
                if(type == ChessPiece.PieceType.KNIGHT){
                    return EscapeSequences.BLACK_KNIGHT;
                }
                if(type == ChessPiece.PieceType.BISHOP){
                    return EscapeSequences.BLACK_BISHOP;
                }
                if(type == ChessPiece.PieceType.QUEEN){
                    return EscapeSequences.BLACK_QUEEN;
                }
                if(type == ChessPiece.PieceType.KING){
                    return EscapeSequences.BLACK_KING;
                }
            }
        }
        return EscapeSequences.EMPTY;
    }
}
