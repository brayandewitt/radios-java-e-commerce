package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import model.Mail;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest requset, HttpServletResponse response) throws ServletException, IOException {
        Response_DTO respons_DTO = new Response_DTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        final User_DTO user_DTO = gson.fromJson(requset.getReader(), User_DTO.class);

        if (user_DTO.getFirstname().isEmpty()) {
            respons_DTO.setContent("Please, enter your first name!");
        } else if (user_DTO.getLastname().isEmpty()) {
            respons_DTO.setContent("Please, enter your last name!");
        } else if (user_DTO.getEmail().isEmpty()) {
            respons_DTO.setContent("Please, enter your Email!");
        } else if (!Validation.isEmailValid(user_DTO.getEmail())) {
            respons_DTO.setContent("Please, enter Valid Email!");
        } else if (user_DTO.getPassword().isEmpty()) {
            respons_DTO.setContent("Please, enter your password!");
        } else if (!Validation.isPasswordValid(user_DTO.getPassword())) {
            respons_DTO.setContent("password must include at least one uppercase letter ,number, special character and be at least eight characters!");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            if (!criteria1.list().isEmpty()) {
                respons_DTO.setContent("User with this email already exists!");

            } else {
                //genarate verification code
                int code = (int) (Math.random() * 1000000);

                User user = new User();
                user.setEmail(user_DTO.getEmail());
                user.setFirstname(user_DTO.getFirstname());
                user.setLsatname(user_DTO.getLastname());
                user.setPassword(user_DTO.getPassword());
                user.setVerification(String.valueOf(code));
                //

                //send verification email
                final String content = "<html lang=\"en\">\n"
                        + "<head>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                        + "    <title>Verify Your Email</title>\n"
                        + "</head>\n"
                        + "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0;\">\n"
                        + "    <table align=\"center\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">\n"
                        + "        <tr>\n"
                        + "            <td style=\"text-align: center;\">\n"
                        + "                <h2 style=\"color: #333333;\">Verify Your Email</h2>\n"
                        + "                <p style=\"color: #666666;\">Thank you for signing up with Radios!</p>\n"
                        + "                <p style=\"color: #666666;\">Please use the verification code below to complete your registration:</p>\n"
                        + "                <h3 style=\"color: #007bff; font-size: 24px; margin: 20px 0;\">" + code + "</h3>\n"
                        + "                <p style=\"color: #666666;\">If you didn’t request this, you can safely ignore this email.</p>\n"
                        + "            </td>\n"
                        + "        </tr>\n"
                        + "    </table>\n"
                        + "</body>\n"
                        + "</html>";

                Thread sendMailThread = new Thread() {
                    @Override
                    public void run() {
                        Mail.sendMail(user_DTO.getEmail(), "Verification Radios Account", content);
                    }
                };

                 sendMailThread.start();
                session.save(user);
                session.beginTransaction().commit();

                requset.getSession().setAttribute("email", user_DTO.getEmail());
                respons_DTO.setSuccess(true);
                respons_DTO.setContent("Registration complete.Please verify your account!");
            }

            session.close();

        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(respons_DTO));
    }

}
