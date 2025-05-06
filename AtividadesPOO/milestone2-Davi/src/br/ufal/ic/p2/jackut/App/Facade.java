package br.ufal.ic.p2.jackut.App;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Recado.SemRecadosException;
import br.ufal.ic.p2.jackut.Servicos.SessionService;
import br.ufal.ic.p2.jackut.Entidades.*;

import br.ufal.ic.p2.jackut.Exceptions.Usuario.*;
import br.ufal.ic.p2.jackut.Exceptions.Sistema.*;
import br.ufal.ic.p2.jackut.Exceptions.Recado.*;
import br.ufal.ic.p2.jackut.Exceptions.Perfil.*;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.*;

import br.ufal.ic.p2.jackut.Utilidade.*;


/**
 * Classe Facade que implementa a interface do sistema.
 */

public class Facade {
    private final SessionService sessionService = new SessionService();

    /**
     * Apaga todos os dados mantidos no sistema.
     *
     * @see SessionService
     */

    public void zerarSistema() {
        this.sessionService.zerarSistema();
    }

    /**
     * Cria um usuário com os dados da conta fornecidos.
     *
     * @param login  Login do usuário
     * @param senha  Senha do usuário
     * @param nome   Nome do usuário
     *
     * @throws LoginOuSenhaInvalidoException  Exceção lançada caso o login ou a senha sejam inválidos
     * @throws ContaJaExisteException        Exceção lançada caso o login já esteja cadastrado
     * 
     * @see User
     */

    public void criarUsuario(String login, String senha, String nome) throws LoginOuSenhaInvalidoException, ContaJaExisteException {
        User user = new User(login, senha, nome);

        this.sessionService.setUsuario(user);
    }

    /**
     * Abre uma sessão para um usuário com o login e a senha fornecidos,
     * e retorna uma id para esta sessão.
     *
     * @param login  Login do usuário
     * @param senha  Senha do usuário
     * @return       ID da sessão
     *
     * @throws LoginOuSenhaInvalidoException   Exceção lançada caso o login ou a senha sejam inválidos
     * @throws UsuarioNaoRegistradoException  Exceção lançada caso o usuário não esteja cadastrado
     */

    public String abrirSessao(String login, String senha) throws LoginOuSenhaInvalidoException, UsuarioNaoRegistradoException {
        return this.sessionService.abrirSessao(login, senha);
    }

    /**
     * Retorna o valor do atributo de um usuário, armazenado em seu perfil.
     *
     * @param login     Login do usuário
     * @param atributo  Atributo a ser retornado
     * @return          Valor do atributo
     *
     * @throws UsuarioNaoRegistradoException   Exceção lançada caso o usuário não esteja cadastrado
     * @throws AtributoNaoPreenchidoException  Exceção lançada caso o atributo não esteja preenchido
     */

    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoRegistradoException, AtributoNaoPreenchidoException {
        User user = this.sessionService.getUsuario(login);

        return user.getAtributo(atributo);
    }

    /**
     * Modifica o valor de um atributo do perfil de um usuário para o valor especificado.
     * Uma sessão válida <b>(identificada por id)</b> deve estar aberta para o usuário
     * cujo perfil se quer editar
     *
     * @param id        ID da sessão
     * @param atributo  Atributo a ser modificado
     * @param valor     Novo valor do atributo
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário não esteja cadastrado
     */

    public void editarPerfil(String id, String atributo, String valor)
            throws UsuarioNaoRegistradoException {
        User user = this.sessionService.getSessaoUsuario(id);

        user.getPerfil().setAtributo(atributo, valor);
    }

    /**
     * Adiciona um amigo ao usuário aberto na sessão especificada através de id.
     *
     * @param id     ID da sessão
     * @param amigo  Login do amigo a ser adicionado
     *
     * @throws UsuarioJaTemRelacaoException        Exceção lançada caso o usuário já seja amigo do usuário aberto na sessão
     * @throws UsuarioNaoRegistradoException       Exceção lançada caso o usuário ou o amigo não estejam cadastrados
     * @throws UsuarioRelacaoParaSiException         Exceção lançada caso o usuário tente adicionar a si mesmo como amigo
     * @throws UsuarioJaPediuSolicitacaoException  Exceção lançada caso o usuário já tenha solicitado amizade ao amigo
     */

