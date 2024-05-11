package org.example.test.controller;

import jakarta.servlet.http.HttpSession;
import org.example.test.model.Favorite;
import org.example.test.model.Customer;
import org.example.test.model.Product;
import org.example.test.service.CartService;
import org.example.test.service.FavoriteService;
import org.example.test.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/favorite")
public class FavoriteController {
    @Autowired
    private CartService cartService;
    @Autowired
    private FavoriteService favoriteService;
    @Autowired
    private ProductService productService;

    @GetMapping("/getFavorite")
    public String getFavorite(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        Favorite favorite = favoriteService.findByCustomer(customer);

        if (favorite == null) {
            favorite = new Favorite();
        } else {
            session.setAttribute("favorite", favorite);
        }

        model.addAttribute("favorite", favorite);
        return "home/favorite";
    }
    @GetMapping("/remove/{productId}")
    public String removeProductFromFavorites(@PathVariable Long productId, HttpSession session, RedirectAttributes redirectAttributes) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            redirectAttributes.addFlashAttribute("message", "Bạn cần đăng nhập để thực hiện thao tác này.");
            return "redirect:/login";
        }

        Product product = productService.getProductById(productId);
        if (product == null) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm không tồn tại.");
            return "redirect:/favorite/getFavorite";
        }

        favoriteService.removeProductFromFavorites(customer, product);
        redirectAttributes.addFlashAttribute("success", "Đã xóa sản phẩm khỏi danh sách yêu thích.");
        return "redirect:/favorite/getFavorite";
    }
}
