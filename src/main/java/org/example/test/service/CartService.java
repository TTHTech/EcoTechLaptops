package org.example.test.service;

import org.example.test.model.Cart;
import org.example.test.model.Customer;
import org.example.test.model.Item;
import org.example.test.model.Product;
import org.example.test.repository.CartRepository;
import org.example.test.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ProductService productService;

    public List<Cart> getAllCarts(){
        return cartRepository.findAll();
    }

    public Cart getCart(long id){
        return this.cartRepository.findCartById(id);
    }

    public void saveCart(Cart cart){
        cartRepository.save(cart);
    }

    public void updateQuantityCart(long cartId, long itemId, int hieuso){
        Cart cart = getCart(cartId);
        Item item = this.itemService.getItemById(itemId);
        System.out.println(">>>check cart: " + cart.getItems());
        System.out.println(">>>check this item: " + item.getProduct().getName());

        int sl = item.getQuantity()+ hieuso;
        if (sl < 0) {
            sl = 1;
        }
        if (sl == 0)
        {
            deleteItemById(cartId, itemId);
            this.cartRepository.save(cart);
            return;
        }

        item.setQuantity(sl);
        this.itemService.saveItem(item);
        this.cartRepository.save(cart);
    }

    public void deleteItemById(long cartId, long itemId) {
        Cart cart = getCart(cartId);
        if (cart != null) {
            // Tim Item can xoa trong Cart
            Item itemToRemove = null;
            for (Item item : cart.getItems()) {
                if (item.getId() == itemId) {
                    itemToRemove = item;
                    break;
                }
            }

            if (itemToRemove != null) {
                cart.getItems().remove(itemToRemove);
                itemService.deleteById(itemId);
                cartRepository.save(cart);
            } else {
                throw new IllegalArgumentException("Item with ID " + itemId + " does not exist in the cart.");
            }
        } else {
            throw new IllegalArgumentException("Cart with ID " + cartId + " does not exist.");
        }
    }

    public void addToCart(long cartId, long productId){
        Cart cart = cartRepository.findCartById(cartId);
        Product product = productService.getProductById(productId);

        long itemId = containsItem(cart, productId);
        //da ton tai san pham nay trong list item
        if ( itemId != 0){
            System.out.println("San pham da ton tai trong gio hang -> tang so luong len 1");
            updateQuantityCart(cartId, itemId, 1);
        } else {
            System.out.println("San pham chua ton tai trong gio hang -> tao moi");
            Item item = new Item(product, 1);
            cart.getItems().add(item);
            itemService.saveItem(item);
            cartRepository.save(cart);
        }
    }

    public long containsItem(Cart cart, long productId) {
        for (Item item : cart.getItems()) {
            if (item.getProduct().getId() == productId) {
                return item.getId();
            }
        }
        return 0;
    }

    //tìm cái cart bằng customer (khi đăng nhập thì tìm cái cart cho nó)
    public Cart findCart(Customer customer){
        return cartRepository.findCartByCustomer(customer);
    }

    //xoa het lineItem ra khoi cart
    public String deleteAllItems(Cart cart){
        List<Item> newList = new ArrayList<>();
        cart.setItems(newList);
        cartRepository.save(cart);

        return "Deleted all Item from cart with id=" + cart.getId();
    }

    public Cart getCartWithProductsOnStatus(Customer customer) {
        // Lấy giỏ hàng của khách hàng
        Cart cart = findCart(customer);

        // Lấy danh sách mục trong giỏ hàng
        List<Item> items = cart.getItems();
        List<Item> filteredItems = new ArrayList<>();

        // Lọc các mục trong giỏ hàng để chỉ giữ lại sản phẩm có trạng thái 'on'
        for (Item item : items) {
            if (item.getProduct().getStatus().equals("on")) {
                filteredItems.add(item);
            }
        }

        // Cập nhật lại danh sách các mục trong giỏ hàng chỉ bao gồm các sản phẩm có trạng thái 'on'
        cart.setItems(filteredItems);

        return cart;
    }

}
