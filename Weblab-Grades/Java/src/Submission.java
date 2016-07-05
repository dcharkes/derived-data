import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Submission {

    private List<Submission> children = new ArrayList<Submission>();

    public void addChild(Submission c){
        children.add(c);
    }

    private String answer = "";

    public void setAnswer(String a){
        answer = a;
    }

    private Double manualGrade = null;

    public void setManualGrade(Double m){
        manualGrade = m;
    }

    public Double getChildGrade() {
        OptionalDouble childGrade = children.stream().map(Submission::getGrade).filter(g -> g != null).mapToDouble(g -> g).average();
        return childGrade.isPresent() ? childGrade.getAsDouble() : null;
    }

    public boolean getChildPass() {
        return children.stream().map(Submission::getPass).reduce(true, (Boolean a, Boolean b) -> a && b);
    }

    public Double getGrade() {
        if (manualGrade != null)
            return manualGrade;
        if (getChildPass())
            return getChildGrade();
        return null;
    }

    public boolean getPass() {
        if (getGrade() != null)
            return getGrade() >= 5.5;
        return false;
    }

}

