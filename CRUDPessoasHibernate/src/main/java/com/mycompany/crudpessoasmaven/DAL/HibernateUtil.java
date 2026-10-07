package com.mycompany.crudpessoasmaven.DAL;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil
{
    private static SessionFactory sessionFactory = null;
    public static String mensagem;
    
    static 
    {
        try
        {
            Configuration configuration = new Configuration();
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
        }
        catch (HibernateException e)
        {
            mensagem = e.getMessage();
        }
    }
    
    public static SessionFactory getSessionFactory()
    {
        mensagem = "";
        return sessionFactory;
    }
}
