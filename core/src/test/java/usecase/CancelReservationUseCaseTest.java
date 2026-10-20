package usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reserva.exception.ExcetionEstadoInvalido;
import reserva.exception.PersistenceException;
import reserva.exception.ReservationNotFoundException;
import reserva.modelo.Reserva;
import reserva.modelo.ReservaEstado;
import reserva.output.ReservationRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelReservationUseCaseTest {
    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private CancelReservationUseCase useCase;

    private Reserva reservaActiva(UUID id) {
        return Reserva.instance(id, "user@example.com", LocalDate.now(), 2);
    }

    @Test
    void cancelarReserva_existente_success() {
        // Arrange
        UUID id = UUID.randomUUID();
        Reserva reserva = reservaActiva(id);
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reserva));
        when(reservationRepository.save(reserva)).thenReturn(reserva);

        // Act
        Reserva reservaCancelada = useCase.execute(id);

        // Assert
        assertSame(reserva, reservaCancelada);
        assertEquals(ReservaEstado.CANCELADA, reservaCancelada.getEstado());
        verify(reservationRepository).findById(id);
        verify(reservationRepository).save(reserva);
    }

    @Test
    void cancelarReserva_inexistente_throwsReservationNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> useCase.execute(id));
        verify(reservationRepository).findById(id);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void cancelarReserva_yaCancelada_throwsExcetionEstadoInvalido() {
        // Arrange
        UUID id = UUID.randomUUID();
        Reserva reserva = reservaActiva(id);
        reserva.cancelar();
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reserva));

        // Act & Assert
        assertThrows(ExcetionEstadoInvalido.class, () -> useCase.execute(id));
        verify(reservationRepository).findById(id);
        verify(reservationRepository, never()).save(reserva);
    }

    @Test
    void cancelarReserva_errorAlGuardar_throwsPersistenceException() {
        // Arrange
        UUID id = UUID.randomUUID();
        Reserva reserva = reservaActiva(id);
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reserva));
        when(reservationRepository.save(reserva)).thenThrow(PersistenceException.class);

        // Act & Assert
        assertThrows(PersistenceException.class, () -> useCase.execute(id));
        verify(reservationRepository).findById(id);
        verify(reservationRepository).save(reserva);
    }
}