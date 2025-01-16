package tes.project.orders.domain.order.dto;

public record ProductResponse(
    Long id,
    String name,
    Integer price
) {}
