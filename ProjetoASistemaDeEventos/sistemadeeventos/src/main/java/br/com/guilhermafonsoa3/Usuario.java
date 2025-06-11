package br.com.guilhermafonsoa3;

public class Usuario extends Pessoa {
    private static final long serialVersionUID = 1L;
    private String email;
    private String telefone;

    public Usuario() {
        // necessário para deserialização
    }

    public Usuario(Long id, String nome, String email, String telefone) {
        super(id, nome);
        this.email = email;
        this.telefone = telefone;
    }

    @Override
    public String getDescricao() {
        return "Usuário: " + nome;
    }

    @Override
    public String getContato() {
        return "Email: " + email + ", Telefone: " + telefone;
    }
}
