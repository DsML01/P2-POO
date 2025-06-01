package br.ufal.ic.p2.jackut.App;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Recado.SemRecadosException;
import br.ufal.ic.p2.jackut.Servicos.JackutServicesFacade;
import br.ufal.ic.p2.jackut.Entidades.*;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.*;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.*;
import br.ufal.ic.p2.jackut.Exceptions.Recado.*;
import br.ufal.ic.p2.jackut.Exceptions.Perfil.*;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.*;
import br.ufal.ic.p2.jackut.Utilidade.*;

/**
 * Fachada principal do sistema Jackut que implementa todas as operações disponíveis para os usuários.
 * <p>
 * Esta classe atua como um ponto único de acesso para todas as funcionalidades do sistema,
 * delegando as operações para os serviços internos.
 * </p>
 *
 * @author Davi
 */
public class Facade {
    private final JackutServicesFacade jackutServicesFacade = new JackutServicesFacade();

    /**
     * Apaga todos os dados mantidos no sistema.
     */
    public void zerarSistema() {
        this.jackutServicesFacade.zerarSistema();
    }

    /**
     * Cria um novo usuário no sistema.
     *
     * @param login Login do novo usuário
     * @param senha Senha do novo usuário
     * @param nome Nome completo do usuário
     * @throws LoginOuSenhaInvalidoException Se login ou senha forem inválidos
     * @throws ContaJaExisteException Se o login já estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginOuSenhaInvalidoException, ContaJaExisteException {
        User user = new User(login, senha, nome);
        this.jackutServicesFacade.setUsuario(user);
    }

    /**
     * Autentica um usuário e inicia uma nova sessão.
     *
     * @param login Login do usuário
     * @param senha Senha do usuário
     * @return ID da sessão criada
     * @throws LoginOuSenhaInvalidoException Se as credenciais forem inválidas
     * @throws UsuarioNaoRegistradoException Se o usuário não existir
     */
    public String abrirSessao(String login, String senha)
            throws LoginOuSenhaInvalidoException, UsuarioNaoRegistradoException {
        return this.jackutServicesFacade.abrirSessao(login, senha);
    }

