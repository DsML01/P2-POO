# **Jackut - Sistema de Rede Social**  

## 📌 **Visão Geral**  
O **Jackut** é um sistema de rede social desenvolvido em Java que permite aos usuários:  
✅ Criar perfis e gerenciar informações pessoais  
✅ Adicionar amigos, ídolos, paqueras e até inimigos  
✅ Enviar recados e mensagens em comunidades  
✅ Participar de comunidades e interagir com outros membros  
✅ Persistir dados em arquivos para recuperação futura  

---

## 🛠 **Funcionalidades Principais**  

### **👤 Gestão de Usuários**  
- **Cadastro**: `login`, `senha` e `nome`.  
- **Perfil**: Atributos personalizáveis (ex: idade, cidade, interesses).  
- **Sessões**: Login/logout com controle de acesso via `ID de sessão`.  
- **Exclusão**: Remoção segura de usuários (limpeza de relacionamentos).  

### **🤝 Relacionamentos**  
- **Amizades**: Solicitações, aceitação e lista de amigos.  
- **Ídolos & Fãs**: Relacionamento unilateral de admiração.  
- **Paqueras**: Conexões românticas com notificações recíprocas.  
- **Inimigos**: Bloqueio de interações indesejadas.  

### **💬 Comunicação**  
- **Recados**: Mensagens diretas entre usuários.  
- **Comunidades**:  
  - Criação por usuários (com `dono`).  
  - Envio de mensagens para todos os membros.  

### **💾 Persistência de Dados**  
- **Arquivos**:  
  - `usuarios.txt`: Dados de cadastro e perfis.  
  - `amigos.txt`, `recados.txt`, `comunidades.txt`, etc.  
- **Recuperação**: Reinicialização do sistema sem perda de dados.  

---

## 📂 **Estrutura do Projeto**  
```plaintext
src/
├── br.ufal.ic.p2.jackut/
│   ├── App/                   # Classe Facade (interface principal)
│   ├── Entidades/             # Modelos (User, Comunidade, Recado, etc.)
│   ├── Exceptions/            # Exceções personalizadas
│   ├── Servicos/              # Lógica de negócio (SessionService)
│   ├── Utilidade/             # Utilitários (manipulação de strings, arquivos)
│   └── tipos/                 # Enums (TiposRelacionamento)
BaseDeDados/                   # Arquivos de persistência
```

---

Diagrama de classes

