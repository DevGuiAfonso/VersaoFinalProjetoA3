package br.com.guilhermafonsoa3;

import java.time.LocalDate;
import java.time.LocalTime;

public record Evento(
    Long id,
    String nome,
    String local,
    LocalDate data,
    LocalTime horario,
    String categoria,
    String descricao
) {
    public Evento(String nome, String local, LocalDate data, LocalTime horario, String categoria, String descricao) {
        this(null, nome, local, data, horario, categoria, descricao);
    }
}
