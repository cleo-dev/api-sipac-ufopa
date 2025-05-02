# RU‑SIPAC API — Consulta de Carteira e Histórico do RU/UFOPA

Projeto Java 17 que faz **login automatizado** no portal SIPAC da UFOPA,
raspando as páginas do Restaurante Universitário (RU) para devolver:

* Dados cadastrais do estudante / servidor  
* Saldo da carteira do RU  
* QR Code da carteira (hash)  
* **Histórico completo de recargas e utilizações** (Almoço / Jantar)

> O programa é distribuído como _CLI_ simples, mas o core foi escrito para
> ser usado como **biblioteca** em aplicações maiores (bots, web services,
> mobile apps, etc.).

---

## Sumário

1. [Funcionalidades](#funcionalidades)  
2. [Requisitos](#requisitos)  
3. [Instalação](#instalação)  
4. [Uso rápido](#uso-rápido)  
5. [Arquitetura do código](#arquitetura-do-código)  
6. [Tratamento de erros](#tratamento-de-erros)  
7. [Segurança](#segurança)  
8. [Licença](#licença)

---

## Funcionalidades

| Recurso | Descrição |
|---------|-----------|
| **Login SIPAC** | Autenticação via POST; captura do cookie de sessão. |
| **Scraping** | OkHttp + regex / Apache Commons Text para extrair campos do HTML. |
| **Histórico 100 % fiel** | Captura data, hora, operação (com _Almoço_/_Jantar_), gerados, receber, adiantados, compensados, saldo anterior, saldo atual. |
| **CLI** | Executável via `mvn exec:java -Dexec.args='usuario senha'`. |
| **Singleton thread‑safe** | Objeto `Usuario` com _double‑checked locking_ (`volatile`). |
| **Encerramento limpo** | `ServicoHttp.shutdown()` fecha `TaskRunner`, `Watchdog` e pool do OkHttp → sem _warnings_ de threads pendentes. |
| **Logging auxiliar** | Salva `page_saldo.html` e `page_perfil.html` no diretório raiz para depuração rápida. |

---

## Requisitos

* **JDK 17** ou superior  
* **Maven 3.8+**  
* Acesso à internet (porta 443) para o domínio `sipac.ufopa.edu.br`

---

## Instalação

```bash
git clone https://github.com/cleo-dev/api-sipac-ufopa
cd ru-sipac-api
mvn clean package        # compila o projeto
```

O jar gerado fica em `target/ru-sipac-api-1.0-SNAPSHOT.jar`.

---

## Uso rápido

### CLI

```bash
# dentro da pasta do projeto
mvn exec:java -Dexec.args='meu_login minha_senha'
```

### Como biblioteca

```java
Credenciais c = new Credenciais("login", "senha");
new Login().fazerLogin(c);                 // popula o singleton

Usuario u = new ControladorUsuario().obterUsuario();
System.out.println(u.getCarteira().getSaldo());
```

---

## Arquitetura do código

```
src/main/java
├── controller         # fachada de alto nível (ControladorUsuario)
├── util               # infraestrutura: HTTP, login, regexs
├── helper             # parsers / analisadores
├── model              # POJOs (Perfil, Carteira, Transacao, etc.)
└── exception          # checked exceptions específicas do domínio
```

* **OkHttp** (`util/ServicoHttp`) – HTTP client singleton reutilizável  
* **Regex centralizadas** (`util/ColecaoRegex`) – fácil manutenção  
* **Parser resiliente** (`helper/AnalisadorRegex`) – ignora comentários,
  NBSPs e pequenas alterações de layout  
* **Main** – exemplo de uso e formatação de saída em tabela ASCII

---

## Tratamento de erros

| Exceção | Cenário |
|---------|---------|
| `ExcecaoUsuarioSenhaInvalido` | Usuário/Senha incorretos |
| `ExcecaoUsuarioSemCadastroRU` | Login ok, mas sem carteira RU |
| `ExcecaoErroDeConectividade`  | Falha de rede, time‑out, 5xx, etc. |
| `IllegalStateException` | Mudança inesperada no HTML (campo não encontrado) |

---

## Segurança

* **HTTPS** – todo tráfego via `https://sipac.ufopa.edu.br`  
* **Sem persistência** – credenciais não são salvas em disco  
* **Dependências atualizadas** – OkHttp 4.12, Commons‑Text 1.10  
* **Thread‑safe** – `Usuario` seguro em ambientes multithread

---



## Licença

Distribuído sob a licença **MIT** – veja `LICENSE.md` para detalhes.

---
