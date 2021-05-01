package com.electiva3.proyecto_android_electiva3.entities;

public class GeneradorPassword
{

    private String NUMEROS = "0123456789";

    private String MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";

    public String getPassword()
    {
        String lista = NUMEROS+MAYUSCULAS+MINUSCULAS;

        String pswd = "";

        for (int i = 0; i < 8; i++)
        {
            pswd+=(lista.charAt((int)(Math.random() * lista.length())));
        }

        return pswd;
    }
}
