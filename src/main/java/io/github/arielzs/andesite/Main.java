package io.github.arielzs.andesite;

import io.github.arielzs.andesite.ui.MenuConsole;

/**
 * Ponto de entrada da aplicação da Andesite Hardware Co.
 *
 * @author Ariel
 */
public class Main {
    private Main() {
        // classe utilitaria: so serve para abrigar o metodo main
    }

    /**
     * Inicia o menu interativo no terminal. O programa só termina quando o usuário
     * escolhe sair ou quando a entrada do teclado é encerrada.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {

        MenuConsole menu = new MenuConsole();
        menu.iniciar();
    }
}
