package web.controlevacinacao.model.filter;

import java.time.LocalDate;

import web.controlevacinacao.model.fidex_model.Status;

public class PessoaFilter {

    private Long codigo;
    private String nome;
    private String cpf;
    private LocalDate dataNascimentoInicial;
    private LocalDate dataNascimentoFinal;
    private String profissao;
    private Status status;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimentoInicial() {
        return dataNascimentoInicial;
    }

    public void setDataNascimentoInicial(LocalDate dataNascimentoInicial) {
        this.dataNascimentoInicial = dataNascimentoInicial;
    }

    public LocalDate getDataNascimentoFinal() {
        return dataNascimentoFinal;
    }

    public void setDataNascimentoFinal(LocalDate dataNascimentoFinal) {
        this.dataNascimentoFinal = dataNascimentoFinal;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "codigo: " + codigo + "\nnome: " + nome + "\ncpf: " + cpf + "\ndataNascimentoInicial: "
                + dataNascimentoInicial + "\ndataNascimentoFinal: " + dataNascimentoFinal + "\nprofissao: " + profissao
                + "\nstatus: " + status;
    }

}
