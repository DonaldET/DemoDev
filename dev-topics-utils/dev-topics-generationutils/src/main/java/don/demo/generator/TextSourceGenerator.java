package don.demo.generator;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Generate the composed output files, based on the templates and models,
 * processed by a template engine (e.g., <code>Velocity</code>,
 * <code>Freemarker</code>), and all guidance is provided by command-line like
 * arguments. The arguments are "strings" following the usual Java command-line
 * argument conventions. They are nominally processed and checked for semantics
 * by <code>Args4J</code>.
 * 
 * @author Donald Trummell
 * 
 *         Copyright (c) 2016. Donald Trummell. All Rights Reserved. Permission
 *         to use, copy, modify, and distribute this software and its
 *         documentation for educational, research, and not-for-profit purposes,
 *         without fee and without a signed licensing agreement, is hereby
 *         granted, provided that the above copyright notice, and this
 *         paragraph, appear in all copies, modifications, and distributions.
 *         Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
public interface TextSourceGenerator extends Serializable
{

    /**
     * Results of the generation operation
     * 
     * @author Donald Trummell
     */
    public static final class Result implements Serializable
    {

        private static final long serialVersionUID = 5416332145467620370L;
        private final String srcDir;
        private final String[] templateList;
        private final String defaultContext;
        private final String[] overrideContextList;
        private final String dstDir;
        private final String generatedFileList[];
        private final int filesRead;
        private final int filesWritten;

        public Result(final String srcDir, final String[] templateList, final String defaultContext,
                final String[] overrideContextList, final String dstDir, final String[] generatedFileList,
                final int filesRead, final int filesWritten) {
            super();
            this.srcDir = srcDir;
            this.templateList = templateList;
            this.defaultContext = defaultContext;
            this.overrideContextList = overrideContextList;
            this.dstDir = dstDir;
            this.generatedFileList = generatedFileList;
            this.filesRead = filesRead;
            this.filesWritten = filesWritten;
        }

        @Override
        public int hashCode()
        {
            final int prime = 3607;
            int result = 1;
            result = prime * result + ((defaultContext == null) ? 0 : defaultContext.hashCode());
            result = prime * result + ((dstDir == null) ? 0 : dstDir.hashCode());
            result = prime * result + filesRead;
            result = prime * result + filesWritten;
            result = prime * result + Arrays.hashCode(generatedFileList);
            result = prime * result + Arrays.hashCode(overrideContextList);
            result = prime * result + ((srcDir == null) ? 0 : srcDir.hashCode());
            result = prime * result + Arrays.hashCode(templateList);
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Result other = (Result) obj;
            if (defaultContext == null)
            {
                if (other.defaultContext != null)
                    return false;
            }
            else
                if (!defaultContext.equals(other.defaultContext))
                    return false;
            if (dstDir == null)
            {
                if (other.dstDir != null)
                    return false;
            }
            else
                if (!dstDir.equals(other.dstDir))
                    return false;
            if (filesRead != other.filesRead)
                return false;
            if (filesWritten != other.filesWritten)
                return false;
            if (!Arrays.equals(generatedFileList, other.generatedFileList))
                return false;
            if (!Arrays.equals(overrideContextList, other.overrideContextList))
                return false;
            if (srcDir == null)
            {
                if (other.srcDir != null)
                    return false;
            }
            else
                if (!srcDir.equals(other.srcDir))
                    return false;
            if (!Arrays.equals(templateList, other.templateList))
                return false;
            return true;
        }

        public String getSrcDir()
        {
            return srcDir;
        }

        public String[] getTemplateList()
        {
            return templateList;
        }

        public String getDefaultContext()
        {
            return defaultContext;
        }

        public String[] getOverrideContextList()
        {
            return overrideContextList;
        }

        public String getDstDir()
        {
            return dstDir;
        }

        public String[] getGeneratedFileList()
        {
            return generatedFileList;
        }

        public int getFilesRead()
        {
            return filesRead;
        }

        public int getFilesWritten()
        {
            return filesWritten;
        }

        @Override
        public String toString()
        {
            return "[Result - 0x" + Integer.toHexString(hashCode()) + ";\n  srcDir: " + srcDir + ";\n  templateList: "
                    + Arrays.toString(templateList) + ";\n  defaultContext: " + defaultContext
                    + ";\n  overrideContextList: " + Arrays.toString(overrideContextList) + ";\n  dstDir: " + dstDir
                    + ";\n  generatedFileList: " + Arrays.toString(generatedFileList) + ";\n  filesRead: " + filesRead
                    + ";  filesWritten: " + filesWritten + "]";
        }
    }

    /**
     * Read models and templates, generate output files, and use arguments to
     * define the operation.
     * 
     * @param args
     *            command-line like arguments to define models/contexts,
     *            templates, and directories.
     * 
     * @return the result of processing the models and templates to produce the
     *         generated files.
     */
    public abstract Result process(final String[] args);
}