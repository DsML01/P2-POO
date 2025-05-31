package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioEhInimigoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioJaPediuSolicitacaoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioJaTemRelacaoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioRelacaoParaSiException;

import java.util.Collection;

public class RelationshipService {
    private final UserService userService;

    public RelationshipService(UserService userService) {
        this.userService = userService;
    }

    public void verificarInimizade(User usuario, User outroUsuario) throws UsuarioEhInimigoException {
        if (usuario.getInimigos().contains(outroUsuario) || outroUsuario.getInimigos().contains(usuario)) {
            throw new UsuarioEhInimigoException(outroUsuario.getNome());
        }
    }

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
            solicitante.aceitarSolicitacao(alvo);
        } else {
            solicitante.enviarSolicitacao(alvo);
        }
    }

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

    public void adicionarNovaPaquera(User paquerador, User paquerado, MessageService messageService)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {

        // Verifica se o usuário está tentando paquerar a si mesmo
        if (paquerador.equals(paquerado)) {
            throw new UsuarioRelacaoParaSiException("paquera");
        }

        // Verifica se os usuários são inimigos
        verificarInimizade(paquerador, paquerado);

        // Verifica se já existe a relação de paquera
        if (paquerador.getPaqueras().contains(paquerado)) {
            throw new UsuarioJaTemRelacaoException("paquera");
        }

        // Estabelece a relação de paquera
        paquerador.setPaquera(paquerado);
        paquerado.setPaquerasRecebidas(paquerador);

        // Verifica se há paquera mútua (ambos se paqueram)
        if (paquerado.getPaqueras().contains(paquerador)) {
            try {
                // Envia mensagem para o PAQUERADOR sobre o PAQUERADOR
                messageService.enviarNovoRecado(
                        paquerador, // remetente (sistema)
                        paquerado,  // destinatário
                        paquerador.getNome() + " é seu paquera - Recado do Jackut." // mensagem
                );

                // Envia mensagem para o PAQUERADOR sobre o PAQUERADOR
                messageService.enviarNovoRecado(
                        paquerado,  // remetente (sistema)
                        paquerador, // destinatário
                        paquerado.getNome() + " é seu paquera - Recado do Jackut." // mensagem
                );
            } catch (Exception e) {
                System.err.println("Erro ao enviar recado de paquera mútua: " + e.getMessage());
            }
        }
    }

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

        // Remover outras relações
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

        // Cancelar solicitações de amizade
        usuario.removerSolicitacaoEnviada(inimigo);
        inimigo.removerSolicitacaoRecebida(usuario);

        usuario.removerSolicitacaoRecebida(inimigo);
        inimigo.removerSolicitacaoEnviada(usuario);
    }

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