    public void adicionarAmigo(String id, String amigo) throws UsuarioJaTemRelacaoException, UsuarioNaoRegistradoException,
            UsuarioRelacaoParaSiException, UsuarioJaPediuSolicitacaoException, UsuarioEhInimigoException {
        User user = this.sessionService.getSessaoUsuario(id);
        User amigoUser = this.sessionService.getUsuario(amigo);

        this.sessionService.adicionarAmigo(user, amigoUser);
    }

    /**
     * Retorna true se os dois usuários são amigos.
     *
     * @param login   Login do primeiro usuário
     * @param amigo   Login do segundo usuário
     * @return        Booleano indicando se os usuários são amigos
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso um dos usuários não esteja cadastrado
     */

    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoRegistradoException {
        User user = this.sessionService.getUsuario(login);
        User amigoUser = this.sessionService.getUsuario(amigo);

        return user.getAmigos().contains(amigoUser);
    }

    /**
     * Retorna a lista de amigos do usuário especificado.
     *
     * @param login  Login do usuário
     * @return       Lista de amigos do usuário formatada em uma String
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário não esteja cadastrado
     *
     * @see UtilidadeString
     */

    public String getAmigos(String login) throws UsuarioNaoRegistradoException {
        User user = this.sessionService.getUsuario(login);

        return user.getAmigosString();
    }

    /**
     * Envia o recado especificado ao destinatário especificado.
     * Uma sessão válida <b>(identificada por id)</b> deve estar aberta
     * para o usuário que deseja enviar o recado.
     *
     * @param id            ID da sessão
     * @param destinatario  Login do destinatário
     * @param recado        Recado a ser enviado
     *
     * @throws UsuarioNaoRegistradoException    Exceção lançada caso o usuário ou o destinatário não estejam cadastrados
     * @throws MensagemParaSiException  Exceção lançada caso o usuário tente enviar um recado para si mesmo
     */

    public void enviarRecado(String id, String destinatario, String recado) throws UsuarioNaoRegistradoException, MensagemParaSiException, UsuarioEhInimigoException {
        User user = this.sessionService.getSessaoUsuario(id);
        User destinatarioUser = this.sessionService.getUsuario(destinatario);

        this.sessionService.enviarRecado(user, destinatarioUser, recado);
    }

    /**
     * Retorna o primeiro recado da fila de recados do usuário com a sessão aberta
     * identificada por id.
     *
     * @param id  ID da sessão
     * @return    Primeiro recado da fila de recados do usuário
     *
     * @throws UsuarioNaoRegistradoException  Exceção lançada caso o usuário não esteja cadastrado
     * @throws SemRecadosException          Exceção lançada caso o usuário não tenha recados na fila
     */

    public String lerRecado(String id) throws UsuarioNaoRegistradoException, SemRecadosException {
        User user = this.sessionService.getSessaoUsuario(id);

        return this.sessionService.lerRecado(user);
    }

    /**
     * Cria uma comunidade com os dados fornecidos.
     *
     * @param id         ID da sessão
     * @param nome       Nome da comunidade
     * @param descricao  Descrição da comunidade
     *
     * @throws UsuarioNaoRegistradoException  Exceção lançada caso o usuário não esteja cadastrado
     * @throws ComunidadeJaExisteException    Exceção lançada caso a comunidade já exista
     */

    public void criarComunidade(String id, String nome, String descricao)
            throws UsuarioNaoRegistradoException, ComunidadeJaExisteException {
        User user = this.sessionService.getSessaoUsuario(id);

        sessionService.criarComunidade(user, nome, descricao);
    }

