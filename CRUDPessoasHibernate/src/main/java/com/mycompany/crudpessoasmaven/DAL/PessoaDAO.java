package com.mycompany.crudpessoasmaven.DAL;

import com.mycompany.crudpessoasmaven.modelo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class PessoaDAO
{
    private String mensagem;
    
    public void cadastrarPessoa(Pessoa pessoa)
    {
        this.mensagem = "";
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.persist(pessoa);
            transaction.commit();
            session.close();
            this.mensagem = "Pessoa cadastrada !";
        }
        catch (Exception e)
        {
            this.mensagem = "Erro ao cadastrar";
        }
    }

    public Pessoa pesquisarPessoa(Pessoa pessoa)
    {
        this.mensagem = "";
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            pessoa = session.find(Pessoa.class, pessoa.getId());
            session.close();
            if (pessoa == null)
            {
                this.mensagem = "Não existe pessoa com este ID";
            }
        }
        catch (Exception e)
        {
            this.mensagem = "Erro ao pesquisa";
        }
        return pessoa;
    }

    public void editarPessoa(Pessoa pessoa)
    {
        this.mensagem = "";
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.merge(pessoa);
            transaction.commit();
            session.close();
            this.mensagem = "Pessoa editada !";
        }
        catch (Exception e)
        {
            this.mensagem = "Erro ao editar";
        }
    }

    public void excluirPessoa(Pessoa pessoa)
    {
        this.mensagem = "";
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            session.remove(pessoa);
            transaction.commit();
            session.close();
            this.mensagem = "Pessoa excluída !";
        }
        catch (Exception e)
        {
            this.mensagem = "Erro ao excluir";
        }
    }

    public List<Pessoa> pesquisarPessoaporNome(Pessoa pessoa)
    {
        this.mensagem = "";
        List<Pessoa> listaDePessoas = new ArrayList<>();
        try
        {
            Session session = HibernateUtil.getSessionFactory().openSession();

            String hql = "FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(:nome)";
            Query<Pessoa> query = session.createQuery(hql, Pessoa.class);

            query.setParameter("nome", "%" + pessoa.getNome() + "%");

            listaDePessoas = query.getResultList();
            session.close();
            this.mensagem = "Pesquisa realizada com sucesso.";
        }
        catch (Exception e)
        {
            this.mensagem = "Erro ao pesquisar pessoa.";
        }

        return listaDePessoas;
    }
    
    public String getMensagem()
    {
        return mensagem;
    }
}
