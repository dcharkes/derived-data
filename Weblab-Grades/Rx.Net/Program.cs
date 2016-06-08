using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Linq;
using System.Reactive.Subjects;

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
            mathAlice.children.OnNext(new[] { examAlice, labAlice });
            labAlice.children.OnNext(new[] { lab1Alice, lab2Alice });
            examAlice.answer.OnNext("Good");
            examAlice.manualGrade.OnNext(8.0f);
            examAlice.answer.OnNext("Perfect");
            lab1Alice.manualGrade.OnNext(10.0f);
            examAlice.answer.OnNext("Sufficient");
            lab2Alice.manualGrade.OnNext(6.0f);
            var mathBob = new Submission();
            var examBob = new Submission();
            var labBob = new Submission();
            var lab1Bob = new Submission();
            var lab2Bob = new Submission();
            mathBob.children.OnNext(new[] { examBob, labBob });
            labBob.children.OnNext(new[] { lab1Bob, lab2Bob });
            examBob.answer.OnNext("Very Good");
            examBob.manualGrade.OnNext(9.0f);
            examBob.answer.OnNext("Insufficient");
            lab1Bob.manualGrade.OnNext(3.0f);
            examBob.answer.OnNext("Perfect");
            lab2Bob.manualGrade.OnNext(10.0f);

            Console.WriteLine("Alice");
            Console.WriteLine(mathAlice.grade.Latest().First());
            Console.WriteLine(mathAlice.pass.Latest().First());
            Console.WriteLine("Bob");
            Console.WriteLine(mathBob.grade.Latest().First() + "Null");
            Console.WriteLine(mathBob.pass.Latest().First());
            Console.WriteLine("");

            lab1Bob.answer.OnNext("Sufficient");
            lab1Bob.manualGrade.OnNext(6.0f);

            Console.WriteLine("Bob");
            Console.WriteLine(mathBob.grade.Latest().First());
            Console.WriteLine(mathBob.pass.Latest().First());
            Console.WriteLine("");

            Console.WriteLine("Press any key to continue");
            Console.ReadKey();
        }
    }

    public class Submission
    {

        public BehaviorSubject<IEnumerable<Submission>> children;

        public BehaviorSubject<string> answer;

        public BehaviorSubject<float?> manualGrade;

        public IObservable<float?> childGrade;

        public IObservable<bool> childPass;

        public IObservable<float?> grade;

        public IObservable<bool> pass;

        public Submission()
        {
            children = new BehaviorSubject<IEnumerable<Submission>>(Enumerable.Empty<Submission>());

            answer = new BehaviorSubject<string>("");

            manualGrade = new BehaviorSubject<float?>(null);

            IObservable<IEnumerable<float>> grades = children.Select(xs => xs.Select(x => x.grade).CombineLatestSafe()).Merge().Select(x => x.Flatten());
            childGrade = grades.Select(x => x.AverageSafe());

            IObservable<IEnumerable<bool>> passes = children.Select(xs => xs.Select(x => x.pass).CombineLatestSafe()).Merge();
            childPass = passes.Select(xs => xs.Conjunction());

            grade = manualGrade.CombineLatest(childGrade, childPass, (manualGrade1, childGrade1, childPass1) =>
            {
                if (manualGrade1.HasValue)
                    return manualGrade1;
                else
                {
                    if (childPass1)
                        return childGrade1;
                    else
                        return null;
                }
            });

            pass = grade.CombineLatest(childPass, (grade1, childPass1) =>
            {
                var gradePass = grade1.HasValue ? grade1 >= 5.5f : false;
                return gradePass && childPass1;
            });

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
         * Merges the specified observable sequences into one observable sequence by emitting
         *     a list with the latest source elements whenever any of the observable sequences
         *     produces an element.
         *     
         * (Original implementation blocks, what exactly happens here?)
         */
        public static IObservable<IEnumerable<T>> CombineLatestSafe<T>(this IEnumerable<IObservable<T>> elems)
        {
            //return elems.CombineLatest();
            //return new[] { Observable.Empty<T>() }.Concat(elems).CombineLatest();
            //return Observable.Return(Enumerable.Empty<T>()).CombineLatest(elems.CombineLatest(), (a, b) => a.Concat(b));
            return elems.FoldL(
                Observable.Return(Enumerable.Empty<T>()),
                (acc, o) => acc.CombineLatest(o, (acc1, o1) => acc1.Append(o1)));
        }

        /**
         * Append a single element
         */
        public static IEnumerable<T> Append<T>(this IEnumerable<T> elems, T elem)
        {
            return elems.Concat(new[] { elem });
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