package br.ufal.ic.p2.jackut.Entidades;

import br.ufal.ic.p2.jackut.Exceptions.Perfil.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.LoginOuSenhaInvalidoException;
import br.ufal.ic.p2.jackut.Utilidade.UtilidadeString;

import java.util.ArrayList;

/**
 * Representa um usuário no sistema Jackut.
 * <p>
 * Esta classe encapsula todos os dados e relacionamentos de um usuário,
 * incluindo seu perfil, amizades, comunidades e caixa de entrada,
 * delegando o gerenciamento de dados para as classes {@link Perfil} e {@link CaixaDeEntrada}.
 *
 * @author Davi
 */
public class User {
    private final String login;
    private final String senha;
    private final String nome;

    private final Perfil perfil = new Perfil();
    private final CaixaDeEntrada caixaDeEntrada = new CaixaDeEntrada();

    private final ArrayList<User> amigos = new ArrayList<>();
    private final ArrayList<User> solicitacoesEnviadas = new ArrayList<>();
    private final ArrayList<User> solicitacoesRecebidas = new ArrayList<>();

    private final ArrayList<Comunidade> comunidadesProprietarias = new ArrayList<>();
    private final ArrayList<Comunidade> comunidadesParticipantes = new ArrayList<>();

    private final ArrayList<User> idolos = new ArrayList<>();
    private final ArrayList<User> fas = new ArrayList<>();
    private final ArrayList<User> paqueras = new ArrayList<>();
    private final ArrayList<User> paquerasRecebidas = new ArrayList<>();
    private final ArrayList<User> inimigos = new ArrayList<>();

    /**
     * Construtor da classe User.
     *
     * @param login Login para acesso ao sistema
     * @param senha Senha para acesso ao sistema
     * @param nome Nome de exibição do usuário
     * @throws LoginOuSenhaInvalidoException se o login ou a senha forem nulos
     */
    public User(String login, String senha, String nome) throws LoginOuSenhaInvalidoException {
        if (login == null) {
            throw new LoginOuSenhaInvalidoException("login");
        }
        if (senha == null) {
            throw new LoginOuSenhaInvalidoException("senha");
        }
        this.login = login;
        this.senha = senha;
        this.nome = nome;
    }

    /**
     * Retorna o login do usuário.
     *
     * @return Login do usuário
     */
    public String getLogin() { return this.login; }

    /**
     * Retorna a senha do usuário.
     *
     * @return Senha do usuário
     */
    public String getSenha() { return this.senha; }

    /**
     * Retorna o nome do usuário.
     *
     * @return Nome do usuário
     */
    public String getNome() { return this.nome; }

    /**
     * Retorna o perfil do usuário.
     *
     * @return Objeto Perfil do usuário
     */
    public Perfil getPerfil() { return this.perfil; }

    /**
     * Retorna a caixa de entrada do usuário.
     *
     * @return Objeto CaixaDeEntrada do usuário
     */
    public CaixaDeEntrada getCaixaDeEntrada() { return this.caixaDeEntrada; }

    /**
     * Retorna a lista de amigos do usuário.
     *
     * @return ArrayList de amigos
     */
    public ArrayList<User> getAmigos() { return this.amigos; }

    /**
     * Retorna a lista de solicitações de amizade enviadas.
     *
     * @return ArrayList de solicitações enviadas
     */
    public ArrayList<User> getSolicitacoesEnviadas() { return this.solicitacoesEnviadas; }

    /**
     * Retorna a lista de solicitações de amizade recebidas.
     *
     * @return ArrayList de solicitações recebidas
     */
    public ArrayList<User> getSolicitacoesRecebidas() { return this.solicitacoesRecebidas; }

    /**
     * Retorna a lista de comunidades das quais o usuário é proprietário.
     *
     * @return ArrayList de comunidades proprietárias
     */
    public ArrayList<Comunidade> getComunidadesProprietarias() { return this.comunidadesProprietarias; }

    /**
     * Retorna a lista de comunidades das quais o usuário participa.
     *
     * @return ArrayList de comunidades participantes
     */
    public ArrayList<Comunidade> getComunidadesParticipantes() { return this.comunidadesParticipantes; }

    /**
     * Retorna a lista de ídolos do usuário.
     *
     * @return ArrayList de ídolos
     */
    public ArrayList<User> getIdolos() { return this.idolos; }

    /**
     * Retorna a lista de fãs do usuário.
     *
     * @return ArrayList de fãs
     */
    public ArrayList<User> getFas() { return this.fas; }

    /**
     * Retorna a lista de paqueras do usuário.
     *
     * @return ArrayList de paqueras
     */
    public ArrayList<User> getPaqueras() { return this.paqueras; }

    /**
     * Retorna a lista de paqueras recebidas pelo usuário.
     *
     * @return ArrayList de paqueras recebidas
     */
    public ArrayList<User> getPaquerasRecebidas() { return this.paquerasRecebidas; }

    /**
     * Retorna a lista de inimigos do usuário.
     *
     * @return ArrayList de inimigos
     */
    public ArrayList<User> getInimigos() { return this.inimigos; }

    /**
     * Obtém o valor de um atributo do usuário.
     * Busca primeiro no atributo "nome" e depois no perfil.
     *
     * @param atributo O nome do atributo a ser buscado
     * @return O valor do atributo
     * @throws AtributoNaoPreenchidoException se o atributo não for encontrado
     */
    public String getAtributo(String atributo) throws AtributoNaoPreenchidoException {
        if (atributo.equals("nome")) {
            return this.getNome();
        } else {
            return this.getPerfil().getAtributo(atributo);
        }
    }

    /**
     * Retorna a lista de amigos formatada como string.
     *
     * @return String formatada com os amigos
     * @see UtilidadeString
     */
    public String getAmigosString() {
        return UtilidadeString.formatArrayList(this.amigos);
    }

