package com.hongbang.web.servlet;

import com.hongbang.util.TokenManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/token/*")
public class TokenServlet extends BaseServlet {
    public void token(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        String token = TokenManager.generateToken(session);
        response.setContentType("text/plain");
        response.getWriter().write(token);
    }
}
