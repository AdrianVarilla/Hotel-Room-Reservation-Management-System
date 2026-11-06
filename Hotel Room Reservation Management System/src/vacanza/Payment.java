package vacanza;

import java.time.LocalDate;

public class Payment {

    private String paymentId;
    private String reservationId;
    private String method;
    private double amount;
    private LocalDate paymentDate;
    private String receivedBy;

    public Payment(String paymentId, String reservationId, String method, double amount,
            LocalDate paymentDate, String receivedBy) {
        this.paymentId = paymentId;
        this.reservationId = reservationId;
        this.method = method;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.receivedBy = receivedBy;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getMethod() {
        return method;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public String getReceivedBy() {
        return receivedBy;
    }
}
