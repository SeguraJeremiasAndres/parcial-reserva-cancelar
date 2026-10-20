package reserva.modelo;

import reserva.exception.*;

import java.util.UUID;

public class Reserva {
    private UUID id;
    private String emailContacto;
    private java.time.LocalDate fecha;
    private int cantidadPersonas;
    private ReservaEstado estado;

    private Reserva(UUID id, String emailContacto, java.time.LocalDate fecha, int cantidadPersonas) {
        this.id = id;
        this.emailContacto = emailContacto;
        this.fecha = fecha;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = ReservaEstado.ACTIVA;
    }
    public static Reserva instance(UUID id, String emailContacto, java.time.LocalDate fecha, int cantidadPersonas) {
        if (id == null) {
            throw new ExcetionIDInvalida("Identificacion no valida (nul)");
        }
        if (fecha == null) {
            throw new ExcetionFechaInvalida("Fecha no valida (nul)");
        }
        if (emailContacto == null) {
            throw new ExcetionEmailContactoInvalido("Email de contacto no valido (nul)");
        }
        if(emailContacto.isEmpty()) {
            throw new ExcetionEmailContactoInvalido("Email de contacto no valido (vacio)");
        }
        if(cantidadPersonas ==0) {
            throw new ExcetionCantidadPersonasInvalida("Cantidad de personas no valida (cero)");
        }
        if(cantidadPersonas < 0) {
            throw new ExcetionCantidadPersonasInvalida("Cantidad de personas no valida (negativa)");
        }
        return new Reserva(id, emailContacto, fecha, cantidadPersonas);
    }

    public UUID getId() {
        return id;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public java.time.LocalDate getFecha() {
        return fecha;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public ReservaEstado getEstado() {
        return estado;
    }

    public void cancelar() {
        if (estado == ReservaEstado.CANCELADA) {
            throw new ExcetionEstadoInvalido("Estado no valido (cancelada)");
        }
        estado = ReservaEstado.CANCELADA;
    }
}
