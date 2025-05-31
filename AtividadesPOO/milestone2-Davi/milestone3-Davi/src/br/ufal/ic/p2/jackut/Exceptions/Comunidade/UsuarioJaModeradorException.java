package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

// UsuarioJaModeradorException.java
public class UsuarioJaModeradorException extends RuntimeException {
    public UsuarioJaModeradorException() {
        super("Usuário já é moderador.");
    }
}
