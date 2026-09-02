package com.mycompany.crudpessoasmaven.modelo;

import java.util.List;

public class Validacao
{

    private int id;
    private String mensagem;

    public void validarId(String numId)
    {
        if (numId == null || numId.trim().isEmpty())
        {
            this.mensagem = "ID não informado.";
        }
        else
        {
            try
            {
                this.id = Integer.parseInt(numId.trim());
                this.mensagem = "";
            }
            catch (NumberFormatException e)
            {
                this.mensagem = "ID inválido. O valor informado não é um número inteiro válido.";
            }
        }
    }

    public void validarDadosPessoa(List<String> listaDadosPessoa)
    {
        StringBuilder mensagens = new StringBuilder();

        if (listaDadosPessoa == null || listaDadosPessoa.size() != 4)
        {
            this.mensagem = "A lista de dados deve conter exatamente 4 elementos: id, nome, rg e cpf.";
            return;
        }

        String idStr = listaDadosPessoa.get(0);
        String nome = listaDadosPessoa.get(1);
        String rg = listaDadosPessoa.get(2);
        String cpf = listaDadosPessoa.get(3);

        validarId(idStr);
        if (!this.mensagem.isEmpty())
        {
            mensagens.append(this.mensagem).append(" ");
        }

        if (nome == null || nome.trim().isEmpty())
        {
            mensagens.append("O nome é obrigatório. ");
        }
        else
        {
            if (nome.trim().length() < 3)
            {
                mensagens.append("O nome deve possuir no mínimo 3 caracteres. ");
            }

            if (nome.length() > 50)
            {
                mensagens.append("O nome deve possuir no máximo 50 caracteres. ");
            }
        }

        if (rg != null && !rg.trim().isEmpty())
        {
            if (rg.length() > 11)
            {
                mensagens.append("O RG deve possuir no máximo 11 caracteres. ");
            }
        }

        if (cpf != null && !cpf.trim().isEmpty())
        {
            if (cpf.length() > 13)
            {
                mensagens.append("O CPF deve possuir no máximo 13 caracteres. ");
            }
        }

        this.mensagem = mensagens.toString().trim();
    }

    public int getId()
    {
        return id;
    }

    public String getMensagem()
    {
        return mensagem;
    }
}
