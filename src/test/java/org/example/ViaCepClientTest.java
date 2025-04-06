package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ViaCepClientTest {

    ViaCepClient client = new ViaCepClient();

    // 1. Testes para entrada inválida ou inexistente (CEP)
    @Test
    public void testCepInvalidoLetras() {
        assertThrows(IllegalArgumentException.class, () -> {
            client.buscarPorCep("ABCDE");
        });
    }

    @Test
    public void testCepVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            client.buscarPorCep("");
        });
    }

    @Test
    public void testCepComCaracteresEspeciais() {
        assertThrows(IllegalArgumentException.class, () -> {
            client.buscarPorCep("01@#1000");
        });
    }

    @Test
    public void testCepCom7Digitos() {
        assertThrows(IllegalArgumentException.class, () -> {
            client.buscarPorCep("0100100");
        });
    }

    @Test
    public void testCepCom9Digitos() {
        assertThrows(IllegalArgumentException.class, () -> {
            client.buscarPorCep("010010000");
        });
    }

    @Test
    public void testCepValido() throws Exception {
        String response = client.buscarPorCep("01001000");
        assertTrue(response.contains("logradouro"));
    }

    // 2. Testes para consulta por endereço (UF + cidade + logradouro)

    @Test
    public void testEnderecoValido() throws Exception {
        String response = client.buscarPorEndereco("PR", "Paraná", "Marechal Deodoro");
        assertTrue(response.contains("Paranaguá"));
    }

    @Test
    public void testEnderecoComAcento() {
        assertDoesNotThrow(() -> {
            String response = client.buscarPorEndereco("PR", "Paraná", "Marechal Deodoro");
            assertTrue(response.contains("Paranaguá"));
        });
    }

    @Test
    public void testEnderecoInexistente() {
        assertThrows(IllegalArgumentException.class, () -> {
            client.buscarPorEndereco("SP", "Sao Paulo", "RuaInexistente123");
        });
    }

    // 3. Tabela de decisão: UF válida/inválida, cidade com ou sem acentuação, logradouro inexistente

    @Test
    public void testUfInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            client.buscarPorEndereco("XX", "Sao Paulo", "Avenida Paulista");
        });
    }

    @Test
    public void testUfCom1Digito() throws Exception {
        String response = client.buscarPorEndereco("S", "Sao Paulo", "Avenida Paulista");
        assertTrue(response.contains("Http 400") || response.contains("Bad Request"));
    }

    @Test
    public void testUfCom3Digitos() throws Exception {
        String response = client.buscarPorEndereco("SPO", "Sao Paulo", "Avenida Paulista");
        assertTrue(response.contains("Http 400") || response.contains("Bad Request"));
    }

    @Test
    public void testCidadeVazia() throws Exception {
        String response = client.buscarPorEndereco("SP", "", "Avenida Paulista");
        assertTrue(response.contains("Http 400") || response.contains("Bad Request"));
    }

    @Test
    public void testLogradouroVazio() throws Exception {
        String response = client.buscarPorEndereco("SP", "Sao Paulo", "");
        assertTrue(response.contains("Http 400") || response.contains("Bad Request"));
    }
}
