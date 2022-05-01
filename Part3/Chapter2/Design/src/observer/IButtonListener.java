package observer;

public interface IButtonListener {
    // 버튼에 대한 클릭이 일어나면, event를 받을 수 있음.
    void clickEvent(String event);
}
