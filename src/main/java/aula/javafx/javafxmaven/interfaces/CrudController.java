package aula.javafx.javafxmaven.interfaces;

import javafx.stage.Stage;

public interface CrudController<T> {
    void criarUI();
    void abrirFormulario();
    void salvar();
    void carregar();
    void abrirFormularioEdicao(T item);
}