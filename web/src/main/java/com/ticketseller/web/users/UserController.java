package com.ticketseller.web.users;

import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.service.users.User;
import com.ticketseller.service.users.UserService;
import com.ticketseller.service.users.exceptions.UserAlreadyRegisteredException;
import com.ticketseller.service.users.exceptions.UserNotFoundException;
import com.ticketseller.web.tickets.TicketController;
import com.ticketseller.web.tickets.TicketDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "User", description = "The User API")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RepresentationModelAssembler<User, EntityModel<UserDto>> userModelAssembler =
            entity -> EntityModel.of(
                    UserDto.of(entity),
                    linkTo(methodOn(UserController.class).one(entity.login())).withSelfRel()
            );

    private final RepresentationModelAssembler<TicketDetails, EntityModel<TicketDto>> ticketModelAssembler;
    private final PagedResourcesAssembler<TicketDetails> ticketPagedResourcesAssembler;

    @Autowired
    public UserController(UserService userService,
                          RepresentationModelAssembler<TicketDetails, EntityModel<TicketDto>> ticketModelAssembler,
                          PagedResourcesAssembler<TicketDetails> ticketPagedResourcesAssembler) {
        this.userService = userService;
        this.ticketModelAssembler = ticketModelAssembler;
        this.ticketPagedResourcesAssembler = ticketPagedResourcesAssembler;
    }

    @Operation(summary = "Get user detail", tags = "User")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User provided"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. The user login must be string and has length larger than 4 and lower than 200.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "A user with the specified login was not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping(
            path = "/{login}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EntityModel<UserDto>> one(@PathVariable @Size(min = 4, max = 2000) String login) {
        try {
            return ResponseEntity.ok(userModelAssembler.toModel(userService.getUser(login)));
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Register new user", tags = "User")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User registered"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Invalid user data.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "A user with the specified login was not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "The specified login is already occupied by another user",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))

            )
    })
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> registerUser(
            @Schema(description = "User summary", implementation = UserDto.class)
            @RequestBody @Valid @NotNull UserDto user
    ) {
        try {
            userService.registerUser(user);
        } catch (UserAlreadyRegisteredException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }

        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).one(user.login())).toUri())
                .build();
    }

    @Operation(summary = "Get tickets that are purchased by user.", tags = "User")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tickets provided"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. The user login must be string and has length larger than 4 and lower than 200.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Variables page and size must be specified.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "A user with the specified login was not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping(
            path = "/{login}/tickets",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PagedModel<EntityModel<TicketDto>>> purchasedTickets(
            @Schema(description = "User login", implementation = String.class)
            @PathVariable @Size(min = 4, max = 200) String login,
            @Schema(description = "Page information", implementation = Pageable.class)
            @Valid @NotNull Pageable pageable
    ) {
        Page<TicketDetails> page;
        try {
            page = userService.getTickets(login, pageable);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        PagedModel<EntityModel<TicketDto>> pagedModel = ticketPagedResourcesAssembler.toModel(page, ticketModelAssembler);
        pagedModel.add(linkTo(methodOn(UserController.class).purchasedTickets(login, pageable)).withSelfRel());

        return ResponseEntity.ok(pagedModel);
    }
}
