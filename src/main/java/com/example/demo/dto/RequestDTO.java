package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDTO {

    private Long id;
    private Long companyUserId;
    private String expediente;
    private String paso;
    private String linea;
    private Boolean estado;
    private String descripcion;
}
