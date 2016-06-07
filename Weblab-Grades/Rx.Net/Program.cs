using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Linq;

namespace WeblabGrades
{
    public class Program
    {
        public static void Main(string[] args)
        {
            var mathAlice = new Submission();
            var examAlice = new Submission();
            var labAlice = new Submission();
            var lab1Alice = new Submission();
            var lab2Alice = new Submission();
            mathAlice.children.Add(examAlice);
            mathAlice.children.Add(labAlice);
            labAlice.children.Add(lab1Alice);
            labAlice.children.Add(lab2Alice);
            examAlice.answer = "Good";
            examAlice.manualGrade = 8.0f;
            // examAlice.manualGrade2.
            lab1Alice.answer = "Perfect";
            lab1Alice.manualGrade = 10.0f;
            lab2Alice.answer = "Sufficient";
            lab2Alice.manualGrade = 6.0f;
            var mathBob = new Submission();
            var examBob = new Submission();
            var labBob = new Submission();
            var lab1Bob = new Submission();
            var lab2Bob = new Submission();
            mathBob.children.Add(examBob);
            mathBob.children.Add(labBob);
            labBob.children.Add(lab1Bob);
            labBob.children.Add(lab2Bob);
            examBob.answer = "Very Good";
            examBob.manualGrade = 9.0f;
            lab1Bob.answer = "Insufficient";
            lab1Bob.manualGrade = 3.0f;
            lab2Bob.answer = "Perfect";
            lab2Bob.manualGrade = 10.0f;

            Console.WriteLine("Alice");
            Console.WriteLine(mathAlice.grade());
            Console.WriteLine(mathAlice.pass());
            Console.WriteLine("Bob");
            Console.WriteLine(mathBob.grade() + "Null");
            Console.WriteLine(mathBob.pass());
            Console.WriteLine("");
            Console.WriteLine("Press any key to continue");
            Console.ReadKey();
        }
    }

    public class Submission
    {
        public List<Submission> children = new List<Submission>();

        public string answer = "";

        public float? manualGrade = null;

        public float? childGrade()
        {
            var grades = children.Select(x => x.grade());
            if (grades.Any())
                return grades.Average();
            else
                return null;
        }

        public bool childPass()
        {
            var passes = children.Select(x => x.pass());
            return passes.Aggregate(true, (acc, next) => acc && next);
        }

        public float? grade()
        {
            if (manualGrade.HasValue)
                return manualGrade;
            else
                if (childPass())
                return childGrade();
            else
                return null;
        }

        public bool pass()
        {
            var gradePass = grade().HasValue ? grade() >= 5.5f : false;
            return gradePass && childPass();
        }

        public IObservable<float?> manualGrade2;

        public IObservable<float?> childGrade2;

        public IObservable<bool> childPass2;

        public IObservable<float?> grade2;

        public IObservable<bool> pass2;

        public Submission()
        {
            pass2 = grade2.CombineLatest(childPass2, (g2, cp2) =>
            {
                var gradePass = g2.HasValue ? g2 >= 5.5f : false;
                return gradePass && cp2;
            });
        }

    }
}