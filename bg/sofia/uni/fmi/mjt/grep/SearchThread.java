package bg.sofia.uni.fmi.mjt.grep;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SearchThread implements Runnable {
	final static String WHITESPACE_PREFIX = ".*\\b";
	final static String WHITESPACE_POSTFIX = "\\b.*";

	private Path path;
	private String search;
	private List<String> reports = new ArrayList<String>();
	private boolean isCaseInsensitive;
	private boolean isCompleteMatch;

	public SearchThread(Path path, String search, boolean isCaseInsensitive, boolean isCompleteMatch) {

		this.path = path;
		this.search = search;
		this.isCaseInsensitive = isCaseInsensitive;
		this.isCompleteMatch = isCompleteMatch;

	}

	@Override
	public void run() {

		try {

			List<String> fileContent = Files.readAllLines(path);

			int lineNumber = 0;
			for (String line : fileContent) {

				++lineNumber;

				if (isCaseInsensitive) {

					if (Pattern.compile(Pattern.quote(search), Pattern.CASE_INSENSITIVE).matcher(line).find()) {
						buildReport(lineNumber, line);
					}

				} else {

					if (line.contains(search)) {
						buildReport(lineNumber, line);
					}

				}
			}

			if (isCompleteMatch) {
				filterCompleteMatches();
			}

			exportReports();

		} catch (IOException e) {
			System.out.println("General IO exception. ");
		}
	}

	private void buildReport(int lineNumber, String line) {

		StringBuilder reportBuilder = new StringBuilder();
		reportBuilder.append(path);
		reportBuilder.append(':');
		reportBuilder.append(lineNumber);
		reportBuilder.append(':');
		reportBuilder.append(line);

		reports.add(reportBuilder.toString());

	}

	private void filterCompleteMatches() {

		reports = reports.stream()
				.filter(report -> report.matches(WHITESPACE_PREFIX + Pattern.quote("grep") + WHITESPACE_POSTFIX))
				.collect(Collectors.toList());

	}

	private void exportReports() {

		for (String report : this.reports) {
			Main.reports.add(report);
		}

	}

}
