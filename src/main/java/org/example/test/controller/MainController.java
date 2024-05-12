package org.example.test.controller;

import jakarta.servlet.http.HttpSession;
import org.example.test.model.Cart;
import org.example.test.model.Category;
import org.example.test.model.Customer;
import org.example.test.model.Product;
import org.example.test.service.CartService;
import org.example.test.service.CategoryService;
import org.example.test.service.CustomerServiceRegister;
import org.example.test.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

	@Autowired
	private CustomerServiceRegister customerService ;
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CartService cartService;
	public void setSession(HttpSession sesssion, String email){
		//set user
		Optional<Customer> cus = customerService.getCustomerByEmail(email);
		Customer customer = cus.get();
		//System.out.println("check set session khi login77: " + customer.getName());
		sesssion.setAttribute("customer", customer);

		//set products, categories
		List<Category> categoryList = categoryService.getAllCategoryByStatus("on");
		List<Product> productList = productService.getAllProductByStatus("on");
		sesssion.setAttribute("categories", categoryList);
		sesssion.setAttribute("products", productList);

		//set cart (tuy nhiên mỗi lần thêm xoá sửa cart thì phải set lại nó mới hiện đúng ngay nha)
		//chỉ lấy status='on'
		Cart cart = cartService.getCartWithProductsOnStatus(customer);
		sesssion.setAttribute("cart", cart);

	}

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

//	@GetMapping("/login")
//	public String login() {
//		logger.info("Accessing the login page");
//		return "home/login";
//	}

	@GetMapping("/index")
	public String home(HttpSession session) {
		logger.info("Accessing the home page");

		//lấy username (email) từ SpringSecurity
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();

		//set session
		setSession(session, username);

		return "redirect:/home";
	}
}
