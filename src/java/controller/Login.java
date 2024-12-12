package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.Cart_DTO;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest requset, HttpServletResponse response) throws ServletException, IOException {
        Response_DTO respons_DTO = new Response_DTO();

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        final User_DTO user_DTO = gson.fromJson(requset.getReader(), User_DTO.class);

        if (user_DTO.getEmail().isEmpty()) {
            respons_DTO.setContent("Please, enter your Email!");
        } else if (user_DTO.getPassword().isEmpty()) {
            respons_DTO.setContent("Please, enter your password!");
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            criteria1.add(Restrictions.eq("password", user_DTO.getPassword()));

            if (!criteria1.list().isEmpty()) {
                User user = (User) criteria1.list().get(0);
                if (!user.getVerification().equals("Verified")) {
                    // not verified
                    requset.getSession().setAttribute("email", user_DTO.getEmail());

                    respons_DTO.setContent("Unverified");
                } else {
                    //verified
                    user_DTO.setFirstname(user.getFirstname());
                    user_DTO.setLastname(user.getLsatname());
                    user_DTO.setPassword(null);
                    requset.getSession().setAttribute("user", user_DTO);
                    
                    //Transfer Session Cart to Db cart
                    if (requset.getSession().getAttribute("sessionCart") != null) {
//                    get  session cart
                        ArrayList<Cart_DTO> sessionCart = (ArrayList<Cart_DTO>) requset.getSession().getAttribute("sessionCart");
//                     get db cart
                        Criteria criteria2 = session.createCriteria(Cart.class);
                        criteria2.add(Restrictions.eq("user", user));
                        List<Cart> dbCart = criteria2.list();

                        if (dbCart.isEmpty()) {
                            //Db cart Empty
                            //Add all session cart items to DB cart

                            for (Cart_DTO cart_DTO : sessionCart) {
                                Cart cart = new Cart();
                                cart.setProduct(cart_DTO.getProduct());
                                cart.setQty(cart_DTO.getQty());
                                cart.setUser(user);
                                session.save(cart);
                            }
                        } else {
                            //Found items in DB Cart
                            for (Cart_DTO cart_DTO : sessionCart) {

                                boolean isFoundInCart = false;

                                for (Cart cart : dbCart) {
                                    if (cart_DTO.getProduct().getId() == cart.getProduct().getId()) {
                                        //same item found in session Cart & Db cart
                                        isFoundInCart = true;
                                        if ((cart_DTO.getQty() + cart.getQty()) <= cart.getProduct().getQty()) {
                                            //quantity availavle 
                                            cart.setQty(cart_DTO.getQty() + cart.getQty());
                                            session.update(cart);
                                        } else {
                                            ///quantity not available
                                            //set max available qty
                                            cart.setQty(cart.getProduct().getQty());
                                            session.update(cart);
                                        }
                                    }
                                }
                                if (!isFoundInCart) {
                                    //not Found in DB Cart 
                                    Cart cart = new Cart();
                                    cart.setProduct(cart_DTO.getProduct());
                                    cart.setQty(cart_DTO.getQty());
                                    cart.setUser(user);
                                    session.save(cart);
                                }
                            }
                        }
                        requset.getSession().removeAttribute("sessionCart");
                        session.beginTransaction().commit();
                    }

                    respons_DTO.setSuccess(true);
                    respons_DTO.setContent("Login success");
                }
            } else {
                respons_DTO.setContent("Invalid details! Please try again");
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(respons_DTO));
    }

}
