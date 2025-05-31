package br.ufal.ic.p2.jackut.Entidades;

import br.ufal.ic.p2.jackut.Exceptions.Comunidade.SemMensagensException;
import br.ufal.ic.p2.jackut.Exceptions.Recado.SemRecadosException;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Classe que encapsula a responsabilidade de gerenciar as filas de Recados e Mensagens de um usuário.
 */
public class CaixaDeEntrada {
    private final Queue<Recado> recados = new LinkedList<>();
    private final Queue<Mensagem> mensagens = new LinkedList<>();

    /**
     * Adiciona um recado na fila de recados.
     * @param recado Recado a ser adicionado.
     */
    public void receberRecado(Recado recado) {
        this.recados.add(recado);
    }

    /**
     * Adiciona uma mensagem na fila de mensagens.
     * @param mensagem Mensagem a ser adicionada.
     */
    public void receberMensagem(Mensagem mensagem) {
        this.mensagens.add(mensagem);
    }

    /**
     * Retorna e remove o primeiro recado da fila.
     * @return O primeiro recado da fila.
     * @throws SemRecadosException se a fila de recados estiver vazia.
     */
    public Recado lerRecado() throws SemRecadosException {
        if (this.recados.isEmpty()) {
            throw new SemRecadosException();
        }
        return this.recados.poll();
    }

    /**
     * Retorna e remove a primeira mensagem da fila.
     * @return A primeira mensagem da fila.
     * @throws SemMensagensException se a fila de mensagens estiver vazia.
     */
    public Mensagem lerMensagem() throws SemMensagensException {
        if (this.mensagens.isEmpty()) {
            throw new SemMensagensException();
        }
        return this.mensagens.poll();
    }

    /**
     * Retorna a fila de recados (usado principalmente para persistência).
     * @return A fila de recados.
     */
    public Queue<Recado> getRecados() {
        return this.recados;
    }

    /**
     * Retorna a fila de mensagens (usado principalmente para persistência).
     * @return A fila de mensagens.
     */
    public Queue<Mensagem> getMensagens() {
        return this.mensagens;
    }
}