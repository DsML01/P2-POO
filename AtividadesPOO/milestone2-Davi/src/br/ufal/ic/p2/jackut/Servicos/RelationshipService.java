package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioEhInimigoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioJaPediuSolicitacaoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioJaTemRelacaoException;
import br.ufal.ic.p2.jackut.Exceptions.Usuario.UsuarioRelacaoParaSiException;

import java.util.Collection;

public class RelationshipService {
    private final UserService userService; // Para buscar usuários, se necessário
    // Pode precisar do MessageService se enviar notificações (ex: paquera mútua)

    public RelationshipService(UserService userService) {
        this.userService = userService;
    }

    public void verificarInimizade(User usuario, User outroUsuario) throws UsuarioEhInimigoException {
        if (usuario.getInimigos().contains(outroUsuario) || outroUsuario.getInimigos().contains(usuario)) {
            throw new UsuarioEhInimigoException(outroUsuario.getNome()); // Ou uma mensagem genérica
        }
    }

    public void solicitarOuConfirmarAmizade(User solicitante, User alvo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioJaPediuSolicitacaoException, UsuarioEhInimigoException {
        if (solicitante.equals(alvo)) {
            throw new UsuarioRelacaoParaSiException("amizade");
        }
        verificarInimizade(solicitante, alvo);

        if (solicitante.getAmigos().contains(alvo)) { // Já são amigos
            throw new UsuarioJaTemRelacaoException("amigo");
        }

        if (solicitante.getSolicitacoesEnviadas().contains(alvo)) { // Solicitante já enviou
            throw new UsuarioJaPediuSolicitacaoException();
        } else if (solicitante.getSolicitacoesRecebidas().contains(alvo)) { // Alvo enviou para solicitante, então aceita
            solicitante.aceitarSolicitacao(alvo); // Isso deve atualizar ambos os usuários
        } else { // Nova solicitação
            solicitante.enviarSolicitacao(alvo); // Isso deve atualizar ambos os usuários
        }
    }

    public void adicionarNovoIdolo(User fã, User idolo)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        if (fã.equals(idolo)) {
            throw new UsuarioRelacaoParaSiException("ídolo");
        }
        verificarInimizade(fã, idolo);
        if (fã.getIdolos().contains(idolo)) {
            throw new UsuarioJaTemRelacaoException("ídolo");
        }
        fã.setIdolo(idolo); // User entidade atualiza suas listas
        idolo.setFa(fã);     // e as do outro User
    }

    public void adicionarNovaPaquera(User paquerador, User paquerado, MessageService messageService)
            throws UsuarioRelacaoParaSiException, UsuarioJaTemRelacaoException, UsuarioEhInimigoException {
        if (paquerador.equals(paquerado)) {
            throw new UsuarioRelacaoParaSiException("paquera");
        }
        verificarInimizade(paquerador, paquerado);

        if (paquerador.getPaqueras().contains(paquerado)) {
            throw new UsuarioJaTemRelacaoException("paquera");
        }

        // Lógica de notificação de paquera mútua
        // A entidade User não deveria chamar MessageService. Isso é responsabilidade do serviço.
        boolean paqueraMutuaAntes = paquerador.getPaquerasRecebidas().contains(paquerado) || paquerado.getPaqueras().contains(paquerador);

        paquerador.setPaquera(paquerado);
        paquerado.setPaquerasRecebidas(paquerador);

        boolean paqueraMutuaAgora = paquerador.getPaqueras().contains(paquerado) && paquerado.getPaqueras().contains(paquerador);

        // A lógica original era: if (user.getPaquerasRecebidas().contains(paquera) || paquera.getPaquerasRecebidas().contains(user))
        // Isso parece checar se *este* usuário já recebeu uma paquera do outro, ou se o outro *já* recebeu uma paquera deste.
        // A intenção é notificar quando AMBOS se paqueram.
        if (paquerado.getPaqueras().contains(paquerador)) { // Se o paquerado também paquera o paquerador (agora é mútuo)
            try {
                messageService.enviarNovoRecado(paquerador, paquerado, paquerador.getNome() + " também te paquera! Vocês são um casal Jackut!");
                messageService.enviarNovoRecado(paquerado, paquerador, paquerado.getNome() + " também te paquera! Vocês são um casal Jackut!");
            } catch (Exception e) {
                // Tratar exceção de envio de recado, se necessário
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
        // Relações de inimizade podem remover outras relações (amizade, paquera, ídolo)
        // Essa lógica pode ser complexa e pertencer aqui ou ser coordenada.
        // Por ora, simples adição mútua:
        usuario.setInimigo(inimigo);
        inimigo.setInimigo(usuario);

        // Remover outras relações
        usuario.removerAmigo(inimigo); inimigo.removerAmigo(usuario);
        usuario.removerIdolo(inimigo); inimigo.removerFa(usuario);
        usuario.removerFa(inimigo); inimigo.removerIdolo(usuario);
        usuario.removerPaquera(inimigo); inimigo.removerPaqueraRecebida(usuario);
        usuario.removerPaqueraRecebida(inimigo); inimigo.removerPaquera(usuario);

        // Cancelar solicitações de amizade
        usuario.removerSolicitacaoEnviada(inimigo); inimigo.removerSolicitacaoRecebida(usuario);
        usuario.removerSolicitacaoRecebida(inimigo); inimigo.removerSolicitacaoEnviada(usuario);
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