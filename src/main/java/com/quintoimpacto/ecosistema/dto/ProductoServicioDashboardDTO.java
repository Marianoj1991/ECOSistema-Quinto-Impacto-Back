package com.quintoimpacto.ecosistema.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductoServicioDashboardDTO {

    private long nuevosPerfilesCreados;

    private long aprobados;

    private long denegados;

    private long enRevision;

    private long categoriaBienestar;

    private long categoriaCapacitaciones;

    private long categoriaConstruccion;

    private long categoriaCultivos;

    private long categoriaGastronomia;

    private long categoriaIndumentaria;

    private long categoriaMerchandising;

    private long categoriaMueblesDeco;

    private long categoriaReciclaje;

    private long categoriaTecnologia;

    private long categoriaTransporte;
}
