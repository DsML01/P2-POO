package br.ufal.ic.p2.jackut.Exceptions.Comunidade;

public class BanirDonoException extends RuntimeException {
    public BanirDonoException() {
        super("Não é possível banir o dono da comunidade.");
    }
}
