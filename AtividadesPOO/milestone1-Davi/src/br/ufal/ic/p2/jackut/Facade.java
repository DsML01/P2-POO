package br.ufal.ic.p2.jackut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;
import java.io.*;

/**
 * Classe principal do sistema Jackut que implementa o padrão Facade,
 * fornecendo uma interface simplificada para todas as operações do sistema.
 * <p>
 * Gerencia usuários, sessões, amizades e mensagens, além de persistência em arquivo JSON.
 * </p>
 *
 * @author Davi
 */
public class Facade {

    /** Mensagem padrão para usuário não encontrado */
    public static final String USER_NOT_FOUND= "Usuário não cadastrado.";
    /** Mapa de usuários cadastrados (login -> User) */
    private Map<String, User> users; // Mapa para armazenar usuários
    /** Mapa de sessões ativas (sessionId -> User) */
    private Map<String, User> sessions; // Mapa para armazenar sessões

    /**
     * Constrói uma nova instância do Facade e carrega os dados do sistema.
     * <p>
     * Inicializa as estruturas de dados e carrega os usuários do arquivo JSON se existir.
     * </p>
     */
    public Facade()
    {
        this.users = new HashMap<>();
        this.sessions = new HashMap<>();
        carregarSistema();
    }

    /**
     * Zera o sistema, removendo todos os usuários e sessões.
     * <p>
     * Também apaga o arquivo de persistência de dados.
     * </p>
     */
    public void zerarSistema()
    {
        users.clear();
        sessions.clear();
        File usersData = new File("database.json");
        usersData.delete();
    }

    /**
     * Carrega os dados do sistema a partir do arquivo JSON.
     * <p>
     * Se o arquivo existir, recria todos os usuários com seus atributos,
     * amizades, solicitações e mensagens.
     * </p>
     */
    public void carregarSistema()
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File usersData = new File("database.json");

