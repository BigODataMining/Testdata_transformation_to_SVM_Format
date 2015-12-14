import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;

public class Testdata_transformation_to_SVM_Format {
	public static HashMap<String, Integer> ngrams(
			HashMap<String, Integer> ngrams, int n, String str) {
		// TODO 檢查有沒有 tab
		str = str.substring(str.indexOf("\t") + 1);
		String[] words = str.split(" ");
		for (int i = 0; i < words.length - n + 1; i++) {
			String ngram = concat(words, i, i + n);
			if (!ngrams.containsKey(ngram)) {
				ngrams.put(ngram, 0);
			}
			ngrams.put(ngram, ngrams.get(ngram) + 1);
		}
		return ngrams;
	}

	// public static long count = 1;

	/**
	 * 
	 * @param index
	 * @param n_gram
	 * @param str
	 * @return
	 */
	public static HashMap<String, Long> index(HashMap<String, Long> index,
			int n, String str) {
		// 檢查有沒有 tab
		String[] words = str.split(" ");
		int i = 0;
		String key = concat(words, i, i + n);
		String value_string = concat(words, i + n, i + n + 1);
		Long value = Long.valueOf(value_string).longValue();
		index.put(key, value);
		return index;
	}

	public static String concat(String[] words, int start, int end) {
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < end; i++)
			sb.append((i > start ? " " : "") + words[i]);
		return sb.toString();
	}

	public static void main(String[] argv) throws IOException {
		FileInputStream Ngram_fr = new FileInputStream("Seg_TestingData.txt");
		BufferedReader Ngram_br = new BufferedReader(new InputStreamReader(
				Ngram_fr, "UTF-8"));
		FileInputStream Index_fr = new FileInputStream("Index_data_output.txt");
		BufferedReader Index_br = new BufferedReader(new InputStreamReader(
				Index_fr, "UTF-8"));
		FileInputStream SVM_fr = new FileInputStream("Seg_TestingData.txt");
		BufferedReader SVM_br = new BufferedReader(new InputStreamReader(
				SVM_fr, "UTF-8"));
		String strNum_Ngram = null;
		String strNum_index = null;
		String strNum_SVM = null;
		String Ngram_line = null;
		String Index_line = null;
		String SVM_line = null;
		Path Ngram_file = null;
		Path Index_file = null;
		Path SVM_file = null;
		BufferedWriter bufferedWriter = null;
		HashMap<String, Integer> ngrams = new HashMap<String, Integer>();
		HashMap<String, Long> index = new HashMap<String, Long>();

		while ((strNum_Ngram = Ngram_br.readLine()) != null) {
			// ngrams = Testdata_transformation_to_SVM_Format.ngrams(ngrams, 1,
			// strNum_Ngram);
			ngrams = Testdata_transformation_to_SVM_Format.ngrams(ngrams, 2,
					strNum_Ngram);
			// ngrams = Testdata_transformation_to_SVM_Format.ngrams(ngrams, 3,
			// strNum_Ngram);
		}
		while ((strNum_index = Index_br.readLine()) != null) {
			// index = Testdata_transformation_to_SVM_Format.index(index, 1,
			// strNum_index);
			index = Testdata_transformation_to_SVM_Format.index(index, 2,
					strNum_index);
			// index = Testdata_transformation_to_SVM_Format.index(index, 3,
			// strNum_index);
		}
		try {
			if (Files.exists(Paths.get("Ngram_Testdata_output.txt"))) {
				Files.delete(Paths.get("Ngram_Testdata_output.txt"));
			}
			Ngram_file = Files.createFile(Paths
					.get("Ngram_Testdata_output.txt"));
			bufferedWriter = Files.newBufferedWriter(Ngram_file,
					Charset.forName("UTF-8"));
			for (Entry<String, Integer> m : ngrams.entrySet()) {
				Ngram_line = (m.getKey() + " " + m.getValue());
				bufferedWriter.write(Ngram_line);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * try { if (Files.exists(Paths.get("Index_Testdata_output.txt"))) {
		 * Files.delete(Paths.get("Index_Testdata_output.txt")); } Index_file =
		 * Files.createFile(Paths.get("Index_data_output.txt")); bufferedWriter
		 * = Files.newBufferedWriter(Index_file, Charset.forName("UTF-8")); for
		 * (Entry<String, Long> m : index.entrySet()) { Index_line = (m.getKey()
		 * + " " + m.getValue()); bufferedWriter.write(Index_line);
		 * bufferedWriter.newLine(); } bufferedWriter.close(); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		try {
			if (Files.exists(Paths.get("SVM_Testdata_output.txt"))) {
				Files.delete(Paths.get("SVM_Testdata_output.txt"));
			}
			SVM_file = Files.createFile(Paths.get("SVM_Testdata_output.txt"));
			bufferedWriter = Files.newBufferedWriter(SVM_file,
					Charset.forName("UTF-8"));
			while ((strNum_SVM = SVM_br.readLine()) != null) {
				String compare = "";
				if (strNum_SVM.indexOf("sports") == 0) {
					SVM_line = ("1 ");
					bufferedWriter.write(SVM_line);
					strNum_SVM = strNum_SVM
							.substring(strNum_SVM.indexOf("\t") + 1);
					String[] words = strNum_SVM.split(" ");
					Map ascSortedMap = new TreeMap();
					for (int i = 0; i < words.length - 1; i++) {
						String ngram = concat(words, i, i + 2);
						Long index_term = index.get(ngram);
						if (index_term != null) {
							if (compare.indexOf(index_term + "") != -1)
								continue;
							ascSortedMap.put(index_term, ngrams.get(ngram));
						} else {

						}
					}
					for (Object entry : ascSortedMap.entrySet()) {
						if (((Map.Entry) entry).getKey() != null) {
							if (compare.indexOf(((Map.Entry) entry).getKey()
									+ "") != -1)
								continue;
							SVM_line = ((Map.Entry) entry).getKey() + ":"
									+ ((Map.Entry) entry).getValue() + " ";
							bufferedWriter.write(SVM_line);
							compare = compare + ((Map.Entry) entry).getKey()
									+ "";
						} else {

						}
					}
					bufferedWriter.newLine();
				}
				if (strNum_SVM.indexOf("health") == 0) {
					SVM_line = ("2 ");
					bufferedWriter.write(SVM_line);
					strNum_SVM = strNum_SVM
							.substring(strNum_SVM.indexOf("\t") + 1);
					String[] words1 = strNum_SVM.split(" ");
					Map ascSortedMap1 = new TreeMap();
					for (int i = 0; i < words1.length - 1; i++) {
						String ngram = concat(words1, i, i + 2);
						Long index_term = index.get(ngram);

						if (index_term != null) {
							if (compare.indexOf(index_term + "") != -1)
								continue;
							ascSortedMap1.put(index_term, ngrams.get(ngram));
						} else {

						}
					}
					for (Object entry : ascSortedMap1.entrySet()) {
						if (((Map.Entry) entry).getKey() != null) {
							if (compare.indexOf(((Map.Entry) entry).getKey()
									+ "") != -1)
								continue;
							SVM_line = ((Map.Entry) entry).getKey() + ":"
									+ ((Map.Entry) entry).getValue() + " ";
							bufferedWriter.write(SVM_line);
							compare = compare + ((Map.Entry) entry).getKey()
									+ "";
						} else {

						}
					}
					bufferedWriter.newLine();
				}
				if (strNum_SVM.indexOf("politics") == 0) {
					SVM_line = ("3 ");
					bufferedWriter.write(SVM_line);
					strNum_SVM = strNum_SVM
							.substring(strNum_SVM.indexOf("\t") + 1);
					String[] words2 = strNum_SVM.split(" ");
					Map ascSortedMap2 = new TreeMap();
					for (int i = 0; i < words2.length - 1; i++) {
						String ngram = concat(words2, i, i + 2);
						Long index_term = index.get(ngram);
						if (index_term != null) {
							if (compare.indexOf(index_term + "") != -1)
								continue;
							ascSortedMap2.put(index_term, ngrams.get(ngram));
						} else {

						}
					}
					for (Object entry : ascSortedMap2.entrySet()) {
						if (((Map.Entry) entry).getKey() != null) {
							if (compare.indexOf(((Map.Entry) entry).getKey()
									+ "") != -1)
								continue;
							SVM_line = ((Map.Entry) entry).getKey() + ":"
									+ ((Map.Entry) entry).getValue() + " ";
							bufferedWriter.write(SVM_line);
							compare = compare + ((Map.Entry) entry).getKey()
									+ "";
						} else {

						}
					}
					bufferedWriter.newLine();
				}
				if (strNum_SVM.indexOf("travel") == 0) {
					SVM_line = ("4 ");
					bufferedWriter.write(SVM_line);
					strNum_SVM = strNum_SVM
							.substring(strNum_SVM.indexOf("\t") + 1);
					String[] words3 = strNum_SVM.split(" ");
					Map ascSortedMap3 = new TreeMap();
					for (int i = 0; i < words3.length - 1; i++) {
						String ngram = concat(words3, i, i + 2);
						Long index_term = index.get(ngram);
						if (index_term != null) {
							if (compare.indexOf(index_term + "") != -1)
								continue;
							ascSortedMap3.put(index_term, ngrams.get(ngram));
						} else {

						}
					}
					for (Object entry : ascSortedMap3.entrySet()) {
						if (((Map.Entry) entry).getKey() != null) {
							if (compare.indexOf(((Map.Entry) entry).getKey()
									+ "") != -1)
								continue;
							SVM_line = ((Map.Entry) entry).getKey() + ":"
									+ ((Map.Entry) entry).getValue() + " ";
							bufferedWriter.write(SVM_line);
							compare = compare + ((Map.Entry) entry).getKey()
									+ "";
						} else {

						}
					}
					bufferedWriter.newLine();
				}
				if (strNum_SVM.indexOf("edu") == 0) {
					SVM_line = ("5 ");
					bufferedWriter.write(SVM_line);
					strNum_SVM = strNum_SVM
							.substring(strNum_SVM.indexOf("\t") + 1);
					String[] words4 = strNum_SVM.split(" ");
					Map ascSortedMap4 = new TreeMap();
					for (int i = 0; i < words4.length - 1; i++) {
						String ngram = concat(words4, i, i + 2);
						Long index_term = index.get(ngram);
						if (index_term != null) {
							if (compare.indexOf(index_term + "") != -1)
								continue;
							ascSortedMap4.put(index_term, ngrams.get(ngram));
						} else {

						}
					}
					for (Object entry : ascSortedMap4.entrySet()) {
						if (((Map.Entry) entry).getKey() != null) {
							if (compare.indexOf(((Map.Entry) entry).getKey()
									+ "") != -1)
								continue;
							SVM_line = ((Map.Entry) entry).getKey() + ":"
									+ ((Map.Entry) entry).getValue() + " ";
							bufferedWriter.write(SVM_line);
							compare = compare + ((Map.Entry) entry).getKey()
									+ "";
						} else {

						}
					}
					bufferedWriter.newLine();
				}
			}
		bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Complete");
	}

}
