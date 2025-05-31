package br.ufal.ic.p2.jackut.Exceptions.Usuario;


/**
 * Exceção que indica que o User já tem uma relação com outro User.
 */

public class UsuarioJaTemRelacaoException extends Exception {

    /**
     * Exceção que indica que o User já tem uma relação com outro User.
     *
     * Em que se deve ter uma destas relacoes
     *  Amigo
     *  Ídolo
     *  Paquera
     *  Inimigo
     *
     * @param relacao Relação que está sendo adicionada.
     */

    public UsuarioJaTemRelacaoException(String relacao) {
        super("Usuário já está adicionado como " + relacao + ".");
    }
}
