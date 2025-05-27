package aula.javafx.javafxmaven.persistencia;

import java.util.ArrayList;

public class Programa {
    public static void main(String[] args) {

        // Criando e adicionando objetos
        Pessoa p1 = new Pessoa("Abimael", "11111111111");
        Pessoa p2 = new Pessoa("Zé Grillo", "22222222222");

        // Persistindo os objetos no arquivo
        ArquivoPessoa.adicionarPessoa(p1);
        ArquivoPessoa.adicionarPessoa(p2);

        // Lendo todos os objetos gravados
        ArrayList<Pessoa> pessoasLidas = ArquivoPessoa.lerLista();

        System.out.println("Pessoas obtidas do arquivo:");
        for (Pessoa p : pessoasLidas) {
            System.out.println(p);
        }
    }
}
