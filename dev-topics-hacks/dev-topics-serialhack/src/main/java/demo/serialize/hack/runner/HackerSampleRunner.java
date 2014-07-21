/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.serialize.hack.runner;

import demo.serialize.hack.HackUtil;
import demo.serialize.hack.HackerSample.Token;
import demo.serialize.hack.HackerSample.TokenFactory;

public class HackerSampleRunner
{
  private static final int INITIAL_SEED = 1;
  private static final long INJECTED_KEY = 16969691;

  /**
   * Construct me
   */
  public HackerSampleRunner()
  {
  }

  /**
   * @param args
   */
  public static void main(final String[] args)
  {
    final HackerSampleRunner hacker = new HackerSampleRunner();

    final byte[] instance = hacker.makeSerializedTokenWithKey(INITIAL_SEED);
    hacker.injectKeyValue(instance, INJECTED_KEY);
    final Token tkHacked = hacker.deSerialize(instance);
    assert (INJECTED_KEY == tkHacked.getKey());
  }

  private byte[] makeSerializedTokenWithKey(final int initialSeed)
  {
    final Token tk = TokenFactory.createToken(initialSeed);
    final long expectedKey = TokenFactory.createKey(initialSeed);
    assert (expectedKey == tk.getKey());
    System.out.println("\nHack a class instance!");
    System.out.println("\tBefore      : " + tk);
    final byte[] keyBytes = HackUtil.longToBytes(expectedKey);
    System.out.println("\tKey         : "
        + HackUtil.displayLongKey(keyBytes, expectedKey));

    final byte[] instance = HackUtil.serializableToBytes(tk);
    System.out.println("\tInstance    : " + instance.length + " bytes");

    return instance;
  }

  private int locateKeyInSerializedToken(final byte[] instance,
      final int initialSeed)
  {
    final int idx = HackUtil.indexOfLongKey(instance,
        TokenFactory.createKey(initialSeed));
    System.out.println("\tKey Position: " + idx);

    return idx;
  }

  private void injectKeyValue(final byte[] instance, final long replacement)
  {
    final int idx = locateKeyInSerializedToken(instance, INITIAL_SEED);

    final byte[] replaceBytes = HackUtil.longToBytes(replacement);
    System.out.println("\tReplace Key : "
        + HackUtil.displayLongKey(replaceBytes, replacement));
    System.arraycopy(replaceBytes, 0, instance, idx, HackUtil.BYTES_IN_LONG);
  }

  private Token deSerialize(final byte[] instance)
  {
    final Token tkHacked = (Token) HackUtil.bytesToSerializable(instance);
    System.out.println("\tAfter       : " + tkHacked);

    return tkHacked;
  }
}