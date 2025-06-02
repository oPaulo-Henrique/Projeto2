import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.github.javafaker.Faker;

import java.time.Duration;

public class ContatoTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private Faker faker;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:/Users/paulo/IntelliJProjects/Projeto2/chromedriver-win64/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        faker = new Faker();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void deveCadastrarContatoComSucesso() {
        driver.get("https://projeto2-seven-sandy.vercel.app/");

        WebElement nome = driver.findElement(By.id("name"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement telefone = driver.findElement(By.id("phone"));
        WebElement botaoSalvar = driver.findElement(By.cssSelector("button[type='submit']"));

        String nomeFalso = faker.name().fullName();
        String emailFalso = faker.internet().emailAddress();
        String telefoneFalso = faker.phoneNumber().cellPhone();

        nome.sendKeys(nomeFalso);
        email.sendKeys(emailFalso);
        telefone.sendKeys(telefoneFalso);
        botaoSalvar.click();

        WebElement mensagem = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-message")));
        Assertions.assertTrue(mensagem.getText().contains("sucesso"));
    }
}
