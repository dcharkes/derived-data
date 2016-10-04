/*
 * Documentation on mutable graphs in Rust: https://github.com/nrc/r4cppp/blob/master/graphs/README.md
 *  - arena allocation or reference counting for lifetimes
 *    -> reference counting, as we want to be able to delete objects separately
 *      -> means to manually break cycles to not leak memory: is no issue we explicitly delete objects or we want to keep them
 *
 * More documentation on Rc and RefCell: http://manishearth.github.io/blog/2015/05/27/wrapper-types-in-rust-choosing-your-guarantees/
 */

/*
 * Adapton requires Rust nigthly
 */

// not using Adapton yet
#[macro_use] extern crate adapton ;

use std::fmt::Debug;
use std::hash::Hash;

use std::rc::Rc;

use adapton::engine::* ;
use adapton::macros::* ;

static mut counter : usize = 0;

fn mycell<X:Debug+Hash+Eq+PartialEq+Clone>(item : X) -> Art<X> {
    return cell(name_of_usize(unsafe{counter+=1;counter}), item);
}

fn main() {
    init_dcg();

//    let math_alice = Submission::new(None, None);
//    let exam_alice = Submission::new(Some("Good".to_string()), Some(8));
//    let lab_alice = Submission::new(None, None);
//    let lab1_alice = Submission::new(Some("Perfect".to_string()), Some(10));
//    let lab2_alice = Submission::new(Some("Sufficient".to_string()), Some(6));
//    {
//        set(&lab_alice.children, vec!(lab1_alice,lab2_alice));
//        set(&math_alice.children, vec!(exam_alice,lab_alice));
//    }
    let math_bob = Submission::new(None, None);
    let exam_bob = Submission::new(Some("Very Good".to_string()), Some(9));
    let lab_bob = Submission::new(None, None);
    let lab1_bob = Submission::new(Some("Insufficient".to_string()), Some(3));
    let lab2_bob = Submission::new(Some("Perfect".to_string()), Some(10));
    let lab1_bob_mangrade = lab1_bob.manual_grade.clone();
    {
        set(&lab_bob.children, vec!(lab1_bob,lab2_bob));
        set(&math_bob.children, vec!(exam_bob,lab_bob));
    }
//    println!("Alice");
//    println!("{:?}", math_alice.grade());
//    println!("{:?}", math_alice.pass());

    println!("");
    println!("Bob");
    println!("{:?}", math_bob.grade());
    println!("{:?}", math_bob.pass());

    set(&lab1_bob_mangrade, Some(800));
    println!("{:?}", force(&lab1_bob_mangrade));
    println!("{:?}", grade(force(&math_bob.children)[1].clone()));

    println!("");
    println!("Bob2");
    println!("{:?}", math_bob.grade());
    println!("{:?}", math_bob.pass());
}

#[allow(dead_code)]
#[derive(Debug,Hash,Eq,PartialEq,Clone)]
struct Submission {
    children: Art<Vec<Submission>>,
    answer: Option<String>,
    manual_grade: Art<Option<i32>>
}
impl Submission {
    fn new(answer: Option<String>, manual_grade: Option<i32>) -> Submission {
        Submission {
            children: mycell(Vec::new()),
            answer: answer,
            manual_grade: mycell(manual_grade),
        }
    }

    fn child_grade(&self) -> Option<i32> {
        let grades: Vec<i32> = force(&self.children).iter().flat_map(|x| -> Option<i32>{
            memo!(grade, s:x.clone())
        }).collect();
        avg(&grades)
    }

    fn child_pass(&self) -> bool {
        let passes: Vec<bool> = force(&self.children).iter().map(|x| -> bool{
            x.pass()
        }).collect();
        conj(&passes)
    }

    fn grade(&self) -> Option<i32> {
        match force(&self.manual_grade) {
            Some(x) => Some(x),
            None => {
                if self.child_pass() {
                    self.child_grade()
                } else {
                    None
                }
            }
        }
    }

    fn pass(&self) -> bool {
//        println!("{:?}", self);
        let g = memo!(grade, s:self.clone());
        match g {
            Option::None => false,
            Some(v) => v >= 5
        }
    }
}

fn grade(me:Submission) -> Option<i32> {
    me.grade()
}

fn sum(numbers: &Vec<i32>) -> i32 {
    numbers.iter().fold(0, |p, &q| p + q)
}
fn avg(numbers: &Vec<i32>) -> Option<i32> {
    if numbers.is_empty() {
        None
    } else {
        Some(sum(numbers) / numbers.len() as i32)
    }
}
fn conj(bools: &Vec<bool>) -> bool {
    bools.iter().fold(true, |p, &q| p && q)
}