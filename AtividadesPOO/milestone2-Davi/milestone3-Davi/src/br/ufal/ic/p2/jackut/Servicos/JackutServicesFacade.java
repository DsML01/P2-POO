package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;
import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.*;
import br.ufal.ic.p2.jackut.Exceptions.Recado.MensagemParaSiException;
import br.ufal.ic.p2.jackut.Exceptions.Recado.SemRecadosException;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.ContaJaExisteException;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.LoginOuSenhaInvalidoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.*;
import br.ufal.ic.p2.jackut.Utilidade.UtilidadeString;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Fachada principal para os serviços do sistema Jackut.
 * <p>
 * Esta classe atua como um ponto de entrada unificado para a lógica de negócios,
 * coordenando os diversos serviços (usuário, autenticação, comunidade, etc.)
 * para executar as operações solicitadas.
 *
 * @author Davi
 */
public class JackutServicesFacade {

    private final Map<String, User> usuariosData;
    private final Map<String, Comunidade> comunidadesData;
    private final Map<String, User> sessoesData;

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final CommunityService communityService;
    private final RelationshipService relationshipService;
    private final MessageService messageService;
    private final PersistenceService persistenceService;

    /**
     * Constrói a fachada de serviços.
     * Inicializa todos os serviços subjacentes e os mapas de dados,
     * e carrega os dados iniciais do sistema a partir da persistência.
     */
    public JackutServicesFacade() {
        this.usuariosData = new HashMap<>();
        this.comunidadesData = new HashMap<>();
        this.sessoesData = new HashMap<>();

        this.userService = new UserService(this.usuariosData);
        this.authenticationService = new AuthenticationService(this.sessoesData, this.userService);
        this.communityService = new CommunityService(this.comunidadesData, this.userService);
        this.relationshipService = new RelationshipService(this.userService);
        this.messageService = new MessageService();

        this.persistenceService = new PersistenceService(this.usuariosData, this.comunidadesData, this);
        this.persistenceService.carregarDadosIniciais();
    }

    public User getUsuario(String login) throws UsuarioNaoRegistradoException {
        return this.userService.getUsuarioPorLogin(login);
    }

    public User getSessaoUsuario(String id) throws UsuarioNaoRegistradoException {
        return this.authenticationService.getUsuarioDaSessao(id);
    }

    public Comunidade getComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.communityService.getComunidadePorNome(nome);
    }

    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getDono().getLogin();
    }

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getDescricao();
    }

    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getMembrosString();
    }

    public void atribuirModerador(User user, String nomeComunidade, String loginModerador)
            throws ComunidadeNaoExisteException, UsuarioNaoRegistradoException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        User moderador = this.userService.getUsuarioPorLogin(loginModerador);

        this.communityService.atribuirModerador(user, comunidade, moderador);
    }

    public String getModeradoresComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getModeradoresString();
    }

    public void expulsarMembroComunidade(User executor, String nomeComunidade, String loginMembro)
            throws ComunidadeNaoExisteException, UsuarioNaoRegistradoException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        User membro = this.userService.getUsuarioPorLogin(loginMembro);

        this.communityService.expulsarMembro(executor, comunidade, membro);
    }

    public String getComunidades(User user) {
        return UtilidadeString.formatArrayList(user.getComunidadesParticipantes());
    }

    public String getFas(User user) {
        return UtilidadeString.formatArrayList(user.getFas());
    }



    public String getPaqueras(User user) {
        return UtilidadeString.formatArrayList(user.getPaqueras());
    }

    /**
     * Adiciona um novo usuário ao sistema.
     * A lógica de verificação de duplicidade é delegada ao {@link UserService}.
     *
     * @param user O objeto {@link User} a ser registrado.
     * @throws ContaJaExisteException se um usuário com o mesmo login já existir.
     */
    public void setUsuario(User user) throws ContaJaExisteException {
        this.userService.registrarNovoUsuario(user);
    }

    public String abrirSessao(String login, String senha) throws LoginOuSenhaInvalidoException {
        return this.authenticationService.login(login, senha);
    }

    public void adicionarAmigo(User user, User amigo)
            throws UsuarioJaTemRelacaoException, UsuarioJaPediuSolicitacaoException, UsuarioRelacaoParaSiException,
            UsuarioEhInimigoException {
        this.relationshipService.solicitarOuConfirmarAmizade(user, amigo);
    }

    public void enviarRecado(User remetente, User destinatario, String recado) throws MensagemParaSiException, UsuarioEhInimigoException {
        this.relationshipService.verificarInimizade(remetente, destinatario);
        this.messageService.enviarNovoRecado(remetente, destinatario, recado);
    }

    public String lerRecado(User user) throws SemRecadosException {
        return this.messageService.lerProximoRecado(user);
    }

    public void setComunidade(User dono, String nome, String descricao) throws ComunidadeJaExisteException {
        this.communityService.registrarNovaComunidade(dono, nome, descricao);
    }

    public void carregarComunidade(Comunidade comunidade) {
        this.communityService.carregarComunidade(comunidade);
    }

