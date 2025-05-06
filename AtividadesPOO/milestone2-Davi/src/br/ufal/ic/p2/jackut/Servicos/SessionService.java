package br.ufal.ic.p2.jackut.Servicos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Recado.SemRecadosException;
import br.ufal.ic.p2.jackut.Utilidade.*;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;
import br.ufal.ic.p2.jackut.Entidades.Mensagem;
import br.ufal.ic.p2.jackut.Entidades.Recado;

import br.ufal.ic.p2.jackut.Exceptions.Sistema.*;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.*;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.*;
import br.ufal.ic.p2.jackut.Exceptions.Recado.*;


/**
 * Classe que representa o sistema.
 */

public class SessionService {
    private Map<String, User> usuarios = new HashMap<>();
    private Map<String, User> sessoes = new HashMap<>();
    private Map<String, Comunidade> comunidades = new HashMap<>();

    /**
     * Constrói um novo Sistema responsável por gerenciar os usuários e as sessões.
     *
     * Inicializa as listas de usuários e sessões.
     *
     * @see User
     */

    public SessionService() {
        EscritaDeArquivos.criarPasta();

        LeituraDeArquivos.lerArquivos(this);
    }

    /**
     * Retorna um usuário do sistema pelo login do mesmo.
     *
     * @param login Login do usuário.
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário não esteja cadastrado.
     *
     * @see User
     */

    public User getUsuario(String login) throws UsuarioNaoRegistradoException {
        if (!this.usuarios.containsKey(login)) {
            throw new UsuarioNaoRegistradoException();
        }

        return this.usuarios.get(login);
    }

    /**
     * Retorna um usuário logado do sistema pelo id da sua sessão.
     *
     * @param id ID da sessão do usuário.
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário não esteja cadastrado.
     *
     * @see User
     */

    public User getSessaoUsuario(String id) throws UsuarioNaoRegistradoException {
        if (!this.sessoes.containsKey(id)) {
            throw new UsuarioNaoRegistradoException();
        }

        return this.sessoes.get(id);
    }


    /**
     * Retorna a comunidade do sistema pelo nome.
     *
     * @param nome  Nome da comunidade.
     * @return      Comunidade com o nome especificado.
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista.
     */

    public Comunidade getComunidade(String nome) throws ComunidadeNaoExisteException {
        if (!this.comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }

        return this.comunidades.get(nome);
    }

    /**
     * Retorna o dono da comunidade pelo nome da mesma.
     *
     * @param nome  Nome da comunidade.
     * @return      Dono da comunidade com o nome especificado.
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista.
     */

    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.getComunidade(nome);

