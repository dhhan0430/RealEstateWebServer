public class Main {
    public static void main(String[] args) {

        String url = "www.naver.com/books/it?page=10&size=20&spring-boot";

        // base 64 encoding
        Base64Encoder base64Encoderencoder = new Base64Encoder();
        String base64Result = base64Encoderencoder.encode(url);
        // System.out.println(base64Result);

        // IoC, DI를 사용하지 않으면 아래와 같이 계속 Class 추가 및 코드 추가해야 한다.
        // url encoding
        UrlEncoder urlEncoder = new UrlEncoder();
        String urlResult = urlEncoder.encode(url);
        // System.out.println(urlResult);

        // Interface를 활용 1.
        IEncoder encoder1 = new Base64Encoder();
        String result1 = encoder1.encode(url);

        IEncoder encoder2 = new UrlEncoder();
        String result2 = encoder2.encode(url);

        // Interface를 활용 2.
        Encoder encoder = new Encoder(new Base64Encoder());
        // encoder 바꾸고 싶을 때, Encoder 클래스는 건들지 않고, 매개변수 클래스만 바꿔줌.
        // DI(Dependency Injection)을 사용한 것이다. 넘겨주는 주입 객체만 바꿔줌.
        // Encoder encoder = new Encoder(new UrlEncoder());
        String result = encoder.encode(url);
        System.out.println(url);

        // 이건 Spring이 아닌 그냥 Java App이기 때문에 주입 객체를 개발자가 관리함.
    }


}