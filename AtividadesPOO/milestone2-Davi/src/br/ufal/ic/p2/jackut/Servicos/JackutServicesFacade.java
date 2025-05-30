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
// Removido UUID daqui, pois AuthenticationService irá gerenciá-lo
// Removido Entidades.Mensagem e Entidades.Recado, pois os serviços as usarão internamente


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

    public JackutServicesFacade() {
        this.usuariosData = new HashMap<>();
        this.comunidadesData = new HashMap<>();
        this.sessoesData = new HashMap<>();

        this.userService = new UserService(this.usuariosData);
        this.authenticationService = new AuthenticationService(this.sessoesData, this.userService);
        this.communityService = new CommunityService(this.comunidadesData, this.userService);
        this.relationshipService = new RelationshipService(this.userService);
        this.messageService = new MessageService(); // Se precisar de outros serviços, injete-os

        this.persistenceService = new PersistenceService(this.usuariosData, this.comunidadesData, this);
        this.persistenceService.carregarDadosIniciais();
    }

    // MÉTODOS PÚBLICOS DA FACADE (DELEGANDO AOS SERVIÇOS)

    // --- GETTERS DE DADOS BRUTOS (geralmente via UserService ou CommunityService) ---
    public User getUsuario(String login) throws UsuarioNaoRegistradoException {
        return this.userService.getUsuarioPorLogin(login);
    }

    public User getSessaoUsuario(String id) throws UsuarioNaoRegistradoException {
        return this.authenticationService.getUsuarioDaSessao(id);
    }

    public Comunidade getComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.communityService.getComunidadePorNome(nome);
    }

    // --- MÉTODOS QUE RETORNAM STRINGS FORMATADAS (podem continuar aqui ou mover para App.Facade) ---
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

    public String getComunidades(User user) { // Recebe User, então não precisa buscar
        return UtilidadeString.formatArrayList(user.getComunidadesParticipantes());
    }

    public String getFas(User user) { // Recebe User
        return UtilidadeString.formatArrayList(user.getFas());
    }

    public String getPaqueras(User user) { // Recebe User
        return UtilidadeString.formatArrayList(user.getPaqueras());
    }

    // --- OPERAÇÕES DE NEGÓCIO (DELEGADAS) ---

    /**
     * Adiciona um usuário ao sistema. Este método é chamado pela App.Facade,
     * que já instanciou o objeto User.
     * A lógica de negócio de registro (ex: checar se já existe) fica no UserService.
     *
     * @param user Usuário a ser adicionado.
     * @throws ContaJaExisteException Exceção lançada caso o login do usuário já esteja cadastrado.
     */
    public void setUsuario(User user) throws ContaJaExisteException {
        this.userService.registrarNovoUsuario(user);
    }

    public String abrirSessao(String login, String senha) throws LoginOuSenhaInvalidoException, UsuarioNaoRegistradoException {
        return this.authenticationService.login(login, senha);
    }

    public void adicionarAmigo(User user, User amigo)
            throws UsuarioJaTemRelacaoException, UsuarioJaPediuSolicitacaoException, UsuarioRelacaoParaSiException,
            UsuarioEhInimigoException {
        this.relationshipService.solicitarOuConfirmarAmizade(user, amigo);
    }

    public void enviarRecado(User remetente, User destinatario, String recado) throws MensagemParaSiException, UsuarioEhInimigoException {
        this.relationshipService.verificarInimizade(remetente, destinatario); // Regra de negócio antes
        this.messageService.enviarNovoRecado(remetente, destinatario, recado);
    }

    public String lerRecado(User user) throws SemRecadosException {
        return this.messageService.lerProximoRecado(user);
    }

    public void setComunidade(User dono, String nome, String descricao) throws ComunidadeJaExisteException {
        this.communityService.registrarNovaComunidade(dono, nome, descricao);
    }

    public void adicionarComunidade(User user, String nomeComunidade)
            throws ComunidadeNaoExisteException, UsuarioJaNaComunidadeException, UsuarioNaoRegistradoException {
        Comunidade comunidade = this.communityService.getComunidadePorNome(nomeComunidade);
        this.communityService.adicionarMembroComunidade(user, comunidade);
    }

    public String lerMensagem(User user) throws SemMensagensException {
        return this.messageService.lerProximaMensagemDeComunidade(user);
    }

    public void enviarMensagem(Comunidade comunidade, String msg) {
        // Para enviar mensagem para uma comunidade, precisamos do remetente.
        // A assinatura original não tinha. Assumindo que a App.Facade obterá o remetente da sessão.
        // Se a JackutServicesFacade for chamada pela App.Facade que já tem o User remetente:
        // public void enviarMensagem(User remetente, Comunidade comunidade, String msg)
        this.messageService.enviarNovaMensagemParaComunidade(null, comunidade, msg); // REMETENTE PRECISA SER PASSADO AQUI
        // Se a sua App.Facade que chama este método já tem o User remetente, ajuste a assinatura aqui
        // e no MessageService para incluir o User remetente.
        // Por ex: this.messageService.enviarNovaMensagemParaComunidade(remetenteObtidoDaSessao, comunidade, msg);
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

    // O método verificarInimigo agora é privado no RelationshipService e usado internamente lá.
    // Se a Facade precisar expor essa verificação, ela pode chamar o RelationshipService.
    // public void verificarInimigo(User user, User inimigo) throws UsuarioEhInimigoException {

    public void removerUsuario(User user, String idSessao) throws UsuarioNaoRegistradoException {
        // 1. Invalidar todas as sessões do usuário (se houver outras além da atual)
        this.authenticationService.invalidarTodasSessoesDeUsuario(user);
        this.authenticationService.invalidarSessaoPorId(idSessao); // Garante que a sessão atual foi removida

        // 2. Remover de relacionamentos
        this.relationshipService.removerTodosRelacionamentosDe(user, this.userService.getAllUsers());

        // 3. Remover de comunidades e, se dono, remover comunidades
        //   3a. Sair de todas as comunidades que participa
        for (Comunidade c : user.getComunidadesParticipantes()) { // Usa cópia para evitar ConcurrentModificationException
            c.getMembros().remove(user); // Remove da lista de membros da comunidade
        }
        user.getComunidadesParticipantes().clear(); // Limpa a lista do usuário

        //   3b. Remover comunidades das quais é dono
        //      Iterar sobre uma cópia dos nomes das comunidades para evitar ConcurrentModificationException
        for (String nomeComunidade : this.communityService.getAllComunidades().stream()
                .filter(c -> c.getDono().equals(user))
                .map(Comunidade::getNome)
                .collect(Collectors.toList())) {
            try {
                this.communityService.removerComunidade(nomeComunidade);
            } catch (ComunidadeNaoExisteException e) { /* Ignorar, já pode ter sido removida */ }
        }

        // 4. Remover mensagens/recados (Esta parte é complexa e pode precisar de mais detalhes no MessageService)
        this.messageService.removerTodasMensagensDeOuPara(user, this.userService.getAllUsers());

        // 5. Remover o usuário do sistema
        this.userService.deletarUsuario(user);
    }
    //    this.relationshipService.verificarInimizade(user, inimigo);
    // }

    public void zerarSistema() {
        this.usuariosData.clear();
        this.sessoesData.clear();
        this.comunidadesData.clear();
        this.persistenceService.limparDadosPersistidos();
    }

    public void encerrarSistema() {
        this.persistenceService.salvarDadosAtuais();
    }
}