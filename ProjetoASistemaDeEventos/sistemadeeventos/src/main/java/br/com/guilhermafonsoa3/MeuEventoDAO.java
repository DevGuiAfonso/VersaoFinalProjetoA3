package br.com.guilhermafonsoa3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MeuEventoDAO {
    private static final String ARQUIVO = "meus_eventos.data";

    public void salvar(List<Evento> meusEventos) throws Exception {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            out.writeObject(meusEventos);
        }
    }
    
@SuppressWarnings("unchecked")
    public List<Evento> buscarTodos() throws Exception {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            return (List<Evento>) in.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        }
    }
}
