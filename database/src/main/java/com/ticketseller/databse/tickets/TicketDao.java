package com.ticketseller.databse.tickets;

import com.ticketseller.service.carriers.CarrierDetails;
import com.ticketseller.service.pathes.PathDetails;
import com.ticketseller.service.tickets.Ticket;
import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.service.tickets.TicketFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
class TicketDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String ticketByOwnerIdIsNullQuery = "SELECT * FROM ticket WHERE owner_id IS NULL";
    private final RowMapper<TicketEntity> ticketRowMapper = (resultSet, rowNum) -> new TicketEntity(
            resultSet.getLong("id"),
            resultSet.getInt("path_id"),
            resultSet.getInt("place"),
            resultSet.getDouble("price"),
            resultSet.getTimestamp("date_time"),
            resultSet.getLong("owner_id")
    );

    private final String ticketSummaryQuery = "SELECT ticket.*" +
            ", departure.name AS departure_point_name" +
            ", destination.name AS destination_point_name" +
            ", carrier.name AS carrier_name, carrier.phone AS carrier_phone" +
            ", path.duration" +
            " FROM ticket" +
            " JOIN path ON path.id = ticket.path_id" +
            " JOIN point AS departure ON path.departure_point_id = departure.id" +
            " JOIN point AS destination ON path.destination_point_id = destination.id" +
            " JOIN carrier ON carrier.id = path.carrier_id";
    private final RowMapper<TicketDetails> ticketDetailsRowMapper = (resultSet, rowNum) -> {
        CarrierDetails carrier = new CarrierDetails(
                resultSet.getString("carrier_name"),
                resultSet.getString("carrier_phone")
        );
        PathDetails path = new PathDetails(
                resultSet.getString("departure_point_name"),
                resultSet.getString("destination_point_name"),
                carrier,
                resultSet.getTime("duration")
        );
        return new TicketDetails(
                resultSet.getLong("id"),
                path,
                resultSet.getInt("place"),
                resultSet.getDouble("price"),
                resultSet.getTimestamp("date_time")
        );
    };

    @Autowired
    public TicketDao(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ticket")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<TicketEntity> findById(long id) {
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject("SELECT * FROM ticket WHERE id = :id",
                    new MapSqlParameterSource().addValue("id", id),
                    ticketRowMapper
            ));
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public TicketDetails getTicketDetailsById(long id) {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    ticketSummaryQuery + " WHERE ticket.id = :id",
                    new MapSqlParameterSource().addValue("id", id),
                    ticketDetailsRowMapper
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public List<TicketEntity> findByOwnerIdIsNull(int limit, long offset) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", limit);
        params.addValue("offset", offset);
        // TODO: not so fast, redesign to key-set pagination
        return namedParameterJdbcTemplate.query(
                ticketByOwnerIdIsNullQuery + " LIMIT :limit OFFSET :offset",
                params,
                ticketRowMapper
        );
    }

    public List<TicketDetails> getTicketSummaryByOwnerIdIsNull(Pageable pageable, TicketFilter ticketFilter) {
        StringBuilder sb = new StringBuilder(ticketSummaryQuery);
        sb.append(" WHERE ticket.owner_id IS NULL");
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (ticketFilter != null) {
            // Можно вставить значения сразу в строку, но тогда их будет видно в ошибке если что-то пойдет не так.
            if (ticketFilter.date() != null) {
                params.addValue("dateTime", Timestamp.valueOf(ticketFilter.date()));
                sb.append(" AND date_time >= :dateTime");
            }
            if (ticketFilter.departurePoint() != null) {
                params.addValue("departurePoint", ticketFilter.departurePoint());
                sb.append(" AND departure.name LIKE '%:departurePoint%'");
            }
            if (ticketFilter.destinationPoint() != null) {
                params.addValue("destinationPoint", ticketFilter.destinationPoint());
                sb.append(" AND destination.name LIKE '%:destinationPoint%'");
            }
            if (ticketFilter.carrierName() != null) {
                params.addValue("carrierName", ticketFilter.carrierName());
                sb.append(" AND carrier.name LIKE '%:carrierName%'");
            }
        }
        if (pageable.isPaged()) {
            params.addValue("limit", pageable.getPageSize());
            params.addValue("offset", pageable.getOffset());
            sb.append(" LIMIT :limit OFFSET :offset");
        }
        // TODO: not so fast, redesign to key-set pagination
        return namedParameterJdbcTemplate.query(
                sb.toString(),
                params,
                ticketDetailsRowMapper
        );
    }

    public long getNumberOfAvailableTickets() {
        Long result = jdbcTemplate.queryForObject("SELECT count(*) FROM ticket WHERE owner_id IS NULL", Long.class);
        return result != null ? result : 0;
    }

    public List<TicketEntity> findByOwnerId(Long id) {
        try {
            return namedParameterJdbcTemplate.query(
                    "SELECT * FROM ticket WHERE owner_id = :id",
                    new MapSqlParameterSource().addValue("owner_id", id),
                    ticketRowMapper
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public List<TicketDetails> getTicketsSummaryByOwnerId(Long id) {
        try {
            return namedParameterJdbcTemplate.query(
                    ticketSummaryQuery + " WHERE owner_id = :owner_id",
                    new MapSqlParameterSource().addValue("owner_id", id),
                    ticketDetailsRowMapper
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public long getNumberOfTicketsByOwnerId(Long id) {
        try {
            Long result = namedParameterJdbcTemplate.queryForObject(
                    "SELECT count(*) FROM ticket WHERE owner_id = :id",
                    new MapSqlParameterSource().addValue("id", id),
                    Long.class
            );
            return result != null ? result : 0;
        } catch (EmptyResultDataAccessException ex) {
            return 0;
        }
    }

    public void update(Ticket ticket) {
        namedParameterJdbcTemplate.update(
                "UPDATE ticket SET owner_id = :ownerId WHERE id = :id",
                new BeanPropertySqlParameterSource(ticket)
        );
    }
}
