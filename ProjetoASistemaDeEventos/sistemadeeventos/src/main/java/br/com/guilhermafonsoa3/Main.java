package br.com.guilhermafonsoa3;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static EventoDAO eventoDAO = new EventoDAO();
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static Usuario usuario;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Cadastro inicial do usuário (salvo no .data)
        System.out.println("Cadastro de Usuário:");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        Long id = System.currentTimeMillis(); // gera um ID único baseado no tempo atual
        usuario = new Usuario(id, nome, email, telefone);


        try {
            usuarioDAO.salvar(usuario);
            System.out.println("Usuário salvo com sucesso em usuarios.data!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
        }

        int opcao;
        do {
            System.out.println("\n=== MENU ===");
            System.out.println("1. Cadastrar evento");
            System.out.println("2. Ver eventos disponíveis");
            System.out.println("3. Confirmar participação");
            System.out.println("4. Ver meus eventos");
            System.out.println("5. Cancelar participação");
            System.out.println("6. Ver eventos em andamento");
            System.out.println("7. Ver eventos passados");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1 -> cadastrarEvento();
                case 2 -> listarEventos();
                case 3 -> confirmarParticipacao();
                case 4 -> verMeusEventos();
                case 5 -> cancelarParticipacao();
                case 6 -> eventosEmAndamento();
                case 7 -> eventosPassados();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

private static void cadastrarEvento() {
    System.out.println("Cadastro de Evento:");
    System.out.print("Nome: ");
    String nome = scanner.nextLine();
    System.out.print("Local: ");
    String local = scanner.nextLine();
    System.out.print("Data (yyyy-MM-dd) [pressione Enter para deixar em branco]: ");
    String dataStr = scanner.nextLine();
    System.out.print("Horário (HH:mm) [pressione Enter para deixar em branco]: ");
    String horarioStr = scanner.nextLine();
    System.out.print("Categoria: ");
    String categoria = scanner.nextLine();
    System.out.print("Descrição: ");
    String descricao = scanner.nextLine();

    try {
        LocalDate data = null;
        LocalTime horario = null;

        if (!dataStr.isBlank()) {
            data = LocalDate.parse(dataStr);
        }

        if (!horarioStr.isBlank()) {
            horario = LocalTime.parse(horarioStr);
        }

        Evento evento = new Evento(null, nome, local, data, horario, categoria, descricao);
        eventoDAO.salvar(evento);
        System.out.println("Evento salvo com sucesso!");
    } catch (Exception e) {
        System.out.println("Erro ao salvar evento: " + e.getMessage());
    }
}



    private static void listarEventos() {
        try {
            var eventos = eventoDAO.buscarTodos();
            if (eventos.isEmpty()) {
                System.out.println("Nenhum evento cadastrado.");
            } else {
                System.out.println("Eventos Disponíveis:");
                for (Evento evento : eventos) {
                    System.out.println(evento);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar eventos: " + e.getMessage());
        }
    }

    
//Lista os eventos do usuário
    private static List<Evento> meusEventos = new ArrayList<>();
    private static MeuEventoDAO meuEventoDAO = new MeuEventoDAO();



    // Confirmar participação em um evento

    private static void confirmarParticipacao() {
        try {
            List<Evento> eventos = eventoDAO.buscarTodos();
            if (eventos.isEmpty()) {
                System.out.println("Nenhum evento disponível.");
                return;
            }

            System.out.println("Eventos disponíveis:");
            for (Evento evento : eventos) {
                System.out.println(evento);
            }

            System.out.print("Digite o ID do evento que deseja participar: ");
            Long idEscolhido = Long.parseLong(scanner.nextLine());

            Evento eventoSelecionado = null;
            for (Evento evento : eventos) {
                if (evento.id().equals(idEscolhido)) {
                    eventoSelecionado = evento;
                    break;
                }
            }

            if (eventoSelecionado != null) {
                meusEventos.add(eventoSelecionado);
                meuEventoDAO.salvar(meusEventos);
                System.out.println("Participação confirmada com sucesso!");
            } else {
                System.out.println("Evento não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Participação confirmada com sucesso!"   );
        }
    }



// Ver meus eventos confirmados 

    private static void verMeusEventos() {
        if (meusEventos.isEmpty()) {
            System.out.println("Você não confirmou participação em nenhum evento.");
        } else {
            System.out.println("Meus Eventos:");
            for (Evento evento : meusEventos) {
                System.out.println(evento);
            }
        }
    }




// Cancelar participação em um evento

    private static void cancelarParticipacao() {
        if (meusEventos.isEmpty()) {
            System.out.println("Nenhum evento para cancelar.");
            return;
        }

        System.out.println("Meus Eventos:");
        for (Evento evento : meusEventos) {
            System.out.println(evento);
        }

        System.out.print("Digite o ID do evento que deseja cancelar: ");
        Long idCancelar = Long.parseLong(scanner.nextLine());

        Evento eventoParaRemover = null;
        for (Evento evento : meusEventos) {
            if (evento.id().equals(idCancelar)) {
                eventoParaRemover = evento;
                break;
            }
        }

        if (eventoParaRemover != null) {
            meusEventos.remove(eventoParaRemover);
            try {
                meuEventoDAO.salvar(meusEventos);
                System.out.println("Participação cancelada com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao atualizar lista de eventos: " + e.getMessage());
            }
        } else {
            System.out.println("Evento não encontrado na sua lista.");
        }
    }


// Ver eventos em andamento (hoje, dentro de 60 minutos)
    private static void eventosEmAndamento() {
        try {
            var eventos = eventoDAO.buscarTodos();
            LocalDate dataAtual = LocalDate.now();
            LocalTime horaAtual = LocalTime.now();

            List<Evento> eventosEmAndamento = new ArrayList<>();

            for (Evento evento : eventos) {
                // Verifica se a data e o horário não são nulos antes de comparar
                if (evento.data() != null && evento.horario() != null) {
                    // Se a data for hoje
                    if (evento.data().equals(dataAtual)) {
                        // Define o intervalo de tolerância de 1 hora
                        LocalTime horaMinima = horaAtual.minusHours(1);
                        LocalTime horaMaxima = horaAtual.plusHours(1);

                        // Verifica se o horário do evento está dentro desse intervalo
                        if (!evento.horario().isBefore(horaMinima) && !evento.horario().isAfter(horaMaxima)) {
                            eventosEmAndamento.add(evento);
                        }
                    }
                }
            }

            if (!eventosEmAndamento.isEmpty()) {
                System.out.println("Eventos disponíveis no momento:");
                for (Evento evento : eventosEmAndamento) {
                    System.out.println(evento);
                }
            } else {
                System.out.println("No momento não há eventos disponíveis em Porto Alegre.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar eventos em andamento: " + e.getMessage());
        }
    }



// Ver eventos passados (antes da data atual) ((data referente ao dia que o usuário está executando o programa))

    private static void eventosPassados() {
        try {
            var eventos = eventoDAO.buscarTodos();
            LocalDate dataAtual = LocalDate.now();

            List<Evento> eventosPassados = new ArrayList<>();

            for (Evento evento : eventos) {
                // Verifica se a data não é nula antes de comparar
                if (evento.data() != null) {
                    // Se a data do evento for antes da data atual, é considerado passado
                    if (evento.data().isBefore(dataAtual)) {
                        eventosPassados.add(evento);
                    }
                }
            }

            if (!eventosPassados.isEmpty()) {
                System.out.println("Estes são os eventos que já passaram:");
                for (Evento evento : eventosPassados) {
                    System.out.println(evento);
                }
            } else {
                System.out.println("Não há eventos passados registrados.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao verificar eventos passados: " + e.getMessage());
        }
    }

}
