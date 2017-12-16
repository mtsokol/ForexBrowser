package com.oop.browser.subcommands;

import com.oop.browser.builders.TableBuilder;
import com.oop.browser.exceptions.DataNotFoundException;
import com.oop.browser.exceptions.InvalidArgumentsException;
import com.oop.browser.serializable.Table;
import picocli.CommandLine;
import java.io.IOException;
import java.util.Date;

@CommandLine.Command(
        name = "currency"
)
public class CurrencyCommand extends Subcommand implements Runnable {

    @CommandLine.Parameters(index = "0", arity = "1", paramLabel = "SYMBOL",
            description = "symbol")
    private String symbol;

    @CommandLine.Parameters(index = "1", arity = "1", paramLabel = "DATE",
            description = "date")
    private Date date;

    private TableBuilder tableBuilder = new TableBuilder();

    @Override
    public void run() {


        String[] url = generateURL(date);

        try {
            tableBuilder.setURL(url).sendRequest().buildSerializable("Table");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidArgumentsException e) {
            System.out.println("Invalid arguments");
            System.exit(1);
        } catch (DataNotFoundException e) {
            System.out.println("No data for that");
            System.exit(1);
        }

        perform();

    }

    private String[] generateURL(Date date) {
        return new String[]{"http://api.nbp.pl/api/exchangerates/rates/a/" + symbol + "/" + df.format(date) + "/?format=json"};
    }

    private void perform() {
        System.out.println(symbol.toUpperCase() + " price on " + df.format(date) + ":");
        System.out.println(((Table[])tableBuilder.serializable.get(0))[0].getRates()[0].getMid());
    }


}