public class Main {
    public static void main(String[] args){
        Submission mathAlice = new Submission();
        Submission examAlice = new Submission();
        Submission labAlice = new Submission();
        Submission lab1Alice = new Submission();
        Submission lab2Alice = new Submission();
        mathAlice.children.add(examAlice);
        mathAlice.children.add(labAlice);
        labAlice.children.add(lab1Alice);
        labAlice.children.add(lab2Alice);
        examAlice.answer = "Good";
        examAlice.manualGrade = 8.0;
        lab1Alice.answer = "Perfect";
        lab1Alice.manualGrade = 10.0;
        lab2Alice.answer = "Sufficient";
        lab2Alice.manualGrade = 6.0;
        Submission mathBob = new Submission();
        Submission examBob = new Submission();
        Submission labBob = new Submission();
        Submission lab1Bob = new Submission();
        Submission lab2Bob = new Submission();
        mathBob.children.add(examBob);
        mathBob.children.add(labBob);
        examBob.children.add(lab1Bob);
        examBob.children.add(lab2Bob);
        examBob.answer = "Very Good";
        examBob.manualGrade = 9.0;
        lab1Bob.answer = "Insufficient";
        lab1Bob.manualGrade = 3.0;
        lab2Bob.answer = "Perfect";
        lab2Bob.manualGrade = 10.0;

        System.out.println("Alice");
        System.out.println(mathAlice.grade());
        System.out.println(mathAlice.pass());
        System.out.println();
        System.out.println("Bob");
        System.out.println(mathBob.grade());
        System.out.println(mathBob.pass());
    }
}
