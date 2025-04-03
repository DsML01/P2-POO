# Relatório do Projeto Jackut

## 1. Introdução
O Jackut é uma rede social simples que oferece:

- Criação e personalização de perfis
- Sistema de amizades com confirmação mútua
- Troca de mensagens entre usuários
- Armazenamento automático dos dados

## 2. Componentes Principais

### 2.1 Fachada (Facade)
A interface principal do sistema que concentra todas as operações disponíveis.

Principais funcionalidades:
- `zerarSistema()` - Reinicia todo o sistema
- `criarUsuario()` - Cadastra novos usuários
- `abrirSessao()` - Gerencia logins
- `editarPerfil()` - Modifica informações do usuário
- `adicionarAmigo()` - Controla relações de amizade
- `enviarRecado()` - Gerencia mensagens

### 2.2 Entidade User
Representa cada usuário com:

- Credenciais (login, senha)
- Informações do perfil
- Listas de amigos e solicitações
- Caixa de entrada de mensagens

## 3. Persistência de Dados

### 3.1 Armazenamento
- Dados salvos em arquivo JSON (`database.json`)
- Conversão automática de objetos para JSON
- Preserva todos os relacionamentos

### 3.2 Recuperação
- Carrega dados ao iniciar o sistema
- Reconstroi toda a estrutura de objetos
- Mantém o estado anterior da aplicação

### 3.3 Sessões
- IDs únicos gerados por combinação de login e timestamp
- Voláteis (não persistem após encerramento)
- Mapeiam usuários ativos

## 4. Fluxos Principais

### 4.1 Gerenciamento de Amizades
1. Solicitação enviada por um usuário
2. Confirmação requerida pelo destinatário
3. Relação bilateral estabelecida após aceite

### 4.2 Troca de Mensagens
1. Mensagens armazenadas em fila (FIFO)
2. Ordem de leitura preservada
3. Remoção após leitura

## 5. Diagrama de Classes
Visualização da estrutura do sistema:  
[Diagrama de Classes Jackut](https://www.mermaidchart.com/raw/98404ebb-b2d9-4b47-a1fe-9390af478389)

## 6. Decisões de Projeto

### 6.1 Arquitetura
- Separação clara de responsabilidades
- Baixo acoplamento entre componentes

### 6.2 Persistência
- JSON escolhido por simplicidade
- Fácil depuração (formato legível)

### 6.3 Modelagem
- Entidade User autossuficiente
- Estruturas de dados otimizadas
- Relacionamentos bidirecionais