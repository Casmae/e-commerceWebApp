package com.example.client.proxy;

import com.example.client.bean.CartItemBean;
import com.example.client.bean.OrderBean;
import com.example.client.bean.OrderItemBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ms-order", url = "localhost:8092")

public interface MsOrderProxy {

    @PostMapping("/order")
    public ResponseEntity<OrderBean> createNewOrder(@RequestBody OrderBean orderBody);

    @PostMapping("/orderItem")
    public ResponseEntity<List<OrderItemBean>> createNewOrderItems(@RequestBody List<OrderItemBean> orderItemsBody);


}
