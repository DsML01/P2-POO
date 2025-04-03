package br.ufal.ic.p2.jackut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.*;
import java.io.*;

/**
 * Classe principal do sistema Jackut que implementa o padr�o Facade,
 * fornecendo uma interface simplificada para todas as opera��es do sistema.
 * <p>
 * Gerencia usu�rios, sess�es, amizades e mensagens, al�m de persist�ncia em arquivo JSON.
 * </p>
 *
 * @author Davi
 */
public class Facade {

    /** Mensagem padr�o para usu�rio n�o encontrado */
    public static final String USER_NOT_FOUND= "Usu�rio n�o cadastrado.";
    /** Mapa de usu�rios cadastrados (login -> User) */
    private Map<String, User> users; // Mapa para armazenar usu�rios
    /** Mapa de sess�es ativas (sessionId -> User) */
    private Map<String, User> sessions; // Mapa para armazenar sess�es

    /**
     * Constr�i uma nova inst�ncia do Facade e carrega os dados do sistema.
     * <p>
     * Inicializa as estruturas de dados e carrega os usu�rios do arquivo JSON se existir.
     * </p>
     */
    public Facade()
    {
        this.users = new HashMap<>();
        this.sessions = new HashMap<>();
        carregarSistema();
    }

    /**
     * Zera o sistema, removendo todos os usu�rios e sess�es.
     * <p>
     * Tamb�m apaga o arquivo de persist�ncia de dados.
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
     * Se o arquivo existir, recria todos os usu�rios com seus atributos,
     * amizades, solicita��es e mensagens.
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
     * Cria um novo usu�rio no sistema.
     *
     * @param login Identificador �nico do usu�rio
     * @param senha Senha de acesso do usu�rio
     * @param nome Nome de exibi��o do usu�rio
     * @throws RuntimeException Se o login for inv�lido, senha inv�lida ou conta j� existir
     */
    public void criarUsuario(String login, String senha, String nome){
        if (!users.containsKey(login)) {
            if(login == null) throw new RuntimeException("Login inv�lido.");
            if (senha == null) throw new RuntimeException("Senha inv�lida.");
            User user = new User(login, senha, nome);
            users.put(login, user);
        }
        else throw new RuntimeException("Conta com esse nome j� existe.");
    }

    /**
     * Obt�m um atributo espec�fico de um usu�rio.
     *
     * @param login Login do usu�rio
     * @param atributo Atributo desejado (nome, senha, login ou atributo extra)
     * @return Valor do atributo solicitado
     * @throws RuntimeException Se usu�rio n�o for encontrado ou atributo n�o existir
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
     * Abre uma nova sess�o para um usu�rio.
     *
     * @param login Login do usu�rio
     * @param senha Senha do usu�rio
     * @return ID da sess�o criada
     * @throws RuntimeException Se login ou senha forem inv�lidos
     */
    public String abrirSessao (String login, String senha) {
        User user = users.get(login);
        if(user != null && user.verificarSenha(senha)){
            String id = generateSessionId(login);
            sessions.put(id, user);
            return id;
        }
        else throw new RuntimeException("Login ou senha inv�lidos.");
    }

    /**
     * Gera um ID �nico para a sess�o.
     *
     * @param login Login do usu�rio
     * @return ID da sess�o no formato "login_timestamp"
     */
    private String generateSessionId(String login) {
        long timestamp = System.currentTimeMillis();
        return login + "_" + timestamp;
    }

    /**
     * Edita um atributo do perfil do usu�rio logado.
     *
     * @param Id da sess�o
     * @param atributo Atributo a ser modificado
     * @param valor Novo valor para o atributo
     * @throws RuntimeException Se usu�rio n�o for encontrado ou login for inv�lido
     */
    public void editarPerfil(String Id, String atributo, String valor){
        User user = sessions.get(Id);
        if (user != null) {
            if (Objects.equals(atributo, "nome")) user.setName(valor);
            else if (Objects.equals(atributo, "senha")) user.setPassword(valor);
            else if (Objects.equals(atributo, "login")) {
                if (!users.containsKey(valor)) {
                    user.setLogin(valor);
                } else throw new RuntimeException("Login inv�lido.");
            }
            else user.setExtraAttributes(atributo, valor);
        }
        else throw new RuntimeException(USER_NOT_FOUND);
    }

    /**
     * Verifica se dois usu�rios s�o amigos.
     *
     * @param login Login do primeiro usu�rio
     * @param amigo Login do segundo usu�rio
     * @return true se forem amigos, false caso contr�rio
     */
    public boolean ehAmigo(String login, String amigo){
        return users.get(login).getFriends().contains(amigo);
    }

    /**
     * Adiciona um amigo para o usu�rio logado.
     * <p>
     * Se o outro usu�rio j� tiver enviado solicita��o, estabelece amizade.
     * Caso contr�rio, envia uma solicita��o de amizade.
     * </p>
     *
     * @param id ID da sess�o
     * @param amigo Login do usu�rio a ser adicionado como amigo
     * @throws RuntimeException Em casos de erro (usu�rio n�o encontrado, autoamizade, etc.)
     */
    public void adicionarAmigo(String id, String amigo){
        User user = sessions.get(id);
        if (user == null)throw new RuntimeException(USER_NOT_FOUND);
        User friendUser = users.get(amigo);
        if (friendUser != null) {
            if (Objects.equals(user.getLogin(), amigo))
                throw new RuntimeException("Usu�rio n�o pode adicionar a si mesmo como amigo.");
            else if (user.getFriendSolicitation().contains(amigo)) {
                user.addFriends(amigo);
                friendUser.addFriends(user.getLogin());
            } else if (friendUser.getFriendSolicitation().contains(user.getLogin()))
                throw new RuntimeException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
            else if (ehAmigo(user.getLogin(), amigo))
                throw new RuntimeException("Usu�rio j� est� adicionado como amigo.");
            else {
                friendUser.addFriendSolicitation(user.getLogin());
            }
        }
        else{ throw new RuntimeException(USER_NOT_FOUND);}
    }

    /**
     * Obt�m a lista de amigos de um usu�rio no formato JSON.
     *
     * @param login Login do usu�rio
     * @return String no formato "{amigo1,amigo2}" ou "{}" se n�o tiver amigos
     */
    public String getAmigos(String login){
        User user = users.get(login);
        ArrayList<String> friends = user.getFriends();
        return friends.isEmpty() ? "{}" : "{" + String.join(",", friends) + "}";
    }

    /**
     * Envia um recado para outro usu�rio.
     *
     * @param id ID da sess�o do remetente
     * @param destinatario Login do usu�rio destinat�rio
     * @param mensagem Conte�do do recado
     * @throws RuntimeException Em casos de erro (usu�rios n�o encontrados, autoenvio)
     */
    public void enviarRecado(String id, String destinatario, String mensagem){
        User sender = sessions.get(id);
        User receiver = users.get(destinatario);
        if (sender == receiver) throw new RuntimeException("Usu�rio n�o pode enviar recado para si mesmo.");
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
     * L� o pr�ximo recado da caixa de entrada do usu�rio logado.
     *
     * @param id ID da sess�o
     * @return Conte�do do recado
     * @throws RuntimeException Se n�o houver recados ou usu�rio n�o encontrado
     */
    public String lerRecado(String id){
        User user = sessions.get(id);
        Recado recado = user.getMessageBox().poll();
        if(recado == null) throw new RuntimeException("N�o h� recados.");
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

