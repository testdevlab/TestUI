package testUI.elements;

public interface SlideActions {

    UIElement customSwipeUp(int PixelGap, int numberOfSwipes);

    UIElement customSwipeDown(int PixelGap, int numberOfSwipes);

    UIElement swipeLeft(int PixelGap, int startX, int startY);

    UIElement swipeRight(int PixelGap, int startX, int startY);

    UIElement view(boolean upCenter);

    UIElement view(String options);

    UIElement click();
}
