package com.example.client.service;

import com.example.client.bean.CartItemBean;
import com.example.client.bean.OrderItemBean;
import com.example.client.bean.ProductBean;
import com.example.client.proxy.MsCartProxy;
import com.example.client.proxy.MsProductProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private MsProductProxy msProductProxy;

    @Autowired
    private MsCartProxy msCartProxy;

    public CartService() {
    }

    public Map<ProductBean, CartItemBean> convertListToMap(List<CartItemBean> items) {
        Map<ProductBean, CartItemBean> map = new HashMap<>();
        for (CartItemBean item : items) {
            Optional<ProductBean> product = msProductProxy.get(item.getProductId());
            map.put(product.get(), item);
        }
        return map;
    }

    public double calculateTotalPrice (Map<ProductBean,CartItemBean> map){
        double total = 0L;
        for( Map.Entry<ProductBean,CartItemBean> mapIterate : map.entrySet()){
            total = total + (mapIterate.getKey().getPrice() * mapIterate.getValue().getQuantity());

        }

        return total;
    }

    public List<OrderItemBean> convertCartItemListToOrderItemList(List<CartItemBean> cartItems){

        List<OrderItemBean> orderItems = cartItems.stream()
                .map(cartItemBean -> convertToOrderItem(cartItemBean)).collect(Collectors.toList());
        return orderItems;
    }

    public OrderItemBean convertToOrderItem (CartItemBean cartItemBean){
        OrderItemBean orderItemBean = new OrderItemBean();
                orderItemBean.setQuantity(cartItemBean.getQuantity());
                orderItemBean.setProductId(cartItemBean.getProductId());

        return  orderItemBean;
    }
}
