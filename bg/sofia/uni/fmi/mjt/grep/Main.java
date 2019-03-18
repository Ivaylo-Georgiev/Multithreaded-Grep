package bg.sofia.uni.fmi.mjt.grep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
	final static String WHITESPACE_DELIMITER = "\\s+";

	private boolean isCaseInsensitive = false;
	private boolean isCompleteMatch = false;
	private String search;
	private String root;
	private String outputLocation;
	private int parallelThreads;

	public static List<String> reports = new ArrayList<String>();

	public static void main(String[] args) {

		Main main = new Main();

		try (Scanner scanner = new Scanner(System.in)) {

			String command = scanner.nextLine().trim();
			main.parseCommand(command);

			main.grep();

		}

	}

	public void grep() {

		try (Stream<Path> paths = Files.walk(Paths.get(root));
				OutputStream output = (outputLocation.equals("")) ? System.out
						: new FileOutputStream(new File(outputLocation));) {

			List<Path> filePaths = paths.filter(Files::isRegularFile).collect(Collectors.toList());

			ExecutorService executor = Executors.newFixedThreadPool(parallelThreads);

			for (Path filePath : filePaths) {
				Runnable searchPath = new SearchThread(filePath, search, isCaseInsensitive, isCompleteMatch);
				executor.execute(searchPath);
			}

			executor.shutdown();
			waitToTerminate(executor);
			printReports(output);

		} catch (IOException e) {
			System.out.println("General IOException. ");
		}

	}

	private void parseCommand(String command) {

		int index = 1;

		if (command.contains("-i")) {
			isCaseInsensitive = true;
			++index;
		}

		if (command.contains("-w")) {
			isCompleteMatch = true;
			++index;
		}

		String[] commandComponents = command.split(WHITESPACE_DELIMITER);
		search = commandComponents[index++];
		root = commandComponents[index++];
		parallelThreads = Integer.parseInt(commandComponents[index++]);
		outputLocation = (index == commandComponents.length) ? "" : commandComponents[index++];

	}

	private void printReports(OutputStream output) throws IOException {

		for (String report : reports) {
			output.write((report + System.lineSeparator()).getBytes());
			output.flush();
		}

	}

	private void waitToTerminate(ExecutorService executor) {

		while (!executor.isTerminated()) {

		}

	}

}
