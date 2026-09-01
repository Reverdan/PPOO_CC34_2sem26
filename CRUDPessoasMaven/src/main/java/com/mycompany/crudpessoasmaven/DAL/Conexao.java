package com.mycompany.crudpessoasmaven.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
create database cc34
go
use cc34
go
create table Pessoas
(
	id int primary key identity(1,1),
	nome varchar(50) not null,
	rg varchar(11),
	cpf varchar(13)
)
jdbc:sqlserver://localhost\DESKTOP-0BMMDJG\SQLEXPRESS:1433;databaseName=cc34; encrypt=false
 */
public class Conexao
{
    Connection conexao;
    public String mensagem;
    
    public Connection conectar()
    {
        this.mensagem = "";
        try
        {
            if (conexao == null || conexao.isClosed())
            {
                conexao = DriverManager.getConnection(
                        "jdbc:sqlserver://localhost\\DESKTOP-0BMMDJG\\SQLEXPRESS:1433;"
                                + "databaseName=cc34; encrypt=false", "sa", "unip");
            }
        } 
        catch (SQLException e)
        {
            this.mensagem = "Erro de conexâo com BD";
        }
        return conexao;
    }
    
    public void desconectar()
    {
        try
        {
            if (!conexao.isClosed())
            {
                conexao.close();
            }
        } 
        catch (SQLException e)
        {
            this.mensagem = "Erro ao fechar BD";
        }
    }
}
