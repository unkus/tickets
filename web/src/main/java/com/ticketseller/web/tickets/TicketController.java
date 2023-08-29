package com.ticketseller.web.tickets;

import com.ticketseller.service.tickets.TicketDetails;
import com.ticketseller.service.tickets.TicketService;
import com.ticketseller.service.tickets.exceptions.TicketAlreadyPurchasedException;
import com.ticketseller.service.tickets.exceptions.TicketNotFoundException;
import com.ticketseller.service.users.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
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

@Tag(name = "Ticket", description = "The Ticket API")
@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    private final RepresentationModelAssembler<TicketDetails, EntityModel<TicketDto>> ticketModelAssembler;

    private final PagedResourcesAssembler<TicketDetails> pagedResourcesAssembler;

    @Autowired
    public TicketController(TicketService ticketService,
                            RepresentationModelAssembler<TicketDetails, EntityModel<TicketDto>> ticketModelAssembler,
                            PagedResourcesAssembler<TicketDetails> pagedResourcesAssembler) {
        this.ticketService = ticketService;
        this.ticketModelAssembler = ticketModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;

    }

    @Operation(summary = "Get ticket detail", tags = "Ticket")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket details provided"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. The ticket id must be greater than 0.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "A ticket with the specified id was not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EntityModel<TicketDto>> one(
            @Parameter(description = "Ticket identifier")
            @PathVariable @Min(1) long id) {
        try {
            return ResponseEntity.ok(ticketModelAssembler.toModel(ticketService.getTicket(id)));
        } catch (TicketNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get tickets that are available for purchase.", tags = "Ticket")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tickets provided"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Variables page and size must be specified.",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "A page with the specified number was not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PagedModel<EntityModel<TicketDto>>> availableTickets(
            @Schema(description = "Page information", implementation = Pageable.class)
            @Valid Pageable pageable,
            @Valid TicketFilterDto ticketFilter
    ) {
        Page<TicketDetails> page = ticketService.getAvailableTickets(pageable, ticketFilter);

        PagedModel<EntityModel<TicketDto>> pagedModel = pagedResourcesAssembler.toModel(page, ticketModelAssembler);
        pagedModel.add(linkTo(methodOn(TicketController.class).availableTickets(pageable, ticketFilter)).withSelfRel());

        return ResponseEntity.ok(pagedModel);
    }

    @Operation(summary = "Purchase ticket", tags = "Ticket")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Ticket purchased"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Ticket ID must be an integer and larger than 0.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. The user login must be string and has length larger than 4 and lower than 200.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "A user with the specified login was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "A ticket with the specified id was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "The ticket with the specified ID has already been purchased.",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    @PatchMapping(
            path = "/{id}"
    )
    public ResponseEntity<?> purchaseTicket(
            @Parameter(description = "Ticket identifier")
            @PathVariable("id") @Min(0) int ticketId,
            @Parameter(description = "User login")
            @RequestParam(value = "login") @Size(min = 4, max = 200) String login
    ) {
        try {
            ticketService.purchaseTicket(login, ticketId);
        } catch (UserNotFoundException | TicketNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (TicketAlreadyPurchasedException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
