package com.example.cart.controller;


import com.example.cart.domain.Cart;
import com.example.cart.domain.CartItem;
import com.example.cart.repositories.CartItemRepository;
import com.example.cart.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @PostMapping("/carts")
    public ResponseEntity<Cart> createNewCart(){
        Cart cart = cartRepository.save(new Cart());

        if (cart.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't create a new cart");

        return new ResponseEntity<Cart>(cart, HttpStatus.CREATED);
    }


    @PostMapping(value = "/cart/{id}")
    public Cart addProductToCart(@PathVariable Long id, @RequestBody CartItem cartItem)
    {

        Optional<Cart> cart = cartRepository.findById(id);

        if(!cart.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't get cart");

        cart.get().addProduct(cartItem);

        cartRepository.save(cart.get());
        return cart.get();
    }

    @PostMapping("/cartItem")
    public ResponseEntity<CartItem> createNewCartItem(@RequestBody CartItem cartItemBody){

        CartItem cartItem = cartItemRepository.save(cartItemBody);

        if (cartItem.getId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't create a new cart Item");

        return new ResponseEntity<CartItem>(cartItem, HttpStatus.CREATED);
    }

    @GetMapping(value = "/getCart/{id}")
    public Optional<Cart> get(@PathVariable Long id)
    {
        Optional<Cart> cartInstance = cartRepository.findById(id);
        if (!cartInstance.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Specified Cart doesn't exist");
        return cartInstance;
    }


    @PostMapping("/updateCartItem")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItemBody){

        Optional<CartItem> cartItem = cartItemRepository.findById(cartItemBody.getId());

        if(!cartItem.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't get cart");

        cartItem.get().setQuantity(cartItemBody.getQuantity());
        cartItemRepository.save(cartItem.get());
        return new ResponseEntity<CartItem>(cartItem.get(), HttpStatus.CREATED);
    }

    @GetMapping(value = "/cartItem/{id}")
    public Optional<CartItem> getCartItem(@PathVariable Long id)
    {
        Optional<CartItem> cartInstance = cartItemRepository.findByProductId(id);
        if (!cartInstance.isPresent())
            return null;
        return cartInstance;
    }

    @PostMapping("/deleteCartItems")
    public void deleteCartItems(@RequestBody Cart cart){

        Optional<Cart> cartInstance = cartRepository.findById(cart.getId());
        cartInstance.get().setProducts(null);
        cartItemRepository.deleteAll(cart.getProducts());

        List<CartItem> cartItems = cartItemRepository.findAll();
        assert(cartItems.isEmpty());

    }

    @PostMapping("/deleteCartItem/{id}")
    public void deleteCartItem(@PathVariable Long id){

        Cart cart = cartRepository.findAll().get(0);
        cart.getProducts().remove(cartItemRepository.getById(id));
        cartItemRepository.delete(cartItemRepository.getById(id));
    }



}
