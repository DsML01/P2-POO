package br.ufal.ic.p2.jackut.Entidades;

import br.ufal.ic.p2.jackut.Exceptions.Comunidade.ModeradorException;
import br.ufal.ic.p2.jackut.Utilidade.UtilidadeString;

import java.util.ArrayList;


/**
 * Classe que representa uma comunidade.
 */

public class Comunidade {
    private final User dono;
    private final String nome;
    private final String descricao;
    private final ArrayList<User> membros = new ArrayList<>();

    /**
     * Cria uma nova Comunidade
     * Cria a lista de membros com o dono da comunidade.
     *
     * @param dono    Dono da comunidade
     * @param nome       Nome da comunidade
     * @param descricao  Descrição da comunidade
     */

    public Comunidade(User dono, String nome, String descricao) {
        this.dono = dono;
        this.nome = nome;
        this.descricao = descricao;
        this.membros.add(dono);
    }

    /**
     * Retorna o nome da comunidade.
     *
     * @return Nome da comunidade
     */

    public String getNome() {
        return nome;
    }

    /**
     * Retorna a descrição da comunidade.
     *
     * @return Descrição da comunidade
     */

    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna o dono da comunidade.
     *
     * @return Dono da comunidade
     */

    public User getDono() {
        return dono;
    }

    /**
     * Retorna uma lista com os membros que participam da comunidade
     *
     * @return Lista de membros da comunidade
     */

    public ArrayList<User> getMembros() {
        return membros;
    }

    /**
     * Retorna uma lista de membros da comunidade formatada como uma String
     *
     * @return Lista de membros da comunidade formatada como uma string
     *
     * @see UtilidadeString
     */

    public String getMembrosString() {
        return UtilidadeString.formatArrayList(membros);
    }

    /**
     * Adiciona uma lista de User como membros da comunidade.
     * A lista de membros atual é substituída pela nova lista.
     *
     * @param membros Lista de membros a serem adicionados
     */

    public void setMembros(ArrayList<User> membros) {
        this.membros.clear();
        this.membros.addAll(membros);
    }

    /**
     * Adiciona um membro na lista de membros da comunidade.
     *
     * @param user User a ser adicionado
     */

    public void adicionarMembro(User user) {
        this.membros.add(user);
    }

     /** Envia uma mensagem para todos os membros da comunidade.
     * @param mensagem Mensagem a ser enviada
     **/
    public void enviarMensagem(Mensagem mensagem) {
        for (User membro : membros) {
            // ANTES: membro.receberMensagem(mensagem);
            membro.getCaixaDeEntrada().receberMensagem(mensagem); // DEPOIS
        }
    }

    private final ArrayList<User> moderadores = new ArrayList<>();

    public void adicionarModerador(User user) {
        if (!moderadores.contains(user)) {
            moderadores.add(user);
        }
    }

    public boolean isModerador(User user) {
        return moderadores.contains(user);
    }

    public String getModeradoresString() {
        return UtilidadeString.formatArrayList(moderadores);
    }

    public ArrayList<User> getModeradores() {
        return moderadores;
    }

    public void removerMembro(User user) throws ModeradorException {
        // Não permite remover o dono
        if (user.equals(this.dono)) {
            throw new ModeradorException("Não é possível expulsar o dono da comunidade.");
        }

        this.membros.remove(user);
        this.moderadores.remove(user); // Remove também dos moderadores se for o caso
    }

    public boolean isMembro(User user) {
        return this.membros.contains(user);
    }

    /**
     * Retorna uma String que representa a comunidade.
     *
     * @return String que representa o nome da comunidade.
     */

    @Override
    public String toString() {
        return this.getNome();
    }
}
