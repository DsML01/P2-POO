package br.ufal.ic.p2.jackut.Exceptions.Recado;


/**
 * Exception que indica que o User não pode enviar recado para si mesmo.
 */

public class MensagemParaSiException extends RuntimeException {
    public MensagemParaSiException() {
        super("Usuário não pode enviar recado para si mesmo.");
    }
    
}
