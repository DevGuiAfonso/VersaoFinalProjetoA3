package br.com.guilhermafonsoa3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    
    private static final String URL = "jdbc:mysql://localhost:3306/sistemadeeventos";
    private static final String USUARIO = "root";
    private static final String SENHA = "";// Substitua pela sua senha do MySQL

    public static Connection obterConexao() throws SQLException{
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}


