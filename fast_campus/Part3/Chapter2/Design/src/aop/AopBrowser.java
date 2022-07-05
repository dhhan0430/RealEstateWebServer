package aop;

import proxy.Html;
import proxy.IBrowser;

public class AopBrowser implements IBrowser {

    private String url;
    private Html html;
    private Runnable before;
    private Runnable after;

    public AopBrowser(String url, Runnable before, Runnable after) {
        this.url = url;
        this.before = before;
        this.after = after;
    }

    @Override
    public Html show() {

        before.run();
        // caching 구현
        if (html == null) {
            System.out.println("AopBrowser html loading from:" + url);
            this.html = new Html(url);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        after.run();

        System.out.println("AopBrowser html cache:" + url);
        return html;
    }
}