    /**
     * Obtém um atributo do perfil de um usuário.
     *
     * @param login Login do usuário
     * @param atributo Nome do atributo
     * @return Valor do atributo
     * @throws UsuarioNaoRegistradoException Se o usuário não existir
     * @throws AtributoNaoPreenchidoException Se o atributo não estiver definido
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoRegistradoException, AtributoNaoPreenchidoException {
        User user = this.jackutServicesFacade.getUsuario(login);
        return user.getAtributo(atributo);
    }

    /**
     * Modifica um atributo do perfil do usuário.
     *
     * @param id ID da sessão
     * @param atributo Nome do atributo
     * @param valor Novo valor
     * @throws UsuarioNaoRegistradoException Se a sessão for inválida
     */
    public void editarPerfil(String id, String atributo, String valor)
            throws UsuarioNaoRegistradoException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        user.getPerfil().setAtributo(atributo, valor);
    }

    /**
     * Adiciona um amigo ao usuário.
     *
     * @param id ID da sessão
     * @param amigo Login do amigo
     * @throws UsuarioJaTemRelacaoException Se já forem amigos
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     * @throws UsuarioRelacaoParaSiException Se tentar adicionar a si mesmo
     * @throws UsuarioJaPediuSolicitacaoException Se já houver solicitação pendente
     * @throws UsuarioEhInimigoException Se os usuários forem inimigos
     */
    public void adicionarAmigo(String id, String amigo)
            throws UsuarioJaTemRelacaoException, UsuarioNaoRegistradoException,
            UsuarioRelacaoParaSiException, UsuarioJaPediuSolicitacaoException, UsuarioEhInimigoException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        User amigoUser = this.jackutServicesFacade.getUsuario(amigo);
        this.jackutServicesFacade.adicionarAmigo(user, amigoUser);
    }

    /**
     * Verifica se dois usuários são amigos.
     *
     * @param login Login do primeiro usuário
     * @param amigo Login do segundo usuário
     * @return true se forem amigos
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     */
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoRegistradoException {
        User user = this.jackutServicesFacade.getUsuario(login);
        User amigoUser = this.jackutServicesFacade.getUsuario(amigo);
        return user.getAmigos().contains(amigoUser);
    }

    /**
     * Obtém a lista de amigos de um usuário.
     *
     * @param login Login do usuário
     * @return Lista de amigos formatada
     * @throws UsuarioNaoRegistradoException Se o usuário não existir
     */
    public String getAmigos(String login) throws UsuarioNaoRegistradoException {
        User user = this.jackutServicesFacade.getUsuario(login);
        return user.getAmigosString();
    }

    /**
     * Envia um recado para outro usuário.
     *
     * @param id ID da sessão
     * @param destinatario Login do destinatário
     * @param recado Texto do recado
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     * @throws MensagemParaSiException Se tentar enviar para si mesmo
     * @throws UsuarioEhInimigoException Se os usuários forem inimigos
     */
    public void enviarRecado(String id, String destinatario, String recado)
            throws UsuarioNaoRegistradoException, MensagemParaSiException, UsuarioEhInimigoException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        User destinatarioUser = this.jackutServicesFacade.getUsuario(destinatario);
        this.jackutServicesFacade.enviarRecado(user, destinatarioUser, recado);
    }

    /**
     * Lê o próximo recado na fila do usuário.
     *
     * @param id ID da sessão
     * @return Texto do recado
     * @throws UsuarioNaoRegistradoException Se a sessão for inválida
     * @throws SemRecadosException Se não houver recados
     */
    public String lerRecado(String id) throws UsuarioNaoRegistradoException, SemRecadosException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        return this.jackutServicesFacade.lerRecado(user);
    }

    /**
     * Cria uma nova comunidade.
     *
     * @param id ID da sessão
     * @param nome Nome da comunidade
     * @param descricao Descrição da comunidade
     * @throws UsuarioNaoRegistradoException Se a sessão for inválida
     * @throws ComunidadeJaExisteException Se a comunidade já existir
     */
    public void criarComunidade(String id, String nome, String descricao)
            throws UsuarioNaoRegistradoException, ComunidadeJaExisteException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        jackutServicesFacade.setComunidade(user, nome, descricao);
    }

    /**
     * Obtém a descrição de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Descrição da comunidade
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.jackutServicesFacade.getDescricaoComunidade(nome);
    }

    /**
     * Obtém o dono de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Login do dono
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.jackutServicesFacade.getDonoComunidade(nome);
    }

    /**
     * Obtém os membros de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Lista de membros formatada
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.jackutServicesFacade.getMembrosComunidade(nome);
    }

    /**
     * Obtém as comunidades de um usuário.
     *
     * @param login Login do usuário
     * @return Lista de comunidades formatada
     * @throws UsuarioNaoRegistradoException Se o usuário não existir
     */
    public String getComunidades(String login) throws UsuarioNaoRegistradoException {
        User user = this.jackutServicesFacade.getUsuario(login);
        return this.jackutServicesFacade.getComunidades(user);
    }

    /**
     * Adiciona um usuário a uma comunidade.
     *
     * @param id ID da sessão
     * @param nomeComunidade Nome da comunidade
     * @throws UsuarioNaoRegistradoException Se a sessão for inválida
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     * @throws UsuarioJaNaComunidadeException Se o usuário já for membro
     * @throws ModeradorException Se o usuário estiver banido
     */
    public void adicionarComunidade(String id, String nomeComunidade)
            throws UsuarioNaoRegistradoException, ComunidadeNaoExisteException,
            UsuarioJaNaComunidadeException, ModeradorException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        this.jackutServicesFacade.adicionarMembroComunidade(user, nomeComunidade);
    }

    /**
     * Bane um membro de uma comunidade.
     *
     * @param id ID da sessão
     * @param comunidade Nome da comunidade
     * @param loginMembro Login do membro a banir
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     * @throws ModeradorException Se não tiver permissão
     */
    public void banirMembroComunidade(String id, String comunidade, String loginMembro)
            throws UsuarioNaoRegistradoException, ComunidadeNaoExisteException, ModeradorException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        this.jackutServicesFacade.banirMembroComunidade(user, comunidade, loginMembro);
    }

    /**
     * Remove o banimento de um membro.
     *
     * @param id ID da sessão
     * @param comunidade Nome da comunidade
     * @param loginMembro Login do membro
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     * @throws ModeradorException Se não tiver permissão
     */
    public void desbanirMembroComunidade(String id, String comunidade, String loginMembro)
            throws UsuarioNaoRegistradoException, ComunidadeNaoExisteException, ModeradorException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        this.jackutServicesFacade.desbanirMembroComunidade(user, comunidade, loginMembro);
    }

    /**
     * Obtém os membros banidos de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Lista de banidos formatada
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    public String getMembrosBanidosComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.jackutServicesFacade.getMembrosBanidosComunidade(nome);
    }

    /**
     * Lê a próxima mensagem da comunidade.
     *
     * @param id ID da sessão
     * @return Texto da mensagem
     * @throws UsuarioNaoRegistradoException Se a sessão for inválida
     * @throws SemMensagensException Se não houver mensagens
     */
    public String lerMensagem(String id) throws UsuarioNaoRegistradoException, SemMensagensException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        return this.jackutServicesFacade.lerMensagem(user);
    }

    /**
     * Envia uma mensagem para uma comunidade.
     *
     * @param id ID da sessão
     * @param comunidade Nome da comunidade
     * @param mensagem Texto da mensagem
     * @throws UsuarioNaoRegistradoException Se a sessão for inválida
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    public void enviarMensagem(String id, String comunidade, String mensagem)
            throws UsuarioNaoRegistradoException, ComunidadeNaoExisteException {
        this.jackutServicesFacade.getSessaoUsuario(id);
        Comunidade comunidadeAlvo = this.jackutServicesFacade.getComunidade(comunidade);
        this.jackutServicesFacade.enviarMensagem(comunidadeAlvo, mensagem);
    }

    /**
     * Verifica se um usuário é fã de outro.
     *
     * @param login Login do usuário
     * @param loginIdolo Login do ídolo
     * @return true se for fã
     */
    public boolean ehFa(String login, String loginIdolo) {
        User user = this.jackutServicesFacade.getUsuario(login);
        User idolo = this.jackutServicesFacade.getUsuario(loginIdolo);
        return idolo.getFas().contains(user);
    }

    /**
     * Adiciona um ídolo ao usuário.
     *
     * @param id ID da sessão
     * @param loginIdolo Login do ídolo
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     * @throws UsuarioJaTemRelacaoException Se já for ídolo
     * @throws UsuarioRelacaoParaSiException Se tentar adicionar a si mesmo
     * @throws UsuarioEhInimigoException Se forem inimigos
     */
    public void adicionarIdolo(String id, String loginIdolo)
            throws UsuarioNaoRegistradoException, UsuarioJaTemRelacaoException,
            UsuarioRelacaoParaSiException, UsuarioEhInimigoException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        User idolo = this.jackutServicesFacade.getUsuario(loginIdolo);
        this.jackutServicesFacade.adicionarIdolo(user, idolo);
    }

    /**
     * Obtém os fãs de um usuário.
     *
     * @param login Login do usuário
     * @return Lista de fãs formatada
     * @throws UsuarioNaoRegistradoException Se o usuário não existir
     */
    public String getFas(String login) throws UsuarioNaoRegistradoException {
        User user = this.jackutServicesFacade.getUsuario(login);
        return this.jackutServicesFacade.getFas(user);
    }

    /**
     * Verifica se um usuário paquera outro.
     *
     * @param id ID da sessão
     * @param loginPaquera Login do paquerado
     * @return true se paquera
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     */
    public boolean ehPaquera(String id, String loginPaquera) throws UsuarioNaoRegistradoException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        User paquera = this.jackutServicesFacade.getUsuario(loginPaquera);
        return user.getPaqueras().contains(paquera);
    }

    /**
     * Adiciona um paquera ao usuário.
     *
     * @param id ID da sessão
     * @param loginPaquera Login do paquerado
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     * @throws UsuarioJaTemRelacaoException Se já paquera
     * @throws UsuarioRelacaoParaSiException Se tentar adicionar a si mesmo
     * @throws UsuarioEhInimigoException Se forem inimigos
     */
    public void adicionarPaquera(String id, String loginPaquera)
            throws UsuarioNaoRegistradoException, UsuarioJaTemRelacaoException,
            UsuarioRelacaoParaSiException, UsuarioEhInimigoException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        User paquera = this.jackutServicesFacade.getUsuario(loginPaquera);
        this.jackutServicesFacade.adicionarPaquera(user, paquera);
    }

    /**
     * Obtém os paqueras do usuário.
     *
     * @param id ID da sessão
     * @return Lista de paqueras formatada
     * @throws UsuarioNaoRegistradoException Se a sessão for inválida
     */
    public String getPaqueras(String id) throws UsuarioNaoRegistradoException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        return this.jackutServicesFacade.getPaqueras(user);
    }

    /**
     * Adiciona um inimigo ao usuário.
     *
     * @param id ID da sessão
     * @param loginInimigo Login do inimigo
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     * @throws UsuarioJaTemRelacaoException Se já for inimigo
     * @throws UsuarioRelacaoParaSiException Se tentar adicionar a si mesmo
     */
    public void adicionarInimigo(String id, String loginInimigo)
            throws UsuarioNaoRegistradoException, UsuarioJaTemRelacaoException, UsuarioRelacaoParaSiException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        User inimigo = this.jackutServicesFacade.getUsuario(loginInimigo);
        this.jackutServicesFacade.adicionarInimigo(user, inimigo);
    }

    /**
     * Remove um usuário do sistema.
     *
     * @param id ID da sessão
     * @throws UsuarioNaoRegistradoException Se a sessão for inválida
     */
    public void removerUsuario(String id) throws UsuarioNaoRegistradoException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        this.jackutServicesFacade.removerUsuario(user, id);
    }

    /**
     * Atribui moderador a um membro da comunidade.
     *
     * @param id ID da sessão
     * @param comunidade Nome da comunidade
     * @param loginModerador Login do novo moderador
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     * @throws ModeradorException Se não tiver permissão
     */
    public void atribuirModerador(String id, String comunidade, String loginModerador)
            throws UsuarioNaoRegistradoException, ComunidadeNaoExisteException, ModeradorException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        this.jackutServicesFacade.atribuirModerador(user, comunidade, loginModerador);
    }

    /**
     * Obtém os moderadores de uma comunidade.
     *
     * @param nome Nome da comunidade
     * @return Lista de moderadores formatada
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    public String getModeradoresComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.jackutServicesFacade.getModeradoresComunidade(nome);
    }

    /**
     * Expulsa um membro de uma comunidade.
     *
     * @param id ID da sessão
     * @param comunidade Nome da comunidade
     * @param loginMembro Login do membro
     * @throws UsuarioNaoRegistradoException Se algum usuário não existir
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     * @throws ModeradorException Se não tiver permissão
     */
    public void expulsarMembroComunidade(String id, String comunidade, String loginMembro)
            throws UsuarioNaoRegistradoException, ComunidadeNaoExisteException, ModeradorException {
        User user = this.jackutServicesFacade.getSessaoUsuario(id);
        this.jackutServicesFacade.expulsarMembroComunidade(user, comunidade, loginMembro);
    }

    /**
     * Salva os dados do sistema e encerra.
     */
    public void encerrarSistema() {
        this.jackutServicesFacade.encerrarSistema();
    }
}