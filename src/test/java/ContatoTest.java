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

        boolean ok = wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.id("success-message"), "sucesso"
        ));

        Assertions.assertTrue(ok, "Mensagem de sucesso não apareceu");
    }

    @Test
    public void deveEditarContatoExistente() {
        driver.get("https://projeto2-seven-sandy.vercel.app/");

        // === Cadastro de contato ===
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

        // === Navegar para tela de contatos ===
        driver.findElement(By.linkText("Ver contatos cadastrados")).click();

        // === Clicar no botão Editar do primeiro contato ===
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//div[@class='contact-buttons']/button[contains(text(), 'Editar')]")
        )).click();

        // === Editar o nome ===
        WebElement campoNome = driver.findElement(By.id("name"));
        campoNome.clear();
        String novoNome = "Editado " + nomeOriginal;
        campoNome.sendKeys(novoNome);

        // === Salvar as alterações ===
        botaoSalvar = driver.findElement(By.cssSelector("button[type='submit']"));
        botaoSalvar.click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("success-message"), "sucesso"));
    }

    @Test
    public void deveRemoverContato() {
        driver.get("https://projeto2-seven-sandy.vercel.app/");

        // === Cadastro de contato ===
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

        // === Navegar para tela de contatos ===
        driver.findElement(By.linkText("Ver contatos cadastrados")).click();

        // === Remover o contato ===
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[text()='Remover']")
        )).click();

        // === Verifica se sumiu da tela ===
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
            By.xpath("//strong[contains(text(), '" + nomeFalso + "')]")
        ));
    }

    @Test
    public void naoDeveEditarComCamposVazios() {
        driver.get("https://projeto2-seven-sandy.vercel.app/");

        // === Cadastro de contato ===
        WebElement nome = driver.findElement(By.id("name"));
        WebElement email = driver.findElement(By.id("email"));
        WebElement telefone = driver.findElement(By.id("phone"));
        WebElement botaoSalvar = driver.findElement(By.cssSelector("button[type='submit']"));

        nome.sendKeys("Teste Edição");
        email.sendKeys("teste@email.com");
        telefone.sendKeys("11999999999");
        botaoSalvar.click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("success-message"), "sucesso"));

        // === Acessar lista de contatos ===
        driver.findElement(By.linkText("Ver contatos cadastrados")).click();

        try {
            Thread.sleep(1000); // tempo de renderização
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // === Clicar no botão Editar ===
        wait.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("//button[contains(text(), 'Editar')]")
        )).click();

        

        // === Limpar campos ===
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("phone")).clear();

        // === Tentar salvar com campos vazios ===
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // === Verificar alerta ===
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        Assertions.assertTrue(alert.getText().contains("Preencha todos os campos!"));
        alert.accept();
    }
}
