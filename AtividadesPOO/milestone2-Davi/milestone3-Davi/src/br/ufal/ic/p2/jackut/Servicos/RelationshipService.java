package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioEhInimigoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioJaPediuSolicitacaoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioJaTemRelacaoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioRelacaoParaSiException;

import java.util.Collection;

/**
 * Serviço responsável por gerenciar a lógica de negócios para todos os tipos de relacionamentos entre usuários.
 *
 * @author Davi
 */
public class RelationshipService {
    private final UserService userService;

    /**
     * Constrói uma nova instância de RelationshipService.
     *
     * @param userService O serviço de usuário, necessário para validações.
     */
    public RelationshipService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Verifica se dois usuários possuem uma relação de inimizade mútua.
     *
     * @param usuario      Um dos usuários na verificação.
     * @param outroUsuario O outro usuário na verificação.
     * @throws UsuarioEhInimigoException se os usuários forem inimigos um do outro.
     */
    public void verificarInimizade(User usuario, User outroUsuario) throws UsuarioEhInimigoException {
        if (usuario.getInimigos().contains(outroUsuario) || outroUsuario.getInimigos().contains(usuario)) {
            throw new UsuarioEhInimigoException(outroUsuario.getNome());
        }
    }

    /**
     * Orquestra o processo de solicitação ou aceitação de amizade.
     * Se um usuário solicita amizade a outro que já lhe enviou uma solicitação,
     * a amizade é confirmada. Caso contrário, uma nova solicitação é criada.
     *
     * @param solicitante O usuário que está iniciando a ação.
     * @param alvo        O usuário que é o alvo da ação.
     * @throws UsuarioRelacaoParaSiException      Se um usuário tentar adicionar a si mesmo.
     * @throws UsuarioJaTemRelacaoException       Se já forem amigos.
     * @throws UsuarioJaPediuSolicitacaoException Se uma solicitação já foi enviada.
     * @throws UsuarioEhInimigoException          Se os usuários forem inimigos.
     */
    public void solicitarOuConfirmarAmizade(User solicitante, User alvo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException,
            UsuarioJaPediuSolicitacaoException, UsuarioEhInimigoException {

        if (solicitante.equals(alvo)) {
            throw new UsuarioRelacaoParaSiException("amizade");
        }

        verificarInimizade(solicitante, alvo);

        if (solicitante.getAmigos().contains(alvo)) {
            throw new UsuarioJaTemRelacaoException("amigo");
        }

        if (solicitante.getSolicitacoesEnviadas().contains(alvo)) {
            throw new UsuarioJaPediuSolicitacaoException();
        } else if (solicitante.getSolicitacoesRecebidas().contains(alvo)) {
            solicitante.adicionarAmigo(alvo);
            solicitante.removerSolicitacaoRecebida(alvo);

            alvo.adicionarAmigo(solicitante);
            alvo.removerSolicitacaoEnviada(solicitante);
        } else {
            solicitante.adicionarSolicitacaoEnviada(alvo);
            alvo.adicionarSolicitacaoRecebida(solicitante);
        }
    }

    /**
     * Adiciona uma relação de fã-ídolo entre dois usuários.
     * A relação é bidirecional: um usuário se torna fã do outro, que por sua vez ganha um fã.
     *
     * @param fa    O usuário que se tornará fã.
     * @param idolo O usuário que se tornará o ídolo.
     * @throws UsuarioRelacaoParaSiException Se um usuário tentar ser fã de si mesmo.
     * @throws UsuarioJaTemRelacaoException  Se a relação fã-ídolo já existir.
     * @throws UsuarioEhInimigoException     Se os usuários forem inimigos.
     */
    public void adicionarNovoIdolo(User fa, User idolo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {

        if (fa.equals(idolo)) {
            throw new UsuarioRelacaoParaSiException("fã");
        }
        verificarInimizade(fa, idolo);
        if (fa.getIdolos().contains(idolo)) {
            throw new UsuarioJaTemRelacaoException("ídolo");
        }

        fa.setIdolo(idolo);
        idolo.setFa(fa);
    }

    /**
     * Adiciona uma relação de paquera.
     * Se a paquera se tornar mútua (o paquerado já paquerava o paquerador),
     * um recado de notificação é enviado para ambos.
     *
     * @param paquerador     O usuário que está iniciando a paquera.
     * @param paquerado      O usuário que está recebendo a paquera.
     * @param messageService O serviço de mensagens para enviar notificações.
     * @throws UsuarioRelacaoParaSiException Se um usuário tentar paquerar a si mesmo.
     * @throws UsuarioJaTemRelacaoException  Se a relação de paquera já existir.
     * @throws UsuarioEhInimigoException     Se os usuários forem inimigos.
     */
    public void adicionarNovaPaquera(User paquerador, User paquerado, MessageService messageService)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {

        if (paquerador.equals(paquerado)) {
            throw new UsuarioRelacaoParaSiException("paquera");
        }
        verificarInimizade(paquerador, paquerado);
        if (paquerador.getPaqueras().contains(paquerado)) {
            throw new UsuarioJaTemRelacaoException("paquera");
        }

        paquerador.setPaquera(paquerado);
        paquerado.setPaquerasRecebidas(paquerador);

        if (paquerado.getPaqueras().contains(paquerador)) {
            try {
                String mensagem = " é seu paquera - Recado do Jackut.";
                messageService.enviarNovoRecado(paquerado, paquerador, paquerado.getNome() + mensagem);
                messageService.enviarNovoRecado(paquerador, paquerado, paquerador.getNome() + mensagem);
            } catch (Exception e) {
                System.err.println("Erro ao enviar recado de paquera mútua: " + e.getMessage());
            }
        }
    }


    /**
     * Adiciona uma relação de inimizade mútua entre dois usuários.
     * Este ato remove todas as outras formas de relacionamento existentes entre eles
     * (amizade, paquera, fã/ídolo, e solicitações pendentes).
     *
     * @param usuario O usuário que está declarando a inimizade.
     * @param inimigo O usuário que se tornará o inimigo.
     * @throws UsuarioRelacaoParaSiException Se um usuário tentar se tornar inimigo de si mesmo.
     * @throws UsuarioJaTemRelacaoException  Se a relação de inimizade já existir.
     */
    public void adicionarNovoInimigo(User usuario, User inimigo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException {

        if (usuario.equals(inimigo)) {
            throw new UsuarioRelacaoParaSiException("inimigo");
        }
        if (usuario.getInimigos().contains(inimigo)) {
            throw new UsuarioJaTemRelacaoException("inimigo");
        }

        usuario.setInimigo(inimigo);
        inimigo.setInimigo(usuario);

        usuario.removerAmigo(inimigo);
        inimigo.removerAmigo(usuario);

        usuario.removerIdolo(inimigo);
        inimigo.removerFa(usuario);

        usuario.removerFa(inimigo);
        inimigo.removerIdolo(usuario);

        usuario.removerPaquera(inimigo);
        inimigo.removerPaqueraRecebida(usuario);

        usuario.removerPaqueraRecebida(inimigo);
        inimigo.removerPaquera(usuario);

        usuario.removerSolicitacaoEnviada(inimigo);
        inimigo.removerSolicitacaoRecebida(usuario);

        usuario.removerSolicitacaoRecebida(inimigo);
        inimigo.removerSolicitacaoEnviada(usuario);
    }


    /**
     * Remove todas as referências de relacionamento de um {@code usuarioAlvo} das listas de todos os outros usuários.
     * Este método é uma operação de limpeza, tipicamente usada ao remover um usuário do sistema.
     *
     * @param usuarioAlvo   O usuário a ser removido do grafo de relacionamentos.
     * @param todosUsuarios A coleção de todos os usuários do sistema.
     */
    public void removerTodosRelacionamentosDe(User usuarioAlvo, Collection<User> todosUsuarios) {
        for (User outroUsuario : todosUsuarios) {
            if (outroUsuario.equals(usuarioAlvo)) continue;

            outroUsuario.removerAmigo(usuarioAlvo);
            outroUsuario.removerFa(usuarioAlvo);
            outroUsuario.removerIdolo(usuarioAlvo);
            outroUsuario.removerPaquera(usuarioAlvo);
            outroUsuario.removerPaqueraRecebida(usuarioAlvo);
            outroUsuario.removerInimigo(usuarioAlvo);
            outroUsuario.removerSolicitacaoEnviada(usuarioAlvo);
            outroUsuario.removerSolicitacaoRecebida(usuarioAlvo);
        }
    }
}