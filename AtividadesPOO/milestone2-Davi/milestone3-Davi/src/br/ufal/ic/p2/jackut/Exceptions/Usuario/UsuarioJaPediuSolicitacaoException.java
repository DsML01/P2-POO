package br.ufal.ic.p2.jackut.Exceptions.Usuario;


/**
 * Exceção que indica que o User já solicitou amizade para o outro User.
 */

public class UsuarioJaPediuSolicitacaoException extends RuntimeException {
    public UsuarioJaPediuSolicitacaoException() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
    
}
