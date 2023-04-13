package com.example.demo.transfer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Zadanie dotyczy stworzenia samego kontrolera ktory powinien przyjac i zwalidowac kwote do transferu pieniedzy
@RestController
@RequestMapping("/api/money/transfer")
public class MoneyTransferController {

    // 1. Metoda HTTP:
    //    wybralem metode POST, nie PATCH poniewaz ten endpoint prawdopodobnie nie tylko zmodyfikuje istniejace zasoby (ilosc pieniedzy na dwoch kontach) ale takze stworzy rekord w bazie danych odnosnie samego transferu z informacja np. o dacie tego transferu
    //    Zwracam status 200 wraz z kwota odpowiadajaca transferowi - zwrotna informacja jest jednak zalezna od potrzeb klienta naszego API

    // 2. Mapowanie RequestBody, typ response body:
    //    Zastanawailem sie nad konwertowaniem kwoty do BigDecimal od razu w parametrze metody `transferMoney`
    //    Jednakze stwierdzilem ze konwersja do String ma wiecej zalet m.in:
    //    Wartosci String są powszechnie obslugiwane i latwe do serializacji i deserializacji a podczas wymiany danych miedzy systemami wazne jest, aby obie strony rozumialy format wymienianych danych.
    @PostMapping
    public ResponseEntity<String> transferMoney(@RequestBody String amount) {
        // zdecydowalem uzyc RoundingMode.DOWN
        // W przypadkach transferu pieniedzy pomiedzy roznymi walutami mozemy spotkac sie z kwota np. 1.04531 do przeslania
        // W zaleznosci od polityki firmy odpowiadajacej za transfer moze byc to rozwiazane na rozne sposoby
        // Aczkolwiek najkorzystniejsza wersja dla firmy to zaokraglanie tej kwoty w dol, do dwoch cyfr po przecinku
        try {
            BigDecimal money = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);

            if (money.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Money to be transferred must be a positive number");
            }

            return ResponseEntity.ok(money.toString());
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Money to be transferred should be in numerical format");
        }
    }
}
