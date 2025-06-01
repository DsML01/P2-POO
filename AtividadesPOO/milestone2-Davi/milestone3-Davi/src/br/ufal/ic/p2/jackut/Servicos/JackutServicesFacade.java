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

    /**
     * Obtém um usuário pelo login.
     *
     * @param login Login do usuário
     * @return Objeto User correspondente
     * @throws UsuarioNaoRegistradoException se o usuário não for encontrado
     */
    public User getUsuario(String login) throws UsuarioNaoRegistradoException {
        return this.userService.getUsuarioPorLogin(login);
    }

    /**
     * Obtém o usuário associado a uma sessão.
     *
     * @param id ID da sessão
     * @return Objeto User da sessão
     * @throws UsuarioNaoRegistradoException se a sessão não for válida
     */
    public User getSessaoUsuario(String id) throws UsuarioNaoRegistradoException {
        return this.authenticationService.getUsuarioDaSessao(id);
    }

    /**
     * Obtém uma comunidade pelo nome.
     *
     * @param nome Nome da comunidade
     * @return Objeto Comunidade correspondente
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     */
    public Comunidade getComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.communityService.getComunidadePorNome(nome);
    }

    /**
     * Obtém o dono de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Login do dono da comunidade
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     */
    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getDono().getLogin();
    }

    /**
     * Obtém a descrição de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Descrição da comunidade
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     */
    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getDescricao();
    }

    /**
     * Obtém a lista de membros de uma comunidade formatada como string.
     *
     * @param nome Nome da comunidade
     * @return String formatada com os membros
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     */
    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getMembrosString();
    }

    /**
     * Atribui um moderador a uma comunidade.
     *
     * @param user Usuário que está executando a ação
     * @param nomeComunidade Nome da comunidade
     * @param loginModerador Login do novo moderador
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     * @throws UsuarioNaoRegistradoException se o moderador não for encontrado
     * @throws ModeradorException se o usuário não tiver permissão
     */
    public void atribuirModerador(User user, String nomeComunidade, String loginModerador)
            throws ComunidadeNaoExisteException, UsuarioNaoRegistradoException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        User moderador = this.userService.getUsuarioPorLogin(loginModerador);

        this.communityService.atribuirModerador(user, comunidade, moderador);
    }

    /**
     * Obtém a lista de moderadores de uma comunidade formatada como string.
     *
     * @param nome Nome da comunidade
     * @return String formatada com os moderadores
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     */
    public String getModeradoresComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getModeradoresString();
    }

    /**
     * Expulsa um membro de uma comunidade.
     *
     * @param executor Usuário que está executando a ação
     * @param nomeComunidade Nome da comunidade
     * @param loginMembro Login do membro a ser expulso
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     * @throws UsuarioNaoRegistradoException se o membro não for encontrado
     * @throws ModeradorException se o executor não tiver permissão
     */
    public void expulsarMembroComunidade(User executor, String nomeComunidade, String loginMembro)
            throws ComunidadeNaoExisteException, UsuarioNaoRegistradoException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        User membro = this.userService.getUsuarioPorLogin(loginMembro);

        this.communityService.expulsarMembro(executor, comunidade, membro);
    }

    /**
     * Obtém a lista de comunidades de um usuário formatada como string.
     *
     * @param user Usuário
     * @return String formatada com as comunidades
     */
    public String getComunidades(User user) {
        return UtilidadeString.formatArrayList(user.getComunidadesParticipantes());
    }

    /**
     * Obtém a lista de fãs de um usuário formatada como string.
     *
     * @param user Usuário
     * @return String formatada com os fãs
     */
    public String getFas(User user) {
        return UtilidadeString.formatArrayList(user.getFas());
    }

    /**
     * Obtém a lista de paqueras de um usuário formatada como string.
     *
     * @param user Usuário
     * @return String formatada com as paqueras
     */
    public String getPaqueras(User user) {
        return UtilidadeString.formatArrayList(user.getPaqueras());
    }

    /**
     * Adiciona um novo usuário ao sistema.
     *
     * @param user Objeto User a ser registrado
     * @throws ContaJaExisteException se um usuário com o mesmo login já existir
     */
    public void setUsuario(User user) throws ContaJaExisteException {
        this.userService.registrarNovoUsuario(user);
    }

    /**
     * Abre uma nova sessão para um usuário.
     *
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return ID da nova sessão
     * @throws LoginOuSenhaInvalidoException se as credenciais forem inválidas
     */
    public String abrirSessao(String login, String senha) throws LoginOuSenhaInvalidoException {
        return this.authenticationService.login(login, senha);
    }

    /**
     * Adiciona um amigo para um usuário.
     *
     * @param user Usuário que está adicionando
     * @param amigo Amigo a ser adicionado
     * @throws UsuarioJaTemRelacaoException se já existir relação entre os usuários
     * @throws UsuarioJaPediuSolicitacaoException se já existir solicitação pendente
     * @throws UsuarioRelacaoParaSiException se tentar adicionar a si mesmo
     * @throws UsuarioEhInimigoException se o usuário for inimigo
     */
    public void adicionarAmigo(User user, User amigo)
            throws UsuarioJaTemRelacaoException, UsuarioJaPediuSolicitacaoException, UsuarioRelacaoParaSiException,
            UsuarioEhInimigoException {
        this.relationshipService.solicitarOuConfirmarAmizade(user, amigo);
    }

    /**
     * Envia um recado de um usuário para outro.
     *
     * @param remetente Usuário que está enviando
     * @param destinatario Usuário que está recebendo
     * @param recado Conteúdo do recado
     * @throws MensagemParaSiException se tentar enviar para si mesmo
     * @throws UsuarioEhInimigoException se os usuários forem inimigos
     */
    public void enviarRecado(User remetente, User destinatario, String recado) throws MensagemParaSiException, UsuarioEhInimigoException {
        this.relationshipService.verificarInimizade(remetente, destinatario);
        this.messageService.enviarNovoRecado(remetente, destinatario, recado);
    }

    /**
     * Lê o próximo recado na caixa de entrada de um usuário.
     *
     * @param user Usuário
     * @return Conteúdo do recado
     * @throws SemRecadosException se não houver recados
     */
    public String lerRecado(User user) throws SemRecadosException {
        return this.messageService.lerProximoRecado(user);
    }

    /**
     * Cria uma nova comunidade.
     *
     * @param dono Dono da comunidade
     * @param nome Nome da comunidade
     * @param descricao Descrição da comunidade
     * @throws ComunidadeJaExisteException se já existir comunidade com mesmo nome
     */
    public void setComunidade(User dono, String nome, String descricao) throws ComunidadeJaExisteException {
        this.communityService.registrarNovaComunidade(dono, nome, descricao);
    }

    /**
     * Carrega uma comunidade existente no sistema.
     *
     * @param comunidade Comunidade a ser carregada
     */
    public void carregarComunidade(Comunidade comunidade) {
        this.communityService.carregarComunidade(comunidade);
    }

    /**
     * Adiciona um membro a uma comunidade.
     *
     * @param user Usuário a ser adicionado
     * @param nomeComunidade Nome da comunidade
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     * @throws UsuarioJaNaComunidadeException se o usuário já for membro
     * @throws ModeradorException se o usuário estiver banido
     */
    public void adicionarMembroComunidade(User user, String nomeComunidade)
            throws ComunidadeNaoExisteException, UsuarioJaNaComunidadeException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        this.communityService.adicionarMembroComunidade(user, comunidade);
    }

    /**
     * Lê a próxima mensagem de comunidade de um usuário.
     *
     * @param user Usuário
     * @return Conteúdo da mensagem
     * @throws SemMensagensException se não houver mensagens
     */
    public String lerMensagem(User user) throws SemMensagensException {
        return this.messageService.lerProximaMensagemDeComunidade(user);
    }

    /**
     * Envia uma mensagem para todos os membros de uma comunidade.
     *
     * @param comunidade Comunidade que receberá a mensagem
     * @param msg Conteúdo da mensagem
     */
    public void enviarMensagem(Comunidade comunidade, String msg) {
        this.messageService.enviarNovaMensagemParaComunidade(null, comunidade, msg);
    }

    /**
     * Adiciona um ídolo para um usuário.
     *
     * @param user Usuário que está adicionando
     * @param idolo Ídolo a ser adicionado
     * @throws UsuarioRelacaoParaSiException se tentar adicionar a si mesmo
     * @throws UsuarioJaTemRelacaoException se já existir relação
     * @throws UsuarioEhInimigoException se o usuário for inimigo
     */
    public void adicionarIdolo(User user, User idolo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        this.relationshipService.adicionarNovoIdolo(user, idolo);
    }

    /**
     * Adiciona uma paquera para um usuário.
     *
     * @param user Usuário que está adicionando
     * @param paquera Paquera a ser adicionada
     * @throws UsuarioRelacaoParaSiException se tentar adicionar a si mesmo
     * @throws UsuarioJaTemRelacaoException se já existir relação
     * @throws UsuarioEhInimigoException se o usuário for inimigo
     */
    public void adicionarPaquera(User user, User paquera)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        this.relationshipService.adicionarNovaPaquera(user, paquera, this.messageService);
    }

    /**
     * Adiciona um inimigo para um usuário.
     *
     * @param user Usuário que está adicionando
     * @param inimigo Inimigo a ser adicionado
     * @throws UsuarioRelacaoParaSiException se tentar adicionar a si mesmo
     * @throws UsuarioJaTemRelacaoException se já existir relação
     */
    public void adicionarInimigo(User user, User inimigo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException {
        this.relationshipService.adicionarNovoInimigo(user, inimigo);
    }

    /**
     * Remove um usuário e todos os seus dados associados do sistema.
     *
     * @param user Usuário a ser removido
     * @param idSessao ID da sessão atual
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

    /**
     * Banir um membro de uma comunidade.
     *
     * @param executor Usuário que está executando a ação
     * @param nomeComunidade Nome da comunidade
     * @param loginMembro Login do membro a ser banido
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     * @throws UsuarioNaoRegistradoException se o membro não for encontrado
     * @throws ModeradorException se o executor não tiver permissão
     */
    public void banirMembroComunidade(User executor, String nomeComunidade, String loginMembro)
            throws ComunidadeNaoExisteException, UsuarioNaoRegistradoException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        User membro = this.userService.getUsuarioPorLogin(loginMembro);
        this.communityService.banirMembro(executor, comunidade, membro);
    }

    /**
     * Desbanir um membro de uma comunidade.
     *
     * @param executor Usuário que está executando a ação
     * @param nomeComunidade Nome da comunidade
     * @param loginMembro Login do membro a ser desbanido
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     * @throws UsuarioNaoRegistradoException se o membro não for encontrado
     * @throws ModeradorException se o executor não tiver permissão
     */
    public void desbanirMembroComunidade(User executor, String nomeComunidade, String loginMembro)
            throws ComunidadeNaoExisteException, UsuarioNaoRegistradoException, ModeradorException {

        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        User membro = this.userService.getUsuarioPorLogin(loginMembro);
        this.communityService.desbanirMembro(executor, comunidade, membro);
    }

    /**
     * Obtém a lista de membros banidos de uma comunidade formatada como string.
     *
     * @param nome Nome da comunidade
     * @return String formatada com os membros banidos
     * @throws ComunidadeNaoExisteException se a comunidade não for encontrada
     */
    public String getMembrosBanidosComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nome);
        return comunidade.getMembrosBanidosString();
    }

    /**
     * Limpa todos os dados do sistema.
     */
    public void zerarSistema() {
        this.usuariosData.clear();
        this.sessoesData.clear();
        this.comunidadesData.clear();
        this.persistenceService.limparDadosPersistidos();
    }

    /**
     * Encerra o sistema, salvando todos os dados atuais.
     */
    public void encerrarSistema() {
        this.persistenceService.salvarDadosAtuais();
    }
}