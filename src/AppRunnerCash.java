import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunnerCash {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CashAcceptor cashAcceptorMy;

    private static boolean isExit = false;


    private AppRunnerCash() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        cashAcceptorMy = new CashAcceptor(100);
    }

    public static void run() {
        AppRunnerCash app = new AppRunnerCash();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны: " + cashAcceptorMy.getAmount());
        print("либо пополните сумму...");
        System.out.println("----------------");
        showProducts(products);
        System.out.println("----------------");

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (cashAcceptorMy.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            cashAcceptorMy.setAmount(cashAcceptorMy.getAmount() + 10);
            print("Вы пополнили баланс на 10");
            return;
        }


        if ("h".equalsIgnoreCase(action) && cashAcceptorMy.getAmount() == 0) {
            isExit = true;
        }

        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    cashAcceptorMy.setAmount(cashAcceptorMy.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName() + "\n");
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                System.out.println("ваш остаток, заберите: " + cashAcceptorMy.getAmount());
                isExit = true;
            } else {
                print("Недопустимая буква. Попробуйте еще раз.");
                chooseAction(products);
            }

        }
    }


    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
