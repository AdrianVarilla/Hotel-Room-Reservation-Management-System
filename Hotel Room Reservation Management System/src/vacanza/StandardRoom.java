package vacanza;

public class StandardRoom extends Room {

    public StandardRoom(String roomNumber, int floor, String status, double rate) {
        super(roomNumber, floor, status, rate);
    }

    @Override
    public String getRoomType() {
        return "Standard";
    }

    @Override
    public double getDefaultRate() {
        return 2500.00;
    }

    @Override
    public String getBedType() {
        return "Double bed";
    }

    @Override
    public String getAmenities() {
        return "Air-conditioning, TV,\nWi-Fi, private bathroom";
    }
}
