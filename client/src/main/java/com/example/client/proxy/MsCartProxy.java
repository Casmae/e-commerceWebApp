package com.example.client.proxy;

import com.example.client.bean.CartBean;
import com.example.client.bean.CartItemBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "ms-cart", url = "localhost:8095")
public interface MsCartProxy {

    @PostMapping(value = "/carts")
    public ResponseEntity<CartBean> createNewCart(@RequestBody CartBean cartData);

    @GetMapping(value = "/getCart/{id}")
    public Optional<CartBean> getCart(@PathVariable Long id);

    @PostMapping(value = "/cart/{id}")
    public ResponseEntity<CartBean> addProductToCart(@PathVariable Long id, @RequestBody CartItemBean cartItem);

    @PostMapping("/cartItem")
    public ResponseEntity<CartItemBean> createNewCartItem(@RequestBody CartItemBean cartItemBody);

    @GetMapping(value = "/cartItem/{id}")
    public Optional<CartItemBean> getCartItem(@PathVariable Long id);

    @PostMapping("/updateCartItem")
    public ResponseEntity<CartItemBean> updateCartItem(@RequestBody CartItemBean cartItemBody);

    @PostMapping("/deleteCartItems")
    public void deleteCartItems(@RequestBody CartBean cartItemBody);

    @PostMapping("/deleteCartItem/{id}")
    public void deleteCartItem(@PathVariable Long id);

    }

