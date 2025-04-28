package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
@Execution(ExecutionMode.SAME_THREAD)
@UsePlaywright(HeadlessChromeOptions.class)
public class PlaywrightFormsTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;




    @DisplayName("Interacting with Text Fields")
    @Nested
    class WhenInteractingWithTextFields{
        @BeforeEach
        void openContactPage(Page page){
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Complete the form")
        @Test
        void completeForm(Page page) throws URISyntaxException {
            var firstNameField = page.getByLabel("First Name");
            var lastNameField = page.getByLabel("Last Name");
            var emailNameField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            //drop down
            var subject = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");



            firstNameField.fill("Deepak");
            lastNameField.fill("Das");
            emailNameField.fill("deepak-2090@gmail.com");
            messageField.fill("Hello Deepak Das");
            subject.selectOption(new SelectOption().setIndex(1));
            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample.txt").toURI());
            page.setInputFiles("#attachment", fileToUpload);

            assertThat(firstNameField).hasValue("Deepak");
            assertThat(lastNameField).hasValue("Das");
            assertThat(emailNameField).hasValue("deepak-2090@gmail.com");
            assertThat(messageField).hasValue("Hello Deepak Das");
            assertThat(subject).hasValue("customer-service");

            //check uploaded file
            String uploadedFile = uploadField.inputValue();
            org.assertj.core.api.Assertions.assertThat(uploadedFile).endsWith("sample.txt");

        }

        @DisplayName("Mandatory fields")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
        void mandatoryFields(String fieldName, Page page){
            var firstNameField = page.getByLabel("First Name");
            var lastNameField = page.getByLabel("Last Name");
            var emailNameField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            //drop down
            var subject = page.getByLabel("Subject");
            var sendButton = page.getByText("Send");

            //FILL IN THE FIELD VALUES
            firstNameField.fill("Deepak");
            lastNameField.fill("Das");
            emailNameField.fill("deepak-xxxads2090@gmail.com");
            messageField.fill("Hello Deepak Das this is a min 50             charecters test");
            subject.selectOption(new SelectOption().setIndex(1));

            //clear one of the fields

            page.getByLabel(fieldName).clear();
            //check the error message for that field

            sendButton.click();

            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");
            assertThat(errorMessage).isVisible();


        }

    }
}
