using System;
using System.Collections.Generic;
using System.Linq;

namespace ConsoleApplication
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
            Console.WriteLine(mathBob.grade()+"Null");
            Console.WriteLine(mathBob.pass());
        }
    }

    public class Submission
    {
        public List<Submission> children = new List<Submission>();

        public string answer = "";

        public float? manualGrade = null;

        public float? childGrade()
        {
            return children.Select(x => x.grade()).Flatten().AverageSafe();
        }

        public bool childPass()
        {
            return children.Select(x => x.pass()).Conjunction();
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
            return grade().HasValue ? grade() >= 5.5f : false;
        }

    }

    public static class MyExtensions
    {
        /**
         * Conjunction of a list of bools
         */
        public static bool Conjunction(this IEnumerable<bool> elems)
        {
            return elems.FoldL(true, (a, b) => a && b);
        }

        /**
         * Average of floats returing an optional float (null if imput list is empty)
         */
        public static float? AverageSafe(this IEnumerable<float> elems)
        {
            return elems.Any() ? elems.Average() : (float?)null;
        }

        /**
         * Flatten for list of optional floats
         */
        public static IEnumerable<float> Flatten(this IEnumerable<float?> elems)
        {
            return elems.Where(x => x.HasValue).Select(x => x.Value);
        }

        /**
         * FoldL extension method
         */
        public static A FoldL<A, B>(this IEnumerable<B> b, A a, Func<A, B, A> f)
        {
            return FoldL(f, a, b);
        }

        /**
         * Tail recursive FoldL (haskell style parameters)
         */
        public static A FoldL<A, B>(Func<A, B, A> f, A a, IEnumerable<B> b)
        {
            var acc = a;
            var bs = b;
            while (true)
            {
                if (!bs.Any())
                    return acc;
                else
                {
                    var first = bs.First();
                    acc = f(acc, first);
                    bs = bs.Skip(1);
                }
            }
        }
    }
}
