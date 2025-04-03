package br.ufal.ic.p2.jackut;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * Classe que representa um usu�rio do sistema Jackut.
 * <p>
 * Armazena informa��es pessoais, amigos, solicita��es de amizade e mensagens.
 * </p>
 *
 * @author Davi Silva
 */

public class User {
    /** Login �nico do usu�rio */
    private String login;// Login do usu�rio
    /** Nome de exibi��o do usu�rio */
    private String name;// Nome do usu�rio
    /** Senha do usu�rio */
    private String password;// Senha do usu�rio
    /** Lista de amigos (logins) */
    private ArrayList<String> friends;// Lista de amigos do usu�rio
    /** Lista de solicita��es de amizade pendentes */
    private ArrayList<String> friendSolicitation;// Lista de solicita��es de amizade pendentes
    /** Atributos extras do perfil (chave-valor) */
    private Map<String, String> attributes;// Atributos extras do usu�rio
    /** Caixa de mensagens recebidas (FIFO) */
    private Queue<Recado> messageBox;// Caixa de mensagens do usu�rio

    /**
     * Construtor vazio para serializa��o JSON.
     */
    public User() {
    }

    /**
     * Constr�i um novo usu�rio com informa��es b�sicas.
     *
     * @param login Identificador �nico
     * @param senha Senha de acesso
     * @param nome Nome de exibi��o
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
     * Verifica se a senha fornecida corresponde � senha do usu�rio.
     *
     * @param senha Senha a ser verificada
     * @return true se as senhas coincidirem, false caso contr�rio
     */
    public boolean verificarSenha(String senha)
    {
        return Objects.equals(senha, this.password);
    }

    /**
     * Obt�m o nome de exibi��o do usu�rio.
     *
     * @return Nome do usu�rio
     */
    public String getName() {
        return name;
    }

    /**
     * Obt�m o login �nico do usu�rio.
     *
     * @return Login do usu�rio
     */
    public String getLogin() {
        return login;
    }

    /**
     * Obt�m a senha do usu�rio.
     *
     * @return Senha do usu�rio
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obt�m a lista de amigos do usu�rio.
     *
     * @return Lista contendo os logins dos amigos
     */
    public ArrayList<String> getFriends() {
        return friends;
    }

    /**
     * Obt�m a lista de solicita��es de amizade pendentes.
     *
     * @return Lista contendo logins de usu�rios que enviaram solicita��es
     */
    public ArrayList<String> getFriendSolicitation() {
        return friendSolicitation;
    }

    /**
     * Obt�m a caixa de mensagens do usu�rio.
     *
     * @return Fila de mensagens recebidas (ordem FIFO)
     */
    public Queue<Recado> getMessageBox() {
        return messageBox;
    }

    /**
     * Define um novo nome para o usu�rio.
     *
     * @param name Novo nome de exibi��o
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Define um novo login para o usu�rio.
     *
     * @param login Novo identificador �nico
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Define uma nova senha para o usu�rio.
     *
     * @param password Nova senha de acesso
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Substitui toda a lista de amigos do usu�rio.
     *
     * @param friends Nova lista de amigos (logins)
     */
    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    /**
     * Substitui toda a lista de solicita��es de amizade.
     *
     * @param friendSolicitation Nova lista de solicita��es
     */
    public void setFriendSolicitation(ArrayList<String> friendSolicitation) {
        this.friendSolicitation = friendSolicitation;
    }

    /**
     * Substitui toda a caixa de mensagens do usu�rio.
     *
     * @param messageBox Nova fila de mensagens
     */
    public void setMessageBox(Queue<Recado> messageBox) {
        this.messageBox = messageBox;
    }

    /**
     * Define ou atualiza um atributo extra no perfil do usu�rio.
     *
     * @param attribute Chave do atributo
     * @param content Valor do atributo
     */
    public void setExtraAttributes(String attribute, String content) {
        if (attributes.containsKey(attribute)) attributes.replace(attribute, content);
        else attributes.put(attribute, content);
    }

    /**
     * Obt�m o valor de um atributo extra do perfil.
     *
     * @param attribute Chave do atributo desejado
     * @return Valor do atributo
     * @throws RuntimeException Se o atributo n�o existir
     */
    public String getExtraAttribute(String attribute) {
        if (attributes.containsKey(attribute)) return attributes.get(attribute);
        else throw new RuntimeException("Atributo n�o preenchido.");
    }

    /**
     * Obt�m todos os atributos extras do usu�rio para serializa��o.
     *
     * @return Mapa de atributos (chave-valor)
     */
    @JsonAnyGetter
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Define um atributo extra durante a desserializa��o.
     *
     * @param attribute Chave do atributo
     * @param value Valor do atributo
     */
    @JsonAnySetter
    public void setAttributes(String attribute, String value) {
        this.attributes.put(attribute, value);
    }
    /**
     * Adiciona um amigo � lista de amigos.
     *
     * @param friend Login do amigo a ser adicionado
     */
    public void addFriends(String friend)
    {
        this.friendSolicitation.remove(friend);
        this.friends.add(friend);
    }

    /**
     * Adiciona uma solicita��o de amizade � lista pendente.
     *
     * @param friendSolicitation Login do usu�rio que enviou a solicita��o
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
