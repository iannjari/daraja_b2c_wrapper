package dev.njari.daraja.exception;

/**
 * @author njari_mathenge
 * on 20/07/2024.
 * github.com/iannjari
 */
public class InternalServerException extends RuntimeException{
    public InternalServerException(String s, Exception ex) {
        super(s, ex);
    }
}
