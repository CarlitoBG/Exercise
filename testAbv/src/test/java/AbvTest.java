import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;



/**
 * Created by Dian on 10.01.2017 г..
 */
public class AbvTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        this.driver = new FirefoxDriver();
        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void login() {
        this.driver.get("https://www.abv.bg/");

        String validUsername = "lefty_";
        String validPassword = "xxxxx";

        WebElement usernameField = this.driver.findElement(By.id("username"));
        WebElement passwordField = this.driver.findElement(By.id("password"));

        usernameField.clear();
        usernameField.sendKeys(validUsername);

        passwordField.clear();
        passwordField.sendKeys(validPassword);

        WebElement loginButton = this.driver.findElement(By.id("loginBut"));
        loginButton.click();
    }

    public void sendAndReceiveMail() {
        WebElement inboxButton = this.driver.findElement(By.xpath("//td[@class='foldersCell']/div[contains( . , 'Кутия')]"));

        Actions action = new Actions(driver);
        action.moveToElement(inboxButton);
        action.contextClick(inboxButton).build().perform();

        WebElement emptyInboxButton = this.driver.findElement(By.xpath("//td[@class='gwt-MenuItem' and contains( . , 'Изпразни')]"));
        emptyInboxButton.click();

        WebElement confirmButton = this.driver.findElement(By.xpath("//div[@class='abv-button' and contains( . , 'Потвърди')]"));
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].click();", confirmButton);

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement composeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='abv-button' and contains( . , 'Напиши')]")));
        composeButton.click();

        WebElement sendToField = this.driver.findElement(By.cssSelector("input.fl"));
        sendToField.clear();
        sendToField.sendKeys("lefty_@abv.bg;");

        WebElement subjectField = this.driver.findElement(By.cssSelector("input.gwt-TextBox"));
        subjectField.clear();
        subjectField.sendKeys("Test mail sending Selenium");

        WebElement textArea = this.driver.findElement(By.xpath("//iframe[@class='gwt-RichTextArea']"));
        textArea.sendKeys("Test body of the test mail");

        try {
            WebElement sendButton = this.driver.findElement(By.xpath("//div[@class='abv-button' and contains(. , 'Изпрати')]"));
            sendButton.click();
        }catch (UnhandledAlertException e){
            driver.switchTo().alert().dismiss();
        }
    }

    public void bugReporting(){
        this.driver.get("https://github.com/login");

        String username = "CarlitoBG";
        String password = "xxxxxx";

        WebElement loginField = this.driver.findElement(By.id("login_field"));
        loginField.clear();
        loginField.sendKeys(username);

        WebElement passwordField = this.driver.findElement(By.id("password"));
        passwordField.clear();
        passwordField.sendKeys(password);

        WebElement signInButton = this.driver.findElement(By.xpath("//input[@class='btn btn-primary btn-block']"));
        signInButton.click();

        this.driver.navigate().to("https://github.com/CarlitoBG/Exercise/issues/new");

        WebElement labelsButton = this.driver.findElement(By.xpath("//button[@class='discussion-sidebar-heading discussion-sidebar-toggle js-menu-target' and contains( . , 'Labels')]"));
        labelsButton.click();

        WebElement bugLabelButton = this.driver.findElement(By.cssSelector("div.color-label.labelstyle-ee0701"));
        bugLabelButton.click();

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("arguments[0].click();", labelsButton);

        WebElement issueTitleField = this.driver.findElement(By.id("issue_title"));
        issueTitleField.clear();
        issueTitleField.sendKeys("wrong fullname shown");

        WebElement issueBodyField = this.driver.findElement(By.id("issue_body"));
        issueBodyField.clear();
        issueBodyField.sendKeys(String.format("Expected result: %s;%nActual result: %s", "Диан Димитров", "Иван Иванов"));

        WebElement submitNewIssueButton = this.driver.findElement(By.xpath("//button[@class='btn btn-primary' and contains( . , 'Submit new issue')]"));
        submitNewIssueButton.click();
    }

    @Test
    public void testLogin_validCredentials_shouldLoginCorrectly() {
        this.login();

        Assert.assertEquals(
                "https://nm40.abv.bg/Mail.html",
                this.driver.getCurrentUrl());
    }

    @Test
    public void testLoggedUsername_successfulLogin_correctUsername(){
        this.login();

        WebElement fullname = this.driver.findElement(By.cssSelector("div.userName"));

        try {
            Assert.assertEquals(
                    "Иван Иванов",
                    fullname.getText());
        }catch(ComparisonFailure comparisonFailure) {
          this.bugReporting();
        }
    }

    @Test
    public void testInboxCount_newMailSent_mailCountIncrementedByOne() {
        this.login();
        this.sendAndReceiveMail();

        WebElement inboxCount = this.driver.findElement(By.xpath("//em[@class='fl' and contains( . , '1')]"));

        String inboxCountText = inboxCount.getText();
        boolean expectedInboxCount = inboxCountText.equals("1");

        Assert.assertTrue(expectedInboxCount);
    }

    @Test
    public void testMailSender_senderFieldPopulated_expectedSenderShown() {
        this.login();
        this.sendAndReceiveMail();

        WebElement inboxNewMailButton = this.driver.findElement(By.xpath("//div[@class='fl' and contains( . , 'Кутия')]"));
        inboxNewMailButton.click();

        WebElement receivedMailSender = this.driver.findElement(By.xpath("//div[@__gwt_cell and contains( . , 'Диан Димитров')]"));

        String receivedMailSenderText = receivedMailSender.getText();
        boolean expectedSender = receivedMailSenderText.equals("Диан Димитров");

        Assert.assertTrue(expectedSender);
    }

    @Test
    public void testMailSubject_subjectFieldPopulated_expectedSubjectShown() {
        this.login();
        this.sendAndReceiveMail();

        WebElement inboxNewMailButton = this.driver.findElement(By.xpath("//div[@class='fl' and contains( . , 'Кутия')]"));
        inboxNewMailButton.click();

        WebElement receivedMailSubject = this.driver.findElement(By.xpath("//div[@__gwt_cell and contains( . , 'Test mail sending Selenium')]"));

        String receivedMailSubjectText = receivedMailSubject.getText();
        boolean expectedSubject = receivedMailSubjectText.equals("Test mail sending Selenium");

        Assert.assertTrue(expectedSubject);
    }

    @After
    public void tearDown(){
        this.driver.quit();
    }
}
