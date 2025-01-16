package tes.project.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import tes.project.orders.domain.orderProduct.OrderProduct;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByOrderId(Long orderId);

    @Transactional
    @Modifying
    @Query("DELETE FROM OrderProduct op WHERE op.orderId = :orderId")
    void deleteByOrderId(@Param("orderId") Long orderId);
}
