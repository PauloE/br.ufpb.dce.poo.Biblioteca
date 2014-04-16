package br.ufpb.dce.poo.projetopack;



import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppSerializacao {

    public static void main(String[] args) {

        ArrayList<Tenis> listaTenis = new ArrayList<Tenis>();
        listaTenis.add(new Tenis(44, "Nike", "Shoks TL 2"));
        listaTenis.add(new Tenis(42, "Adidas", "Microbounce"));

        serializaListaTenis(listaTenis, "C:\\dados\\serializados.dat");

        ArrayList<Tenis> listaRecuperada = deserializaListaTenis("C:\\dados\\serializados.dat");

        for (Tenis t : listaRecuperada) {
            System.out.println(t);
        }
    }

    private static void serializaListaTenis(ArrayList<Tenis> listaTenis, String arquivo) {
        FileOutputStream arq = null;
        ObjectOutputStream out = null;
        try {
            //arquivo no qual os dados vao ser gravados
            arq = new FileOutputStream(arquivo);

            //objeto que vai escrever os dados
            out = new ObjectOutputStream(arq);

            //escreve todos os dados
            out.writeObject(listaTenis);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                arq.close();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static ArrayList<Tenis> deserializaListaTenis(String arquivo) {
        FileInputStream arqLeitura = null;
        ObjectInputStream in = null;
        ArrayList<Tenis> lista = null;
        try {
            //arquivo onde estao os dados serializados
            arqLeitura = new FileInputStream(arquivo);

            //objeto que vai ler os dados do arquivo
            in = new ObjectInputStream(arqLeitura);

            //recupera os dados
            lista = (ArrayList<Tenis>) in.readObject();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                arqLeitura.close();
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return lista;
    }
}
