import re
import sys
from collections import Counter
from pathlib import Path


def classify_word_frequencies(input_file):
    """Count the frequency of every word in a text file."""
    text = Path(input_file).read_text(encoding="utf-8")
    words = re.findall(r"[a-zA-Z0-9]+", text.lower())
    return Counter(words)


def main():
    input_file = sys.argv[1] if len(sys.argv) > 1 else "sample.txt"
    output_file = sys.argv[2] if len(sys.argv) > 2 else "word_frequency_output.txt"

    frequencies = classify_word_frequencies(input_file)

    with open(output_file, "w", encoding="utf-8") as output:
        output.write("WORD\tFREQUENCY\n")
        for word, count in sorted(frequencies.items()):
            output.write(f"{word}\t{count}\n")

    print("LARGE-SCALE WORD FREQUENCY CLASSIFIER")
    print("-------------------------------------")
    print("Word\tFrequency")
    for word, count in sorted(frequencies.items()):
        print(f"{word}\t{count}")

    print(f"\nOutput saved to: {output_file}")


if __name__ == "__main__":
    main()
