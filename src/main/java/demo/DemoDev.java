/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo;

/**
 * Project overview test class to verify some of the dependencies required to
 * build the useful sub-projects.
 * 
 * @author Donald Trummell
 */
public class DemoDev
{
  public DemoDev()
  {
  }

  public static void main(String[] args)
  {
    new DemoDev().sayHello("world");
  }

  private void sayHello(final String person)
  {
    System.out.println("\n*** Hello " + nameMe(person) + "!");
  }

  public String nameMe(final String person)
  {
    final String baseName = String.valueOf(person).toLowerCase();

    return baseName.isEmpty() ? baseName : baseName.substring(0, 1)
        .toUpperCase() + baseName.substring(1);
  }
}
