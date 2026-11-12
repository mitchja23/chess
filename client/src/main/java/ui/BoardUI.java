package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

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

    private static void colors(ChessBoard board, int row, int col){
        var piece = board.getPiece(new ChessPosition(row, col));
        String boardColor;
        String playerColor;
        if((row + col) %2 != 0){
            boardColor = EscapeSequences.SET_BG_COLOR_GREEN;
        }else{
            boardColor = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
        }
        if(piece != null){
            if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                playerColor = EscapeSequences.SET_TEXT_COLOR_WHITE;
            }else{
                playerColor = EscapeSequences.SET_TEXT_COLOR_BLACK;
            }
        }else{
            playerColor = EscapeSequences.SET_TEXT_COLOR_MAGENTA;
        }
        System.out.print(boardColor + playerColor + icon(piece));

    }

    private static void makeRow(ChessBoard board, int row, String color){
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
        if(color.equals("WHITE")){
            for(int col = 1; col <= 8; col++){
                colors(board,row, col);
            }
        }else{
            for(int col = 8; col >= 1; col--){
                colors(board,row, col);
            }
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println();
    }

    private static void rowLabels(String color){
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_BLACK );
        String whiteRows = "    a  b  c  d  e  f  g  h    ";
        String blackRows = "    h  g  f  e  d  c  b  a    ";
        if (color.equals("WHITE")){
            System.out.print(whiteRows);
        }else{
            System.out.print(blackRows);
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println();

    }

}
