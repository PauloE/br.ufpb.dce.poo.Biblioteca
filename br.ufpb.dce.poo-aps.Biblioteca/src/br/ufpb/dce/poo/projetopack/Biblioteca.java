package br.ufpb.dce.poo.projetopack;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;


public class Biblioteca {
	Configuracao configuracao = Configuracao.getInstance();
	List<Livro> livros;
	List<Emprestimo> emprestimosAtivos;
	List<Usuario> usuarios;
	
	public void CadastrarLivro (Livro l){
		for (Livro lv: this.livros){
			if (lv.getCodigo().equals(l.getCodigo())){
				lv.setQuantidade(lv.getQuantidade()+l.getQuantidade());
			}
			
		}
		this.livros.add(l);
	}
	
	public void CadastrarUsuario (Usuario u) throws UsuarioJaExisteException{
		if(this.usuarios.contains(u)){
			throw new UsuarioJaExisteException ("O usuário já existe.");
		}
		this.usuarios.add(u);
	}
	
	public Usuario getUsuario(String mat) throws UsuarioInexistenteException{
		for (Usuario u: usuarios){
			if (u.getMatricula().equals(mat)){
				return u;
			}
		}
		throw new UsuarioInexistenteException ("Este usuário não existe");
	}
	
	public Livro getLivro (String codLivro) throws LivroInexistenteException{
		for (Livro l: livros){
			if(l.getCodigo().equals(codLivro)){
				return l;
			}
		}
		throw new LivroInexistenteException ("Este Livro não está cadastrado");
	}
	
	public List<Emprestimo> listarEmprestimosEmAtraso () throws ListaDeAtrasoInexistenteException{
		Calendar DiaDeHoje = Calendar.getInstance();
		List<Emprestimo> atraso = new LinkedList<Emprestimo>();
		for (Emprestimo e: this.emprestimosAtivos){
			if (e.getDataDevolucao().before(DiaDeHoje)){
				atraso.add(e);
			}
		}
		if (atraso.size() == 0) {
			throw new ListaDeAtrasoInexistenteException ("Não existem usuarios em atraso");
		}
		return atraso;
		
	}
	
	public double calcularMulta (Usuario u) throws ListaDeAtrasoInexistenteException{
		
		long diasAtraso = 0;
		Configuracao c = Configuracao.getInstance();
		double total = 0;
		for (Emprestimo e: this.listarEmprestimosEmAtraso()){
			if (e.getUsuario().equals(u)){
				diasAtraso += diasEntre(e.getDataDevolucao(), Calendar.getInstance());
			}
			
		}
		total = diasAtraso * c.getValorMulta();
		
		return total;
		
		
	}
	
	public long diasEntre(Calendar a, Calendar b){ 
		Calendar dInicial = a;
		Calendar dFinal = b;
		long diferenca = dFinal.getTimeInMillis() - dInicial.getTimeInMillis();
		int tempoDia = 1000 * 60 * 60 * 24;
		long diasDiferenca = diferenca / tempoDia;
		return diasDiferenca;
	   }
	
	public Calendar calculaDataDevolucaoProfessor(){
		Calendar dataAtual = Calendar.getInstance();
		System.out.println(dataAtual);
		dataAtual.set(Calendar.DAY_OF_MONTH, 30);
		System.out.println(dataAtual);
		
		return dataAtual;
	}
	
	public void emprestarLivro (Usuario u, Livro lv) throws NumeroDeLivrosEmprestadosException, UsuarioEmAtrasoException, QuantidadeDeLivrosInsuficienteException, ListaDeAtrasoInexistenteException{
		if (u.getEmprestimos().size() == 3){
			throw new NumeroDeLivrosEmprestadosException ("Usuário atingiu limite de emprestimos");
		}
		for (Emprestimo e: this.listarEmprestimosEmAtraso()){
			if (e.getUsuario().equals(u)){
				throw new UsuarioEmAtrasoException ("O Caboclo está devendo, cobre ao danado!");
			}
		}
		for (Livro l: this.livros){
			if (l.getCodigo().equals(lv.getCodigo()) && l.getQuantidade() == 1){
				throw new QuantidadeDeLivrosInsuficienteException("Quantidade de livros insuficientes para emprestimo.");
			}
		}
		
		Calendar diaDevolucao = Calendar.getInstance();
		diaDevolucao.add(Calendar.DAY_OF_YEAR, u.getQuantDiasEmprestimo());
		Emprestimo e = new Emprestimo (u, lv, Calendar.getInstance(), diaDevolucao);
		for (Livro livro: this.livros){
			if (livro.getCodigo().equals(lv.getCodigo())){
				lv.setQuantidade(lv.getQuantidade()-1);
			}
		}
		this.emprestimosAtivos.add(e);
		u.adicionarEmprestimo(e);		
			
	}
	
