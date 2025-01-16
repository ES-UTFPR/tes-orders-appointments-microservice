package tes.project.orders.domain.order.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import tes.project.orders.domain.orderProduct.dto.OrderProductDTO;

public record OrderDTO(
    @NotBlank(message = "User cannot be empty")
    String userId,

    List<OrderProductDTO> products
) {}
