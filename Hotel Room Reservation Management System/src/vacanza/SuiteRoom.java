package vacanza;

public class SuiteRoom extends Room {

    public SuiteRoom(String roomNumber, int floor, String status, double rate) {
        super(roomNumber, floor, status, rate);
    }

    @Override
    public String getRoomType() {
        return "Suite";
    }

    @Override
    public double getDefaultRate() {
        return 8200.00;
    }

    @Override
    public String getBedType() {
        return "King bed";
    }

    @Override
    public String getAmenities() {
        return "Air-conditioning, Smart TV,\nWi-Fi, mini bar, living area,\nbathtub, kitchenette";
    }
}
