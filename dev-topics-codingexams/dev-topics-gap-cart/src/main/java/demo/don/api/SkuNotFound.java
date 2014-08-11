package demo.don.api;

public class SkuNotFound extends RuntimeException
{
  private static final long serialVersionUID = -2400927512056318274L;

  public SkuNotFound()
  {
  }

  public SkuNotFound(final String message)
  {
    super(message);
  }

  public SkuNotFound(final Throwable cause)
  {
    super(cause);
  }

  public SkuNotFound(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  public SkuNotFound(final String message, final Throwable cause,
      final boolean enableSuppression, final boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
