package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import dto.User_DTO;
import entity.Category;
import entity.Color;
import entity.Model;
import entity.Product;
import entity.Product_Status;
import entity.Product_condition;
import entity.Storage;
import entity.User;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@MultipartConfig
@WebServlet(name = "ProductListing", urlPatterns = {"/ProductListing"})
public class ProductListing extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();

        String categoryId = request.getParameter("categoryId");
        String modelId = request.getParameter("modelId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String storageId = request.getParameter("storageId");
        String colorId = request.getParameter("colorId");
        String conditionId = request.getParameter("conditionId");
        String price = request.getParameter("price");
        String quantity = request.getParameter("quantity");

        Part image1 = request.getPart("image1");
        Part image2 = request.getPart("image2");
        Part image3 = request.getPart("image3");

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (!Validation.isInteger(categoryId)) {
            response_DTO.setContent("Invalid Category");

        } else if (!Validation.isInteger(modelId)) {
            response_DTO.setContent("Invalid Model");

        } else if (!Validation.isInteger(storageId)) {
            response_DTO.setContent("Invalid Storage");

        } else if (!Validation.isInteger(colorId)) {
            response_DTO.setContent("Invalid Color");

        } else if (!Validation.isInteger(conditionId)) {
            response_DTO.setContent("Invalid Condition");

        } else if (title.isEmpty()) {
            response_DTO.setContent("Please fill Tilte");
        } else if (description.isEmpty()) {
            response_DTO.setContent("Please fill description");
        } else if (price.isEmpty()) {
            response_DTO.setContent("Please fill price");
        } else if (!Validation.isDouble(price)) {
            response_DTO.setContent("Invalid Price");
        } else if (Double.parseDouble(price) <= 0) {
            response_DTO.setContent("Price must be greater than 0");
        } else if (quantity.isEmpty()) {
            response_DTO.setContent("Please fill Quantity");
        } else if (!Validation.isInteger(quantity)) {
            response_DTO.setContent("Invalid quantitiy");
        } else if (Integer.parseInt(quantity) <= 0) {
            response_DTO.setContent("Quantity must be greater than 0");

        } else if (image1.getSubmittedFileName() == null) {
            response_DTO.setContent("Please upload Image 1");

        } else if (image2.getSubmittedFileName() == null) {
            response_DTO.setContent("Please upload Image 2");

        } else if (image3.getSubmittedFileName() == null) {
            response_DTO.setContent("Please upload Image 3");

        } else {

            Category category = (Category) session.get(Category.class, Integer.parseInt(categoryId));

            if (category == null) {
                response_DTO.setContent("Please Select a Valid Category");
            } else {

                Model model = (Model) session.get(Model.class, Integer.parseInt(modelId));

                if (model == null) {
                    response_DTO.setContent("Please Select a Valid Model");
                } else {

                    if (model.getCategory().getId() != category.getId()) {
                        response_DTO.setContent("Please Select a Valid Model");
                    } else {
                        Storage storage = (Storage) session.get(Storage.class, Integer.parseInt(storageId));

                        if (storage == null) {
                            response_DTO.setContent("Please Select a Valid Storage");
                        } else {
                            Color color = (Color) session.get(Color.class, Integer.parseInt(colorId));

                            if (color == null) {
                                response_DTO.setContent("Please Select a Valid Color");
                            } else {
                                Product_condition condition = (Product_condition) session.get(Product_condition.class, Integer.parseInt(conditionId));

                                if (condition == null) {
                                    response_DTO.setContent("Please Select a Valid Condition");
                                } else {
                                    Product product = new Product();
                                    product.setColor(color);
                                    product.setDate_time(new Date());
                                    product.setDescription(description);
                                    product.setModel(model);
                                    product.setPrice(Double.parseDouble(price));

                                    //get Active status
                                    Product_Status product_Status = (Product_Status) session.load(Product_Status.class, 1);
                                    product.setProduct_Status(product_Status);

                                    product.setProduct_condition(condition);
                                    product.setQty(Integer.parseInt(quantity));
                                    product.setStorage(storage);
                                    product.setTitle(title);

                                    //get user
                                    User_DTO user_DTO = (User_DTO) request.getSession().getAttribute("user");
                                    Criteria criteria1 = session.createCriteria(User.class);
                                    criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
                                    User user = (User) criteria1.uniqueResult();
                                    product.setUser(user);

                                    
                                    int pid = (int) session.save(product);
                                    session.beginTransaction().commit();

                                    String applicationPath = request.getServletContext().getRealPath("");
                                    String newApplicationPath = applicationPath.replace("build" + File.separator + "web", "web");
                                    File folder = new File(newApplicationPath + "//product-images//" + pid);
                                    folder.mkdir();

                                    File file1 = new File(folder, pid + "image1.png");
                                    InputStream inputStream1 = image1.getInputStream();
                                    Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    File file2 = new File(folder, pid + "image2.png");
                                    InputStream inputStream2 = image2.getInputStream();
                                    Files.copy(inputStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    File file3 = new File(folder, pid + "image3.png");
                                    InputStream inputStream3 = image3.getInputStream();
                                    Files.copy(inputStream3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                    response_DTO.setSuccess(true);
                                    response_DTO.setContent("New Product Added");
                                }
                            }
                        }
                    }

                }
            }

//            Criteria criteria1 = session.createCriteria(type);
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
        session.close();

    }

}
