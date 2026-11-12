package com.project.wordfrequency;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordFrequencyClassifier {

    // Mapper: converts each word into (word, 1).
    public static class WordMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private static final IntWritable ONE = new IntWritable(1);
        private final Text outputWord = new Text();

        @Override
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString().toLowerCase();
            line = line.replaceAll("[^a-zA-Z0-9]+", " ");

            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                outputWord.set(tokenizer.nextToken());
                context.write(outputWord, ONE);
            }
        }
    }

    // Reducer: adds all values belonging to the same word.
    public static class FrequencyReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {

        private final IntWritable result = new IntWritable();

        @Override
        public void reduce(Text word, Iterable<IntWritable> values,
                           Context context)
                throws IOException, InterruptedException {

            int total = 0;
            for (IntWritable value : values) {
                total += value.get();
            }

            result.set(total);
            context.write(word, result);
        }
    }

    // Driver: configures and starts the MapReduce job.
    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println(
                "Usage: WordFrequencyClassifier <input path> <output path>");
            System.exit(2);
        }

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(
            configuration, "Large-Scale Word Frequency Classifier");

        job.setJarByClass(WordFrequencyClassifier.class);
        job.setMapperClass(WordMapper.class);
        job.setCombinerClass(FrequencyReducer.class);
        job.setReducerClass(FrequencyReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
