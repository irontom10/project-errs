package net.errs.internal.workorder.closed;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClosedWorkOrderDto(
        long workOrderId,
        String workOrderNumber,
        String customerName,
        LocalDateTime receivedAt,
        String unitNumber,
        Long odometerIn,
        String status,
        String recommendations,
        String complaint,
        BigDecimal workOrderTotal
) {}
