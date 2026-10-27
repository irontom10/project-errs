package net.errs.internal.workorder.closed;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClosedWorkOrderService {

    private final JdbcTemplate jdbc;

    public ClosedWorkOrderService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<ClosedWorkOrderDto> list(int page, int limit) {
        long offset = ((long) page - 1) * limit;

        String sql = """
                SELECT
                    work_orders.work_order_id,
                    work_orders.work_order_number,
                    customers.company_name,
                    work_orders.received_at,
                    assets.unit_number,
                    work_orders.odometer_in,
                    work_orders.status,
                    work_orders.recommendations,
                    work_orders.complaint,
                    ROUND(
                        COALESCE(parts_totals.parts_total, 0) +
                        COALESCE(labor_totals.labor_total, 0) +
                        COALESCE(sublet_totals.sublet_total, 0),
                        2
                    ) AS work_order_total
                FROM work_orders
                LEFT JOIN customers
                    ON work_orders.customer_id = customers.customer_id
                LEFT JOIN assets
                    ON work_orders.asset_id = assets.asset_id
                LEFT JOIN (
                    SELECT work_order_id,
                        SUM(quantity * unit_price + core_charge +
                            hazardous_charge + federal_excise_tax) AS parts_total
                    FROM work_order_parts
                    GROUP BY work_order_id
                ) AS parts_totals
                    ON work_orders.work_order_id = parts_totals.work_order_id
                LEFT JOIN (
                    SELECT work_order_id,
                        SUM(COALESCE(estimated_hours, 0) * hourly_rate) AS labor_total
                    FROM work_order_labor
                    GROUP BY work_order_id
                ) AS labor_totals
                    ON work_orders.work_order_id = labor_totals.work_order_id
                LEFT JOIN (
                    SELECT work_order_id, SUM(sale_price) AS sublet_total
                    FROM work_order_sublets
                    GROUP BY work_order_id
                ) AS sublet_totals
                    ON work_orders.work_order_id = sublet_totals.work_order_id
                WHERE work_orders.status = 'closed'
                  AND work_orders.deleted_at IS NULL
                ORDER BY work_orders.work_order_id DESC
                LIMIT ? OFFSET ?
                """;

        return jdbc.query(sql, (rs, rowNum) -> new ClosedWorkOrderDto(
                rs.getLong("work_order_id"),
                rs.getString("work_order_number"),
                rs.getString("company_name"),
                rs.getTimestamp("received_at") == null
                        ? null : rs.getTimestamp("received_at").toLocalDateTime(),
                rs.getString("unit_number"),
                rs.getObject("odometer_in", Long.class),
                rs.getString("status"),
                rs.getString("recommendations"),
                rs.getString("complaint"),
                rs.getBigDecimal("work_order_total")
        ), limit, offset);
    }
}
