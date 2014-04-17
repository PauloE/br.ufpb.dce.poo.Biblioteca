package br.ufpb.dce.poo.projetopack;
public class Livro {
	
	private String nome;
	private String codigo;
	private String autor;
	private int quantidade;
	private String classificacao;
	
	public Livro(String nome, String codigo, String autor, int quantidade, String classificacao){
		this.nome = nome;
		this.codigo = codigo;
		this.autor = autor;
		this.quantidade = quantidade;
		this.classificacao = classificacao;
	}
	
	public String getNome() {
		return this.nome;
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
	
	public String getAutor() {
		return this.autor;
	}

	public String getClassificacao() {
		return this.classificacao;	
	}
	
}
