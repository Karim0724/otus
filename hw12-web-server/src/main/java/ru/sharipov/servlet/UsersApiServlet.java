package ru.sharipov.servlet;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.sharipov.dto.UserCreateDto;
import ru.sharipov.model.User;
import ru.sharipov.services.DbUserService;

import java.io.IOException;
import java.util.stream.Collectors;


public class UsersApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DbUserService dbUserService;
    private final Gson gson;

    public UsersApiServlet(DbUserService dbUserService, Gson gson) {
        this.dbUserService = dbUserService;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = dbUserService.findById(extractIdFromRequest(request)).orElse(null);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        out.print(gson.toJson(user));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = gson.fromJson(req.getReader(), UserCreateDto.class);

        dbUserService.save(new User(null, user.getName(), user.getLogin(), user.getPassword()));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

}
