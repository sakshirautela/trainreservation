
// ReservationController.java
@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestParam Long trainId,
            @RequestParam Long passengerId,
            @RequestParam int seats) {
        return ResponseEntity.ok(reservationService.createReservation(trainId, passengerId, seats));
    }

    @GetMapping("/{pnr}")
    public ResponseEntity<Reservation> getReservation(@PathVariable String pnr) {
        return reservationService.getReservationByPnr(pnr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }
}