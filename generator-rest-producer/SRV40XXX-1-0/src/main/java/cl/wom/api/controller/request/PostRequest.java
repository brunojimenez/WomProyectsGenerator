package cl.wom.api.controller.request;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class PostRequest {

    @NotBlank
    private String id;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^([0-9Kk]){8,9}$")
    private String rut;

    @Pattern(regexp = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$")
    private String ipAddress;

    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @Email
    private String email;

    @NotNull
    @Min(1)
    @Max(120)
    private Integer age;

    // Nested object require @Valid annotation
    @NotNull
    @Valid
    private NestedObject nestedObject;

}
