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
     * @param login Login para acesso ao sistema.
     * @param senha Senha para acesso ao sistema.
     * @param nome  Nome de exibição do usuário.
     * @throws LoginOuSenhaInvalidoException se o login ou a senha forem nulos.
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

    public String getLogin() { return this.login; }
    public String getSenha() { return this.senha; }
    public String getNome() { return this.nome; }
    public Perfil getPerfil() { return this.perfil; }
    public CaixaDeEntrada getCaixaDeEntrada() { return this.caixaDeEntrada; }
    public ArrayList<User> getAmigos() { return this.amigos; }
    public ArrayList<User> getSolicitacoesEnviadas() { return this.solicitacoesEnviadas; }
    public ArrayList<User> getSolicitacoesRecebidas() { return this.solicitacoesRecebidas; }
    public ArrayList<Comunidade> getComunidadesProprietarias() { return this.comunidadesProprietarias; }
    public ArrayList<Comunidade> getComunidadesParticipantes() { return this.comunidadesParticipantes; }
    public ArrayList<User> getIdolos() { return this.idolos; }
    public ArrayList<User> getFas() { return this.fas; }
    public ArrayList<User> getPaqueras() { return this.paqueras; }
    public ArrayList<User> getPaquerasRecebidas() { return this.paquerasRecebidas; }
    public ArrayList<User> getInimigos() { return this.inimigos; }

    /**
     * Obtém o valor de um atributo do usuário.
     * Busca primeiro no atributo "nome" e depois no perfil.
     *
     * @param atributo O nome do atributo a ser buscado.
     * @return O valor do atributo.
     * @throws AtributoNaoPreenchidoException se o atributo não for encontrado.
     */
    public String getAtributo(String atributo) throws AtributoNaoPreenchidoException {
        if (atributo.equals("nome")) {
            return this.getNome();
        } else {
            return this.getPerfil().getAtributo(atributo);
        }
    }

    /**
     * Retorna a lista de amigos formatada como uma String.
     * A ordem dos amigos na string depende da ordem de inserção na lista.
     *
     * @return Uma string com os logins dos amigos. Ex: "{amigo1,amigo2}"
     */
    public String getAmigosString() {
        return UtilidadeString.formatArrayList(this.amigos);
    }

    /**
     * Retorna a lista de fãs formatada como uma String.
     *
     * @return Uma string com os logins dos fãs.
     */
    public String getFasString() { return UtilidadeString.formatArrayList(this.fas); }

    /**
     * Retorna a lista de paqueras formatada como uma String.
     *
     * @return Uma string com os logins dos paqueras.
     */
    public String getPaquerasString() { return UtilidadeString.formatArrayList(this.paqueras); }


    public void adicionarAmigo(User amigo) {
        if (!this.amigos.contains(amigo)) {
            this.amigos.add(amigo);
        }
    }

    public void adicionarSolicitacaoEnviada(User user) { this.solicitacoesEnviadas.add(user); }
    public void adicionarSolicitacaoRecebida(User user) { this.solicitacoesRecebidas.add(user); }
    public void setDonoComunidade(Comunidade comunidade) { this.comunidadesProprietarias.add(comunidade); }
    public void setParticipanteComunidade(Comunidade comunidade) {
        if (!this.comunidadesParticipantes.contains(comunidade)) {
            this.comunidadesParticipantes.add(comunidade);
        }
    }
    public void setIdolo(User idolo) { this.idolos.add(idolo); }
    public void setFa(User fa) { this.fas.add(fa); }
    public void setPaquera(User paquera) { this.paqueras.add(paquera); }
    public void setPaquerasRecebidas(User user) { this.paquerasRecebidas.add(user); }
    public void setInimigo(User inimigo) { this.inimigos.add(inimigo); }

    public void removerAmigo(User user) { this.amigos.remove(user); }
    public void removerSolicitacaoEnviada(User user) { this.solicitacoesEnviadas.remove(user); }
    public void removerSolicitacaoRecebida(User user) { this.solicitacoesRecebidas.remove(user); }
    public void sairComunidade(Comunidade comunidade) { this.comunidadesParticipantes.remove(comunidade); }
    public void removerFa(User user) { this.fas.remove(user); }
    public void removerIdolo(User user) { this.idolos.remove(user); }
    public void removerPaquera(User user) { this.paqueras.remove(user); }
    public void removerPaqueraRecebida(User user) { this.paquerasRecebidas.remove(user); }
    public void removerInimigo(User user) { this.inimigos.remove(user); }

    /**
     * Retorna uma lista das comunidades em que o usuário participa.
     * A lista é formatada como uma String, sem duplicatas e ordenada
     * alfabeticamente pelo nome da comunidade.
     *
     * @return String formatada com a lista de comunidades. Ex: "{Comunidade A,Comunidade B}"
     */
    public String getComunidadesString() {
        ArrayList<Comunidade> comunidadesOrdenadas = new ArrayList<>(this.comunidadesParticipantes);
        comunidadesOrdenadas.sort((c1, c2) -> String.CASE_INSENSITIVE_ORDER.compare(c1.getNome(), c2.getNome()));
        return UtilidadeString.formatArrayList(comunidadesOrdenadas);
    }

    /**
     * Verifica se a senha fornecida corresponde à senha do usuário.
     *
     * @param senha A senha a ser verificada.
     * @return {@code true} se a senha for correta, {@code false} caso contrário.
     */
    public boolean verificarSenha(String senha) {
        return this.senha.equals(senha);
    }

    /**
     * Retorna a representação em String do objeto User, que é o seu login.
     *
     * @return O login do usuário.
     */
    @Override
    public String toString() {
        return this.getLogin();
    }
}