        return comunidade.getDono().getLogin();
    }

    /**
     * Retorna uma comunidade do sistema pelo nome.
     *
     * @param nome  Nome da comunidade.
     * @return      Comunidade com o nome especificado.
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista.
     */

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.getComunidade(nome);

        return comunidade.getDescricao();
    }

    /**
     * Retorna os membros da comunidade em formato de String.
     *
     * @param nome  Nome da comunidade.
     * @return      Membros da comunidade com o nome especificado.
     *
     * @throws ComunidadeNaoExisteException Exceção lançada caso a comunidade não exista.
     */

    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        Comunidade comunidade = this.getComunidade(nome);

        return comunidade.getMembrosString();
    }

    /**
     * Retorna as comunidades das quais o usuário é membro em formato de String.
     *
     * @param user  O usuário do qual as comunidades serão retornadas.
     * @return         Comunidades das quais o usuário é membro.
     */

    public String getComunidades(User user) {
        return UtilidadeString.formatArrayList(user.getComunidadesParticipantes());
    }

    /**
     * Retorna os fãs do usuário formatados em uma String.
     *
     * @param user  O usuário do qual os fãs serão retornados.
     * @return         Fãs do usuário.
     *
     * @see UtilidadeString
     */

    public String getFas(User user) {
        return UtilidadeString.formatArrayList(user.getFas());
    }

    /**
     * Retorna os paqueras do usuário formatados como uma String.
     *
     * @param user  O usuário do qual os paqueras serão retornados.
     * @return         Paqueras do usuário.
     *
     * @see UtilidadeString
     */

    public String getPaqueras(User user) {
        return UtilidadeString.formatArrayList(user.getPaqueras());
    }

    /**
     * Adiciona um usuário ao sistema.
     *
     * @param user Usuário a ser adicionado.
     *
     * @throws ContaJaExisteException Exceção lançada caso o usuário já esteja cadastrado.
     *
     * @see User
     */

    public void setUsuario(User user) throws ContaJaExisteException {
        String login = user.getLogin();

        if (this.usuarios.containsKey(login)) {
            throw new ContaJaExisteException();
        }

        this.usuarios.put(login, user);
    }

    /**
     * Loga um usuário no sistema, criando seu id de sessão e retornando-o.
     *
     * @param login  Login do usuário.
     * @param senha  Senha do usuário.
     * @return       ID da sessão do usuário.
     *
     * @throws LoginOuSenhaInvalidoException Exceção lançada caso o login ou a senha sejam inválidos.
     *
     * @see User
     */

    public String abrirSessao(String login, String senha) throws LoginOuSenhaInvalidoException {
        try{
            User user = this.getUsuario(login);

            if (!user.verificarSenha(senha)) {
                throw new LoginOuSenhaInvalidoException("any");
            }

            String id = UUID.randomUUID().toString();

            this.sessoes.put(id, user);

            return id;
        } catch (UsuarioNaoRegistradoException e) {
            throw new LoginOuSenhaInvalidoException("any");
        }
    }

    /**
     * Adiciona um amigo ao usuário
     * O amigo não será adicionado caso ele não seja amigo, já tenha solicitado amizade ou já tenha recebido uma solicitação de amizade do usuário.
     *
     * @param amigo Login do amigo a ser adicionado
     *
     * @throws UsuarioJaTemRelacaoException        Exceção lançada caso o usuário já seja amigo do usuário aberto na sessão
     * @throws UsuarioRelacaoParaSiException         Exceção lançada caso o usuário tente adicionar a si mesmo como amigo
     * @throws UsuarioJaPediuSolicitacaoException  Exceção lançada caso o usuário já tenha solicitado amizade ao amigo
     * @throws UsuarioEhInimigoException           Exceção lançada caso o usuário seja inimigo do usuário aberto na sessão
     */

    public void adicionarAmigo(User user, User amigo)
            throws UsuarioJaTemRelacaoException, UsuarioJaPediuSolicitacaoException, UsuarioRelacaoParaSiException,
            UsuarioEhInimigoException {
        if (user.equals(amigo)) {
            throw new UsuarioRelacaoParaSiException("amizade");
        }

        if (user.getAmigos().contains(amigo) || amigo.getAmigos().contains(user)) {
            throw new UsuarioJaTemRelacaoException("amigo");
        }

        this.verificarInimigo(user, amigo);

        if (user.getSolicitacoesEnviadas().contains(amigo)) {
            throw new UsuarioJaPediuSolicitacaoException();
        } else if (user.getSolicitacoesRecebidas().contains(amigo)) {
            user.aceitarSolicitacao(amigo);
        } else {
            user.enviarSolicitacao(amigo);
        }
    }

    /**
     * Envia um recado para o usuário passado como parâmetro.
     *
     * @param destinatario  Usuário para o qual o recado será enviado.
     * @param recado        Recado a ser enviado.
     *
     * @throws MensagemParaSiException Exceção lançada caso o usuário tente enviar um recado para si mesmo.
     */

    public void enviarRecado(User remetente, User destinatario, String recado) throws MensagemParaSiException, UsuarioEhInimigoException {
        if (remetente.getLogin().equals(destinatario.getLogin())) {
            throw new MensagemParaSiException();
        }

        this.verificarInimigo(remetente, destinatario);

        Recado r = new Recado(remetente, destinatario, recado);
        destinatario.receberRecado(r);
    }

    /**
     * Retorna o primeiro registro de recado não lido do usuário.
     *
     * @param user  O usuário do qual o recado será lido.
     * @return         Recado do usuário.
     *
     * @throws SemRecadosException Exceção lançada se não houver recados para ler.
     */

    public String lerRecado(User user) throws SemRecadosException {
        Recado recado = user.lerRecado();
        return recado.getRecado();
    }

    /**
     * Cria uma comunidade no sistema.
     *
     * @param dono    Usuario dono da comunidade
     * @param nome       Nome da comunidade
     * @param descricao  Descricao da comunidade
     *
     * @throws ComunidadeJaExisteException Exceção lançada caso a comunidade já exista com base no nome.
     *
     * @see Comunidade
     */

    public void criarComunidade(User dono, String nome, String descricao) throws ComunidadeJaExisteException {
        if (this.comunidades.containsKey(nome)) {
            throw new ComunidadeJaExisteException();
        }

        Comunidade comunidade = new Comunidade(dono, nome, descricao);
        this.comunidades.put(nome, comunidade);

        dono.setDonoComunidade(comunidade);
        dono.setParticipanteComunidade(comunidade);
    }

    /**
     * Adiciona uma comunidade ao sistema.
     * AVISO: Método utilizado apenas para carregar os dados do arquivo.
     *
     * @param nome        Nome da comunidade.
     * @param comunidade  Comunidade a ser adicionada.
     *
     * @see Comunidade
     */

    public void setComunidade(String nome, Comunidade comunidade) {
        this.comunidades.put(nome, comunidade);
    }


    /**
     * Adiciona um usuário a uma comunidade.
     *
     * @param user  O usuário a ser adicionado.
     * @param nome     Nome da comunidade.
     *
     * @throws ComunidadeNaoExisteException        Exceção lançada caso a comunidade não exista.
     * @throws UsuarioJaNaComunidadeException  Exceção lançada caso o usuário já esteja na comunidade.
     */

    public void adicionarComunidade(User user, String nome)
            throws ComunidadeNaoExisteException, UsuarioJaNaComunidadeException {
        Comunidade comunidade = this.getComunidade(nome);

        if (user.getComunidadesParticipantes().contains(comunidade)) {
            throw new UsuarioJaNaComunidadeException();
        }

        comunidade.adicionarMembro(user);
        user.setParticipanteComunidade(comunidade);
    }

    /**
     * Lê a mensagem de um usuário.
     *
     * @param user  O usuário do qual a mensagem será lida.
     * @return         Mensagem do usuário.
     *
     * @throws SemMensagensException  Exceção lançada se não houver mensagens para ler.
     */

    public String lerMensagem(User user) throws SemMensagensException {
        Mensagem mensagem = user.lerMensagem();
        return mensagem.getMensagem();
    }

    /**
     * Envia uma mensagem para uma comunidade.
     * A mensagem será enviada para todos os membros da comunidade.
     *
     * @param comunidade  A comunidade para a qual a mensagem será enviada.
     * @param msg         A mensagem a ser enviada.
     */

    public void enviarMensagem(Comunidade comunidade, String msg) {
        Mensagem mensagem = new Mensagem(msg);

        comunidade.enviarMensagem(mensagem);
    }

    /**
     * Adiciona um usuário como fã de outro.
     *
     * @param user  Usuário que será fã.
     * @param idolo    Usuário que será ídolo.
     *
     * @throws UsuarioRelacaoParaSiException   Exceção lançada caso o usuário tente adicionar a si mesmo como fã.
     * @throws UsuarioJaTemRelacaoException  Exceção lançada caso o usuário já seja fã do usuário.
     * @throws UsuarioEhInimigoException     Exceção lançada caso o usuário seja inimigo do usuário.
     */

    public void adicionarIdolo(User user, User idolo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        if (user.equals(idolo)) {
            throw new UsuarioRelacaoParaSiException("fã");
        }

        if (user.getIdolos().contains(idolo)) {
            throw new UsuarioJaTemRelacaoException("ídolo");
        }

        this.verificarInimigo(user, idolo);

        user.setIdolo(idolo);
        idolo.setFa(user);
    }

    /**
     * Adiciona um usuário como paquera de outro.
     *
     * @param user Usuário que irá paquerar.
     * @param paquera Usuário que será paquerado.
     *
     * @throws UsuarioRelacaoParaSiException   Exceção lançada caso o usuário tente adicionar a si mesmo como paquera.
     * @throws UsuarioJaTemRelacaoException  Exceção lançada caso o usuário já seja paquera do usuário aberto na sessão.
     * @throws UsuarioEhInimigoException     Exceção lançada caso o usuário seja inimigo do usuário aberto na sessão.
     */

    public void adicionarPaquera(User user, User paquera)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        if (user.equals(paquera)) {
            throw new UsuarioRelacaoParaSiException("paquera");
        }

        if (user.getPaqueras().contains(paquera)) {
            throw new UsuarioJaTemRelacaoException("paquera");
        }

        this.verificarInimigo(user, paquera);

        if (user.getPaquerasRecebidas().contains(paquera) || paquera.getPaquerasRecebidas().contains(user)) {
            this.enviarRecado(paquera, user, paquera.getNome() + " é seu paquera - Recado do Jackut.");
            this.enviarRecado(user, paquera, user.getNome() + " é seu paquera - Recado do Jackut.");
        }

        user.setPaquera(paquera);
        paquera.setPaquerasRecebidas(user);
    }

    /**
     * Adiciona um usuário como inimigo de outro.
     *
     * @param user Usuário que irá adicionar o inimigo.
     * @param inimigo Usuário que será inimigo.
     *
     * @throws UsuarioRelacaoParaSiException   Exceção lançada caso o usuário tente adicionar a si mesmo como inimigo.
     * @throws UsuarioJaTemRelacaoException  Exceção lançada caso o usuário já seja inimigo do usuário aberto na sessão.
     */

    public void adicionarInimigo(User user, User inimigo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException {
        if (user.equals(inimigo)) {
            throw new UsuarioRelacaoParaSiException("inimigo");
        }

        if (user.getInimigos().contains(inimigo)) {
            throw new UsuarioJaTemRelacaoException("inimigo");
        }

        user.setInimigo(inimigo);
        inimigo.setInimigo(user);
    }

    /**
     * Verifica se o usuário é inimigo do usuário aberto na sessão.
     * Caso seja, lança uma exceção.
     *
     * @param user Usuário que irá verificar.
     * @param inimigo O inimigo que será verificado.
     *
     * @throws UsuarioEhInimigoException Exceção lançada caso o usuário seja inimigo do usuário aberto na sessão.
     */

    public void verificarInimigo(User user, User inimigo) throws UsuarioEhInimigoException {
        if (user.getInimigos().contains(inimigo)) {
            throw new UsuarioEhInimigoException(inimigo.getNome());
        }
    }

    /**
     * Remove um usuário do sistema.
     *
     * @param user  Usuário a ser removido.
     * @param id       ID da sessão do usuário.
     *
     * @throws UsuarioNaoRegistradoException Exceção lançada caso o usuário não esteja cadastrado.
     */

    public void removerUsuario(User user, String id) throws UsuarioNaoRegistradoException {
        for (User amigo : user.getAmigos()) {
            amigo.removerAmigo(user);
        }

        for (User fa : user.getIdolos()) {
            fa.removerFa(user);
        }

        for (User paquera : user.getPaqueras()) {
            paquera.removerPaquera(user);
        }

        for (User paqueraRecebida : user.getPaquerasRecebidas()) {
            paqueraRecebida.removerPaqueraRecebida(user);
        }

        for (User inimigo : user.getInimigos()) {
            inimigo.removerInimigo(user);
        }

        for (User solicitacaoEnviada : user.getSolicitacoesEnviadas()) {
            solicitacaoEnviada.removerSolicitacaoEnviada(user);
        }

        for (User solicitacaoRecebida : user.getSolicitacoesRecebidas()) {
            solicitacaoRecebida.removerSolicitacaoRecebida(user);
        }

        for (Comunidade comunidade : this.comunidades.values()) {
            if (comunidade.getDono().equals(user)) {
                for (User membro : comunidade.getMembros()) {
                    membro.sairComunidade(comunidade);
                }
                this.comunidades.remove(comunidade.getNome());
            }
        }

        for (User destinatario : this.usuarios.values()) {
            for (Recado recado : destinatario.getRecados()) {
                if (recado.getRemetente().equals(user)) {
                    destinatario.removerRecado(recado);
                }
            }
        }

        this.usuarios.remove(user.getLogin());
        this.sessoes.remove(id);
    }

    /**
     * Zera as informações do sistema.
     */

    public void zerarSistema() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        this.comunidades = new HashMap<>();

        EscritaDeArquivos.limparArquivos();
    }

    /**
     * Grava o cadastro em arquivo e encerra o programa.
     *
     * Salva apenas os usuários cadastrados.
     */

    public void encerrarSistema() {
        EscritaDeArquivos.criarPasta();
        EscritaDeArquivos.persistirDados(this.usuarios, this.comunidades);
    }
}
