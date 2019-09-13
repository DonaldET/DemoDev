package don.demo.generator.arguments.tools;

import java.io.Serializable;
import java.util.Arrays;

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;

/**
 * Support for command-line argument parsing.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 *         Copyright (c) 2016. Donald Trummell. All Rights Reserved. Permission
 *         to use, copy, modify, and distribute this software and its
 *         documentation for educational, research, and not-for-profit purposes,
 *         without fee and without a signed licensing agreement, is hereby
 *         granted, provided that the above copyright notice, and this
 *         paragraph, appear in all copies, modifications, and distributions.
 *         Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
@SuppressWarnings("deprecation")
public class ArgumentUtils implements Serializable
{
    private static final long serialVersionUID = -3523231197127430920L;

    private static enum DOING
    {
        UNQUOTED, QUOTED, APOS
    };

    /**
     * Prevent construction
     */
    private ArgumentUtils() {
        throw new IllegalStateException("static, do not construct");
    }

    public static final String[] commaToList(final String arg, final char mask)
    {
        if (arg == null)
            throw new IllegalArgumentException("null arg");

        if (arg.isEmpty())
            return new String[0];

        final String workArg = arg.trim();
        int lth = workArg.length();
        if (lth < 1)
            return new String[]
            { arg };

        final String fixedArg = maskComma(workArg, mask);
        if (fixedArg.length() < 1)
            return new String[]
            { arg };

        final String[] args = fixedArg.split(",", -1);
        lth = args.length;
        if (lth < 1)
            return args;

        for (int i = 0; i < lth; i++)
        {
            args[i] = args[i].replace(mask, ',');
        }

        return args;
    }

    public static String maskComma(final String workArg, final char mask)
    {
        if (workArg == null)
            throw new IllegalArgumentException("null arg");

        final int lth = workArg.length();
        if (lth < 1)
            return workArg;

        DOING doing = DOING.UNQUOTED;
        final StringBuilder chunks = new StringBuilder(lth);
        for (char c : workArg.toCharArray())
        {
            if (c == 0)
            {
                continue;
            }

            switch (c)
            {
                case ',': {
                    chunks.append((doing == DOING.UNQUOTED) ? c : mask);
                    continue;
                }

                case '\'': {
                    if (doing == DOING.APOS)
                    {
                        // End an APOS chunk
                        chunks.append(c);
                        doing = DOING.UNQUOTED;
                        continue;
                    }
                    else
                        if (doing == DOING.QUOTED)
                        {
                            // Continue a QUOTED chunk
                            chunks.append(c);
                            continue;
                        }
                        else
                        {
                            // Begin an APOS chunk
                            chunks.append(c);
                            doing = DOING.APOS;
                            continue;
                        }
                }

                case '"': {
                    if (doing == DOING.QUOTED)
                    {
                        // End a QUOTE chunk
                        chunks.append(c);
                        doing = DOING.UNQUOTED;
                        continue;
                    }
                    else
                        if (doing == DOING.APOS)
                        {
                            // Continue an APOS chunk
                            chunks.append(c);
                            continue;
                        }
                        else
                        {
                            // Begin a QUOTED chunk
                            chunks.append(c);
                            doing = DOING.QUOTED;
                            continue;
                        }
                }

                default: {
                    chunks.append(c);
                }
            }
        }

        return chunks.toString();
    }

    public static RuntimeException showParseError(final String[] args, final CmdLineParser parser, final String label,
            final Throwable ex)
    {
        RuntimeException error = null;
        try
        {
            System.err.println(label + " error: " + ex.getMessage() + "\n");
            parser.printUsage(System.err);
            System.err.println();
            System.err.println("  Usage: " + parser.printExample(ExampleMode.ALL));
        }
        catch (Throwable th)
        {
            error = new IllegalArgumentException("Secondary error: " + Arrays.toString(args) + "\n" + th.getMessage(),
                    ex);
        }

        return error;
    }
}
