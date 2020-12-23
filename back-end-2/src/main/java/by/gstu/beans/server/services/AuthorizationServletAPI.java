package by.gstu.beans.server.services;

import by.gstu.beans.User;
import by.gstu.beans.dao.interfaces.UserService;
import by.gstu.beans.dao.mysql.MySqlUserServiceImpl;
import by.gstu.beans.interfaces.Role;
import by.gstu.beans.utils.json.UserJsonConvert;
import by.gstu.beans.utils.json.UtilFactory;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/chat/authorization/*")
public class AuthorizationServletAPI extends HttpServlet {

    private UserService userSerice;

    @Override
    public void init() throws ServletException {
        super.init();
        userSerice = new MySqlUserServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] urlPart = req.getPathInfo().split("/");
        JsonObject bodyReq = UtilFactory.convertHttpRequestToObject(req);
        switch (urlPart[urlPart.length - 1]){
            case "auth":
                User checkUser = userSerice.checkUser(bodyReq.get("nickname").getAsString(),
                                                      bodyReq.get("password").getAsString());
                if (checkUser != null){
                    String userJSON = new Gson().toJson(new User(checkUser.getFullName(), checkUser.getEmail(),
                                                                 checkUser.getNickname(),
                                                                 Role.REGISTERED_USER, checkUser.getPassword()));

                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    resp.setHeader("Access-Control-Allow-Origin", "*");
                    resp.getWriter().write(userJSON);
                }
                else{
                    String userJSON = new Gson().toJson(new User());

                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    resp.setHeader("Access-Control-Allow-Origin", "*");
                    resp.getWriter().write(userJSON);
                }
                break;
            case "record":
                String fullName = bodyReq.get("fullName").getAsString();
                String email = bodyReq.get("email").getAsString();
                String nick = bodyReq.get("nickname").getAsString();
                String pass = bodyReq.get("password").getAsString();

                User newUser = new User(fullName, email, nick, Role.REGISTERED_USER, pass);
                userSerice.add(newUser);

                String newUserJSON = new Gson().toJson(newUser);

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.setHeader("Access-Control-Allow-Origin", "*");
                resp.getWriter().write(newUserJSON);
                break;
            default:
                throw new IllegalAccessError("eror");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