    /**
     * Retorna a descrição da comunidade especificada.
     *
     * @param nome  Nome da comunidade
     * @return      Descrição da comunidade
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista
     */

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.sessionService.getDescricaoComunidade(nome);
    }

    /**
     * Retorna o dono da comunidade especificada.
     *
     * @param nome  Nome da comunidade
     * @return      Dono da comunidade
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista
     */

    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.sessionService.getDonoComunidade(nome);
    }

    /**
     * Retorna a lista de membros da comunidade especificada.
     *
     * @param nome  Nome da comunidade
     * @return      Lista de membros da comunidade formatada em uma String
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista
     *
     * @see UtilidadeString
     */

    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        return this.sessionService.getMembrosComunidade(nome);
    }

    /**
     * Retorna a lista de comunidades do usuário especificado.
     *
     * @param login  Login do usuário
     * @return       Lista de comunidades do usuário formatada em uma String
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário não esteja cadastrado
     *
     * @see UtilidadeString
     */

    public String getComunidades(String login) throws UsuarioNaoRegistradoException {
        User user = this.sessionService.getUsuario(login);

        return this.sessionService.getComunidades(user);
    }

    /**
     * Adiciona o usuário com a sessão aberta identificada por id à comunidade especificada.
     *
     * @param id    ID da sessão
     * @param nome  Nome da comunidade
     *
     * @throws UsuarioNaoRegistradoException       Exceção lançada caso o usuário não esteja cadastrado
     * @throws ComunidadeNaoExisteException        Exceção lançada caso a comunidade não exista
     * @throws UsuarioJaNaComunidadeException  Exceção lançada caso o usuário já esteja na comunidade
     */

    public void adicionarComunidade(String id, String nome)
            throws UsuarioNaoRegistradoException, ComunidadeNaoExisteException, UsuarioJaNaComunidadeException {
        User user = this.sessionService.getSessaoUsuario(id);

        this.sessionService.adicionarComunidade(user, nome);
    }

    /**
     * Lê a primeira mensagem da fila de mensagens do usuário com a sessão aberta.
     *
     * @param id  ID da sessão
     * @return    Primeira mensagem da fila de mensagens do usuário
     *
     * @throws UsuarioNaoRegistradoException  Exceção lançada caso o usuário não esteja cadastrado
     * @throws SemMensagensException        Exceção lançada caso o usuário não tenha mensagens na fila
     */

    public String lerMensagem(String id) throws UsuarioNaoRegistradoException, SemMensagensException {
        User user = this.sessionService.getSessaoUsuario(id);

        return this.sessionService.lerMensagem(user);
    }

    /**
     * Envia uma mensagem de um usuário com sessão aberta à comunidade especificada.
     *
     * @param id          ID da sessão
     * @param comunidade  Nome da comunidade
     * @param mensagem    Mensagem a ser enviada
     *
     * @throws UsuarioNaoRegistradoException  Exceção lançada caso o usuário não esteja cadastrado
     * @throws ComunidadeNaoExisteException   Exceção lançada caso a comunidade não exista
     */

    public void enviarMensagem(String id, String comunidade, String mensagem)
            throws UsuarioNaoRegistradoException, ComunidadeNaoExisteException {
        this.sessionService.getSessaoUsuario(id);
        Comunidade comunidadeAlvo = this.sessionService.getComunidade(comunidade);

        this.sessionService.enviarMensagem(comunidadeAlvo, mensagem);
    }

    /**
     * Retorna true se o usuário com a sessão aberta identificada por id é fã do usuário especificado.
     *
     * @param login       Login do usuário
     * @param loginIdolo  Login do ídolo
     * @return            Booleano indicando se o usuário é fã do ídolo
     */
    public boolean ehFa(String login, String loginIdolo) {
        User user = this.sessionService.getUsuario(login);
        User idolo = this.sessionService.getUsuario(loginIdolo);

        return idolo.getFas().contains(user);
    }

    /**
     * Adiciona um ídolo ao usuário com a sessão aberta identificada por id.
     *
     * @param id          ID da sessão
     * @param loginIdolo  Login do ídolo
     *
     * @throws UsuarioNaoRegistradoException  Exceção lançada caso o usuário ou o ídolo não estejam cadastrados
     * @throws UsuarioJaTemRelacaoException   Exceção lançada caso o usuário já seja ídolo do ídolo
     * @throws UsuarioRelacaoParaSiException    Exceção lançada caso o usuário tente adicionar a si mesmo como ídolo
     */

    public void adicionarIdolo (String id, String loginIdolo)
            throws UsuarioNaoRegistradoException, UsuarioJaTemRelacaoException, UsuarioRelacaoParaSiException, UsuarioEhInimigoException {
        User user = this.sessionService.getSessaoUsuario(id);
        User idolo = this.sessionService.getUsuario(loginIdolo);

        this.sessionService.adicionarIdolo(user, idolo);
    }

    /**
     * Retorna a lista de fãs do usuário com a sessão aberta identificada por id.
     *
     * @param login  ID da sessão
     * @return       Lista de ídolos do usuário formatada em uma {@code String}
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário não esteja cadastrado
     *
     * @see UtilidadeString
     */

    public String getFas(String login) throws UsuarioNaoRegistradoException {
        User user = this.sessionService.getUsuario(login);

        return this.sessionService.getFas(user);
    }

    /**
     * Retorna um booleano indicando se o usuário com a sessão aberta identificada por id paquera o usuário que passamos ou nao.
     *
     * @param id            ID da sessão
     * @param loginPaquera  Login do usuário paquerado
     * @return         Booleano indicando se o usuário paquera o usuário especificado
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário ou o paquera não estejam cadastrados
     */

    public boolean ehPaquera(String id, String loginPaquera) throws UsuarioNaoRegistradoException {
        User user = this.sessionService.getSessaoUsuario(id);
        User paquera = this.sessionService.getUsuario(loginPaquera);

        return user.getPaqueras().contains(paquera);
    }

    /**
     * Adiciona um paquera ao usuário com a sessão aberta identificada por id.
     *
     * @param id            ID da sessão
     * @param loginPaquera  Login do usuário paquerado
     *
     * @throws UsuarioNaoRegistradoException  Exceção lançada caso o usuário ou o paquera não estejam cadastrados
     * @throws UsuarioJaTemRelacaoException   Exceção lançada caso o usuário já seja paquera do paquera
     * @throws UsuarioRelacaoParaSiException    Exceção lançada caso o usuário tente adicionar a si mesmo como paquera
     * @throws UsuarioEhInimigoException      Exceção lançada caso o usuário tente adicionar um inimigo como paquera
     */

    public void adicionarPaquera (String id, String loginPaquera)
            throws UsuarioNaoRegistradoException, UsuarioJaTemRelacaoException, UsuarioRelacaoParaSiException, UsuarioEhInimigoException {
        User user = this.sessionService.getSessaoUsuario(id);
        User paquera = this.sessionService.getUsuario(loginPaquera);

        this.sessionService.adicionarPaquera(user, paquera);
    }

    /**
     * Retorna a lista de paqueras do usuário com a sessão aberta identificada por id.
     *
     * @param id  ID da sessão
     * @return    Lista de paqueras do usuário
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário não esteja cadastrado
     *
     * @see UtilidadeString
     */

    public String getPaqueras(String id) throws UsuarioNaoRegistradoException {
        User user = this.sessionService.getSessaoUsuario(id);

        return this.sessionService.getPaqueras(user);
    }

    /**
     * Adiciona um inimigo ao usuário com a sessão aberta identificada por id.
     *
     * @param id            ID da sessão
     * @param loginInimigo  Login do usuário inimigo
     *
     * @throws UsuarioNaoRegistradoException  Exceção lançada caso o usuário ou o inimigo não estejam cadastrados
     * @throws UsuarioJaTemRelacaoException   Exceção lançada caso o usuário já seja inimigo do inimigo
     * @throws UsuarioRelacaoParaSiException    Exceção lançada caso o usuário tente adicionar a si mesmo como inimigo
     */

    public void adicionarInimigo(String id, String loginInimigo)
            throws UsuarioNaoRegistradoException, UsuarioJaTemRelacaoException, UsuarioRelacaoParaSiException {
        User user = this.sessionService.getSessaoUsuario(id);
        User inimigo = this.sessionService.getUsuario(loginInimigo);

        this.sessionService.adicionarInimigo(user, inimigo);
    }

    /**
     * Deleta um usuário do sistema
     *
     * @param id ID da sessão
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário não esteja cadastrado
     */

    public void removerUsuario(String id) throws UsuarioNaoRegistradoException {
        User user = this.sessionService.getSessaoUsuario(id);

        this.sessionService.removerUsuario(user, id);
    }

    /**
     * Grava o cadastro em arquivo e encerra o programa.
     * Salva usuários cadastrados.
     */

    public void encerrarSistema() {
        this.sessionService.encerrarSistema();
    }
}
