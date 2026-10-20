package model;

import org.junit.jupiter.api.Test;
import reserva.exception.ExcetionFechaInvalida;
import reserva.exception.ExcetionIDInvalida;
import reserva.modelo.Reserva;
import reserva.modelo.ReservaEstado;
import reserva.exception.ExcetionEmailContactoInvalido;
import reserva.exception.ExcetionCantidadPersonasInvalida;
import reserva.exception.ExcetionEstadoInvalido;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
Entidad Reserva
La entidad contiene:

id: identificador de tipo UUID, recibido como dato de entrada.
emailContacto: correo de contacto de quien realiza la reserva.
fecha: fecha de la reserva (LocalDate).
cantidadPersonas: cantidad de personas incluidas.
estado: estado de la reserva (ACTIVA o CANCELADA).

El identificador y la fecha son obligatorios.
El correo de contacto es obligatorio y no puede estar vacío.
La cantidad de personas debe ser mayor que cero.
Una reserva nueva comienza en estado ACTIVA.
Una reserva cancelada no puede cancelarse nuevamente.
 */
public class ReservaUnitTest {
    @Test
    public void instanceReserva_allFieldsOK_succes() {
        // Arrange
        UUID id = UUID.randomUUID();
        String emailContacto = "user@example.com";
        LocalDate fecha = LocalDate.now();
        int cantidadPersonas = 2;

        // Act
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, cantidadPersonas);

        // Assert
        assertEquals(id, reserva.getId());
        assertEquals(emailContacto, reserva.getEmailContacto());
        assertEquals(fecha, reserva.getFecha());
        assertEquals(cantidadPersonas, reserva.getCantidadPersonas());
        assertEquals(ReservaEstado.ACTIVA, reserva.getEstado());
    }

    @Test
    public void instanceReserva_IdNULL_throwsException() {
        // Arrange
        UUID id = null;
        String emailContacto = "user@example.com";
        LocalDate fecha = LocalDate.now();
        int cantidadPersonas = 2;

        // Act & Assert
        Exception exception = assertThrows(ExcetionIDInvalida.class, () -> {
            Reserva.instance(id, emailContacto, fecha, cantidadPersonas);
        });
        assertEquals("Identificacion no valida (nul)", exception.getMessage());
    }

    @Test
    public void instanceReserva_FechaNULL_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String emailContacto = "user@example.com";
        LocalDate fecha = null;
        int cantidadPersonas = 2;

        // Act & Assert
        Exception exception = assertThrows(ExcetionFechaInvalida.class, () -> {
            Reserva.instance(id, emailContacto, fecha, cantidadPersonas);
        });
        assertEquals("Fecha no valida (nul)", exception.getMessage());
    }

    @Test
    public void instanceReserva_EmailContactoNULL_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String emailContacto = null;
        LocalDate fecha = LocalDate.now();
        int cantidadPersonas = 2;

        // Act & Assert
        Exception exception = assertThrows(ExcetionEmailContactoInvalido.class, () -> {
            Reserva.instance(id, emailContacto, fecha, cantidadPersonas);
        });
        assertEquals("Email de contacto no valido (nul)", exception.getMessage());
    }

    @Test
    public void instanceReserva_EmailContactoVacio_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String emailContacto = "";
        LocalDate fecha = LocalDate.now();
        int cantidadPersonas = 2;

        // Act & Assert
        Exception exception = assertThrows(ExcetionEmailContactoInvalido.class, () -> {
            Reserva.instance(id, emailContacto, fecha, cantidadPersonas);
        });
        assertEquals("Email de contacto no valido (vacio)", exception.getMessage());
    }

    @Test
    public void instanceReserva_CantidadPersonasCero_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String emailContacto = "user@example.com";
        LocalDate fecha = LocalDate.now();
        int cantidadPersonas = 0;

        // Act & Assert
        Exception exception = assertThrows(ExcetionCantidadPersonasInvalida.class, () -> {
            Reserva.instance(id, emailContacto, fecha, cantidadPersonas);
        });
        assertEquals("Cantidad de personas no valida (cero)", exception.getMessage());
    }

    @Test
    public void instanceReserva_CantidadPersonasNegativa_throwsException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String emailContacto = "user@example.com";
        LocalDate fecha = LocalDate.now();
        int cantidadPersonas = -1;

        // Act & Assert
        Exception exception = assertThrows(ExcetionCantidadPersonasInvalida.class, () -> {
            Reserva.instance(id, emailContacto, fecha, cantidadPersonas);
        });
        assertEquals("Cantidad de personas no valida (negativa)", exception.getMessage());
    }

    @Test
    public void cancelarReserva_dosVeces_throwsPersonalizedException() {
        // Arrange
        UUID id = UUID.randomUUID();
        String emailContacto = "user@example.com";
        LocalDate fecha = LocalDate.now();
        int cantidadPersonas = 2;
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, cantidadPersonas);

        // Act
        reserva.cancelar();

        // Assert
        ExcetionEstadoInvalido exception = assertThrows(ExcetionEstadoInvalido.class, () -> {
            reserva.cancelar();
        });
        assertEquals("Estado no valido (cancelada)", exception.getMessage());
    }
}