package vacanza;

import java.time.LocalDate;

public class Reservation {

    public static final double RETURNING_DISCOUNT = 0.10;

    private String reservationId;
    private String guestId;
    private String guestName;
    private String roomNumber;
    private String roomType;
    private double rate;                            // Keep the booked rate even if the room price changes later.
    private LocalDate checkIn;
    private int nights;
    private String status;
    private String createdBy;
    private double discount;

    public Reservation(String reservationId, String guestId, String guestName, String roomNumber,
            String roomType, double rate, LocalDate checkIn, int nights, String status, String createdBy)
    {
        this(reservationId, guestId, guestName, roomNumber, roomType, rate, checkIn, nights, status, createdBy, 0);
    }

    public Reservation(String reservationId, String guestId, String guestName, String roomNumber,
            String roomType, double rate, LocalDate checkIn, int nights, String status, String createdBy,
            double discount) {
        this.reservationId = reservationId;
        this.guestId = guestId;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.rate = rate;
        this.checkIn = checkIn;
        this.nights = nights;
        this.status = status;
        this.createdBy = createdBy;
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestId() {
        return guestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getRate() {
        return rate;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public int getNights() {
        return nights;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isOpen() {
        return status.equals("Reserved") || status.equals("Checked In");
    }

    public LocalDate getCheckOut() {
        return checkIn.plusDays(nights);
    }

    public double getSubtotal() {
        return rate * nights;
    }

    // Apply the saved discount to the total room charge.
    public double getTotal() {
        return getSubtotal() * (1 - discount);
    }
}
