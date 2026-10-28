package com.mycompany.crudpessoasmaven.modelo;

import com.mycompany.crudpessoasmaven.DAL.Conexao;
import com.mycompany.crudpessoasmaven.DAL.PessoaDAO;
import java.util.ArrayList;
import java.util.List;

public class Controle
{

    private String mensagem;

    public void cadastrarPessoa(List<String> listaDadosPessoa)
    {
        this.mensagem = "";
        Validacao validacao = new Validacao();
        validacao.validarDadosPessoa(listaDadosPessoa);

        if (!validacao.getMensagem().isEmpty())
        {
            this.mensagem = validacao.getMensagem();
        }
        else
        {
            Pessoa pessoa = new Pessoa();
            //pessoa.setId(0);
            pessoa.setNome(listaDadosPessoa.get(1));
            pessoa.setRg(listaDadosPessoa.get(2));
            pessoa.setCpf(listaDadosPessoa.get(3));

            PessoaDAO dao = new PessoaDAO();
            dao.cadastrarPessoa(pessoa);
            this.mensagem = dao.getMensagem();
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

            this.mensagem = dao.getMensagem();

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
            this.mensagem = dao.getMensagem();
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
            this.mensagem = dao.getMensagem();
        }
    }

    public List<Pessoa> pesquisarPessoaPorNome(List<String> listaDadosPessoa)
    {
        this.mensagem = "";
        List<Pessoa> listaRetorno = new ArrayList<>();
        Validacao validacao = new Validacao();
        validacao.validarDadosPessoa(listaDadosPessoa);

        if (validacao.getMensagem().equals(""))
        {
            Pessoa pessoa = new Pessoa();
            pessoa.setNome(listaDadosPessoa.get(1));
            PessoaDAO dao = new PessoaDAO();
            listaRetorno = dao.pesquisarPessoaporNome(pessoa);
        }
        else
        {
            this.mensagem = validacao.getMensagem();
        }
        return listaRetorno;
    }

    public String getMensagem()
    {
        return mensagem;
    }

}
