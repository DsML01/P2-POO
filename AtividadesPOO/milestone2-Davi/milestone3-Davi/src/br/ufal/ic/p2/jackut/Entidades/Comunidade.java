package br.ufal.ic.p2.jackut.Entidades;

import br.ufal.ic.p2.jackut.Exceptions.Comunidade.ModeradorException;
import br.ufal.ic.p2.jackut.Utilidade.UtilidadeString;

import java.util.ArrayList;

/**
 * Classe que representa uma comunidade.
 *
 * @author Davi
 */
public class Comunidade {
    private final User dono;
    private final String nome;
    private final String descricao;
    private final ArrayList<User> membros = new ArrayList<>();
    private final ArrayList<User> membrosBanidos = new ArrayList<>();
    private final ArrayList<User> moderadores = new ArrayList<>();

    /**
     * Cria uma nova Comunidade.
     * Cria a lista de membros com o dono da comunidade.
     *
     * @param dono Dono da comunidade
     * @param nome Nome da comunidade
     * @param descricao Descrição da comunidade
     */
    public Comunidade(User dono, String nome, String descricao) {
        this.dono = dono;
        this.nome = nome;
        this.descricao = descricao;
        this.membros.add(dono);
    }

    /**
     * Banir um membro da comunidade.
     *
     * @param membro Membro a ser banido
     * @throws ModeradorException Se o membro for o dono da comunidade
     */
    public void banirMembro(User membro) throws ModeradorException {
        if (membro.equals(this.dono)) {
            throw new ModeradorException("Não é possível banir o dono da comunidade.");
        }

        this.membros.remove(membro);
        this.moderadores.remove(membro);
        if (!this.membrosBanidos.contains(membro)) {
            this.membrosBanidos.add(membro);
        }
    }

    /**
     * Desbanir um membro da comunidade.
     *
     * @param membro Membro a ser desbanido
     * @throws ModeradorException Se o usuário não estiver banido
     */
    public void desbanirMembro(User membro) throws ModeradorException {
        if (!this.membrosBanidos.contains(membro)) {
            throw new ModeradorException("Usuário não está banido desta comunidade.");
        }
        this.membrosBanidos.remove(membro);
    }

    /**
     * Verifica se um usuário está banido da comunidade.
     *
     * @param user Usuário a ser verificado
     * @return true se o usuário estiver banido, false caso contrário
     */
    public boolean isBanido(User user) {
        return this.membrosBanidos.contains(user);
    }

    /**
     * Retorna a lista de membros banidos formatada como string.
     *
     * @return String formatada com os membros banidos
     * @see UtilidadeString
     */
    public String getMembrosBanidosString() {
        return UtilidadeString.formatArrayList(this.membrosBanidos);
    }

    /**
     * Retorna a lista de membros banidos.
     *
     * @return ArrayList de membros banidos
     */
    public ArrayList<User> getMembrosBanidos() {
        return this.membrosBanidos;
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
     * Retorna a lista de membros da comunidade.
     *
     * @return ArrayList de membros
     */
    public ArrayList<User> getMembros() {
        return membros;
    }

    /**
     * Retorna a lista de membros formatada como string.
     *
     * @return String formatada com os membros
     * @see UtilidadeString
     */
    public String getMembrosString() {
        return UtilidadeString.formatArrayList(membros);
    }

    /**
     * Define a lista de membros da comunidade.
     *
     * @param membros ArrayList de membros
     */
    public void setMembros(ArrayList<User> membros) {
        this.membros.clear();
        this.membros.addAll(membros);
    }

    /**
     * Adiciona um membro à comunidade.
     *
     * @param user Usuário a ser adicionado
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
            membro.getCaixaDeEntrada().receberMensagem(mensagem);
        }
    }

    /**
     * Adiciona um moderador à comunidade.
     *
     * @param user Usuário a ser adicionado como moderador
     */
    public void adicionarModerador(User user) {
        if (!moderadores.contains(user)) {
            moderadores.add(user);
        }
    }

    /**
     * Verifica se um usuário é moderador da comunidade.
     *
     * @param user Usuário a ser verificado
     * @return true se for moderador, false caso contrário
     */
    public boolean isModerador(User user) {
        return moderadores.contains(user);
    }

    /**
     * Retorna a lista de moderadores formatada como string.
     *
     * @return String formatada com os moderadores
     * @see UtilidadeString
     */
    public String getModeradoresString() {
        return UtilidadeString.formatArrayList(moderadores);
    }

    /**
     * Retorna a lista de moderadores da comunidade.
     *
     * @return ArrayList de moderadores
     */
    public ArrayList<User> getModeradores() {
        return moderadores;
    }

    /**
     * Remove um membro da comunidade.
     *
     * @param user Usuário a ser removido
     * @throws ModeradorException Se o usuário for o dono da comunidade
     */
    public void removerMembro(User user) throws ModeradorException {
        if (user.equals(this.dono)) {
            throw new ModeradorException("Não é possível expulsar o dono da comunidade.");
        }

        this.membros.remove(user);
        this.moderadores.remove(user);
    }

    /**
     * Verifica se um usuário é membro da comunidade.
     *
     * @param user Usuário a ser verificado
     * @return true se for membro, false caso contrário
     */
    public boolean isMembro(User user) {
        return this.membros.contains(user);
    }

    /**
     * Retorna uma representação em string da comunidade.
     *
     * @return Nome da comunidade
     */
    @Override
    public String toString() {
        return this.getNome();
    }
}