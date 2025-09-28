package com.microservicedemoproject.order_service.service;

import com.microservicedemoproject.order_service.dto.OrderLineItemsDto;
import com.microservicedemoproject.order_service.dto.OrderRequest;
import com.microservicedemoproject.order_service.model.Order;
import com.microservicedemoproject.order_service.model.OrderLineItems;
import com.microservicedemoproject.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

         List<OrderLineItems> orderLineItemsList =  orderRequest.getOrderLineItemDtoList()
                 .stream()
                 .map(this::mapToOrderLineItem).toList();

         order.setOrderLineItemList(orderLineItemsList);
         orderRepository.save(order);
    }

    private OrderLineItems mapToOrderLineItem(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return  orderLineItems;
    }
}
