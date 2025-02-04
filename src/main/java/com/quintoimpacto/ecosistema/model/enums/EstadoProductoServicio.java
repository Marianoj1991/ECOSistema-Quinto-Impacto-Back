package com.quintoimpacto.ecosistema.model.enums;

public enum EstadoProductoServicio {

    REVISION_INICIAL, // para el estado inicial cuando se crea un proveedor.

    ACEPTADO, // cuando el administrador ha aprobado el proveedor.

    DENEGADO, // cuando el administrador ha rechazado el proveedor.

    REQUIERE_CAMBIOS, // cuando el administrador solicita modificaciones.

    CAMBIOS_REALIZADOS // cuando el usuario ha editado el proveedor después de recibir feedback.
}
