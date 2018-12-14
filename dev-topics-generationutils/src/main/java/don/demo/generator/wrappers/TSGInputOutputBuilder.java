package don.demo.generator.wrappers;

import don.demo.generator.InputOutputBuilder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * Create the input-output pairs that drive the generation process.
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
 * 
 * <pre>
 * <code>
 * <strong>Note</strong>: See <a href="http://www.benf.org/other/cfr/faq.html for decompiling">decompiling</a></strong>
 * </code>
 * </pre>
 */
@Service(value = "tsginputoutputbuilder")
public class TSGInputOutputBuilder implements InputOutputBuilder {
	private static final long serialVersionUID = 8884065599802401879L;

	@Override
	public List<Map.Entry<String, String>> createPairedList(String srcDir, String[] templateList, String dstDir,
			String[] generatedFileList, boolean useSrcDir) {
		this.checkPreConditions(srcDir, templateList, dstDir, generatedFileList);
		String workingDstDir = dstDir == "*" ? srcDir : dstDir;
		ArrayList<Map.Entry<String, String>> pairs = new ArrayList<Map.Entry<String, String>>();
		int i = 0;
		while (i < templateList.length) {
			String inFile = templateList[i];
			if (inFile == null || inFile.isEmpty()) {
				throw new IllegalArgumentException("input template file[" + i + "] null or empty");
			}
			Path srcpath = useSrcDir ? Paths.get(srcDir, inFile) : Paths.get(inFile, new String[0]);
			String outFile = generatedFileList[i] == "*" ? inFile : generatedFileList[i];
			if (outFile == null || outFile.isEmpty()) {
				throw new IllegalArgumentException("output file[" + i + "] null or empty");
			}
			Path dstpath = Paths.get(workingDstDir, outFile);
			pairs.add(new AbstractMap.SimpleEntry<String, String>(
					(useSrcDir ? srcpath.toAbsolutePath() : srcpath).toString(), dstpath.toAbsolutePath().toString()));
			++i;
		}
		return pairs;
	}

	private void checkPreConditions(String srcDir, String[] templateList, String dstDir, String[] generatedFileList) {
		if (srcDir == null) {
			throw new IllegalArgumentException("srcDir null");
		}
		if (srcDir.isEmpty()) {
			throw new IllegalArgumentException("srcDir empty");
		}
		if (dstDir == null) {
			throw new IllegalArgumentException("dstDir null");
		}
		if (dstDir.isEmpty()) {
			throw new IllegalArgumentException("dstDir empty");
		}
		if (templateList == null) {
			throw new IllegalArgumentException("templateList null");
		}
		if (templateList.length < 1) {
			throw new IllegalArgumentException("templateList empty");
		}
		if (generatedFileList == null) {
			throw new IllegalArgumentException("generatedFileList null");
		}
		if (generatedFileList.length < 1) {
			throw new IllegalArgumentException("generatedFileList empty");
		}
		if (templateList.length != generatedFileList.length) {
			throw new IllegalArgumentException("unequal list lengths: template:(" + templateList.length
					+ ") differes from generatedFileList:(" + generatedFileList.length + ")");
		}
	}
}
