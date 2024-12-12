package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.Response_DTO;
import dto.User_DTO;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Verify", urlPatterns = {"/Verify"})
public class Verify extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Response_DTO respons_DTO = new Response_DTO();

        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(request.getReader(), JsonObject.class);

        String verification = dto.get("verification").getAsString();

        if (request.getSession().getAttribute("email") != null) {

            String email = request.getSession().getAttribute("email").toString();

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", email));
            criteria1.add(Restrictions.eq("verification", verification));

            if (!criteria1.list().isEmpty()) {

                User user = (User) criteria1.list().get(0);
                user.setVerification("Verified");

                session.update(user);
                session.beginTransaction().commit();

                
                User_DTO user_DTO = new User_DTO();
                user_DTO.setFirstname(user.getFirstname());
                user_DTO.setLastname(user.getLsatname());
                user_DTO.setEmail(email);
                request.getSession().removeAttribute("email");
                request.getSession().setAttribute("user", user_DTO);

                respons_DTO.setSuccess(true);
                respons_DTO.setContent("Verification success");

            } else {
                //invalid verification code
                respons_DTO.setContent("Invalid verification code!");
            }

        } else {
            respons_DTO.setContent("Verification unavailable! please login again");
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(respons_DTO));

    }

}