//    public void adicionarMembroComunidade(User usuario, Comunidade comunidade)
//            throws UsuarioJaNaComunidadeException {
//
//        // Permite adicionar mesmo se já foi membro antes (para casos de retorno após expulsão)
//        if (comunidade.getMembros().contains(usuario) ||
//                usuario.getComunidadesParticipantes().contains(comunidade)) {
//            throw new UsuarioJaNaComunidadeException();
//        }
//
//        comunidade.adicionarMembro(usuario);
//        usuario.getComunidadesParticipantes().add(comunidade);
//    }

    public void adicionarMembroComunidade(User user, String nomeComunidade)
            throws ComunidadeNaoExisteException, UsuarioJaNaComunidadeException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        this.communityService.adicionarMembroComunidade(user, comunidade);
    }

    public String lerMensagem(User user) throws SemMensagensException {
        return this.messageService.lerProximaMensagemDeComunidade(user);
    }

    /**
     * Envia uma mensagem para todos os membros de uma comunidade.
     * Esta implementação passa um remetente nulo para o {@link MessageService}.
     * A camada de aplicação que consome esta fachada é responsável por obter o
     * usuário da sessão e, se necessário, passar o remetente explicitamente.
     *
     * @param comunidade A comunidade que receberá a mensagem.
     * @param msg        O conteúdo da mensagem.
     */
    public void enviarMensagem(Comunidade comunidade, String msg) {
        this.messageService.enviarNovaMensagemParaComunidade(null, comunidade, msg);
    }

    public void adicionarIdolo(User user, User idolo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        this.relationshipService.adicionarNovoIdolo(user, idolo);
    }

    public void adicionarPaquera(User user, User paquera)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        this.relationshipService.adicionarNovaPaquera(user, paquera, this.messageService);
    }

    public void adicionarInimigo(User user, User inimigo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException {
        this.relationshipService.adicionarNovoInimigo(user, inimigo);
    }

    /**
     * Remove um usuário e todos os seus dados associados do sistema.
     * A operação é executada em múltiplos passos para garantir a consistência dos dados:
     * <ol>
     * <li>Invalida todas as sessões ativas do usuário.</li>
     * <li>Remove todos os seus relacionamentos (amizades, fãs, etc.) com outros usuários.</li>
     * <li>Remove o usuário de todas as comunidades que participa e exclui as comunidades das quais ele é dono.</li>
     * <li>Remove todas as mensagens e recados enviados pelo usuário de outras caixas de entrada.</li>
     * <li>Exclui permanentemente o usuário do sistema.</li>
     * </ol>
     *
     * @param user     O usuário a ser removido.
     * @param idSessao O ID da sessão atual, que também será invalidada.
     */
    public void removerUsuario(User user, String idSessao) {
        this.authenticationService.invalidarTodasSessoesDeUsuario(user);
        this.authenticationService.invalidarSessaoPorId(idSessao);

        this.relationshipService.removerTodosRelacionamentosDe(user, this.userService.getAllUsers());

        for (Comunidade c : user.getComunidadesParticipantes()) {
            c.getMembros().remove(user);
        }
        user.getComunidadesParticipantes().clear();

        for (String nomeComunidade : this.communityService.getAllComunidades().stream()
                .filter(c -> c.getDono().equals(user))
                .map(Comunidade::getNome)
                .collect(Collectors.toList())) {
            try {
                this.communityService.removerComunidade(nomeComunidade);
            } catch (ComunidadeNaoExisteException e) { /* Ignora se já removida. */ }
        }

        this.messageService.removerTodasMensagensDeOuPara(user, this.userService.getAllUsers());

        this.userService.deletarUsuario(user);
    }

    public void banirMembroComunidade(User executor, String nomeComunidade, String loginMembro)
            throws ComunidadeNaoExisteException, UsuarioNaoRegistradoException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        User membro = this.userService.getUsuarioPorLogin(loginMembro);
        this.communityService.banirMembro(executor, comunidade, membro);
    }

    public void desbanirMembroComunidade(User executor, String nomeComunidade, String loginMembro)
            throws ComunidadeNaoExisteException, UsuarioNaoRegistradoException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        User membro = this.userService.getUsuarioPorLogin(loginMembro);
        this.communityService.desbanirMembro(executor, comunidade, membro);
    }

    public String getMembrosBanidosComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getMembrosBanidosString();
    }

    // Atualize o método adicionarMembroComunidade para verificar banimentos
    public void adicionarMembroComunidade(User usuario, Comunidade comunidade)
            throws UsuarioJaNaComunidadeException, ModeradorException {

        this.communityService.verificarBanimento(usuario, comunidade);
        this.communityService.adicionarMembroComunidade(usuario, comunidade);
    }

    /**
     * Limpa todos os dados do sistema.
     * Remove todos os usuários, comunidades e sessões da memória e da persistência.
     */
    public void zerarSistema() {
        this.usuariosData.clear();
        this.sessoesData.clear();
        this.comunidadesData.clear();
        this.persistenceService.limparDadosPersistidos();
    }

    /**
     * Encerra o sistema, salvando todos os dados atuais em arquivos de persistência.
     */
    public void encerrarSistema() {
        this.persistenceService.salvarDadosAtuais();
    }
}