// Exceptions/Comunidade/UsuarioBanidoException.java
package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

public class UsuarioBanidoException extends RuntimeException {
    public UsuarioBanidoException() {
        super("Usuário está banido desta comunidade.");
    }

    public UsuarioBanidoException(String login) {
        super("Usuário " + login + " está banido desta comunidade.");
    }
}