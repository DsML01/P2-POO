package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

// AutoBanimentoException.java
public class AutoBanimentoException extends RuntimeException {
    public AutoBanimentoException() {
        super("Não é possível banir a si mesmo.");
    }
}
