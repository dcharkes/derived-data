using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
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
            mathAlice.children2.OnNext(new List<Submission> { examAlice, labAlice });
            labAlice.children2.OnNext(new List<Submission> { lab1Alice, lab2Alice });
            examAlice.answer2.OnNext("Good");
            examAlice.manualGrade2.OnNext(8.0f);
            examAlice.answer2.OnNext("Perfect");
            lab1Alice.manualGrade2.OnNext(10.0f);
            examAlice.answer2.OnNext("Sufficient");
            lab2Alice.manualGrade2.OnNext(6.0f);
            var mathBob = new Submission();
            var examBob = new Submission();
            var labBob = new Submission();
            var lab1Bob = new Submission();
            var lab2Bob = new Submission();
            mathBob.children2.OnNext(new List<Submission> { examBob, labBob });
            labBob.children2.OnNext(new List<Submission> { lab1Bob, lab2Bob });
            examBob.answer2.OnNext("Very Good");
            examBob.manualGrade2.OnNext(9.0f);
            examBob.answer2.OnNext("Insufficient");
            lab1Bob.manualGrade2.OnNext(3.0f);
            examBob.answer2.OnNext("Perfect");
            lab2Bob.manualGrade2.OnNext(10.0f);

            Console.WriteLine("Alice");
            Console.WriteLine(mathAlice.grade2.Latest().First());
            Console.WriteLine(mathAlice.pass2.Latest().First());
            Console.WriteLine("Bob");
            Console.WriteLine(mathBob.grade2.Latest().First() + "Null");
            Console.WriteLine(mathBob.pass2.Latest().First());
            Console.WriteLine("");

            Console.WriteLine("Press any key to continue");
            Console.ReadKey();
        }
    }

    public class Submission
    {

        public BehaviorSubject<List<Submission>> children2; //note List is mutable, should replace list, not mutate it

        public BehaviorSubject<string> answer2;

        public BehaviorSubject<float?> manualGrade2;

        public IObservable<float?> childGrade2;

        public IObservable<bool> childPass2;

        public IObservable<float?> grade2;

        public IObservable<bool> pass2;

        public Submission()
        {
            children2 = new BehaviorSubject<List<Submission>>(new List<Submission>());

            answer2 = new BehaviorSubject<string>("");

            manualGrade2 = new BehaviorSubject<float?>(null);

            IObservable<List<IObservable<float?>>> childGrades1 = children2.Select(xs => xs.Select(x => x.grade2).ToList());
            IObservable<IObservable<List<float?>>> childGrades2 = childGrades1.Select(x => Sequence(x));
            IObservable<List<float?>> childGrades3 = childGrades2.Merge();
            IObservable<List<float>> childGrades4 = childGrades3.Select(x => x.Where(y => y.HasValue).Select(y => y.Value).ToList());
            childGrade2 = childGrades4.Select(x => x.Any() ? x.Average() : (float?)null);

            IObservable<List<bool>> childPasses = children2.Select(xs => Sequence<bool>(xs.Select(x => x.pass2).ToList())).Merge();
            childPass2 = childPasses.Select(x => FoldL((a, b) => a && b, true, x));

            grade2 = manualGrade2.CombineLatest(childGrade2, (mg2, cg2) => Tuple.Create(mg2, cg2)).CombineLatest(childPass2, (tuple, cp2) =>
             {
                 var mg2 = tuple.Item1;
                 var cg2 = tuple.Item2;
                 if (mg2.HasValue)
                     return mg2;
                 else
                 {
                     if (cp2)
                         return cg2;
                     else
                         return null;
                 }
             });

            pass2 = grade2.CombineLatest(childPass2, (g2, cp2) =>
            {
                var gradePass = g2.HasValue ? g2 >= 5.5f : false;
                return gradePass && cp2;
            });

        }

        public static IObservable<List<T>> Sequence<T>(List<IObservable<T>> elems)
        {
            return FoldL((acc, o) =>
            {
                return acc.CombineLatest(o, (acc1, o1) =>
                {
                    var z = new List<T>();
                    z.AddRange(acc1);
                    z.Add(o1);
                    return z;
                });
            }, Observable.Return(new List<T>()), elems);
        }

        public static A FoldL<A, B>(Func<A, B, A> f, A a, IEnumerable<B> b)
        {
            if (!b.Any())
                return a;
            else
            {
                var first = b.First();
                var aprime = f(a, first);
                var brest = b.Skip(1);
                return FoldL(f, aprime, brest);
            }
        }

        public static A FoldL2<A, B>(Func<A, B, A> f, A a, IEnumerable<B> b)
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