package don.demo.generator;

import java.io.Serializable;
import java.util.Arrays;

/**
 * parse arguments as if command-line parameters
 * 
 * @author dtrumme
 */
public interface ParameterSetup extends Serializable {

    /**
     * Represents command-line formatted arguments that are parsed and set into
     * this bean. A context is the Freemarker model; it defines the variables
     * taking part in "interpolation".
     * 
     * @author dtrumme
     */
    public static class ParameterBean implements Serializable {
        private static final long serialVersionUID = 4158898325940790072L;
        private final String srcDir;
        private final String[] templateList;
        private final String defaultContext;
        private final String[] overrideContextList;
        private final String dstDir;
        private final String[] generatedFileList;

        public ParameterBean(final String srcDir, final String[] templateList,
                final String defaultContext,
                final String[] overrideContextList, final String dstDir,
                final String[] generatedFileList) {
            super();
            this.srcDir = srcDir;
            this.templateList = templateList;
            this.defaultContext = defaultContext;
            this.overrideContextList = overrideContextList;
            this.dstDir = dstDir;
            this.generatedFileList = generatedFileList;
        }

        public static long getSerialversionuid() {
            return serialVersionUID;
        }

        public String getSrcDir() {
            return srcDir;
        }

        public String[] getTemplateList() {
            return templateList;
        }

        public String getDefaultContext() {
            return defaultContext;
        }

        public String[] getOverrideContextList() {
            return overrideContextList;
        }

        public String getDstDir() {
            return dstDir;
        }

        public String[] getGeneratedFileList() {
            return generatedFileList;
        }

        @Override
        public String toString() {
            return "{ParameterBean - 0x" + Integer.toHexString(hashCode())
                    + ";\n  srcDir: " + srcDir + ";\n  templateList: "
                    + Arrays.toString(templateList) + ";\n  defaultContext: "
                    + defaultContext + ";\n  overrideContextList: "
                    + Arrays.toString(overrideContextList) + ";\n  dstDir: "
                    + dstDir + ";\n  generatedFileList: "
                    + Arrays.toString(generatedFileList) + "}";
        }

        @Override
        public int hashCode() {
            final int prime = 73;
            int result = 1;
            result = prime
                    * result
                    + ((defaultContext == null) ? 0 : defaultContext.hashCode());
            result = prime * result
                    + ((dstDir == null) ? 0 : dstDir.hashCode());
            result = prime * result + Arrays.hashCode(generatedFileList);
            result = prime * result + Arrays.hashCode(overrideContextList);
            result = prime * result
                    + ((srcDir == null) ? 0 : srcDir.hashCode());
            result = prime * result + Arrays.hashCode(templateList);

            return result;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;

            ParameterBean other = (ParameterBean) obj;
            if (defaultContext == null) {
                if (other.defaultContext != null) {
                    return false;
                }
            } else if (!defaultContext.equals(other.defaultContext))
                return false;

            if (dstDir == null) {
                if (other.dstDir != null)
                    return false;
            } else if (!dstDir.equals(other.dstDir))
                return false;

            if (!Arrays.equals(generatedFileList, other.generatedFileList))
                return false;
            if (!Arrays.equals(overrideContextList, other.overrideContextList))
                return false;

            if (srcDir == null) {
                if (other.srcDir != null)
                    return false;
            } else if (!srcDir.equals(other.srcDir))
                return false;

            if (!Arrays.equals(templateList, other.templateList))
                return false;

            return true;
        }
    }

    /**
     * Parse command-line type parameters
     * 
     * @param args
     *            the parameters passed into utility
     * 
     * @return a <code>ParameterBean</code> instance with parsed parameter
     *         values
     */
    public abstract ParameterBean setupArgs(final String[] args);
}