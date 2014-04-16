package br.ufpb.dce.poo.projetopack;
public class Livro {
	
	String nome;
	String codigo;
	String autor;
	int quantidade;
	String classificacao;
	
	public Livro(String nome, String codigo, String autor, int quantidade, String classificacao){
		this.nome = nome;
		this.codigo = codigo;
		this.autor = autor;
		this.quantidade = quantidade;
		this.classificacao = classificacao;
	}
	
	public String getCodigo(){
		return this.codigo;
	}
	
	public int getQuantidade(){
		return this.quantidade;
	}

	public void setQuantidade(int quant) {
		this.quantidade = quantidade + quant;
	}
}
