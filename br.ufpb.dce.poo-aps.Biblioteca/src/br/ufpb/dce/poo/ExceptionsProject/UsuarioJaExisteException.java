package br.ufpb.dce.poo.ExceptionsProject;

public class UsuarioJaExisteException extends Exception {
	public UsuarioJaExisteException (String msg){
		super(msg);
	}
}
