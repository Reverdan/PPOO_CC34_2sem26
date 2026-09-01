package com.mycompany.crudpessoasmaven.DAL;

import com.mycompany.crudpessoasmaven.modelo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaDAO
{

    Conexao conexao = new Conexao();

    public void cadastrarPessoa(Pessoa pessoa)
    {

        Connection conn = conexao.conectar();

        if (conn != null)
        {
            String sql = "INSERT INTO Pessoas (nome, rg, cpf) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql))
            {
                stmt.setString(1, pessoa.getNome());
                stmt.setString(2, pessoa.getRg());
                stmt.setString(3, pessoa.getCpf());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0)
                {
                    conexao.mensagem = "Pessoa cadastrada com sucesso.";
                }
                else
                {
                    conexao.mensagem = "Falha ao cadastrar a pessoa. Nenhuma linha foi afetada.";
                }
            }
            catch (SQLException e)
            {
                conexao.mensagem = "Erro ao executar a operação no banco de dados: " + e.getMessage();
            }
            finally
            {
                //conexao.desconectar();
            }
        }
    }

    public Pessoa pesquisarPessoa(Pessoa pessoa)
    {
        Connection conn = conexao.conectar();

        if (conn != null)
        {
            String sql = "SELECT id, nome, rg, cpf FROM Pessoas WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql))
            {
                stmt.setInt(1, pessoa.getId());

                try (ResultSet rs = stmt.executeQuery())
                {
                    if (rs.next())
                    {
                        pessoa.setId(rs.getInt("id"));
                        pessoa.setNome(rs.getString("nome"));
                        pessoa.setRg(rs.getString("rg"));
                        pessoa.setCpf(rs.getString("cpf"));
                    }
                }
            }
            catch (SQLException e)
            {
                conexao.mensagem = "Erro ao pesquisar pessoa: " + e.getMessage();
            }
            finally
            {
                conexao.desconectar();
            }
        }

        return pessoa;
    }

    public void editarPessoa(Pessoa pessoa)
    {
        Connection conn = conexao.conectar();

        if (conn != null)
        {
            String sql = "UPDATE Pessoas SET nome = ?, rg = ?, cpf = ? WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql))
            {
                stmt.setString(1, pessoa.getNome());
                stmt.setString(2, pessoa.getRg());
                stmt.setString(3, pessoa.getCpf());
                stmt.setInt(4, pessoa.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0)
                {
                    conexao.mensagem = "Pessoa atualizada com sucesso.";
                }
                else
                {
                    conexao.mensagem = "Falha ao atualizar a pessoa. Nenhuma linha foi afetada ou o ID não foi encontrado.";
                }
            }
            catch (SQLException e)
            {
                conexao.mensagem = "Erro ao executar a operação no banco de dados: " + e.getMessage();
            }
            finally
            {
                conexao.desconectar();
            }
        }
    }

    public void excluirPessoa(Pessoa pessoa)
    {
        Connection conn = conexao.conectar();

        if (conn != null)
        {
            String sql = "DELETE FROM Pessoas WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql))
            {
                stmt.setInt(1, pessoa.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas > 0)
                {
                    conexao.mensagem = "Pessoa excluída com sucesso.";
                }
                else
                {
                    conexao.mensagem = "Falha ao excluir a pessoa. Nenhuma linha foi afetada ou o ID não foi encontrado.";
                }
            }
            catch (SQLException e)
            {
                conexao.mensagem = "Erro ao executar a operação no banco de dados: " + e.getMessage();
            }
            finally
            {
                conexao.desconectar();
            }
        }
    }

    public List<Pessoa> pesquisarPessoaporNome(Pessoa pessoa)
    {
        Connection conn = conexao.conectar();
        List<Pessoa> listaPessoas = new ArrayList<>();

        if (conn != null)
        {
            String sql = "SELECT id, nome, rg, cpf FROM Pessoas WHERE nome LIKE ?";

            try (PreparedStatement stmt = conn.prepareStatement(sql))
            {
                stmt.setString(1, "%" + pessoa.getNome() + "%");

                try (ResultSet rs = stmt.executeQuery())
                {
                    while (rs.next())
                    {
                        Pessoa p = new Pessoa();
                        p.setId(rs.getInt("id"));
                        p.setNome(rs.getString("nome"));
                        p.setRg(rs.getString("rg"));
                        p.setCpf(rs.getString("cpf"));

                        listaPessoas.add(p);
                    }
                }
            }
            catch (SQLException e)
            {
                conexao.mensagem = "Erro ao pesquisar pessoas por nome: " + e.getMessage();
            }
            finally
            {
                conexao.desconectar();
            }
        }

        return listaPessoas;
    }
}
