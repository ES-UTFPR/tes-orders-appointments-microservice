package tes.project.orders.domain.order.dto;

import java.util.List;

import tes.project.orders.domain.orderProduct.dto.OrderProductDTO;

public record OrderUpdateDTO(
    List<OrderProductDTO> products
) {}
