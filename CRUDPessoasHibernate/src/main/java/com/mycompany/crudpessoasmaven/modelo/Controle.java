package com.mycompany.crudpessoasmaven.modelo;

import com.mycompany.crudpessoasmaven.DAL.Conexao;
import com.mycompany.crudpessoasmaven.DAL.PessoaDAO;
import java.util.List;

public class Controle
{

    private String mensagem;

    public void cadastrarPessoa(List<String> listaDadosPessoa)
    {
        Validacao validacao = new Validacao();
        validacao.validarDadosPessoa(listaDadosPessoa);

        if (!validacao.getMensagem().isEmpty())
        {
            this.mensagem = validacao.getMensagem();
        }
        else
        {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(0);
            pessoa.setNome(listaDadosPessoa.get(1));
            pessoa.setRg(listaDadosPessoa.get(2));
            pessoa.setCpf(listaDadosPessoa.get(3));

            PessoaDAO dao = new PessoaDAO();
            dao.cadastrarPessoa(pessoa);
            this.mensagem = Conexao.mensagem;
        }
    }

    public Pessoa pesquisarPessoa(String numId)
    {
        Validacao validacao = new Validacao();
        validacao.validarId(numId);

        if (!validacao.getMensagem().isEmpty())
        {
            this.mensagem = validacao.getMensagem();
            return null;
        }
        else
        {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(validacao.getId());

            PessoaDAO dao = new PessoaDAO();
            pessoa = dao.pesquisarPessoa(pessoa);

            this.mensagem = Conexao.mensagem;

            return pessoa;
        }
    }

    public void editarPessoa(List<String> listaDadosPessoa)
    {
        Validacao validacao = new Validacao();
        validacao.validarDadosPessoa(listaDadosPessoa);

        if (!validacao.getMensagem().isEmpty())
        {
            this.mensagem = validacao.getMensagem();
        }
        else
        {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(validacao.getId());
            pessoa.setNome(listaDadosPessoa.get(1));
            pessoa.setRg(listaDadosPessoa.get(2));
            pessoa.setCpf(listaDadosPessoa.get(3));

            PessoaDAO dao = new PessoaDAO();
            dao.editarPessoa(pessoa);
            this.mensagem = Conexao.mensagem;
        }
    }

    public void excluirPessoa(String numId)
    {
        Validacao validacao = new Validacao();
        validacao.validarId(numId);

        if (!validacao.getMensagem().isEmpty())
        {
            this.mensagem = validacao.getMensagem();
        }
        else
        {
            Pessoa pessoa = new Pessoa();
            pessoa.setId(validacao.getId());

            PessoaDAO dao = new PessoaDAO();
            dao.excluirPessoa(pessoa);
            this.mensagem = Conexao.mensagem;
        }
    }

    public List<Pessoa> pesquisarPessoaPorNome(String nome)
    {
        if (nome == null || nome.trim().isEmpty())
        {
            this.mensagem = "O nome é obrigatório para a pesquisa.";
            return null;
        }

        if (nome.trim().length() < 3)
        {
            this.mensagem = "O nome deve possuir no mínimo 3 caracteres.";
            return null;
        }

        if (nome.length() > 50)
        {
            this.mensagem = "O nome deve possuir no máximo 50 caracteres.";
            return null;
        }

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);

        PessoaDAO dao = new PessoaDAO();
        List<Pessoa> listaRetorno = dao.pesquisarPessoaporNome(pessoa);

        this.mensagem = Conexao.mensagem;

        return listaRetorno;
    }

    public String getMensagem()
    {
        return mensagem;
    }

}
