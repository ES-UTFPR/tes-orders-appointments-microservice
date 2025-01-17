package tes.project.orders.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tes.project.orders.config.RabbitMQConfig;
import tes.project.orders.domain.order.Order;
import tes.project.orders.domain.order.exception.ResourceNotFoundException;
import tes.project.orders.repository.OrderRepository;

@Service
public class PaymentConfirmedListener {

    @Autowired
    private OrderRepository orderRepository;
    
    public void construct(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_CONFIRMED_QUEUE)
    public void handlePaymentConfirmed(Long orderId) {
        System.out.println("Payment confirmed for order: " + orderId);

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setStatus("confirmed");
        orderRepository.save(order);
    }
}