
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class FA {
    Set<String> states;
    Set<String> alphabet;
    Map<Pair<String, String>, ArrayList<String>> transitions;
    String initialState;
    Set<String> finalStates;

    public FA(String filepath) throws IOException{
        var bufferedReader = new BufferedReader(new FileReader(filepath));

        String statesLine = bufferedReader.readLine().replace("\n", "");
        states = Set.of(statesLine.split(","));

        String alphabetLine = bufferedReader.readLine().replace("\n", "");
        alphabet = Set.of(alphabetLine.split(","));

        transitions = new HashMap<>();
        String transition = bufferedReader.readLine().replace("\n", "");
        while (!transition.equals("transition_end")) {
            String[] transitionElements = transition.split(",");
            String sourceState = transitionElements[0];
            String alphabetElement = transitionElements[1];
            String destinationState = transitionElements[2];

            Pair<String,String> keyPair = new Pair<>(sourceState, alphabetElement);
            if (transitions.containsKey(keyPair)) {
                transitions.get(keyPair).add(destinationState);
            } else {
                ArrayList<String> values = new ArrayList<>();
                values.add(destinationState);
                transitions.put(keyPair, values);
            }

            transition = bufferedReader.readLine().replace("\n", "");
        }

        initialState = bufferedReader.readLine().replace("\n", "");

        String finalStatesLine = bufferedReader.readLine().replace("\n", "");
        finalStates = Set.of(finalStatesLine.split(","));

        bufferedReader.close();
    }

    public String verifyTheSequence(String sequence){
        if (!isDeterministic())
            return "the sequence is nondeterministic";
        String sourceState = initialState;
        List<String> seqList = List.of(sequence.split(""));
        for(String letter: seqList){
            System.out.println(letter);

                Pair<String, String> key = new Pair<>(sourceState, letter);
                System.out.println(key);

//            Optional<Pair<String, String>> matchingKey = transitions.keySet().stream()
//                    .filter(pair -> pair.toString().equals(key.toString()))
//                    .findFirst();
            if (transitions.containsKey(key)) {

                sourceState = this.transitions.get(key).get(0);
            }
            else
                //if (transitions.keySet().stream().anyMatch(pair -> pair.toString().equals(key.toString()))) {
                    return "The sequence is not accepted by the FA " + sequence;
            }

        if ( finalStates.contains( sourceState)){
            return "The sequence " + sequence +" is accepted by the FA.";
        }
        return "The sequence is not accepted by the FA " + sequence;

    }
    public boolean isDeterministic(){
        for (Pair<String, String> key: this.transitions.keySet()) {
            if (this.transitions.get(key).size() > 1) {
                return false;
            }
        }
        return true;
    }

    public String statesToString() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Q = ");
        stringBuilder.append(states.toString().replace("[", "{").replace("]", "}"));
        return stringBuilder.toString();
    }

    public String alphabetToString() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("Alphabet = ");
        stringBuilder.append(alphabet.toString().replace("[", "{").replace("]", "}"));
        return stringBuilder.toString();
    }

    public String transitionsToString() {
        var stringBuilder = new StringBuilder();
        transitions.forEach((state_alphabet_pair, destinationState) -> {
            String sourceState = state_alphabet_pair.getFirst();
            String alphabetElement = state_alphabet_pair.getSecond();
            stringBuilder.append("delta(%s,%s) = %s\n".formatted(sourceState, alphabetElement, destinationState));
        });

        //delete the last new line
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());

        return stringBuilder.toString();
    }

    public String initialStateToString() {
        return "q0 = %s".formatted(initialState);
    }

    public String finalStatesToString() {
        var stringBuilder = new StringBuilder();
        stringBuilder.append("F = ");
        stringBuilder.append(finalStates.toString().replace("[", "{").replace("]", "}"));
        return stringBuilder.toString();
    }

    public boolean isSequenceAccepted(String sequence){
        if (!isDeterministic())
            return false;
        String sourceState = initialState;
        List<String> seqList = List.of(sequence.split(""));
        for(String letter: seqList){

            Pair<String, String> key = new Pair<>(sourceState, letter);

            if (transitions.containsKey(key)) {

                sourceState = this.transitions.get(key).get(0);
            }
            else
                return false;
        }

        return finalStates.contains( sourceState);

    }

    @Override
    public String toString() {
        return "FA{" +
                "states=" + states +
                ", alphabet=" + alphabet +
                ", transitions=" + transitions +
                ", initialState='" + initialState + '\'' +
                ", finalStates=" + finalStates +
                '}';
    }
}