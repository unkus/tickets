package com.ticketseller.service.users;

import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.web.MainConfig;
import com.ticketseller.web.exceptions.ApplicationExceptionHandler;
import com.ticketseller.web.users.UserController;
import com.ticketseller.web.users.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {
        MainConfig.class,
        UserController.class,
        ApplicationExceptionHandler.class
})
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void userCreated() throws Exception {
        UserDto newUser = new UserDto(
                null,
                "user",
                "123456",
                "firstName",
                "lastName",
                "patronymic"
        );

        doNothing().when(userService).registerUser(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("**/users/user"));
    }

    @Test
    public void invalidLoginLength() throws Exception {
        UserDto user = new UserDto(
                null,
                "usr",
                "123456",
                "firstName",
                "lastName",
                "patronymic"
        );


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail")
                        .value("login: size must be between 4 and 200"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void invalidPasswordLength() throws Exception {
        UserDto user = new UserDto(
                null,
                "user",
                "123",
                "firstName",
                "lastName",
                "patronymic"
        );

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail")
                        .value("password: size must be between 6 and 200"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void unhandledException() throws Exception {
        UserDto user = new UserDto(
                null,
                "user",
                "123456",
                "firstName",
                "lastName",
                "patronymic"
        );

        Exception ex = new RuntimeException("some confidential data"); // error message should not contain confidential data
        doThrow(ex).when(userService).registerUser(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("Unexpected server error."))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void getPurchasedTickets() throws Exception {
        String userLogin = "user";

        Pageable pageable = PageRequest.of(2, 2);
        List<TicketDetails> expectedList = List.of(
                new TicketDetails(33L, null, 3, 1D, null),
                new TicketDetails(34L, null, 4, 1D, null)
        );
        Page<TicketDetails> expectedPage = new PageImpl<>(expectedList, pageable, 6);

        doReturn(expectedPage)
                .when(userService)
                .getTickets(userLogin, PageRequest.of(2, 2));

        mockMvc.perform(get("/users/%s/tickets".formatted(userLogin))
                        .param("page", String.valueOf(2))
                        .param("size", String.valueOf(2))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ticketDtoList", hasSize(2)))
                .andExpect(jsonPath("$._embedded.ticketDtoList[0].id").value(33))
                .andExpect(jsonPath("$._embedded.ticketDtoList[0].place").value(3))
                .andExpect(jsonPath("$._embedded.ticketDtoList[0].price").value(1D))
                .andExpect(jsonPath("$._embedded.ticketDtoList[1].id").value(34))
                .andExpect(jsonPath("$._embedded.ticketDtoList[1].place").value(4))
                .andExpect(jsonPath("$._embedded.ticketDtoList[1].price").value(1D))
                .andExpect(jsonPath("$.page.size").value(2))
                .andExpect(jsonPath("$.page.totalElements").value(6))
                .andExpect(jsonPath("$.page.totalPages").value(3))
                .andExpect(jsonPath("$.page.number").value(2));
    }

}
