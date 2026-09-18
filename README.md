# CheckPoint 5 - Automação de Testes Funcionais (Login)

Automação do fluxo de login do site de treino [saucedemo.com](https://www.saucedemo.com/) com **Java**, **JUnit 5** e **Selenium WebDriver**, seguindo o cenário em Gherkin (Dado / Quando / E / Então) ensinado no CheckPoint 5.

## Cenários automatizados

O cenário base (**CT1**) foi automatizado em aula. Os demais cenários (**CT2** a **CT5**) automatizam a **Matriz de Status Code** do login definida no CP1 (Compliance QA), cobrindo as linhas marcadas com ✓ que são observáveis na interface do saucedemo.com:

| Cenário | Status HTTP | Descrição |
|---|---|---|
| CT1 | 200 (Sucesso) | Login com credenciais válidas |
| CT2 | 401 (Não autenticado) | Login com senha incorreta |
| CT3 | 403 (Sem permissão) | Login com usuário/conta bloqueada |
| CT4 | 400 (Erro de syntax) | Login sem preencher a senha |
| CT5 | 400 (Erro de syntax) | Login sem preencher o usuário |

Os demais status da matriz (302, 408, 409, 422, 429, 499, 5xx) dependem de infraestrutura de backend/rede que o site de treino saucedemo.com não expõe (não há API real por trás do formulário), por isso não são automatizáveis via UI nesta suíte.

## Pré-requisitos

- JDK 17+
- Maven
- Google Chrome atualizado (o Selenium 4 baixa o driver sozinho via Selenium Manager)

## Autor

Lucas Matsubara, RM 565020 - 2TDSPX
