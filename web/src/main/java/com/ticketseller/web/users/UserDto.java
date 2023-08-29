package com.ticketseller.web.users;

import com.ticketseller.service.users.User;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema
@JsonRootName("User")
public record UserDto (
        @Schema(hidden = true)
        @JsonIgnore Long id,
        @JsonProperty @Size(min = 4, max = 200) String login,
        @Schema(accessMode = Schema.AccessMode.WRITE_ONLY)
        @JsonSetter @Size(min = 6, max = 200) String password,
        @JsonProperty @Size(max = 200) String name,
        @JsonProperty @Size(max = 200) String surname,
        @JsonProperty @Size(max = 200) String patronymic
) implements User {

    static UserDto of(User user) {
        return new UserDto(null, user.login(), null, user.name(), user.surname(), user.patronymic());
    }

}
