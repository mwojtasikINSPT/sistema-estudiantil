package sistemaestudiantil;

import sistemaestudiantil.views.MenuPrincipalView;

public class Main {

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        System.setErr(new java.io.PrintStream(System.err, true, java.nio.charset.StandardCharsets.UTF_8));

        MenuPrincipalView menu = new MenuPrincipalView();
        menu.mostrarMenu();
    }
}
