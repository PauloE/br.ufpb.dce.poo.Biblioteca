package br.ufpb.dce.poo.projetopack;

import java.util.Calendar;


public class Emprestimo {
	private Usuario usuario;
	private Livro livro;
	private Calendar dataEmprestimo;
	private Calendar dataDevolucao;
	
	public Emprestimo(Usuario usuario, Livro livro, Calendar dataEmprestimo, Calendar dataDevolucao){ 
		this.usuario = usuario;
		this.livro = livro;
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucao = dataDevolucao;
	}
	
	
	public Usuario getUsuario() {
		return this.usuario;
	}


	public Livro getLivro() {
		return this.livro;
	}


	public void setLivro(Livro livro) {
		this.livro = livro;
	}


	public Calendar getDataEmprestimo(){
		return this.dataEmprestimo;
	}
	
	public Calendar getDataDevolucao(){
		return this.dataDevolucao;
	}
	
	public void setDataEmprestimo(Calendar dataEmprestimo){
		this.dataEmprestimo = dataEmprestimo;
	}
	
	public void setDataDevolucao(Calendar dataDevolucao){
		this.dataDevolucao = dataDevolucao;
	}	
	
}
