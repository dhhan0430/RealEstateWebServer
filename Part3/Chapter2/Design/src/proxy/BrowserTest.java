package proxy;

public class BrowserTest {
    public static void main(String[] args) {
        /*
        Browser browser = new Browser("www.naver.com");
        // 이렇게 하면 매번 naver 로부터 loading이 일어난다. (caching 없이)
        // 이 때 proxy 패턴을 이용한 caching을 적용해 본다.

        browser.show();
        browser.show();
        browser.show();
        browser.show();
        */

        IBrowser browser = new BrowserProxy("www.naver.com");
        browser.show();
        browser.show();
        browser.show();
        browser.show();
        browser.show();


    }
}