	public void devolverLivro (Usuario u, Livro lv)throws EmprestimoInexistenteException{
		boolean emprestou = false;
		for (Emprestimo el: u.getEmprestimos()){
			if (el.getLivro().equals(lv)){
				for (Emprestimo el2: this.emprestimosAtivos){
					if (el2.getLivro().equals(lv) && el2.getUsuario().equals(u)){
						this.emprestimosAtivos.remove(el2);
						u.getEmprestimos().remove(el2);
						emprestou = true;
						for (Livro livro : this.livros){
							if (livro.getCodigo().equals(lv.getCodigo())){
								livro.setQuantidade(livro.getQuantidade()+1);
							}
						}
					}
				}
			}
		}
		if (emprestou == false){
			throw new EmprestimoInexistenteException ("O usuário não possui o emprestimo referênte.");
		}
		
	}
	
	
	//##########################       PERSISTÊNCIA DE ARQUIVOS      ############################################
	
	public void gravarEmprestimos(String nomeArquivo) throws IOException {
		BufferedWriter gravador = null;
		try {
			gravador = new BufferedWriter(new FileWriter("C:\\date\\datas.txt"));
			for (Emprestimo e: this.emprestimosAtivos){
				gravador.write(e.getUsuario().getMatricula() +"\n");
				gravador.write(e.getLivro().getCodigo() +"\n");
				
				gravador.write(e.getDataEmprestimo().get(Calendar.DAY_OF_MONTH)+"\n");
				gravador.write(e.getDataEmprestimo().get(Calendar.MONTH) +"\n");
				gravador.write(e.getDataEmprestimo().get(Calendar.YEAR) +"\n");
				
				gravador.write(e.getDataDevolucao().get(Calendar.DAY_OF_MONTH) +"\n");
				gravador.write(e.getDataDevolucao().get(Calendar.MONTH) +"\n");
				gravador.write(e.getDataDevolucao().get(Calendar.YEAR) +"\n");
			}
		} finally {
			if (gravador!=null){
				gravador.close();
			}
		}		
	}
	
	public void carregarEmprestimosDeArquivo(String nomeArquivo) throws IOException, EmprestimoJaExisteException, UsuarioInexistenteException, LivroInexistenteException{
		BufferedReader leitor = null;
		try {
			leitor = new BufferedReader(new FileReader(nomeArquivo));
			String matricula = null;
			String codigoLivro;
			
			String diaEmprestimo;
			String mesEmprestimo;
			String anoEmprestimo;
			
			String diaDevolucao;
			String mesDevolucao;
			String anoDevolucao;
			
			
			do {
				matricula = leitor.readLine(); // lê a próxima linha do arquivo: matricula do usuário
				codigoLivro = leitor.readLine();
				
				diaEmprestimo = leitor.readLine();
				mesEmprestimo = leitor.readLine();
				anoEmprestimo = leitor.readLine();
				
				diaDevolucao = leitor.readLine();
				mesDevolucao = leitor.readLine();
				anoDevolucao = leitor.readLine();
				
				Usuario usuario = this.getUsuario(matricula); 
				Livro livro = this.getLivro(codigoLivro); 
				
				Calendar dataEmprestimo = Calendar.getInstance();
				dataEmprestimo.set(Integer.parseInt(diaEmprestimo),Integer.parseInt(mesEmprestimo) , Integer.parseInt(anoEmprestimo));
				
				Calendar dataDevolucao = Calendar.getInstance();
				dataDevolucao.set(Integer.parseInt(anoDevolucao), Integer.parseInt(mesDevolucao), Integer.parseInt(diaDevolucao));
				
				Emprestimo emprestimo = new Emprestimo(usuario, livro, dataEmprestimo, dataDevolucao);
				
				this.emprestimosAtivos.add(emprestimo);
				usuario.adicionarEmprestimo(emprestimo);
				
			} 
			while(matricula != null); //vai ser null quando chegar no fim do arquivo
		} 
		finally {
			if (leitor!=null){
				leitor.close();
			}
		}
		
	}
	
	
	
	public static void main(String[] args) {
		
		Biblioteca bib = new Biblioteca();
		bib.CadastrarUsuario(new Aluno("Odravison", "12345", ""));
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}



