package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;
import br.ufal.ic.p2.jackut.Entidades.Mensagem;
import br.ufal.ic.p2.jackut.Entidades.Recado;
import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.SemMensagensException;
import br.ufal.ic.p2.jackut.Exceptions.Recado.MensagemParaSiException;
import br.ufal.ic.p2.jackut.Exceptions.Recado.SemRecadosException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Serviço responsável por gerenciar a lógica de negócios para mensagens e recados.
 * <p>
 * Esta classe é projetada para ser stateless, operando sobre as entidades
 * ({@link User}, {@link Comunidade}) que são passadas como parâmetros em seus métodos.
 *
 * @author Davi
 */
public class MessageService {

    /**
     * Envia um recado pessoal de um remetente para um destinatário.
     *
     * @param remetente     O usuário que envia o recado.
     * @param destinatario  O usuário que recebe o recado.
     * @param textoRecado   O conteúdo do recado.
     * @throws MensagemParaSiException se o remetente e o destinatário forem o mesmo usuário.
     */
    public void enviarNovoRecado(User remetente, User destinatario, String textoRecado) throws MensagemParaSiException {
        if (remetente.equals(destinatario)) {
            throw new MensagemParaSiException();
        }
        Recado recado = new Recado(remetente, destinatario, textoRecado);
        destinatario.getCaixaDeEntrada().receberRecado(recado);
    }

    /**
     * Lê e remove o próximo recado não lido da caixa de entrada de um usuário.
     *
     * @param usuario O usuário cuja caixa de entrada será lida.
     * @return O conteúdo do recado como uma {@link String}.
     * @throws SemRecadosException se não houver recados na caixa de entrada.
     */
    public String lerProximoRecado(User usuario) throws SemRecadosException {
        Recado recado = usuario.getCaixaDeEntrada().lerRecado();
        return recado.getRecado();
    }

    /**
     * Envia uma mensagem para todos os membros de uma comunidade.
     * Se um remetente for fornecido, seu nome será anexado à mensagem para identificação.
     *
     * @param remetente     O usuário que envia a mensagem (pode ser {@code null}).
     * @param comunidade    A comunidade de destino.
     * @param textoMensagem O conteúdo da mensagem.
     */
    public void enviarNovaMensagemParaComunidade(User remetente, Comunidade comunidade, String textoMensagem) {
        String mensagemFormatada = textoMensagem;
        if (remetente != null) {
            mensagemFormatada = textoMensagem + " (Enviada por: " + remetente.getLogin() + ")";
        }
        Mensagem mensagem = new Mensagem(mensagemFormatada);
        comunidade.enviarMensagem(mensagem);
    }

    /**
     * Lê e remove a próxima mensagem de comunidade não lida da caixa de entrada de um usuário.
     *
     * @param usuario O usuário cuja caixa de entrada será lida.
     * @return O conteúdo da mensagem como uma {@link String}.
     * @throws SemMensagensException se não houver mensagens de comunidade na caixa de entrada.
     */
    public String lerProximaMensagemDeComunidade(User usuario) throws SemMensagensException {
        Mensagem mensagem = usuario.getCaixaDeEntrada().lerMensagem();
        return mensagem.getMensagem();
    }

    /**
     * Remove todos os recados enviados por um usuário específico das caixas de entrada de todos os outros usuários.
     * Este método é crucial para a limpeza de dados ao remover um usuário do sistema.
     *
     * @param usuarioAlvo   O usuário cujos recados enviados serão removidos.
     * @param todosUsuarios Uma coleção de todos os usuários do sistema para verificar suas caixas de entrada.
     */
    public void removerTodasMensagensDeOuPara(User usuarioAlvo, Collection<User> todosUsuarios) {
        for (User outroUsuario : todosUsuarios) {
            if (outroUsuario.equals(usuarioAlvo)) {
                continue;
            }

            Queue<Recado> recados = outroUsuario.getCaixaDeEntrada().getRecados();
            Iterator<Recado> iterator = recados.iterator();

            while (iterator.hasNext()) {
                Recado recado = iterator.next();
                if (recado.getRemetente().equals(usuarioAlvo)) {
                    iterator.remove();
                }
            }
        }
    }
}