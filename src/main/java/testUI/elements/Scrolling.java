package testUI.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import testUI.Configuration;
import testUI.Utils.TestUIException;

import static java.lang.Math.abs;
import static java.time.Duration.ofMillis;
import static java.util.Collections.singletonList;
import static testUI.TestUIDriver.getDriver;

public class Scrolling extends TestUI implements SlideActions {
    private By AppiumElement;
    private By iOSElement;
    private By SelenideElement;
    private String accesibilityId;
    private String accesibilityIdiOS;
    private int index;
    private boolean collection;

    protected Scrolling(By AppiumElement,
                        By SelenideElement,
                        By iOSElement,
                        int index,
                        boolean collection,
                        String accesibilityId,
                        String accesibilityIdiOS) {
        this.AppiumElement = AppiumElement;
        this.iOSElement = iOSElement;
        this.SelenideElement = SelenideElement;
        this.accesibilityId = accesibilityId;
        this.accesibilityIdiOS = accesibilityIdiOS;
        this.index = index;
        this.collection = collection;
    }

    private Element getElementObject() {
        return new Element(
                AppiumElement,
                SelenideElement,
                iOSElement,
                index,
                collection,
                accesibilityId,
                accesibilityIdiOS);
    }

    public UIElement customSwipeUp(int PixelGap, int numberOfSwipes) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                for (int i = 0; i < numberOfSwipes; i++) {
                    int startY = 500;
                    PixelGap = abs(PixelGap);
                    int endY = 500 - PixelGap;
                    if (endY < 0) {
                        endY = 100;
                        startY = endY + PixelGap;
                    }

                    Dimension size = getDriver().manage().window().getSize();
                    int endX = (int) (size.width * 0.10);

                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), endX, startY));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), endX, endY));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));

                    getDriver().perform(singletonList(sequence));
                }
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new TestUIException(e.getMessage());
        }
        return getElementObject();
    }

    public UIElement customSwipeDown(int PixelGap, int numberOfSwipes) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                for (int i = 0; i < numberOfSwipes; i++) {
                    int startY = 500;
                    PixelGap = abs(PixelGap);
                    int endY = 500 + PixelGap;
                    Dimension size = getDriver().manage().window().getSize();
                    int endX = (int) (size.width * 0.10);

                    PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    Sequence sequence = new Sequence(finger, 1);
                    sequence.addAction(finger.createPointerMove(ofMillis(0),
                            PointerInput.Origin.viewport(), endX, startY));
                    sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                    sequence.addAction(new Pause(finger, ofMillis(600)));
                    sequence.addAction(finger.createPointerMove(ofMillis(600),
                            PointerInput.Origin.viewport(), endX, endY));
                    sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));

                    getDriver().perform(singletonList(sequence));
                }
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new TestUIException(e.getMessage());
        }
        return getElementObject();
    }

    public UIElement swipeLeft(int PixelGap, int startX, int startY) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                PixelGap = abs(PixelGap);
                int endX = startX - PixelGap;

                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence sequence = new Sequence(finger, 1);
                sequence.addAction(finger.createPointerMove(ofMillis(0),
                        PointerInput.Origin.viewport(), startX, startY));
                sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                sequence.addAction(new Pause(finger, ofMillis(600)));
                sequence.addAction(finger.createPointerMove(ofMillis(600),
                        PointerInput.Origin.viewport(), endX, startY));
                sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));

                getDriver().perform(singletonList(sequence));
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new TestUIException(e.getMessage());
        }
        return getElementObject();
    }

    public UIElement swipeRight(int PixelGap, int startX, int startY) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                PixelGap = abs(PixelGap);
                int endX = startX + PixelGap;

                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence sequence = new Sequence(finger, 1);
                sequence.addAction(finger.createPointerMove(ofMillis(0),
                        PointerInput.Origin.viewport(), startX, startY));
                sequence.addAction(finger.createPointerDown(PointerInput.MouseButton.MIDDLE.asArg()));
                sequence.addAction(new Pause(finger, ofMillis(600)));
                sequence.addAction(finger.createPointerMove(ofMillis(600),
                        PointerInput.Origin.viewport(), endX, startY));
                sequence.addAction(finger.createPointerUp(PointerInput.MouseButton.MIDDLE.asArg()));

                getDriver().perform(singletonList(sequence));
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(true);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new TestUIException(e.getMessage());
        }
        return getElementObject();
    }

    public UIElement view(boolean upCenter) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                ((JavascriptExecutor) getDriver()).executeScript(
                        "arguments[0].scrollIntoView(" + upCenter + ");",
                        getElementWithoutException(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                AppiumElement,
                                index,
                                collection));
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(upCenter);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new TestUIException(e.getMessage());
        }
        return getElementObject();
    }

    public UIElement view(String options) {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                ((JavascriptExecutor) getDriver()).executeScript(
                        "arguments[0].scrollIntoView(" + options + ");",
                        getElementWithoutException(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                AppiumElement,
                                index,
                                collection));
            } else {
                getSelenide(SelenideElement, index, collection).scrollIntoView(options);
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new TestUIException(e.getMessage());
        }
        return getElementObject();
    }

    public UIElement click() {
        try {
            if (!Configuration.automationType.equals(Configuration.DESKTOP_PLATFORM)) {
                ((JavascriptExecutor) getDriver()).executeScript(
                        "arguments[0].scrollIntoView(" +
                                "{behavior: \"smooth\", block: \"center\", inline: \"nearest\"});",
                        getElementWithoutException(
                                accesibilityIdiOS,
                                accesibilityId,
                                iOSElement,
                                AppiumElement,
                                index,
                                collection
                        )
                );
                getElement(
                        accesibilityIdiOS,
                        accesibilityId,
                        iOSElement,
                        AppiumElement,
                        index,
                        collection).click();
            } else {
                getSelenide(SelenideElement, index, collection).
                        scrollIntoView(
                                "{behavior: \"smooth\", block: \"center\", inline: \"nearest\"}")
                        .click();
            }
        } catch (Throwable e) {
            takeScreenshotsAllure();
            throw new TestUIException(e.getMessage());
        }
        return getElementObject();
    }
}
