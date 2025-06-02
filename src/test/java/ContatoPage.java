import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContatoPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public ContatoPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void abrirFormulario() {
        driver.get("https://projeto2-seven-sandy.vercel.app/");
    }

    public void preencherFormulario(String nome, String email, String telefone) {
        driver.findElement(By.id("name")).sendKeys(nome);
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("phone")).sendKeys(telefone);
    }

    public void salvar() {
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    public boolean mensagemDeSucessoVisivel() {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(
            By.id("success-message"), "sucesso"
        ));
    }

    public void irParaListaDeContatos() {
        driver.findElement(By.linkText("Ver contatos cadastrados")).click();
    }

    public void editarPrimeiroContato(String novoNome) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Editar']"))).click();
        WebElement campoNome = driver.findElement(By.id("name"));
        campoNome.clear();
        campoNome.sendKeys(novoNome);
        salvar();
    }

    public void removerPrimeiroContato() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Remover']"))).click();
    }

    public boolean contatoFoiRemovido(String nome) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//strong[contains(text(), '" + nome + "')]")));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean alertaEstaPresente(String mensagemEsperada) {
        try {
            Alert alerta = wait.until(ExpectedConditions.alertIsPresent());
            boolean contem = alerta.getText().contains(mensagemEsperada);
            alerta.accept();
            return contem;
        } catch (TimeoutException e) {
            return false;
        }
    }
}
