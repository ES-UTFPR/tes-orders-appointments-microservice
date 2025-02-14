package tes.project.orders.domain.order.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OrderStatusDTO(
    @NotNull(message = "Status cannot be null")
    @Pattern(regexp = "pending|confirmed|canceled", message = "Invalid status value")
    String status
) {}
