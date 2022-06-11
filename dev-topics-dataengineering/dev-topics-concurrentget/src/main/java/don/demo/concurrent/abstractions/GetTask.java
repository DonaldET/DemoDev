package don.demo.concurrent.abstractions;

import java.util.function.Consumer;

/**
 * Defines what a GET Task does
 */
public interface GetTask extends Consumer<String> {
}
