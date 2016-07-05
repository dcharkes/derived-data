import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

class Submission {

    private List<Submission> children = new ArrayList<Submission>();

    void addChild(Submission c) {
        children.add(c);
    }

    private String answer = "";

    void setAnswer(String a) {
        answer = a;
    }

    private Double manualGrade = null;

    void setManualGrade(Double m) {
        manualGrade = m;
    }

    Double getChildGrade() {
        OptionalDouble childGrade = children.stream().map(Submission::getGrade).filter(g -> g != null).mapToDouble(g -> g).average();
        return childGrade.isPresent() ? childGrade.getAsDouble() : null;
    }

    boolean getChildPass() {
        return children.stream().map(Submission::getPass).reduce(true, (Boolean a, Boolean b) -> a && b);
    }

    Double getGrade() {
        if (manualGrade != null)
            return manualGrade;
        if (getChildPass())
            return getChildGrade();
        return null;
    }

    boolean getPass() {
        return getGrade() != null && getGrade() >= 5.5;
    }

}

