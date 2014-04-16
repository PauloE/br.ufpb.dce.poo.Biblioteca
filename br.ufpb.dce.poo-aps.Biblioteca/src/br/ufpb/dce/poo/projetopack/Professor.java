package br.ufpb.dce.poo.projetopack;


public class Professor extends UsuarioComposto{

	
	public Professor(String nome, String matricula, String cpf, String departamento){
		super(nome, matricula, cpf);
		super.setDepartamento(departamento);
	}

	public int getQuantDiasEmprestimo() {
		return Configuracao.getInstance().getDiasEmprestimoProfessor();
	}
}