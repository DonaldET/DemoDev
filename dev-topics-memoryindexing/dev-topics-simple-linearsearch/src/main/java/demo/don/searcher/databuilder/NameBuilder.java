/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.searcher.databuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;

import demo.don.searcher.FLName;
import demo.don.searcher.UserID;

/**
 * This class creates a set of &quot;<em>fake</em>&quot; user identifiers with
 * names and Email address to search. The list of user identifiers is created
 * invoking the <code>initialize</code> method.
 * <p>
 * Testing input is retrieved from files found off the class path with default
 * names of:
 * <ul>
 * <li><code>FIRST_NAME_FILE = "/resources/firstnames.txt";</code></li>
 * <li><code>FULL_NAME_FILE = "/resources/winners.txt";</code></li>
 * </ul>
 * <p>
 * The fields of the user identifiers are cleaned and sorted
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class NameBuilder {
	public static final class Results<T> {
		private final Throwable error;
		private final List<T> values;

		public Results(final Throwable error, final List<T> values) {
			super();

			this.error = error;
			this.values = values;
		}

		public Throwable getError() {
			return error;
		}

		public List<T> getValues() {
			return values;
		}
	}

	// ---------------------------------------------------------------------------

	private static final String[] domains = new String[] { "gmail.com", "aol.com", "hotmail.com", "yahoo.com",
			"gmail.com", "intrinsx.com", "green-island.au", "tabak.ru", "home_town.cz", "apache.org",
			"springsource.org", "netbuilders.net" };

	private static final int FIELDS_PER_FULL_NAME = 2;
	private static final String NOT_A_NAME = "ZZZZZ";
	private static final String FIRST_NAME_FILE = "/firstnames.txt";
	private static final String FULL_NAME_FILE = "/winners.txt";
	private static final String spaceDelimiters = "\\s";
	private static final Pattern whiteSpacePattern = Pattern.compile(spaceDelimiters);
	private static final String backSlashDelimiters = "/";
	private static final Pattern backSlashPattern = Pattern.compile(backSlashDelimiters);

	// ---------------------------------------------------------------------------

	private boolean initialized = false;

	private String firstNameFile = FIRST_NAME_FILE;
	private String[] firstNames = null;

	private String fullNameFile = FULL_NAME_FILE;
	private FLName[] fullNames = null;

	// ---------------------------------------------------------------------------

	public NameBuilder() {
		super();
	}

	/**
	 * Setup the first names and full names by reading in files as resources
	 */
	public void initialize() {
		final String fnameFile = getFirstNameFile();
		Validate.notEmpty(fnameFile, "firstNameFile empty");
		final String flNameFile = getFullNameFile();
		Validate.notEmpty(flNameFile, "fullNameFile empty");

		InputStream stream = NameBuilder.class.getResourceAsStream(fnameFile);
		Validate.notNull(stream, "no stream for " + fnameFile);
		final String[] fn = NameBuilder.readFirstNames(stream);
		Validate.notNull(fn, "null data returned for " + fnameFile);
		int fLth = fn.length;
		Validate.isTrue(fLth > 0, fnameFile + " is small, ", fLth);

		stream = NameBuilder.class.getResourceAsStream(flNameFile);
		Validate.notNull(stream, "no stream for " + flNameFile);
		final FLName[] flnm = NameBuilder.readFLNames(stream);
		Validate.notNull(fn, "null data returned for " + flNameFile);
		fLth = flnm.length;
		Validate.isTrue(fLth > 0, flNameFile + " is small, ", fLth);

		setFirstNames(fn);
		setFullNames(flnm);
		setInitialized(true);
	}

	/**
	 * Create fake names and associated Email addresses by replacing the first name
	 * part of a full name component with successive fake first names
	 *
	 * @param firstNameCount a count to optionally limit first names used
	 * @param fullNameCount  a count to optionally limit full names used
	 *
	 * @return a list of generated user identifiers (first name, last name, email)
	 */
	public UserID[] createFakeNames(final int firstNameCount, final int fullNameCount) {
		Validate.isTrue(isInitialized(), "uninitialized");
		Validate.isTrue(firstNameCount > 0, "firstNameCount small, ", firstNameCount);
		Validate.isTrue(fullNameCount > 0, "fullNameCount small, ", fullNameCount);

		final int first = Math.min(firstNameCount, firstNames.length);
		final int full = Math.min(fullNameCount, fullNames.length);

		final List<UserID> fakeList = new ArrayList<UserID>();
		for (int lnameIdx = 0; lnameIdx < full; lnameIdx++) {
			final String lnIndex = "_" + lnameIdx;

			FLName flName = fullNames[lnameIdx];
			String firstName = flName.getFirstName();
			final String lastName = flName.getLastName();

			UserID aFake = new UserID(firstName, lastName, createEmailAddr(firstName, lastName, 0, lnIndex));
			fakeList.add(aFake);

			for (int fnameIdx = 0; fnameIdx < first; fnameIdx++) {
				firstName = firstNames[fnameIdx];
				aFake = new UserID(firstName, lastName, createEmailAddr(firstName, lastName, fnameIdx, lnIndex));
				fakeList.add(aFake);
			}
		}

		final int n = fakeList.size();
		final UserID[] fakes = new UserID[n];
		if (n > 0)
			fakeList.toArray(fakes);

		return fakes;
	}

	/**
	 * Create a fake email address; first and last names at a standard list of
	 * domains
	 *
	 * @param firstName the first name to use
	 * @param lastName  the last name to use
	 * @param fnIndex   list position of the first name
	 * @param lnIndex   list position of the last name
	 *
	 * @return a fake email (e.g., don.trummell@at;gmail.com)
	 */
	public String createEmailAddr(final String firstName, final String lastName, final int fnIndex,
			final String lnIndex) {
		return firstName + "." + lastName + lnIndex + "@" + domains[fnIndex % domains.length];
	}

	// ---------------------------------------------------------------------------

	/**
	 * Initialization state
	 *
	 * @return true if initialized
	 */
	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(final boolean initialized) {
		this.initialized = initialized;
	}

	public FLName[] getFullNames() {
		return fullNames;
	}

	private void setFullNames(final FLName[] fullNames) {
		this.fullNames = fullNames;
	}

	public String[] getFirstNames() {
		return firstNames;
	}

	private void setFirstNames(final String[] firstNames) {
		this.firstNames = firstNames;
	}

	public String getFirstNameFile() {
		return firstNameFile;
	}

	public void setFirstNameFile(final String firstNameFile) {
		this.firstNameFile = firstNameFile;
	}

	public String getFullNameFile() {
		return fullNameFile;
	}

	public void setFullNameFile(final String fullNameFile) {
		this.fullNameFile = fullNameFile;
	}

	// ---------------------------------------------------------------------------

	/**
	 * Read, clean and sort full names
	 *
	 * @param dataStream
	 *
	 * @return a sorted array of cleaned first names
	 */
	public static FLName[] readFLNames(final InputStream dataStream) {
		Validate.notNull(dataStream, "dataStream null");
		final String label = "First-Last-Names";

		final List<FLName> rawFLNames = readFLNamesRaw(label, dataStream);
		Validate.notNull(rawFLNames, label + " raw: f/l names null");
		Validate.isTrue(!rawFLNames.isEmpty(), label + ": raw f/l names null");

		Collections.sort(rawFLNames);
		final List<FLName> clean = new ArrayList<FLName>();
		FLName last = new FLName(NOT_A_NAME, NOT_A_NAME);
		for (final FLName flName : rawFLNames) {
			if (!last.equals(flName)) {
				clean.add(flName);
				last = flName;
			}
		}

		final int n = clean.size();
		final FLName[] names = new FLName[n];
		if (n > 0)
			clean.toArray(names);

		return names;
	}

	/**
	 * Read, sort and clean first names
	 *
	 * @param dataStream
	 *
	 * @return a sorted array of cleaned first names
	 */
	public static String[] readFirstNames(final InputStream dataStream) {
		Validate.notNull(dataStream, "dataStream null");
		final String label = "First-Names";

		final List<String> rawNames = readFirstNamesRaw(label, dataStream);
		Validate.notNull(rawNames, label + ": raw names null");
		Validate.isTrue(!rawNames.isEmpty(), label + ": raw names empty");

		Collections.sort(rawNames);
		final List<String> clean = new ArrayList<String>();
		String last = "";
		for (String s : rawNames) {
			if (!s.isEmpty() && !last.equals(s)) {
				clean.add(s);
				last = s;
			}
		}

		final int n = rawNames.size();
		final String[] all = new String[n];
		if (n > 0)
			rawNames.toArray(all);

		return all;
	}

	// ----------------------------------------------------------------------------

	private static List<FLName> readFLNamesRaw(final String label, final InputStream dataStream) {
		Validate.notEmpty(label, "label empty");
		Validate.notNull(dataStream, "dataStream null");

		final Results<String[]> results = readEntireTokenizedFile(label, dataStream, whiteSpacePattern);
		Validate.notNull(results, label + ": tokenized file null");

		List<FLName> names = null;
		final Throwable error = results.getError();
		if (error == null) {
			final List<String[]> values = results.getValues();
			Validate.notNull(values, label + ": f/l values null");
			Validate.isTrue(!values.isEmpty(), label + ": f/l values empty");

			int line = 0;
			names = new ArrayList<FLName>();
			for (final String[] value : values) {
				line++;

				final int n = value.length;

				if (n < FIELDS_PER_FULL_NAME)
					throw new IllegalArgumentException(line + " had invalid name count for [" + String.valueOf(value[0])
							+ "], expected 2 but had " + n);
				else if (n > FIELDS_PER_FULL_NAME)
					throw new IllegalArgumentException(line + " had invalid name count for [" + String.valueOf(value[0])
							+ ", " + String.valueOf(value[1]) + ", " + String.valueOf(value[2])
							+ "], expected 2 but had " + n);
				else
					names.add(new FLName(value[0], value[1]));

				names.add(new FLName(value[0], value[1]));
			}
		} else {
			throw new IllegalStateException(label + ": got error " + error.getMessage(), error);
		}

		return names;
	}

	private static List<String> readFirstNamesRaw(final String label, final InputStream dataStream) {
		Validate.notEmpty(label, "label empty");
		Validate.notNull(dataStream, "dataStream null");

		final Results<String[]> results = readSlashedFile(label, dataStream);
		Validate.notNull(results, label + " got null results");

		List<String> linear = null;
		final Throwable error = results.getError();
		if (error == null) {
			final List<String[]> values = results.getValues();
			Validate.notNull(values, label + " values null");
			Validate.isTrue(!values.isEmpty(), label + " values null");

			linear = new ArrayList<String>();
			for (final String[] value : values) {
				for (final String s : value) {
					if (s != null && !s.isEmpty()) {
						final String t = s.trim().toLowerCase();
						if (!t.isEmpty() && !Character.isDigit(t.charAt(0)))
							linear.add(t);
					}
				}
			}
		} else {
			throw new IllegalStateException(label + " got error: " + error.getMessage(), error);
		}

		return linear;
	}

	private static Results<String[]> readSlashedFile(final String label, final InputStream dataStream) {
		Validate.notEmpty(label, "label empty");
		Validate.notNull(dataStream, "dataStream null");

		final Results<String[]> results = readEntireTokenizedFile(label, dataStream, whiteSpacePattern);
		if (results.getError() == null) {
			final List<String[]> values = results.getValues();
			Validate.notEmpty(values, label + ": name list is null or empty");

			int i = -1;
			for (final String[] oldValues : values) {
				i++;
				Validate.notNull(oldValues, label + "[" + i + "] is null");
				Validate.isTrue(oldValues.length > 0, label + "[" + i + "] is empty");

				final String[] slashedValues = extractSlashedValues(i, oldValues);

				Validate.notNull(slashedValues, label + "[" + i + "] has null split");
				Validate.isTrue(slashedValues.length > 0, label + "[" + i + " has empty split");
				values.set(i, slashedValues);
			}
		}

		return results;
	}

	private static Results<String[]> readEntireTokenizedFile(final String label, final InputStream dataStream,
			final Pattern splitDelims) {
		final List<String[]> values = new ArrayList<String[]>();

		Exception error = null;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(dataStream), 1024);
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				final String[] tokens = splitDelims.split(line, 0);
				Validate.notNull(tokens, label + ":tokens null for " + String.valueOf(line));
				if (tokens.length > 0)
					values.add(tokens);
			}
		} catch (final Exception ex) {
			error = ex;
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException ignore) {
					// ignore
				}
		}

		return new Results<String[]>(error, values);
	}

	private static String[] extractSlashedValues(final int listIndex, final String[] oldValues) {
		final int n = oldValues.length;

		final List<String> slashed = new ArrayList<String>();
		for (int i = 0; i < n; i++) {
			final String[] newValues = backSlashPattern.split(oldValues[i]);
			Validate.notNull(newValues, "null split for values[" + listIndex + "][" + i + "]");
			for (final String s : newValues)
				slashed.add(s);
		}

		final int nslashed = slashed.size();
		final String[] newValues = new String[nslashed];
		if (nslashed > 0)
			slashed.toArray(newValues);

		return newValues;
	}
}
