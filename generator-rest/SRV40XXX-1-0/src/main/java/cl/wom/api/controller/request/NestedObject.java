package cl.wom.api.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NestedObject {

    @NotNull
    @NotBlank
    private String data;

}
