package br.ufal.ic.p2.jackut.Entidades;

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

    /**
     * Envia uma mensagem para todos os membros da comunidade.
     *
     * @param mensagem Mensagem a ser enviada
     */

    public void enviarMensagem(Mensagem mensagem) {
        for (User membro : membros) {
            membro.receberMensagem(mensagem);
        }
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
