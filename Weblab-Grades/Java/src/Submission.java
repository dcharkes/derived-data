import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class Submission {

    public List<Submission> children = new ArrayList<Submission>();

    public String answer = "";

    public Double manualGrade = null;

    public Double childGrade() {
        OptionalDouble childGrade = children.stream().map(Submission::grade).filter(g -> g != null).mapToDouble(g -> g).average();
        return childGrade.isPresent() ? childGrade.getAsDouble() : null;
    }

    public boolean childPass() {
        return children.stream().map(Submission::pass).reduce(true, (Boolean a, Boolean b) -> a && b);
    }

    public Double grade() {
        if (manualGrade != null)
            return manualGrade;
        if (childPass())
            return childGrade();
        return null;
    }

    public boolean pass() {
        if (grade() != null)
            return grade() >= 5.5;
        return false;
    }

}

