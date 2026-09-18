package br.com.fiap.login;

import java.time.Duration;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CheckPoint 5 - Login")
public class Login {

    private static final String BASE_URL = "https://www.saucedemo.com/";

    private static final String USUARIO_VALIDO = "standard_user";
    private static final String USUARIO_BLOQUEADO = "locked_out_user";
    private static final String SENHA_VALIDA = "secret_sauce";
    private static final String SENHA_INVALIDA = "senha_incorreta";

    private static final By CAMPO_USUARIO = By.id("user-name");
    private static final By CAMPO_SENHA = By.id("password");
    private static final By BOTAO_LOGIN = By.id("login-button");
    private static final By ICONE_CARRINHO = By.id("shopping_cart_container");
    private static final By MENSAGEM_ERRO = By.cssSelector("[data-test='error']");

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void abrirNavegador() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Dado: que esteja na página saucedemo.com (pré-condição comum a todos os cenários)
        driver.get(BASE_URL);
    }

    @AfterEach
    void fecharNavegador() {
        if (driver != null) driver.quit();
    }

    private void realizarLogin(String usuario, String senha) {
        // Quando: inserir usuário e senha
        driver.findElement(CAMPO_USUARIO).sendKeys(usuario);
        driver.findElement(CAMPO_SENHA).sendKeys(senha);
        // E: clicar no botão "Login"
        driver.findElement(BOTAO_LOGIN).click();
    }

    private String obterMensagemErro() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(MENSAGEM_ERRO));
        return driver.findElement(MENSAGEM_ERRO).getText();
    }

    @Test
    @DisplayName("CT1 - Login com sucesso (200)")
    void deveLogarComCredenciaisValidas() {
        assertEquals(BASE_URL, driver.getCurrentUrl());
        assertEquals("Swag Labs", driver.getTitle());

        realizarLogin(USUARIO_VALIDO, SENHA_VALIDA);

        // Então: deverá ser redirecionado para a página inventory.html
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        assertEquals(BASE_URL + "inventory.html", driver.getCurrentUrl());
        assertTrue(driver.findElement(ICONE_CARRINHO).isDisplayed());
    }

    @Test
    @DisplayName("CT2 - Login com senha incorreta (401)")
    void naoDeveLogarComSenhaIncorreta() {
        realizarLogin(USUARIO_VALIDO, SENHA_INVALIDA);

        // Então: credenciais inválidas, permanece na tela de login
        String mensagem = obterMensagemErro();
        assertTrue(mensagem.contains("do not match"),
                "Esperava mensagem de credenciais inválidas, veio: " + mensagem);
        assertEquals(BASE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("CT3 - Login com usuário bloqueado (403)")
    void naoDeveLogarComUsuarioBloqueado() {
        realizarLogin(USUARIO_BLOQUEADO, SENHA_VALIDA);

        // Então: conta bloqueada, acesso negado mesmo com credenciais corretas
        String mensagem = obterMensagemErro();
        assertTrue(mensagem.contains("locked out"),
                "Esperava mensagem de usuário bloqueado, veio: " + mensagem);
        assertEquals(BASE_URL, driver.getCurrentUrl());
    }

    @Test
    @DisplayName("CT4 - Login sem preencher a senha (400)")
    void naoDeveLogarSemSenha() {
        // Quando: envia a requisição faltando "Senha"
        driver.findElement(CAMPO_USUARIO).sendKeys(USUARIO_VALIDO);
        driver.findElement(BOTAO_LOGIN).click();

        // Então: erro de campo obrigatório
        String mensagem = obterMensagemErro();
        assertTrue(mensagem.contains("Password is required"),
                "Esperava mensagem de senha obrigatória, veio: " + mensagem);
    }

    @Test
    @DisplayName("CT5 - Login sem preencher o usuário (400)")
    void naoDeveLogarSemUsuario() {
        // Quando: envia a requisição faltando "Usuário"
        driver.findElement(CAMPO_SENHA).sendKeys(SENHA_VALIDA);
        driver.findElement(BOTAO_LOGIN).click();

        // Então: erro de campo obrigatório
        String mensagem = obterMensagemErro();
        assertTrue(mensagem.contains("Username is required"),
                "Esperava mensagem de usuário obrigatório, veio: " + mensagem);
    }
}
