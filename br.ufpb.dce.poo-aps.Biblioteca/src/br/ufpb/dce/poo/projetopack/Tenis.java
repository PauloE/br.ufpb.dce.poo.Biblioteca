package br.ufpb.dce.poo.projetopack;


public class Tenis implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int tamanho;
    private String marca;
    private String modelo;

    public Tenis() {
    }

    public Tenis(int tamanho, String marca, String modelo) {
        this.tamanho = tamanho;
        this.marca = marca;
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public String toString(){
        return this.marca + " " + this.modelo + " - Tamanho: " + String.valueOf(this.tamanho);
    }
}
