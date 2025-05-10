package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import org.junit.jupiter.api.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class PlaywrightAssertionsTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    Page page;

    @BeforeAll
    public static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
        );
    }

    @BeforeEach
    public void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Making assertions about the contents of a field")
    @Nested
    class LocatingElementsUsingCSS{
        @BeforeEach
        void openContactPage(){
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Checking the value of a field")
        @Test
        void fieldValues(){
            var firstNameField = page.getByLabel("First name");
            firstNameField.fill("Deepak das");
            assertThat(firstNameField).hasValue("Deepak das");

            assertThat(firstNameField).not().isDisabled();
            assertThat(firstNameField).isVisible();
            assertThat(firstNameField).isEditable();
        }


    }

}