    /**
     * Retorna a lista de fãs formatada como string.
     *
     * @return String formatada com os fãs
     * @see UtilidadeString
     */
    public String getFasString() { return UtilidadeString.formatArrayList(this.fas); }

    /**
     * Retorna a lista de paqueras formatada como string.
     *
     * @return String formatada com as paqueras
     * @see UtilidadeString
     */
    public String getPaquerasString() { return UtilidadeString.formatArrayList(this.paqueras); }

    /**
     * Adiciona um amigo à lista de amigos.
     *
     * @param amigo Usuário a ser adicionado como amigo
     */
    public void adicionarAmigo(User amigo) {
        if (!this.amigos.contains(amigo)) {
            this.amigos.add(amigo);
        }
    }

    /**
     * Adiciona uma solicitação de amizade enviada.
     *
     * @param user Usuário que recebeu a solicitação
     */
    public void adicionarSolicitacaoEnviada(User user) { this.solicitacoesEnviadas.add(user); }

    /**
     * Adiciona uma solicitação de amizade recebida.
     *
     * @param user Usuário que enviou a solicitação
     */
    public void adicionarSolicitacaoRecebida(User user) { this.solicitacoesRecebidas.add(user); }

    /**
     * Adiciona uma comunidade como proprietário.
     *
     * @param comunidade Comunidade a ser adicionada
     */
    public void setDonoComunidade(Comunidade comunidade) { this.comunidadesProprietarias.add(comunidade); }

    /**
     * Adiciona uma comunidade como participante.
     *
     * @param comunidade Comunidade a ser adicionada
     */
    public void setParticipanteComunidade(Comunidade comunidade) {
        if (!this.comunidadesParticipantes.contains(comunidade)) {
            this.comunidadesParticipantes.add(comunidade);
        }
    }

    /**
     * Adiciona um ídolo à lista de ídolos.
     *
     * @param idolo Usuário a ser adicionado como ídolo
     */
    public void setIdolo(User idolo) { this.idolos.add(idolo); }

    /**
     * Adiciona um fã à lista de fãs.
     *
     * @param fa Usuário a ser adicionado como fã
     */
    public void setFa(User fa) { this.fas.add(fa); }

    /**
     * Adiciona uma paquera à lista de paqueras.
     *
     * @param paquera Usuário a ser adicionado como paquera
     */
    public void setPaquera(User paquera) { this.paqueras.add(paquera); }

    /**
     * Adiciona uma paquera recebida.
     *
     * @param user Usuário que enviou a paquera
     */
    public void setPaquerasRecebidas(User user) { this.paquerasRecebidas.add(user); }

    /**
     * Adiciona um inimigo à lista de inimigos.
     *
     * @param inimigo Usuário a ser adicionado como inimigo
     */
    public void setInimigo(User inimigo) { this.inimigos.add(inimigo); }

    /**
     * Remove um amigo da lista de amigos.
     *
     * @param user Usuário a ser removido
     */
    public void removerAmigo(User user) { this.amigos.remove(user); }

    /**
     * Remove uma solicitação de amizade enviada.
     *
     * @param user Usuário a ser removido das solicitações enviadas
     */
    public void removerSolicitacaoEnviada(User user) { this.solicitacoesEnviadas.remove(user); }

    /**
     * Remove uma solicitação de amizade recebida.
     *
     * @param user Usuário a ser removido das solicitações recebidas
     */
    public void removerSolicitacaoRecebida(User user) { this.solicitacoesRecebidas.remove(user); }

    /**
     * Remove o usuário de uma comunidade.
     *
     * @param comunidade Comunidade da qual o usuário vai sair
     */
    public void sairComunidade(Comunidade comunidade) { this.comunidadesParticipantes.remove(comunidade); }

    /**
     * Remove um fã da lista de fãs.
     *
     * @param user Usuário a ser removido
     */
    public void removerFa(User user) { this.fas.remove(user); }

    /**
     * Remove um ídolo da lista de ídolos.
     *
     * @param user Usuário a ser removido
     */
    public void removerIdolo(User user) { this.idolos.remove(user); }

    /**
     * Remove uma paquera da lista de paqueras.
     *
     * @param user Usuário a ser removido
     */
    public void removerPaquera(User user) { this.paqueras.remove(user); }

    /**
     * Remove uma paquera recebida.
     *
     * @param user Usuário a ser removido
     */
    public void removerPaqueraRecebida(User user) { this.paquerasRecebidas.remove(user); }

    /**
     * Remove um inimigo da lista de inimigos.
     *
     * @param user Usuário a ser removido
     */
    public void removerInimigo(User user) { this.inimigos.remove(user); }

    /**
     * Retorna a lista de comunidades formatada como string.
     * A lista é ordenada alfabeticamente pelo nome da comunidade.
     *
     * @return String formatada com as comunidades
     * @see UtilidadeString
     */
    public String getComunidadesString() {
        ArrayList<Comunidade> comunidadesOrdenadas = new ArrayList<>(this.comunidadesParticipantes);
        comunidadesOrdenadas.sort((c1, c2) -> String.CASE_INSENSITIVE_ORDER.compare(c1.getNome(), c2.getNome()));
        return UtilidadeString.formatArrayList(comunidadesOrdenadas);
    }

    /**
     * Verifica se a senha fornecida corresponde à senha do usuário.
     *
     * @param senha Senha a ser verificada
     * @return true se a senha for correta, false caso contrário
     */
    public boolean verificarSenha(String senha) {
        return this.senha.equals(senha);
    }

    /**
     * Retorna a representação em string do usuário (seu login).
     *
     * @return Login do usuário
     */
    @Override
    public String toString() {
        return this.getLogin();
    }
}