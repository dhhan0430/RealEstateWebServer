package adapter;

public class AdapterTest {

    public static void main(String[] args) {

        HairDryer hairDryer = new HairDryer();
        connect(hairDryer);

        Cleaner cleaner = new Cleaner();
        Electronic110V adapter = new SocketAdapter(cleaner);
        connect(adapter);

        AirConditioner airConditioner = new AirConditioner();
        Electronic110V airAdapter = new SocketAdapter(airConditioner);
        connect(airAdapter);

        // connect(cleaner);
    }

    // 110V 콘센트 존재
    public static void connect(Electronic110V electronic110V) {
        electronic110V.powerOn();
    }
}