            if(usersData.exists())
            {
                List<User> listaUsuarios = objectMapper.readValue(usersData, new TypeReference<List<User>>() {});

                for (User user : listaUsuarios)
                {
                    criarUsuario(user.getLogin(), user.getPassword(), user.getName());
                    User newUser = users.get(user.getLogin());
                    newUser.setFriends(user.getFriends());
                    newUser.setFriendSolicitation(user.getFriendSolicitation());
                    newUser.setMessageBox(user.getMessageBox());
                    for(Map.Entry<String, String> entry : user.getAttributes().entrySet()){
                        newUser.setAttributes(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (IOException e){
            System.err.println("Erro ao carregar dados do JSON.");
            e.printStackTrace();
        }
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param login Identificador único do usuário
     * @param senha Senha de acesso do usuário
     * @param nome Nome de exibição do usuário
     * @throws RuntimeException Se o login for inválido, senha inválida ou conta já existir
     */
    public void criarUsuario(String login, String senha, String nome){
        if (!users.containsKey(login)) {
            if(login == null) throw new RuntimeException("Login inválido.");
            if (senha == null) throw new RuntimeException("Senha inválida.");
            User user = new User(login, senha, nome);
            users.put(login, user);
        }
        else throw new RuntimeException("Conta com esse nome já existe.");
    }

    /**
     * Obtém um atributo específico de um usuário.
     *
     * @param login Login do usuário
     * @param atributo Atributo desejado (nome, senha, login ou atributo extra)
     * @return Valor do atributo solicitado
     * @throws RuntimeException Se usuário não for encontrado ou atributo não existir
     */
    public String getAtributoUsuario(String login, String atributo){
        if(users.get(login) != null){
            if (Objects.equals(atributo, "nome")) return users.get(login).getName();
            else if (Objects.equals(atributo, "senha")) return users.get(login).getPassword();
            else if (Objects.equals(atributo, "login")) return users.get(login).getLogin();
            else return users.get(login).getExtraAttribute(atributo);
        }
        else{ throw new RuntimeException(USER_NOT_FOUND);}
    }

    /**
     * Abre uma nova sessão para um usuário.
     *
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return ID da sessão criada
     * @throws RuntimeException Se login ou senha forem inválidos
     */
    public String abrirSessao (String login, String senha) {
        User user = users.get(login);
        if(user != null && user.verificarSenha(senha)){
            String id = generateSessionId(login);
            sessions.put(id, user);
            return id;
        }
        else throw new RuntimeException("Login ou senha inválidos.");
    }

    /**
     * Gera um ID único para a sessão.
     *
     * @param login Login do usuário
     * @return ID da sessão no formato "login_timestamp"
     */
    private String generateSessionId(String login) {
        long timestamp = System.currentTimeMillis();
        return login + "_" + timestamp;
    }

    /**
     * Edita um atributo do perfil do usuário logado.
     *
     * @param Id da sessão
     * @param atributo Atributo a ser modificado
     * @param valor Novo valor para o atributo
     * @throws RuntimeException Se usuário não for encontrado ou login for inválido
     */
    public void editarPerfil(String Id, String atributo, String valor){
        User user = sessions.get(Id);
        if (user != null) {
            if (Objects.equals(atributo, "nome")) user.setName(valor);
            else if (Objects.equals(atributo, "senha")) user.setPassword(valor);
            else if (Objects.equals(atributo, "login")) {
                if (!users.containsKey(valor)) {
                    user.setLogin(valor);
                } else throw new RuntimeException("Login inválido.");
            }
            else user.setExtraAttributes(atributo, valor);
        }
        else throw new RuntimeException(USER_NOT_FOUND);
    }

    /**
     * Verifica se dois usuários são amigos.
     *
     * @param login Login do primeiro usuário
     * @param amigo Login do segundo usuário
     * @return true se forem amigos, false caso contrário
     */
    public boolean ehAmigo(String login, String amigo){
        return users.get(login).getFriends().contains(amigo);
    }

    /**
     * Adiciona um amigo para o usuário logado.
     * <p>
     * Se o outro usuário já tiver enviado solicitação, estabelece amizade.
     * Caso contrário, envia uma solicitação de amizade.
     * </p>
     *
     * @param id ID da sessão
     * @param amigo Login do usuário a ser adicionado como amigo
     * @throws RuntimeException Em casos de erro (usuário não encontrado, autoamizade, etc.)
     */
    public void adicionarAmigo(String id, String amigo){
        User user = sessions.get(id);
        if (user == null)throw new RuntimeException(USER_NOT_FOUND);
        User friendUser = users.get(amigo);
        if (friendUser != null) {
            if (Objects.equals(user.getLogin(), amigo))
                throw new RuntimeException("Usuário não pode adicionar a si mesmo como amigo.");
            else if (user.getFriendSolicitation().contains(amigo)) {
                user.addFriends(amigo);
                friendUser.addFriends(user.getLogin());
            } else if (friendUser.getFriendSolicitation().contains(user.getLogin()))
                throw new RuntimeException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
            else if (ehAmigo(user.getLogin(), amigo))
                throw new RuntimeException("Usuário já está adicionado como amigo.");
            else {
                friendUser.addFriendSolicitation(user.getLogin());
            }
        }
        else{ throw new RuntimeException(USER_NOT_FOUND);}
    }

    /**
     * Obtém a lista de amigos de um usuário no formato JSON.
     *
     * @param login Login do usuário
     * @return String no formato "{amigo1,amigo2}" ou "{}" se não tiver amigos
     */
    public String getAmigos(String login){
        User user = users.get(login);
        ArrayList<String> friends = user.getFriends();
        return friends.isEmpty() ? "{}" : "{" + String.join(",", friends) + "}";
    }

    /**
     * Envia um recado para outro usuário.
     *
     * @param id ID da sessão do remetente
     * @param destinatario Login do usuário destinatário
     * @param mensagem Conteúdo do recado
     * @throws RuntimeException Em casos de erro (usuários não encontrados, autoenvio)
     */
    public void enviarRecado(String id, String destinatario, String mensagem){
        User sender = sessions.get(id);
        User receiver = users.get(destinatario);
        if (sender == receiver) throw new RuntimeException("Usuário não pode enviar recado para si mesmo.");
        if(sender != null){
            if(receiver != null){
                Recado recado = new Recado(sender.getLogin(), mensagem);
                receiver.receiveMessage(recado);
            }
            else throw new RuntimeException(USER_NOT_FOUND);
        }
        else throw new RuntimeException(USER_NOT_FOUND);
    }

    /**
     * Lê o próximo recado da caixa de entrada do usuário logado.
     *
     * @param id ID da sessão
     * @return Conteúdo do recado
     * @throws RuntimeException Se não houver recados ou usuário não encontrado
     */
    public String lerRecado(String id){
        User user = sessions.get(id);
        Recado recado = user.getMessageBox().poll();
        if(recado == null) throw new RuntimeException("Não há recados.");
        else return recado.getMensagem();

    }

    /**
     * Encerra o sistema, salvando todos os dados no arquivo JSON.
     */
    public void encerrarSistema()
    {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File usersData = new File("database.json");

            List<User> listaUsuarios = new ArrayList<>(users.values());

            objectMapper.writeValue(usersData, listaUsuarios);

            System.out.println("Todos os dados foram salvos.");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados.");
            e.printStackTrace();
        }
    }
}

