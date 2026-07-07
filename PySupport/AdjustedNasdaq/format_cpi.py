"""
Reads a U.S. Bureau of Labor Statistics (BLS) Consumer Price Index (CPI)
data file, validates each data record, and writes the validated records
to a comma-separated value (CSV) output file.

The input file is a whitespace-delimited text file containing a header
record followed by CPI observations. Each data record contains the
following fields:

    1. series     - Series identifier (1 to 14 characters)
    2. year       - Four-digit year (1900 or later)
    3. period     - Month code in the form M01 through M12
    4. value      - Floating-point CPI value greater than 0 and less than 500
    5. footnote   - Optional footnote code

Validation performed includes:

    * Record contains at least four fields.
    * Series identifier length is between 1 and 14 characters.
    * Year is an integer greater than or equal to 1900.
    * Month code begins with 'M'.
    * Month value is between 1 and 12.
    * Value is numeric and between 0 and 500 (exclusive).

Only records passing all validation checks are written to the output CSV
file. The program also reports processing statistics including:

    * Number of input and output records
    * Number of tokens processed
    * Counts of each validation error
    * Minimum, maximum, and average CPI value
    * Frequency of each series identifier
"""
import csv

DEFAULT_INPUT_FILE = r"data\su.data.1.AllItems.raw"
DEFAULT_OUTPUT_FILE = r"data\su.data.1.AllItems.csv"


def _process_file(input_file: str, output_file: str) -> None:
    """
    Read a CPI data file, validate each record, and write valid records
    to a CSV file.

    The first line of the input file is treated as a header and skipped.
    Remaining records are tokenized using whitespace. Each record is
    validated for the following:

        * Series identifier length
        * Valid year (1900 or later)
        * Proper month code format (M01-M12)
        * Month number between 1 and 12
        * Numeric value greater than 0 and less than 500

    Records that fail one or more validation checks are excluded from the
    output file. While processing, the function accumulates statistics
    including record counts, validation error counts, minimum, maximum,
    and average values, and counts of each unique series identifier.

    Args:
        input_file: Path to the whitespace-delimited CPI input file.
        output_file: Path of the CSV file to be created.

    Returns:
        None.
    """
    output_record_count = 0
    input_record_count = 0
    out_of_sequence_count = 0
    out_of_sequence_count_limit = 10
    skip_count = 0
    short_count = 0
    token_count = 0
    bad_series_count = 0
    bad_year_count = 0
    bad_month_code_count = 0
    bad_month_code_count_limit = 10
    bad_month_count = 0
    bad_month_count_limit = 10
    bad_value_count = 0
    minvalue = 1.0E+50
    maxvalue = -1.0E+50
    sum_value = 0.0
    nvalues = 0

    series_values = {}

    print(f"Opening {input_file} and writing to {output_file} ...")
    with open(input_file, "r") as infile, \
            open(output_file, "w", newline="", encoding="utf-8") as outfile:

        print(f"  -- Creating CSV writer with excel dialect ...")
        writer = csv.writer(outfile, dialect="excel")

        # Write CSV header
        print(f"  -- Writing CSV header ...")
        writer.writerow(["series", "year", "month", "value", "footnote"])

        # Skip first line
        next(infile)

        print(f"  -- Skipped first line, processing remaining lines ...")
        prior_date: int = 0
        for line_num, line in enumerate(infile, start=2):
            input_record_count += 1
            line = line.strip()

            if not line:
                skip_count += 1
                continue

            tokens = line.split()
            token_count += len(tokens)

            if len(tokens) < 4:
                print(f"Line {line_num}: Not enough fields: '{line}.")
                short_count += 1
                continue

            # Read tokens
            series = tokens[0]
            year_str = tokens[1]
            month_code = tokens[2]
            value_str = tokens[3]
            footnote = tokens[4] if len(tokens) >= 5 else " "

            any_bad = False
            # Validate series
            if not (1 <= len(series) <= 14):
                print(f"Line {line_num}: Invalid series: {series}.")
                bad_series_count += 1
                any_bad |= True

            if series in series_values:
                series_values[series] = series_values[series] + 1
            else:
                series_values[series] = 1

            # Validate year
            year: int = 0
            try:
                year = int(year_str)
                if year < 1900:
                    raise ValueError
            except ValueError:
                print(f"Line {line_num}: Invalid year: {year_str}.")
                bad_year_count += 1
                any_bad |= True

            # Validate month code (M01 - M12)
            if len(month_code) != 3 or month_code[0] != "M":
                bad_month_code_count += 1
                if bad_month_code_count <= bad_month_code_count_limit:
                    print(f"Line {line_num}: Invalid month code: {month_code}.")
                any_bad |= True

            month: int = 0
            try:
                month = int(month_code[1:])
                if not (1 <= month <= 12):
                    raise ValueError
            except ValueError:
                bad_month_count += 1
                if bad_month_count <= bad_month_count_limit:
                    print(f"Line {line_num}: Invalid month: {month}.")
                any_bad |= True

            date_time_code = 1000 * year + month
            if date_time_code <= prior_date:
                out_of_sequence_count += 1
                if out_of_sequence_count <= out_of_sequence_count_limit:
                    print(
                        f"Line {line_num}: OutOfSequence: {out_of_sequence_count} Prior Date: {prior_date} Current Date: {date_time_code}.")
                any_bad |= True
            else:
                prior_date = date_time_code

            # Validate value
            try:
                value = float(value_str)
                if not (0 < value < 500):
                    raise ValueError
                minvalue = min(value, minvalue)
                maxvalue = max(value, maxvalue)
                sum_value += value
                nvalues += 1
            except ValueError:
                print(f"Line {line_num}: Invalid value: {value_str}.")
                bad_value_count += 1
                any_bad |= True

            if any_bad:
                continue

            # Write valid record
            writer.writerow([series, year, month, value, footnote])
            output_record_count += 1

    # Print statistics
    print(f"Records successfully processed : {output_record_count}")
    print(f"Records read                   : {input_record_count}")
    print(f"Tokens processed               : {token_count}")
    if skip_count > 0:
        print(f"  -- skipped records       : {skip_count}")
    if short_count > 0:
        print(f"  -- short records         : {short_count}")
    if bad_series_count > 0:
        print(f"  -- Bad series records: {bad_series_count}")
    if bad_year_count > 0:
        print(f"  -- Bad year records  : {bad_year_count}")
    if bad_month_code_count:
        print(f"  -- Not 'M' records   : {bad_month_count}")
    if bad_month_count:
        print(f"  -- Bad month records : {bad_month_count}")
    if bad_value_count > 0:
        print(f"  -- Bad value records : {bad_value_count}")
    if out_of_sequence_count > 0:
        print(f"  -- Bad sequence recs : {out_of_sequence_count}")

    print(f"Minimum value: {minvalue:.2f}")
    print(f"Maximum value: {maxvalue:.2f}")
    print(f"Average value: {sum_value / output_record_count:.2f}")

    print(f"Series labels: {series_values}")


if __name__ == "__main__":
    input_filename = DEFAULT_INPUT_FILE
    output_filename = DEFAULT_OUTPUT_FILE
    print(f"Processing CPI data in {input_filename} ...")
    print(f"Output to CSV file {output_filename}")
    _process_file(input_filename, output_filename)
