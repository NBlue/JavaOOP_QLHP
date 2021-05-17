package app.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import modules.SentenceGenerator;

public class Output {

    private int id;
    private SentenceGenerator sentenceGenerator;
    private SimpleBooleanProperty selected = new SimpleBooleanProperty(true);
    private SimpleStringProperty sentence;

    public Output() {
        this.id = 0;
        this.sentence = new SimpleStringProperty("");
    }

    public Output(int id, String sentence) {
        this.id = id;
        this.sentence = new SimpleStringProperty(sentence);
    }

    public Output(int id, SentenceGenerator sentenceGenerator) {
        this.id = id;
        this.sentenceGenerator = sentenceGenerator;
        this.sentence = new SimpleStringProperty(sentenceGenerator.example());
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public SentenceGenerator getSentenceGenerator() {
        return sentenceGenerator;
    }

    public void setSentenceGenerator(SentenceGenerator sentenceGenerator) {
        this.sentenceGenerator = sentenceGenerator;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence.get();
    }

    public void setSentence(String sentence) {
        this.sentence.set(sentence);
    }
}
