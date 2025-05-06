package br.ufal.ic.p2.jackut.Entidades;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import br.ufal.ic.p2.jackut.Exceptions.Comunidade.SemMensagensException;
import br.ufal.ic.p2.jackut.Exceptions.Perfil.AtributoNaoPreenchidoException;
import br.ufal.ic.p2.jackut.Exceptions.Recado.SemRecadosException;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.LoginOuSenhaInvalidoException;
import br.ufal.ic.p2.jackut.Utilidade.UtilidadeString;


/**
 * Classe para criar um usuario
 */

public class User {
    private final String login;
    private final String senha;
    private final String nome;

    private final Perfil perfil = new Perfil();

    private final ArrayList<User> amigos = new ArrayList<>();
    private final ArrayList<User> solicitacoesEnviadas = new ArrayList<>();
    private final ArrayList<User> solicitacoesRecebidas = new ArrayList<>();

    private final Queue<Recado> recados = new LinkedList<>();

    private final ArrayList<Comunidade> comunidadesProprietarias = new ArrayList<>();
    private final ArrayList<Comunidade> comunidadesParticipantes = new ArrayList<>();
    private final Queue<Mensagem> mensagens = new LinkedList<>();

    private final ArrayList<User> idolos = new ArrayList<>();
    private final ArrayList<User> fas = new ArrayList<>();

    private final ArrayList<User> paqueras = new ArrayList<>();
    private final ArrayList<User> paquerasRecebidas = new ArrayList<>();

    private final ArrayList<User> inimigos = new ArrayList<>();

    /**
     * Cria um novo User
     * Inicializa um Perfil para o User
     *
     * @param login Login do User.
     * @param senha Senha do User.
     * @param nome Nome do User.
     *
     * @throws LoginOuSenhaInvalidoException Exceção lançada caso o login ou a senha sejam inválidos.
     *
     * @see Perfil
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
     * Retorna o login do User.
     *
     * @return Login do User.
     */

    public String getLogin() {
        return this.login;
    }

    /**
     * Retorna a senha do User.
     *
     * @return Senha do User.
     */

    public String getSenha() {
        return this.senha;
    }

    /**
     * Retorna o nome do User.
     *
     * @return Nome do User.
     */

    public String getNome() {
        return this.nome;
    }

    /**
     * Retorna o perfil do User.
     *
     * @return Perfil do User.
     *
     * @see Perfil
     */

    public Perfil getPerfil() {
        return this.perfil;
    }

    /**
     * Retorna o valor de um atributo do User.
     *
     * @param atributo  Atributo a ser retornado.
     * @return          Valor do atributo.
     *
     * @throws AtributoNaoPreenchidoException Exceção lançada caso o atributo não tenha sido preenchido.
     */

    public String getAtributo(String atributo) throws AtributoNaoPreenchidoException {
        if (atributo.equals("nome")) {
            return this.getNome();
        } else {
            return this.getPerfil().getAtributo(atributo);
        }
    }

    /**
     * Retornar a lista de amigos do User.
     *
     * @return Lista de amigos do User.
     */

    public ArrayList<User> getAmigos() {
        return this.amigos;
    }

    /**
     * Retorna a lista de amigos do User em String
     *
     * @return Lista de amigos do User formatada em uma String
     *
     * @see UtilidadeString
     */

    public String getAmigosString() {
        return UtilidadeString.formatArrayList(this.amigos);
    }

    /**
     * Retorna a lista de solicitações de amizade enviadas pelo User.
     *
     * @return Lista de solicitações de amizade enviadas pelo User
     */

    public ArrayList<User> getSolicitacoesEnviadas() {
        return this.solicitacoesEnviadas;
    }

    /**
     * Retorna uma lista de solicitações de amizade que o User recebeu.
     *
     * @return Lista de solicitações de amizade que o User recebeu.
     */

    public ArrayList<User> getSolicitacoesRecebidas() {
        return this.solicitacoesRecebidas;
    }

    /**
     * Retorna uma lista de recados que o User ainda não leu.
     *
     * @return Lista de recados que o User ainda não leu.
     *
     * @see Recado
     */

    public Queue<Recado> getRecados() {
        return this.recados;
    }

    /**
     * Retorna a lista de comunidades das quais o User é dono.
     *
     * @return Lista de comunidades das quais o User é dono.
     *
     * @see Comunidade
     */

    public ArrayList<Comunidade> getComunidadesProprietarias() {
        return this.comunidadesProprietarias;
    }

    /**
     * Retorna a lista de comunidades das quais o User é participante.
     *
     * @return Lista de comunidades das quais o User é participante.
     *
     * @see Comunidade
     */

