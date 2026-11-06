package vacanza;

public class DeluxeRoom extends Room {

    public DeluxeRoom(String roomNumber, int floor, String status, double rate) {
        super(roomNumber, floor, status, rate);
    }

    @Override
    public String getRoomType() {
        return "Deluxe";
    }

    @Override
    public double getDefaultRate() {
        return 5800.00;
    }

    @Override
    public String getBedType() {
        return "Queen bed";
    }

    @Override
    public String getAmenities() {
        return "Air-conditioning, Smart TV,\nWi-Fi, mini bar, coffee maker, city view";
    }
}
