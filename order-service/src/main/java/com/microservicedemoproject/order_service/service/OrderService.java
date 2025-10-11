package com.microservicedemoproject.order_service.service;

import com.microservicedemoproject.order_service.dto.InventoryResponse;
import com.microservicedemoproject.order_service.dto.OrderLineItemsDto;
import com.microservicedemoproject.order_service.dto.OrderRequest;
import com.microservicedemoproject.order_service.model.Order;
import com.microservicedemoproject.order_service.model.OrderLineItems;
import com.microservicedemoproject.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;


    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

         List<OrderLineItems> orderLineItemsList =  orderRequest.getOrderLineItemDtoList()
                 .stream()
                 .map(this::mapToOrderLineItem).toList();

         order.setOrderLineItemList(orderLineItemsList);

         List<String> skuCodes = order.getOrderLineItemList().stream().map(OrderLineItems::getSkuCode).toList();
         // call inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        Boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::getIsInStock);
        if(allProductsInStock){
            orderRepository.save(order);
        }
        else{
            throw  new IllegalArgumentException("Product not in stock, please try again later");
        }

    }

    private OrderLineItems mapToOrderLineItem(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return  orderLineItems;
    }
}
