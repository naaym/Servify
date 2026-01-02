# Tests added in latest changes

- `BookingServiceImplTest` (unit):
  - `updateStatusAsProviderAllowsDoneAfterAcceptance` verifies a provider can mark an accepted booking as done when they own it.
  - `submitReviewFailsWhenBookingNotDone` ensures clients cannot submit reviews until booking status is `DONE`.
  - `updateStatusAsProviderThrowsWhenBookingMissing` checks missing bookings trigger `ResourceNotFoundException` for provider updates.
- `BookingRepositoryTest` (integration): confirms repository counts bookings by status for a client using an in-memory database.
- `BookingControllerTest` (web layer): ensures clients are forbidden from changing booking status to non-cancelled values and avoids calling the service layer.
