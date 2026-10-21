package model;

import chess.ChessGame;

public record GameData(String whitePlayer, String blackPlayer, String gameName, int gameCode, ChessGame game) {
}
