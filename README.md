# Large-Scale Word Frequency Classifier

## Project description
This project reads text data and calculates how many times each word appears. It includes a simple Python program and a distributed Apache Hadoop MapReduce implementation.

**Note:** This is a word-frequency counting/classification project. It groups identical words and reports their frequency; it is not a machine-learning text-category classifier.

## Project structure
- `python-version/word_frequency_classifier.py` — simple runnable Python code.
- `python-version/sample.txt` — sample dataset.
- `hadoop-mapreduce/src/main/java/com/project/wordfrequency/WordFrequencyClassifier.java` — Hadoop Mapper, Combiner, Reducer and Driver.
- `hadoop-mapreduce/pom.xml` — Maven dependencies and build configuration.
- `hadoop-mapreduce/input/sample.txt` — Hadoop sample dataset.

## Run Python version in VS Code
1. Extract this ZIP file.
2. Open the `python-version` folder in VS Code.
3. Open `word_frequency_classifier.py`.
4. Select Terminal → New Terminal.
5. Run:

```bash
python word_frequency_classifier.py
```

If `python` does not work, try:

```bash
py word_frequency_classifier.py
```

The program prints each word and its frequency and creates `word_frequency_output.txt`.

To process another file:

```bash
python word_frequency_classifier.py mydata.txt result.txt
```

## Run Hadoop MapReduce version
Requirements: Java JDK, Apache Hadoop 3.x, and Maven. The project uses Hadoop 3.3.6 dependencies; update `hadoop.version` in `pom.xml` to match your installation if needed.

From the `hadoop-mapreduce` folder, build:

```bash
mvn clean package
```

Upload sample input to HDFS:

```bash
hdfs dfs -mkdir -p /wordfrequency/input
hdfs dfs -put -f input/sample.txt /wordfrequency/input/
```

Remove an old output directory if present:

```bash
hdfs dfs -rm -r -f /wordfrequency/output
```

Run the job:

```bash
hadoop jar target/large-scale-word-frequency-classifier-1.0.jar /wordfrequency/input /wordfrequency/output
```

View the result:

```bash
hdfs dfs -cat /wordfrequency/output/part-r-00000
```

## Algorithm
1. Read the input text.
2. Convert all letters to lowercase.
3. Remove punctuation.
4. Split the text into individual words.
5. Mapper emits each word with value 1.
6. Combiner adds local word counts.
7. Reducer calculates the final frequency for each word.
8. Store the word-frequency output.

## Expected result
The output contains one word and its total count per line. Word order may vary in Hadoop output.
