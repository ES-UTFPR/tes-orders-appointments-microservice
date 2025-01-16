package tes.project.orders.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.Valid;
import tes.project.orders.domain.order.Order;
import tes.project.orders.domain.order.dto.OrderDTO;
import tes.project.orders.domain.order.dto.OrderStatusDTO;
import tes.project.orders.domain.order.dto.OrderUpdateDTO;
import tes.project.orders.domain.order.dto.ProductResponse;
import tes.project.orders.domain.order.exception.ResourceNotFoundException;
import tes.project.orders.domain.orderProduct.OrderProduct;
import tes.project.orders.domain.orderProduct.dto.OrderProductDTO;
import tes.project.orders.repository.OrderProductRepository;
import tes.project.orders.repository.OrderRepository;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;

    @Autowired
    private RestTemplate restTemplate;

    public OrderController(OrderRepository orderRepository, OrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    @PostMapping
    public ResponseEntity<Order> create(@Valid @RequestBody OrderDTO orderCreateDTO) {
        Order order = new Order();
        order.setUserId(orderCreateDTO.userId());
    
        int totalAmount = calculateTotalAmount(orderCreateDTO.products());
        order.setAmount(totalAmount);
        order.setStatus("pending");
    
        Order savedOrder = orderRepository.save(order);
    
        List<OrderProduct> orderProducts = orderCreateDTO.products().stream().map(dto -> {
            OrderProduct product = new OrderProduct();
            product.setOrderId(savedOrder.getId());
            product.setProductId(dto.productId());
            product.setQuantity(dto.quantity());
            return product;
        }).collect(Collectors.toList());
    
        orderProductRepository.saveAll(orderProducts);
    
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @Valid @RequestBody OrderUpdateDTO orderUpdateDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if ("completed".equals(order.getStatus()) || "cancelled".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot update an order that is completed or cancelled");
        }

        if (orderUpdateDTO.products() != null && !orderUpdateDTO.products().isEmpty()) {
            orderProductRepository.deleteByOrderId(order.getId());

            List<OrderProduct> newProducts = orderUpdateDTO.products().stream().map(dto -> {
                OrderProduct product = new OrderProduct();
                product.setOrderId(order.getId());
                product.setProductId(dto.productId());
                product.setQuantity(dto.quantity());
                return product;
            }).collect(Collectors.toList());

            orderProductRepository.saveAll(newProducts);

            int totalAmount = calculateTotalAmount(orderUpdateDTO.products());
            order.setAmount(totalAmount);
        }

        Order updatedOrder = orderRepository.save(order);

        return ResponseEntity.ok(updatedOrder);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> statusUpdate(@PathVariable Long id, @Valid @RequestBody OrderStatusDTO statusUpdateDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if ("completed".equals(order.getStatus()) || "canceled".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot change a closed order");
        }
        
        order.setStatus(statusUpdateDTO.status());
        Order updatedOrder = orderRepository.save(order);
        
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if ("completed".equals(order.getStatus()) || "canceled".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot delete a completed order");
        }
        
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Order>> index() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<OrderProduct>> products(@PathVariable Long id) {
        return ResponseEntity.ok(orderProductRepository.findByOrderId(id));
    }

    private int calculateTotalAmount(List<OrderProductDTO> products) {
        int total = 0;

        for (OrderProductDTO dto : products) {
            String url = "http://localhost:8084/products/" + dto.productId();
            ProductResponse productResponse = restTemplate.getForObject(url, ProductResponse.class);

            if (productResponse == null || productResponse.price() == null) {
                throw new ResourceNotFoundException("Product not found with id: " + dto.productId());
            }

            total += productResponse.price() * dto.quantity();
        }

        return total;
    }
}
