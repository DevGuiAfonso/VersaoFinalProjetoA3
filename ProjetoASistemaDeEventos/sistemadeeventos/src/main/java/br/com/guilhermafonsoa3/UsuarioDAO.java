package br.com.guilhermafonsoa3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static final String ARQUIVO = "usuarios.data";

    public void salvar(Usuario usuario) throws Exception {
        List<Usuario> usuarios = buscarTodos();
        usuarios.add(usuario);

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARQUIVO))) {
            out.writeObject(usuarios);
        }
    }
@SuppressWarnings("unchecked")
    public List<Usuario> buscarTodos() throws Exception {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) return new ArrayList<>();

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(ARQUIVO))) {
            return (List<Usuario>) in.readObject();
        } catch (EOFException e) {
            return new ArrayList<>(); // Arquivo vazio
        }
    }
}
