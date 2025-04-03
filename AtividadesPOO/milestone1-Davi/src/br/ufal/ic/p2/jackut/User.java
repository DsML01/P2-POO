package br.ufal.ic.p2.jackut;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * Classe que representa um usuário do sistema Jackut.
 * <p>
 * Armazena informações pessoais, amigos, solicitações de amizade e mensagens.
 * </p>
 *
 * @author Davi Silva
 */

public class User {
    /** Login único do usuário */
    private String login;// Login do usuário
    /** Nome de exibição do usuário */
    private String name;// Nome do usuário
    /** Senha do usuário */
    private String password;// Senha do usuário
    /** Lista de amigos (logins) */
    private ArrayList<String> friends;// Lista de amigos do usuário
    /** Lista de solicitações de amizade pendentes */
    private ArrayList<String> friendSolicitation;// Lista de solicitações de amizade pendentes
    /** Atributos extras do perfil (chave-valor) */
    private Map<String, String> attributes;// Atributos extras do usuário
    /** Caixa de mensagens recebidas (FIFO) */
    private Queue<Recado> messageBox;// Caixa de mensagens do usuário

    /**
     * Construtor vazio para serialização JSON.
     */
    public User() {
    }

    /**
     * Constrói um novo usuário com informações básicas.
     *
     * @param login Identificador único
     * @param senha Senha de acesso
     * @param nome Nome de exibição
     */
    @JsonCreator
    public User(@JsonProperty("login") String login, @JsonProperty("senha") String senha, @JsonProperty("nome") String nome)
    {
        this.login = login;
        this.password = senha;
        this.name = nome;
        friends = new ArrayList<>();
        friendSolicitation = new ArrayList<>();
        messageBox = new LinkedList<>();
        attributes = new HashMap<>();
    }

    /**
     * Verifica se a senha fornecida corresponde à senha do usuário.
     *
     * @param senha Senha a ser verificada
     * @return true se as senhas coincidirem, false caso contrário
     */
    public boolean verificarSenha(String senha)
    {
        return Objects.equals(senha, this.password);
    }

    /**
     * Obtém o nome de exibição do usuário.
     *
     * @return Nome do usuário
     */
    public String getName() {
        return name;
    }

    /**
     * Obtém o login único do usuário.
     *
     * @return Login do usuário
     */
    public String getLogin() {
        return login;
    }

    /**
     * Obtém a senha do usuário.
     *
     * @return Senha do usuário
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtém a lista de amigos do usuário.
     *
     * @return Lista contendo os logins dos amigos
     */
    public ArrayList<String> getFriends() {
        return friends;
    }

    /**
     * Obtém a lista de solicitações de amizade pendentes.
     *
     * @return Lista contendo logins de usuários que enviaram solicitações
     */
    public ArrayList<String> getFriendSolicitation() {
        return friendSolicitation;
    }

    /**
     * Obtém a caixa de mensagens do usuário.
     *
     * @return Fila de mensagens recebidas (ordem FIFO)
     */
    public Queue<Recado> getMessageBox() {
        return messageBox;
    }

    /**
     * Define um novo nome para o usuário.
     *
     * @param name Novo nome de exibição
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Define um novo login para o usuário.
     *
     * @param login Novo identificador único
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Define uma nova senha para o usuário.
     *
     * @param password Nova senha de acesso
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Substitui toda a lista de amigos do usuário.
     *
     * @param friends Nova lista de amigos (logins)
     */
    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    /**
     * Substitui toda a lista de solicitações de amizade.
     *
     * @param friendSolicitation Nova lista de solicitações
     */
    public void setFriendSolicitation(ArrayList<String> friendSolicitation) {
        this.friendSolicitation = friendSolicitation;
    }

    /**
     * Substitui toda a caixa de mensagens do usuário.
     *
     * @param messageBox Nova fila de mensagens
     */
    public void setMessageBox(Queue<Recado> messageBox) {
        this.messageBox = messageBox;
    }

    /**
     * Define ou atualiza um atributo extra no perfil do usuário.
     *
     * @param attribute Chave do atributo
     * @param content Valor do atributo
     */
    public void setExtraAttributes(String attribute, String content) {
        if (attributes.containsKey(attribute)) attributes.replace(attribute, content);
        else attributes.put(attribute, content);
    }

    /**
     * Obtém o valor de um atributo extra do perfil.
     *
     * @param attribute Chave do atributo desejado
     * @return Valor do atributo
     * @throws RuntimeException Se o atributo não existir
     */
    public String getExtraAttribute(String attribute) {
        if (attributes.containsKey(attribute)) return attributes.get(attribute);
        else throw new RuntimeException("Atributo não preenchido.");
    }

    /**
     * Obtém todos os atributos extras do usuário para serialização.
     *
     * @return Mapa de atributos (chave-valor)
     */
    @JsonAnyGetter
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Define um atributo extra durante a desserialização.
     *
     * @param attribute Chave do atributo
     * @param value Valor do atributo
     */
    @JsonAnySetter
    public void setAttributes(String attribute, String value) {
        this.attributes.put(attribute, value);
    }
    /**
     * Adiciona um amigo à lista de amigos.
     *
     * @param friend Login do amigo a ser adicionado
     */
    public void addFriends(String friend)
    {
        this.friendSolicitation.remove(friend);
        this.friends.add(friend);
    }

    /**
     * Adiciona uma solicitação de amizade à lista pendente.
     *
     * @param friendSolicitation Login do usuário que enviou a solicitação
     */
    public void addFriendSolicitation(String friendSolicitation)
    {
        this.friendSolicitation.add(friendSolicitation);
    }

    /**
     * Recebe uma nova mensagem na caixa de entrada.
     *
     * @param recado Objeto Recado contendo a mensagem
     */
    public void receiveMessage(Recado recado)
    {
        this.messageBox.add(recado);
    }
}
