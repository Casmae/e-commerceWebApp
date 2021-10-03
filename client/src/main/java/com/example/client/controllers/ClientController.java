package com.example.client.controllers;

import com.example.client.bean.*;
import com.example.client.proxy.MsCartProxy;
import com.example.client.proxy.MsOrderProxy;
import com.example.client.proxy.MsProductProxy;
import com.example.client.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ClientController {

    @Autowired
    private MsProductProxy msProductProxy;

    @Autowired
    private MsCartProxy msCartProxy;

    @Autowired
    private CartService cartService;

    @Autowired
    private MsOrderProxy msOrderProxy;

    Long cartIdentif = null ;

    @RequestMapping("/")
    public String getListProduct(Model model) {
        if (cartIdentif == null){
            cartIdentif = msCartProxy.createNewCart(new CartBean()).getBody().getId();
        }

        List<ProductBean> products = msProductProxy.list();
        model.addAttribute("products", products);
        model.addAttribute("activePage","home");
        return "index";
    }

    @RequestMapping("/product-detail/{id}")
    public String product(@PathVariable Long id,Model model) {
        ProductBean product = msProductProxy.get(id).get();
        model.addAttribute("product", product);
        return "product-detail";
    }

    @RequestMapping(value ="/cart/addProduct", method = RequestMethod.POST,
    consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public RedirectView addProduct(CartItemBean cartItemBean, Model model, RedirectAttributes redirAttrs) {
        Optional<CartItemBean> exist = msCartProxy.getCartItem(cartItemBean.getProductId());
        if (exist.isPresent()){
            exist.get().setQuantity(exist.get().getQuantity() + cartItemBean.getQuantity());
            ResponseEntity<CartItemBean> itemUpdated = msCartProxy.updateCartItem(exist.get());
            if (itemUpdated.getStatusCodeValue()== 201)
                redirAttrs.addFlashAttribute("success", "Le Produit a bien été ajouté au panier");

        }else{
            ResponseEntity<CartItemBean> cartItem = msCartProxy.createNewCartItem(cartItemBean);
            ResponseEntity<CartBean> cart = msCartProxy.addProductToCart(cartIdentif,cartItem.getBody());
            if (cart.getStatusCodeValue()== 200)
                redirAttrs.addFlashAttribute("success", "Le Produit a bien été ajouté au panier");
        }
        return new RedirectView("/product-detail/"+cartItemBean.getProductId());
    }

    @RequestMapping(value ="/getCart")
    public String getCart(Model model) {
        Optional<CartBean> cartItems = msCartProxy.getCart(cartIdentif);
       Map<ProductBean,CartItemBean> map = cartService.convertListToMap(cartItems.get().getProducts());
        model.addAttribute("map", map);
        model.addAttribute("total",cartService.calculateTotalPrice(map));
        model.addAttribute("activePage","shoppingCart-view");
        model.addAttribute("standardDate", new Date());

        return "shopping-cart";
    }


    @RequestMapping(value ="/cartItem/delete/{id}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public RedirectView deleteCartItem(@PathVariable Long id,RedirectAttributes redirAttrs) {

        msCartProxy.deleteCartItem(id);

        return new RedirectView("/getCart");
    }

    @RequestMapping(value ="/getOrder")
    public String getOrder(Model model) {

        model.addAttribute("activePage","order-view");
        return "order";
    }

    @RequestMapping(value ="/order/addOrder", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String addOrder(OrderBean order, Model model) {

        Optional<CartBean> cartItems = msCartProxy.getCart(cartIdentif);
        Map<ProductBean,CartItemBean> map = cartService.convertListToMap(cartItems.get().getProducts());
        List<OrderItemBean> orderItemsBean = cartService.convertCartItemListToOrderItemList(cartItems.get().getProducts());
        order.setProducts(orderItemsBean);
        msOrderProxy.createNewOrder(order);
        msCartProxy.deleteCartItems(cartItems.get());
        model.addAttribute("order",order);
        model.addAttribute("map",map);
        model.addAttribute("activePage","order-view");

        return ("order");
    }
}
