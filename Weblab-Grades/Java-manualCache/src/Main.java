public class Main {
    public static void main(String[] args){
        Submission mathAlice = new Submission();
        Submission examAlice = new Submission();
        Submission labAlice = new Submission();
        Submission lab1Alice = new Submission();
        Submission lab2Alice = new Submission();
        mathAlice.addChild(examAlice);
        mathAlice.addChild(labAlice);
        labAlice.addChild(lab1Alice);
        labAlice.addChild(lab2Alice);
        examAlice.setAnswer("Good");
        examAlice.setManualGrade(8.0);
        lab1Alice.setAnswer("Perfect");
        lab1Alice.setManualGrade(10.0);
        lab2Alice.setAnswer("Sufficient");
        lab2Alice.setManualGrade(6.0);
        Submission mathBob = new Submission();
        Submission examBob = new Submission();
        Submission labBob = new Submission();
        Submission lab1Bob = new Submission();
        Submission lab2Bob = new Submission();
        mathBob.addChild(examBob);
        mathBob.addChild(labBob);
        examBob.addChild(lab1Bob);
        examBob.addChild(lab2Bob);
        examBob.setAnswer("Very Good");
        examBob.setManualGrade(9.0);
        lab1Bob.setAnswer("Insufficient");
        lab1Bob.setManualGrade(3.0);
        lab2Bob.setAnswer("Perfect");
        lab2Bob.setManualGrade(10.0);

        System.out.println("Alice");
        System.out.println(mathAlice.getGrade());
        System.out.println(mathAlice.getPass());
        System.out.println();
        System.out.println("Bob");
        System.out.println(mathBob.getGrade());
        System.out.println(mathBob.getPass());
    }
}
