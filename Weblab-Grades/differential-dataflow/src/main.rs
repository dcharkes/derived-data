extern crate timely;
extern crate differential_dataflow;

use timely::dataflow::*;
use timely::dataflow::operators::*;
use differential_dataflow::AsCollection;
use differential_dataflow::operators::*;

// The computation we are emulating is the aggregation of the grades of students in a class. 
// Each student has a tree of "gradeable" items, where each item is graded by an aggregation 
// of its children items. If all child items are "passes", the grade is their average. If any
// items are "fails" then the grade is a "fail".

// The model from Daco Harkes is: 
//
//   entity Submission {
//     answer     : String?
//     grade      : Float?  = if(childPass) childGrade else no value (default)
//     pass       : Boolean = grade >= 5.5 && childPass <+ false
//     childGrade : Float?  = avg(children.grade)
//     childPass  : Boolean = conj(children.pass)
//   }
//
//   relation Submission.parent ? <-> * Submission.children

// We will use `usize` scores rather than `f32` or `f64` scores, because the latter two do not
// implement the `Eq` or `Ord` traits, because floating point stuff is weird.

fn main() {

    // define a new computational scope, in which to run the computation
    timely::execute_from_args(std::env::args().skip(5), move |computation| {
        
        // define grade computation dataflow; return handles to inputs and an output probe.
        let (mut submissions, mut relations, probe) = computation.scoped(|scope| {

            // `submissions` contains (submission_id, answer, grade) tuples.
            let (input0, submissions) = scope.new_input::<((usize, String, usize), i32)>();
            let submissions = submissions.as_collection();

            // `relations` contains (submission_id, parent_id) pairs.
            let (input1, relations) = scope.new_input::<((usize, usize), i32)>();
            let relations = relations.as_collection();

            // The tree evaluation is done by repeatedly propagating scores to parents,
            // who the accumulate the provided values according to the indicated logic.
            let results = submissions.map(|(id, _, score)| (id, if score > 5 { Some(score) } else { None }))
                                     .iterate(|scores| {

                // pull out the (parent, child) relationship
                let submissions = submissions.enter(&scores.scope());
                let relations = relations.enter(&scores.scope());

                // push grades to parents, for accumulation
                scores.join_map(&relations, |_, &score, &parent| (parent, score))
                      .concat(&submissions.map(|(child, _, score)| (child, Some(score))))
                      .group(|_parent, scores, output| {
                          let result = if scores.peek().unwrap().0.is_some() {
                              // accumulate total and counts
                              let mut total = 0;
                              let mut count = 0;
                              for (score, mult) in scores {
                                  total += score.unwrap() * (mult as usize);
                                  count += mult as usize;
                              }

                              // if the total averages above 5, it is a passing grade.
                              if total > count * 5 { Some(total / count) }
                              else { None }
                          }
                          else { None };
                          output.push((result, 1));
                      })

            });

            // observe results, and probe the output.
            let probe = results.consolidate()
                               .inspect(|x| println!("observed: {:?}", x))
                               .probe().0;

            (input0, input1, probe)
        });

        // introduce Alice's data
        relations.send(((1, 0), 1));
        relations.send(((2, 0), 1));
        relations.send(((3, 2), 1));
        relations.send(((4, 2), 1));
        submissions.send((((1, "Good".to_owned(), 8), 1)));
        submissions.send((((3, "Perfect".to_owned(), 10), 1)));
        submissions.send((((4, "Sufficient".to_owned(), 6), 1)));

        // introduce Bob's data
        relations.send(((11, 10), 1));
        relations.send(((12, 10), 1));
        relations.send(((13, 12), 1));
        relations.send(((14, 12), 1));
        submissions.send((((11, "Very Good".to_owned(), 9), 1)));
        submissions.send((((13, "Insufficient".to_owned(), 3), 1)));
        submissions.send((((14, "Sufficient".to_owned(), 10), 1)));

        // step until we have observed the answers
        relations.advance_to(1);
        submissions.advance_to(1);
        computation.step_while(|| probe.lt(&relations.time()));
        println!(">");

        // update the score for one of Alice's labs.
        submissions.send((((3, "Perfect".to_owned(), 10), -1)));
        submissions.send((((3, "Sufficient".to_owned(), 6), 1)));

        // step until we have observed the changes
        relations.advance_to(2);
        submissions.advance_to(2);
        computation.step_while(|| probe.lt(&relations.time()));
        println!(">");

        // update the score for one of Bob's labs from failing to passing.
        submissions.send((((13, "Insufficient".to_owned(), 3), -1)));
        submissions.send((((13, "Whatever".to_owned(), 6), 1)));

    }).unwrap();
}
