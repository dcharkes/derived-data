import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

class Submission {

    private List<Submission> children = new ArrayList<Submission>();

    private Submission parent = null;

    void addChild(Submission c) {
        children.add(c);
        c.setParent(this);
    }

    private void setParent(Submission p) {
        parent = p;
        p.updateChildGrade();
        p.updateChildPass();
    }

    private String answer = "";

    void setAnswer(String a) {
        answer = a;
    }

    private Double manualGrade = null;

    void setManualGrade(Double m) {
        manualGrade = m;
        updateGrade();
    }

    private Double childGradeCache = null;

    Double getChildGrade() {
        return childGradeCache;
    }

    private void updateChildGrade() {
        OptionalDouble childGrade = children.stream().map(Submission::getGrade).filter(g -> g != null).mapToDouble(g -> g).average();
        childGradeCache = childGrade.isPresent() ? childGrade.getAsDouble() : null;
        updateGrade();
    }

    private boolean childPassCache = true;

    boolean getChildPass() {
        return childPassCache;
    }

    private void updateChildPass() {
        childPassCache = children.stream().map(Submission::getPass).reduce(true, (Boolean a, Boolean b) -> a && b);
        updateGrade();
    }

    private Double gradeCache = null;

    Double getGrade() {
        return gradeCache;
    }

    private void updateGrade() {
        if (manualGrade != null)
            gradeCache = manualGrade;
        else if (getChildPass())
            gradeCache = getChildGrade();
        else
            gradeCache = null;
        updatePass();
        if (parent != null)
            parent.updateChildGrade();
    }

    private boolean passCache = false;

    boolean getPass() {
        return passCache;
    }

    private void updatePass() {
        passCache = getGrade() != null && getGrade() >= 5.5;
        if (parent != null)
            parent.updateChildPass();
    }

}

