package com.ticketseller.service.tickets;

import com.ticketseller.web.MainConfig;
import com.ticketseller.web.tickets.TicketController;
import com.ticketseller.web.tickets.TicketFilterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {
        MainConfig.class,
        TicketController.class
})
public class TicketControllerTest {

    @MockBean
    private TicketService ticketService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAvailableTicketsPage() throws Exception {
        Pageable pageable = PageRequest.of(1, 2);

        List<TicketDetails> expectedList = List.of(
                new TicketDetails(33L, null, 3, 1D, null),
                new TicketDetails(34L, null, 4, 1D, null)
        );
        Page<TicketDetails> expectedPage = new PageImpl<>(expectedList, pageable, 6);

        doReturn(expectedPage)
                .when(ticketService)
                .getAvailableTickets(
                        pageable,
                        new TicketFilterDto(LocalDateTime.parse("2007-12-03T10:15:30"),null,null,null)
                );

        mockMvc.perform(get("/tickets")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .param("date", LocalDateTime.parse("2007-12-03T10:15:30").toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                // TODO: verify links
                //.andExpect(jsonPath("$._links").value())
                .andExpect(jsonPath("$._embedded.ticketDtoList").isArray())
                .andExpect(jsonPath("$._embedded.ticketDtoList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.ticketDtoList[0].id").value(33))
                .andExpect(jsonPath("$._embedded.ticketDtoList[0].place").value(3))
                .andExpect(jsonPath("$._embedded.ticketDtoList[0].price").value(1D))
                .andExpect(jsonPath("$._embedded.ticketDtoList[1].id").value(34))
                .andExpect(jsonPath("$._embedded.ticketDtoList[1].place").value(4))
                .andExpect(jsonPath("$._embedded.ticketDtoList[1].price").value(1D))
                .andExpect(jsonPath("$.page.size").value(pageable.getPageSize()))
                .andExpect(jsonPath("$.page.totalElements").value(6))
                .andExpect(jsonPath("$.page.totalPages").value(3))
                .andExpect(jsonPath("$.page.number").value(pageable.getPageNumber()));
    }

    @Test
    public void purchaseTicked() throws Exception {
        doNothing().when(ticketService).purchaseTicket("user", 1);

        mockMvc.perform(patch("/tickets/%d".formatted(1))
                        .param("login", "user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());
    }

}
