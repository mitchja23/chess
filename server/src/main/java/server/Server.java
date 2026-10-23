package server;


import service.*;
import dataaccess.*;
import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;

import java.util.Map;

public class Server {

    private final Javalin javalin;
    private final Gson serializer = new Gson();
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server() {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        clearService = new ClearService(gameDAO, authDAO, userDAO);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .get("/game", this::listGames)
                .delete("/db", this::clear)
                .exception(Exception.class, (e, ctx) -> {
                    if (e instanceof BadRequestException) {
                        ctx.status(400);
                    } else if (e instanceof UnauthorizedException) {
                        ctx.status(401);
                    } else if (e instanceof AlreadyTakenException) {
                        ctx.status(403);
                    } else {
                        ctx.status(500);
                    }
                    ctx.contentType("application/json");
                    ctx.result(serializer.toJson(Map.of("message", String.format("Error: %s", e.getMessage()))));
                });
    }


    private void register(Context ctx) throws Exception {
        var request = serializer.fromJson(ctx.body(), Request.RegisterRequest.class);
        if (request == null) {
            throw new BadRequestException("bad request");
        }
        ctx.contentType("application/json");
        ctx.result(serializer.toJson(userService.register(request)));
    }
    private void login(Context ctx) throws Exception {
        var request = serializer.fromJson(ctx.body(), Request.LoginRequest.class);
        if (request == null) {
            throw new BadRequestException("bad request");
        }
        ctx.contentType("application/json");
        ctx.result(serializer.toJson(userService.login(request)));
    }

    private void logout(Context ctx) throws Exception {
        userService.logout(ctx.header("authorization"));
        ctx.contentType("application/json");
        ctx.result("{}");
    }

    private void createGame(Context ctx) throws Exception {
        var request = serializer.fromJson(ctx.body(), Request.CreateGameRequest.class);
        if (request == null) {
            throw new BadRequestException("bad request");
        }
        ctx.contentType("application/json");
        ctx.result(serializer.toJson(gameService.createGame(ctx.header("authorization"), request)));
    }
    private void joinGame(Context ctx) throws Exception {
        var request = serializer.fromJson(ctx.body(), Request.JoinGameRequest.class);
        if (request == null) {
            throw new BadRequestException("bad request");
        }
        gameService.joinGame(ctx.header("authorization"), request);
        ctx.contentType("application/json");
        ctx.result("{}");
    }
    private void listGames(Context ctx) throws Exception {
        ctx.contentType("application/json");
        ctx.result(serializer.toJson(gameService.listGames(ctx.header("authorization"))));
    }

    private void clear(Context ctx) throws DataAccessException {
        clearService.clear();
        ctx.contentType("application/json");
        ctx.result("{}");
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
