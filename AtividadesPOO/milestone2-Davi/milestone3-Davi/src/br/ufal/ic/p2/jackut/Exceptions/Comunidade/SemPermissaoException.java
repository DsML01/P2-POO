// Exceptions/Comunidade/SemPermissaoException.java
package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

public class SemPermissaoException extends RuntimeException {
    public SemPermissaoException() {
        super("Apenas o dono ou moderadores podem realizar esta ação.");
    }
}