    public ArrayList<Comunidade> getComunidadesParticipantes() {
        return this.comunidadesParticipantes;
    }

    /**
     * Retorna a fila de mensagens do User.
     *
     * @return Fila de mensagens do User.
     *
     * @see Mensagem
     */

    public Queue<Mensagem> getMensagens() {
        return this.mensagens;
    }

    /**
     * Retorna a lista de ídolos do User.
     *
     * @return Lista de ídolos do User.
     */

    public ArrayList<User> getIdolos() {
        return this.idolos;
    }

    /**
     * Retorna a lista de fãs do User.
     *
     * @return Lista de fãs do User.
     */

    public ArrayList<User> getFas() {
        return this.fas;
    }

    /**
     * Retorna a lista de ídolos do User formatada como uma String.
     *
     * @return Lista de ídolos do User formatada em uma String
     *
     * @see UtilidadeString
     */

    public String getFasString() {
        return UtilidadeString.formatArrayList(this.fas);
    }

    /**
     * Retorna a lista de paqueras do User.
     *
     * @return Lista de paqueras do User.
     */

    public ArrayList<User> getPaqueras() {
        return this.paqueras;
    }

    /**
     * Retorna a lista de paqueras recebidas do User.
     *
     * @return Lista de paqueras recebidas do User.
     */

    public ArrayList<User> getPaquerasRecebidas() {
        return this.paquerasRecebidas;
    }

    /**
     * Retorna a lista de paqueras do User formatada como uma String.
     *
     * @return Lista de paqueras do User formatada em uma String
     *
     * @see UtilidadeString
     */

    public String getPaquerasString() {
        return UtilidadeString.formatArrayList(this.paqueras);
    }

    /**
     * Retorna a lista de inimigos do User.
     *
     * @return Lista de inimigos do User.
     */

    public ArrayList<User> getInimigos() {
        return this.inimigos;
    }

    /**
     * Adiciona um amigo ao User.
     *
     * @param amigo Amigo a ser adicionado.
     */

    public void setAmigo(User amigo) {
        if (!this.amigos.contains(amigo)) {
            this.amigos.add(amigo);
        }
    }

    /**
     * Adiciona uma comunidade dentro da lista de comunidades que o User é o dono.
     *
     * @param comunidade Comunidade a ser adicionada.
     */

    public void setDonoComunidade(Comunidade comunidade) {
        if (this.comunidadesProprietarias.contains(comunidade)) {
            return;
        }

        this.comunidadesProprietarias.add(comunidade);
    }

    /**
     * Adiciona uma comunidade dentro da lista de comunidades que o User participa.
     *
     * @param comunidade Comunidade a ser adicionada.
     */

    public void setParticipanteComunidade(Comunidade comunidade) {
        if (this.comunidadesParticipantes.contains(comunidade)) {return;}

        this.comunidadesParticipantes.add(comunidade);
    }

    /**
     * Adiciona um User na lista de ídolos do User que chamou o metodo.
     *
     * @param idolo Ídolo a ser adicionado.
     */

    public void setIdolo(User idolo) {this.idolos.add(idolo);}

    /**
     * Adiciona um User na lista de fãs de outro User.
     *
     * @param fa Fã a ser adicionado.
     */

    public void setFa(User fa) {this.fas.add(fa);}

    /**
     * Adiciona um User na lista de paqueras de outro User.
     *
     * @param paquera Paquera a ser adicionado.
     */

    public void setPaquera(User paquera) {this.paqueras.add(paquera);}

    /**
     * Adiciona um User na lista de paqueras recebidas de outro User.
     *
     * @param user Usuário a ser adicionado.
     */

    public void setPaquerasRecebidas(User user) {this.paquerasRecebidas.add(user);}

    /**
     * Adiciona um User na lista de inimigos do User que chamou o metodo.
     *
     * @param inimigo Inimigo a ser adicionado.
     */

    public void setInimigo(User inimigo) {this.inimigos.add(inimigo);}

    /**
     * Remove um recado da fila de recados do User que chamou o metodo.
     *
     * @param recado Recado a ser removido.
     */

    public void removerRecado(Recado recado) {this.recados.remove(recado);}

    /**
     * Remove um amigo da lista de amigos do User que chamou o metodo.
     *
     * @param user Usuário que não sera mais amigo.
     */

    public void removerAmigo(User user) {this.amigos.remove(user);}

    /**
     * Remove um fã da lista de fãs do User que chamou o metodo.
     *
     * @param user Usuário que não é mais ídolo.
     */

