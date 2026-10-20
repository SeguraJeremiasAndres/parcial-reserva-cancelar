package reserva.usecase;

import reserva.exception.ReservationNotFoundException;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

import java.util.UUID;

public class CancelReservationUseCase {
    private final ReservationRepository reservationRepository;

    public CancelReservationUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reserva execute(UUID id) {
        Reserva reserva = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(
                        "Reserva no encontrada: " + id
                ));

        reserva.cancelar();
        return reservationRepository.save(reserva);
    }
}
