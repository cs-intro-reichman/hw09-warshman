import java.util.HashMap;
import java.util.Random;


public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {

		String window = "";
        char c;
        In in = new In(fileName);
        
        for (int i = 0; i < windowLength; i++) {
            window += in.readChar();
        }

        while (!in.isEmpty()) {
            // Gets the next character
            c = in.readChar();
            // Checks if the window is already in the map
            List probs = CharDataMap.get(window);
            if (probs == null) {
                probs = new List();
                CharDataMap.put(window, probs);
            }

            probs.update(c);
            window = window.substring(1) + c;

        }
        
        for (List probs : CharDataMap.values())
            calculateProbabilities(probs);

	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {				
        // Get the total number of chars in the list.
        CharData[] array = probs.toArray();
        int total = 0;
        double cumulative = 0;
        for (int i = 0; i < array.length; i++) {
            total += array[i].count;
        }
        // Insert probability into every p
        ListIterator it  = probs.listIterator(0);
        while (it.hasNext()) {
            CharData data = it.next();
            data.p = (double) data.count / total;
            cumulative += data.p;
            data.cp = cumulative;
        }
	}

    char getRandomChar(List probs) {
    double rand = randomGenerator.nextDouble();

    ListIterator it = probs.listIterator(0);
    while (it != null && it.hasNext()) {
        CharData d = it.next();
        if (rand <= d.cp) return d.chr;
    }

    // Safety fallback: return last element if due to rounding cp didn't reach 1.0
    ListIterator it2 = probs.listIterator(0);
    CharData last = null;
    while (it2 != null && it2.hasNext()) last = it2.next();
    return last.chr;
}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
        int initLen = initialText.length();
        String generatedText = initialText;
		if (initLen < windowLength) {
            return initialText;
        }
        String window = initialText.substring(initLen - windowLength);
        //while (check two things 1 is that that the window exists in the keys of CharDataMap)... get the CharData Map -> get the list corresponding to the window -> generate a random double -> get the letter in the list corresponding the double based on the cp -> append the char to generatedText -> move the window ->
        while (CharDataMap.containsKey(window) & generatedText.length() < textLength + initLen) {
            List probs = CharDataMap.get(window);
            char randChar = getRandomChar(probs);
            generatedText += randChar;
            window = window.substring(1) + randChar;
        }

        return generatedText;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    // public static void main(String[] args) {
    //     // Training Test
    //     LanguageModel lang = new LanguageModel(5);
    //    lang.train("originofspecies.txt");


		// Testing random character
        // LanguageModel lang = new LanguageModel(10);
        // List myList = new List();

        // for (int i = 0; i < 18; i++) {
        //     myList.update('a');
        // }
        // for (int i = 0; i < 127; i++) {
        //     myList.update('b');
        // }
        // for (int i = 0; i < 34; i++) {
        //     myList.update('c');
        // }
        // // lang.calculateProbabilities(myList);
        // myList.toString();

        // int aCount = 0;
        // int bCount = 0;
        // int cCount = 0;
        
        // for (int i = 0; i < 500; i++) {
        //     char chr = lang.getRandomChar(myList);
        //     if (chr == 'a') {
        //         aCount++;
        //     } else if (chr == 'b') {
        //         bCount++;
        //     } else if (chr == 'c') {
        //         cCount++;
        //     }
        // }

        // System.out.println("A count was: " + aCount + ", b count was " + bCount + ", and c count was " + cCount);

    // }
    public static void main(String[] args) {
        int windowLength = Integer.parseInt(args[0]);
        String initialText = args[1];
        int generatedTextLength = Integer.parseInt(args[2]);
        Boolean randomGeneration = args[3].equals("random");
        String fileName = args[4];
        // Create the LanguageModel object
        LanguageModel lm;
        if (randomGeneration)
            lm = new LanguageModel(windowLength);
        else
            lm = new LanguageModel(windowLength, 20);
        // Trains the model, creating the map.
        lm.train(fileName);
        // Generates text, and prints it.
        System.out.println(lm.generate(initialText, generatedTextLength));
    }
}
