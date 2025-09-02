package ca.nrc.cadc.groups.web;

import java.io.IOException;


/**
 * For form input validation failures.
 */
public class IllegalInputException extends IOException
{
  public IllegalInputException(final String message)
  {
    super(message);
  }
}