classDiagram
    class Facade {
        -SessionService jackutServicesFacade
        +criarUsuario()
        +abrirSessao()
        +getAtributoUsuario()
        +editarPerfil()
        +adicionarAmigo()
        +enviarRecado()
        +lerRecado()
        +criarComunidade()
        +getDescricaoComunidade()
        +adicionarComunidade()
        +enviarMensagem()
        +lerMensagem()
        +adicionarIdolo()
        +adicionarPaquera()
        +adicionarInimigo()
        +removerUsuario()
        +zerarSistema()
        +encerrarSistema()
    }

    class SessionService {
        -Map~String, User~ usuarios
        -Map~String, User~ sessoes
        -Map~String, Comunidade~ comunidades
        +getUsuario()
        +getSessaoUsuario()
        +setUsuario()
        +abrirSessao()
        +adicionarAmigo()
        +enviarRecado()
        +lerRecado()
        +criarComunidade()
        +adicionarComunidade()
        +enviarMensagem()
        +lerMensagem()
        +adicionarIdolo()
        +adicionarPaquera()
        +adicionarInimigo()
        +removerUsuario()
        +zerarSistema()
        +encerrarSistema()
    }

    class User {
        -String login
        -String senha
        -String nome
        -Perfil perfil
        -ArrayList~User~ amigos
        -ArrayList~User~ solicitacoesEnviadas
        -ArrayList~User~ solicitacoesRecebidas
        -Queue~Recado~ recados
        -ArrayList~Comunidade~ comunidadesProprietarias
        -ArrayList~Comunidade~ comunidadesParticipantes
        -Queue~Mensagem~ mensagens
        -ArrayList~User~ idolos
        -ArrayList~User~ fas
        -ArrayList~User~ paqueras
        -ArrayList~User~ paquerasRecebidas
        -ArrayList~User~ inimigos
        +getLogin()
        +getSenha()
        +getNome()
        +getPerfil()
        +getAtributo()
        +getAmigos()
        +getAmigosString()
        +getSolicitacoesEnviadas()
        +getSolicitacoesRecebidas()
        +getRecados()
        +getComunidadesProprietarias()
        +getComunidadesParticipantes()
        +getMensagens()
        +getIdolos()
        +getFas()
        +getFasString()
        +getPaqueras()
        +getPaquerasString()
        +getInimigos()
        +setAmigo()
        +setDonoComunidade()
        +setParticipanteComunidade()
        +setIdolo()
        +setFa()
        +setPaquera()
        +setPaquerasRecebidas()
        +setInimigo()
        +removerAmigo()
        +removerFa()
        +removerIdolo()
        +removerPaquera()
        +removerPaqueraRecebida()
        +removerInimigo()
        +removerSolicitacaoEnviada()
        +removerSolicitacaoRecebida()
        +verificarSenha()
        +enviarSolicitacao()
        +aceitarSolicitacao()
        +lerRecado()
        +receberRecado()
        +receberMensagem()
        +lerMensagem()
        +sairComunidade()
    }

    class Perfil {
        -Map~String, String~ atributos
        +getAtributo()
        +getAtributos()
        +setAtributo()
    }

    class Comunidade {
        -User dono
        -String nome
        -String descricao
        -ArrayList~User~ membros
        +getNome()
        +getDescricao()
        +getDono()
        +getMembros()
        +getMembrosString()
        +setMembros()
        +adicionarMembro()
        +enviarMensagem()
    }

    class Recado {
        -User remetente
        -User destinatario
        -String recado
        +getRemetente()
        +getDestinatario()
        +getRecado()
    }

    class Mensagem {
        -String mensagem
        +getMensagem()
    }

    class UtilidadeString {
        +formatArrayList()
    }

    class EscritaDeArquivos {
        +criarPasta()
        +escreverArquivo()
        +salvarUsuarios()
        +salvarAmigos()
        +salvarRecados()
        +salvarComunidades()
        +salvarMensagens()
        +salvarRelacoes()
        +persistirDados()
        +limparArquivos()
    }

    class LeituraDeArquivos {
        +lerArquivos()
        +lerArquivo()
        +lerUsuarios()
        +lerAmigos()
        +lerRecados()
        +lerComunidades()
        +lerMensagens()
        +lerRelacoes()
    }

    Facade --> SessionService
    SessionService --> User
    SessionService --> Comunidade
    User --> Perfil
    User --> Recado
    User --> Mensagem
    User --> Comunidade
    Comunidade --> User
    Recado --> User
    EscritaDeArquivos --> User
    EscritaDeArquivos --> Comunidade
    LeituraDeArquivos --> SessionService
    LeituraDeArquivos --> User
    LeituraDeArquivos --> Comunidade

---

## 🔧 **Como Executar**  
1. **Pré-requisitos**:  
   - Java JDK 8+  
   - IDE (Eclipse, IntelliJ) ou terminal.  

2. **Compilação e Execução**:  
   ```bash
   javac br/ufal/ic/p2/jackut/App/*.java
   java br.ufal.ic.p2.jackut.App.Facade
   ```

3. **Funcionalidades Testáveis**:  
   ```java
   Facade jackut = new Facade();
   jackut.criarUsuario("alice", "senha123", "Alice");
   String idSessao = jackut.abrirSessao("alice", "senha123");
   jackut.enviarRecado(idSessao, "bob", "Olá, Bob!");
   ```

---

## 📄 **Licença**  
MIT License - Consulte o arquivo `LICENSE` para detalhes.  

--- 

### ✉ **Contato**  
Desenvolvido por Davi Silva de Melo Lins.  
Dúvidas? Abra uma **issue** ou envie um e-mail para dsml@ic.ufal.br!  

