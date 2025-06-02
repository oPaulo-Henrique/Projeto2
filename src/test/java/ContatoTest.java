import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.github.javafaker.Faker;

import java.time.Duration;
import java.util.List;

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

        boolean ok = wait.until(
            ExpectedConditions.textToBePresentInElementLocated(
                By.id("success-message"), "sucesso"
            )
        );
        Assertions.assertTrue(ok, "Mensagem de sucesso não apareceu");
    }

    @Test
    public void deveEditarContatoExistente() {
        driver.get("https://projeto2-seven-sandy.vercel.app/");

        // Cadastrar contato
        WebElement nome = driver.findElement(By.id("name"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement telefone = driver.findElement(By.id("phone"));
        WebElement botaoSalvar = driver.findElement(By.cssSelector("button[type='submit']"));

        String nomeOriginal = faker.name().firstName();
        String emailOriginal = faker.internet().emailAddress();
        String telefoneOriginal = faker.phoneNumber().cellPhone();

        nome.sendKeys(nomeOriginal);
        email.sendKeys(emailOriginal);
        telefone.sendKeys(telefoneOriginal);
        botaoSalvar.click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("success-message"), "sucesso"));

        // Ir para a tela de contatos
        driver.findElement(By.linkText("Ver contatos cadastrados")).click();

        // Editar o primeiro contato
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Editar']"))).click();

        WebElement campoNome = driver.findElement(By.id("name"));
        campoNome.clear();
        String novoNome = "Editado " + nomeOriginal;
        campoNome.sendKeys(novoNome);

        botaoSalvar = driver.findElement(By.cssSelector("button[type='submit']"));
        botaoSalvar.click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("success-message"), "sucesso"));
    }

    @Test
    public void deveRemoverContato() {
        driver.get("https://projeto2-seven-sandy.vercel.app/");

        // Cadastrar contato
        WebElement nome = driver.findElement(By.id("name"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement telefone = driver.findElement(By.id("phone"));
        WebElement botaoSalvar = driver.findElement(By.cssSelector("button[type='submit']"));

        String nomeFalso = faker.name().firstName();
        String emailFalso = faker.internet().emailAddress();
        String telefoneFalso = faker.phoneNumber().cellPhone();

        nome.sendKeys(nomeFalso);
        email.sendKeys(emailFalso);
        telefone.sendKeys(telefoneFalso);
        botaoSalvar.click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("success-message"), "sucesso"));

        // Ir para a tela de contatos
        driver.findElement(By.linkText("Ver contatos cadastrados")).click();

        // Remover o primeiro contato
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Remover']"))).click();

        // Confirma que foi removido (verifica se há zero contatos ou aguarda mudança na tela)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//strong[contains(text(), '" + nomeFalso + "')]")));
    }
}
