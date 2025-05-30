package br.ufal.ic.p2.jackut.Servicos;

import br.ufal.ic.p2.jackut.Entidades.Comunidade;
import br.ufal.ic.p2.jackut.Entidades.Mensagem;
import br.ufal.ic.p2.jackut.Entidades.Recado;
import br.ufal.ic.p2.jackut.Entidades.User;
import br.ufal.ic.p2.jackut.Exceptions.Comunidade.SemMensagensException;
import br.ufal.ic.p2.jackut.Exceptions.Recado.MensagemParaSiException;
import br.ufal.ic.p2.jackut.Exceptions.Recado.SemRecadosException;

import java.util.Collection;
import java.util.LinkedList; // <-- ADICIONADO
import java.util.Queue;      // <-- ADICIONADO

public class MessageService {

    // Se MessageService precisar de acesso a usuários/comunidades, injete os serviços ou mapas.
    // Por enquanto, os métodos recebem as entidades necessárias.

    public void enviarNovoRecado(User remetente, User destinatario, String textoRecado) throws MensagemParaSiException {
        if (remetente.equals(destinatario)) {
            throw new MensagemParaSiException();
        }
        // A checagem de inimizade já é feita pelo RelationshipService antes de chamar aqui,
        // ou poderia ser feita aqui se o MessageService tiver acesso ao RelationshipService/lógica.
        Recado recado = new Recado(remetente, destinatario, textoRecado);
        destinatario.receberRecado(recado);
    }

    public String lerProximoRecado(User usuario) throws SemRecadosException {
        Recado recado = usuario.lerRecado(); // User.lerRecado() já remove da fila e lança exceção
        return recado.getRecado();
    }

    public void enviarNovaMensagemParaComunidade(User remetente, Comunidade comunidade, String textoMensagem) {
        // Aqui poderia haver lógica para verificar se o remetente é membro da comunidade,
        // mas por enquanto, a Comunidade.enviarMensagem notifica todos os seus membros.
        // Adicionar o nome do remetente à mensagem é uma boa ideia para clareza.
        String mensagemFormatada = textoMensagem;
        if (remetente != null) {
            mensagemFormatada = textoMensagem + " (Enviada por: " + remetente.getLogin() + ")";
        }
        Mensagem mensagem = new Mensagem(mensagemFormatada);
        comunidade.enviarMensagem(mensagem);
    }

    // Corrigido para lançar SemMensagensException, conforme User.lerMensagem()
    public String lerProximaMensagemDeComunidade(User usuario) throws SemMensagensException {
        Mensagem mensagem = usuario.lerMensagem(); // User.lerMensagem() já remove e lança SemMensagensException
        return mensagem.getMensagem();
    }

    public void removerTodasMensagensDeOuPara(User usuarioAlvo, Collection<User> todosUsuarios) {
        // Limpar recados enviados pelo usuarioAlvo (iterando em todos os outros usuários)
        for (User outroUsuario : todosUsuarios) {
            if (outroUsuario.equals(usuarioAlvo)) continue;

            // Abordagem para remover recados de uma Queue se User.getRecados() retorna Queue
            Queue<Recado> recadosOriginais = outroUsuario.getRecados();
            if (recadosOriginais != null) {
                Queue<Recado> recadosMantidos = new LinkedList<>();
                while(!recadosOriginais.isEmpty()){
                    Recado r = recadosOriginais.poll();
                    if(!r.getRemetente().equals(usuarioAlvo)){
                        recadosMantidos.add(r);
                    }
                }
                // Se User.getRecados() permitir, repopule a fila.
                // Isso depende de como a entidade User gerencia sua coleção de recados.
                // Se getRecados() retorna uma referência mutável:
                recadosOriginais.clear(); // Limpa o que sobrou (deveria estar vazia se poll() foi até o fim)
                recadosOriginais.addAll(recadosMantidos);
            }
        }
        // Limpar mensagens de comunidade: Mensagens são por comunidade, não diretamente ligadas a um usuário globalmente
        // A remoção do usuário das comunidades já o impede de receber novas mensagens.
        // As mensagens já na fila do usuário (User.mensagens) seriam limpas quando a entidade User é descartada
        // ou se a lógica de User.removerUsuario limpasse suas próprias filas.
        // Se User.mensagens precisa ser limpa aqui também (improvável se User for deletado):
        // Queue<Mensagem> mensagensUsuario = usuarioAlvo.getMensagens();
        // if (mensagensUsuario != null) {
        //     mensagensUsuario.clear();
        // }
    }
}