/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.serialize.hack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

public class HackUtil implements Serializable
{
  private static final long serialVersionUID = -5191407569726358722L;

  public static final int BYTES_IN_LONG = Long
      .numberOfLeadingZeros(new Long(0l)) / 8;

  private HackUtil()
  {
  }

  public static byte[] longToBytes(final long value)
  {
    return ByteBuffer.allocate(BYTES_IN_LONG).putLong(value).array();
  }

  public static byte[] serializableToBytes(final Serializable obj)
  {
    if (obj == null)
      throw new IllegalArgumentException("obj null");

    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try
    {
      ObjectOutput output = new ObjectOutputStream(bos);
      try
      {
        output.writeObject(obj);
      }
      finally
      {
        output.close();
      }
    }
    catch (final IOException ex)
    {
      throw new IllegalStateException("I/O error serializing: "
          + ex.getMessage(), ex);
    }

    return bos.toByteArray();
  }

  public static Serializable bytesToSerializable(final byte[] instance)
  {
    if (instance == null)
      throw new IllegalArgumentException("instance null");

    if (instance.length < 1)
      throw new IllegalArgumentException("instance empty");

    Serializable result = null;
    final ByteArrayInputStream bis = new ByteArrayInputStream(instance);
    try
    {
      ObjectInput input = new ObjectInputStream(bis);
      try
      {
        result = (Serializable) input.readObject();
      }
      finally
      {
        if (input != null)
          input.close();
      }
    }
    catch (final ClassNotFoundException ex)
    {
      throw new IllegalStateException("unexpected class: " + ex.getMessage(),
          ex);
    }
    catch (final IOException ex)
    {
      throw new IllegalStateException("I/O error: " + ex.getMessage(), ex);
    }
    finally
    {
      if (bis != null)
        try
        {
          bis.close();
        }
        catch (final IOException ioEx)
        {
          throw new IllegalStateException("error closing input stream, "
              + ioEx.getMessage(), ioEx);
        }
    }

    return result;
  }

  public static int indexOfLongKey(final byte[] src, final long key)
  {
    if (src == null)
      throw new IllegalArgumentException("src null");

    final int s = src.length;
    if (s < 1)
      return -1;

    final byte[] pattern = HackUtil.longToBytes(key);
    final int p = pattern.length;
    if (p < 1)
      return -1;

    if (s < p)
      return -1;

    // (s-p+0) (s-p+1) (s-p+2) (s-p+(p-1))
    // SRC ------- Lth=s: 0 1 2 ... . . . . . . . . . (s-1)
    // PATTERN --- Lth=p: 0 1 2 (p-1)
    boolean matched = false;
    int i = 0;
    for (; i <= (s - p); i++)
    {
      for (int j = 0; j < p; j++)
      {
        matched = src[i + j] == pattern[j];
        if (!matched)
          break;
      }

      if (matched)
        break;
    }

    return matched ? i : -1;
  }

  public static String displayLongKey(final byte[] src, final long keyValue)
  {
    if (src == null)
      throw new IllegalArgumentException("src null");

    final StringBuilder m = new StringBuilder();
    m.append("[bytes:");
    final int srcLth = src.length;
    if (srcLth < 1)
      m.append(" empty");
    else
    {
      m.append(" 0x");
      for (int i = 0; i < srcLth; i++)
      {
        final int c = (0x000000ff) & src[i];
        m.append(Integer.toHexString(c));
      }
    }

    m.append(";  longKey=");
    m.append(keyValue);
    m.append("]");

    return m.toString();
  }

  public static Serializable injectLongKey(final long markerKeyValue,
      final long replacementKeyValue, final Serializable token)
  {
    if (token == null)
      throw new IllegalArgumentException();

    final byte[] instance = HackUtil.serializableToBytes(token);
    final int idx = HackUtil.indexOfLongKey(instance, markerKeyValue);
    if (idx < 0)
      throw new IllegalArgumentException("markerKeyValue (" + markerKeyValue
          + ") not found in token (" + token.getClass().getName() + ")");

    final byte[] replaceBytes = HackUtil.longToBytes(replacementKeyValue);
    System.arraycopy(replaceBytes, 0, instance, idx, BYTES_IN_LONG);

    final Serializable tkHacked = (Serializable) HackUtil
        .bytesToSerializable(instance);

    return tkHacked;
  }
}
