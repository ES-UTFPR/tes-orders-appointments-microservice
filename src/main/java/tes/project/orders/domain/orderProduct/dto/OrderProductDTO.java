package tes.project.orders.domain.orderProduct.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderProductDTO(
    @NotBlank(message = "Product cannot be empty")
    Integer productId,

    @Min(1)
    Integer quantity
) {}
