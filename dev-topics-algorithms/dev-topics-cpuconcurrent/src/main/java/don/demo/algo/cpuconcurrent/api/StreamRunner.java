package don.demo.algo.cpuconcurrent.api;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Process a stream of input using the worker function
 * 
 * @author Donald Trummell
 *
 * @param <C> the type of the returned collection
 * @param <S> the type of stream input
 * @param <T> the type of worker function input
 * @param <U> the type of worker function result
 */
public interface StreamRunner<C, S, T, U> extends Serializable {
	public abstract C runStream(final S stream, final Function<U, T> worker);
}
