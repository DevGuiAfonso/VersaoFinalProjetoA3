package br.com.guilhermafonsoa3;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {
    
public void salvar(Evento evento) throws Exception {
    var sql = "INSERT INTO evento (nome, local, data, horario, categoria, descricao) VALUES (?, ?, ?, ?, ?, ?)";

    try (var conexao = Conexao.obterConexao();
         var stmt = conexao.prepareStatement(sql)) {

        stmt.setString(1, evento.nome());
        stmt.setString(2, evento.local());

        // Verifica se data é nula
        if (evento.data() != null) {
            stmt.setDate(3, java.sql.Date.valueOf(evento.data()));
        } else {
            stmt.setNull(3, java.sql.Types.DATE);
        }

        // Verifica se horário é nulo
        if (evento.horario() != null) {
            stmt.setTime(4, java.sql.Time.valueOf(evento.horario()));
        } else {
            stmt.setNull(4, java.sql.Types.TIME);
        }

        stmt.setString(5, evento.categoria());
        stmt.setString(6, evento.descricao());

        stmt.executeUpdate();

    } catch (SQLException e) {
        throw new Exception(e);
    }
}


    public List<Evento> buscarTodos() throws Exception {
        List<Evento> eventos = new ArrayList<>();
        var sql = "SELECT * FROM evento";

        try (var conexao = Conexao.obterConexao();
             var stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Evento evento = montarEvento(rs);
                eventos.add(evento);
            }

        } catch (SQLException e) {
            throw new Exception(e);
        }

        return eventos;
    }

    public Evento buscarPorId(Long id) throws Exception {
        Evento evento = null;
        var sql = "SELECT * FROM evento WHERE id = ?";

        try (var conexao = Conexao.obterConexao();
             var stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    evento = montarEvento(rs);
                }
            }

        } catch (SQLException e) {
            throw new Exception(e);
        }

        return evento;
    }

    public void atualizar(Evento evento) throws Exception {
        var sql = "UPDATE evento SET nome = ?, local = ?, data = ?, horario = ?, categoria = ?, descricao = ? WHERE id = ?";

        try (var conexao = Conexao.obterConexao();
             var stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, evento.nome());
            stmt.setString(2, evento.local());
            stmt.setDate(3, java.sql.Date.valueOf(evento.data()));
            stmt.setTime(4, java.sql.Time.valueOf(evento.horario()));
            stmt.setString(5, evento.categoria());
            stmt.setString(6, evento.descricao());
            stmt.setLong(7, evento.id());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    public void excluir(Long id) throws Exception {
        var sql = "DELETE FROM evento WHERE id = ?";

        try (var conexao = Conexao.obterConexao();
             var stmt = conexao.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new Exception(e);
        }
    }

    // Método auxiliar para reduzir repetição de código
    private Evento montarEvento(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        String nome = rs.getString("nome");
        String local = rs.getString("local");

        // Trata possíveis valores nulos para data e horario
        LocalDate data = null;
        LocalTime horario = null;

        java.sql.Date sqlDate = rs.getDate("data");
        if (sqlDate != null) {
            data = sqlDate.toLocalDate();
        }

        java.sql.Time sqlTime = rs.getTime("horario");
        if (sqlTime != null) {
            horario = sqlTime.toLocalTime();
        }

        String categoria = rs.getString("categoria");
        String descricao = rs.getString("descricao");

        return new Evento(id, nome, local, data, horario, categoria, descricao);
    }

}
