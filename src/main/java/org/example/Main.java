package org.example;

public class Main {
    public static void main(String[] args) {
        ViaCepClient client = new ViaCepClient();

        try {
            String resultadoCep = client.buscarPorCep("80530230");
            System.out.println("Resultado da busca por CEP:");
            System.out.println(resultadoCep);

            String resultadoEndereco = client.buscarPorEndereco("PR", "Paraná", "Marechal Deodoro");
            System.out.println("\nResultado da busca por Endereço:");
            System.out.println(resultadoEndereco);

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}