    public void removerFa(User user) {this.fas.remove(user);}

    /**
     * Remove um ídolo da lista de ídolos do User que chamou o metodo.
     *
     * @param user Usuário que não é mais ídolo.
     */

    public void removerIdolo(User user) {this.idolos.remove(user);}

    /**
     * Remove uma paquera da lista de paqueras do User que chamou o metodo.
     *
     * @param user Usuário que não é mais paquera.
     */

    public void removerPaquera(User user) {this.paqueras.remove(user);}

    /**
     * Remove uma paquera da lista de paqueras recebidas do User que chamou o metodo.
     *
     * @param user Usuário que não é mais paquera.
     */

    public void removerPaqueraRecebida(User user) {this.paquerasRecebidas.remove(user);}

    /**
     * Remove um inimigo da lista de inimigos do User que chamou o metodo.
     *
     * @param user Usuário que não é mais inimigo.
     */

    public void removerInimigo(User user) {this.inimigos.remove(user);}

    /**
     * Remove uma solicitação de amizade da lista de solicitações enviadas do User que chamou o metodo para o User que foi passado como parametro no metodo.
     *
     * @param user Usuário que não receberá mais a solicitação.
     */

    public void removerSolicitacaoEnviada(User user) {this.solicitacoesEnviadas.remove(user);}

    /**
     * Remove uma solicitação de amizade da lista de solicitações recebidas do User que chamou o metodo.
     *
     * @param user Usuário que não receberá mais a solicitação.
     */

    public void removerSolicitacaoRecebida(User user) {this.solicitacoesRecebidas.remove(user);}

    /**
     * Verifica se a senha que foi passada como parametro é igual à senha do User que chamou o metodo.
     *
     * @param senha  Senha a ser verificada.
     * @return       True se a senha for igual, false caso contrário.
     */

    public boolean verificarSenha(String senha) {return this.senha.equals(senha);}

    /**
     * Envia uma solicitação de amizade para o User passado como parâmetro.
     * Adiciona o User que foi passado como parametro à lista de solicitações enviadas e adiciona o User atual à lista de solicitações recebidas do User passado como parâmetro.
     *
     * @param user Usuário para o qual a solicitação será enviada.
     */

    public void enviarSolicitacao(User user) {
        this.solicitacoesEnviadas.add(user);
        user.solicitacoesRecebidas.add(this);
    }

    /**
     * Aceita uma solicitação de amizade.
     * Adiciona o User solicitante na lista de amigos do User que aceitará e remove o User solicitante da lista de solicitações recebidas do User que aceitará.
     *
     * @param user Use que enviou a solicitação.
     */

    public void aceitarSolicitacao(User user) {
        this.amigos.add(user);
        this.solicitacoesRecebidas.remove(user);
        user.amigos.add(this);
        user.solicitacoesEnviadas.remove(this);
    }

    /**
     * Retorna a primeira mensagem da fila de recados do User, e logo após o remove da fila de recados.
     *
     * @return A primeira mensagem da fila de recados do User.
     *
     * @throws SemRecadosException Exceção lançada caso o User não tenha recados na fila.
     */

    public Recado lerRecado() throws SemRecadosException {
        if (this.recados.isEmpty()) {throw new SemRecadosException();}

        return this.recados.poll();
    }

    /**
     * Adiciona um recado na fila de recados do User.
     *
     * @param recado Recado a ser adicionado.
     */

    public void receberRecado(Recado recado) {this.recados.add(recado);}

    /**
     * Recebe uma mensagem de uma comunidade.
     *
     * @param mensagem Mensagem a ser recebida.
     */

    public void receberMensagem(Mensagem mensagem) {this.mensagens.add(mensagem);}

    /**
     * Retorna a primeira mensagem da fila de mensagens do User, e logo após o remove da fila de mensagens.
     *
     * @return A primeira mensagem da fila de mensagens do User.
     *
     * @throws SemMensagensException Exceção lançada caso o User não tenha mensagens na fila.
     */

    public Mensagem lerMensagem() throws SemMensagensException {
        if (this.mensagens.isEmpty()) {throw new SemMensagensException();}

        return this.mensagens.poll();
    }

    /**
     * Remove uma comunidade da lista de comunidades em que o User faz parte.
     *
     * @param comunidade Comunidade em que o User não irá mais fazer parte.
     */

    public void sairComunidade(Comunidade comunidade) {this.comunidadesParticipantes.remove(comunidade);}

    /**
     * Retorna uma String que representa o User
     *
     * @return String que representa o User.
     */

    @Override
    public String toString() {return this.getLogin();}
}