package vacanza;

public abstract class Room {

    private String roomNumber;
    private int floor;
    private String status;
    private double rate;

    public Room(String roomNumber, int floor, String status, double rate) {
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.status = status;
        this.rate = rate;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public int getFloor() {
        return floor;
    }

    public String getStatus() {
        return status;
    }

    public double getRate() {
        return rate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public abstract String getRoomType();

    public abstract double getDefaultRate();

    public abstract String getBedType();

    public abstract String getAmenities();

    public String getDetails() {
        return "Bed type: " + getBedType() + "\n"
                + "Amenities: " + getAmenities();
    }

    public double computeTotal(int nights) {
        return rate * nights;
    }
}
