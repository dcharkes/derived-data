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

use adapton::engine::* ;

static mut counter : usize = 0;

fn mycell<X:Debug+Hash+Eq+PartialEq+Clone>(item : X) -> Art<X> {
    return cell(name_of_usize(unsafe{counter+=1;counter}), item);
}

fn main() {
    init_dcg();

    let math_alice = Submission1::new(None, None);
    let exam_alice = Submission2::new(Some("Good".to_string()), Some(8));
    let lab_alice = Submission2::new(None, None);
    let lab1_alice = Submission3::new(Some("Perfect".to_string()), Some(10));
    let lab2_alice = Submission3::new(Some("Sufficient".to_string()), Some(6));
    {
        set(math_alice.children.clone(), vec!(exam_alice.clone(),lab_alice.clone()));
        set(lab_alice.children.clone(), vec!(lab1_alice.clone(),lab2_alice.clone()));
    }
    let math_bob = Submission1::new(None, None);
    let exam_bob = Submission2::new(Some("Very Good".to_string()), Some(9));
    let lab_bob = Submission2::new(None, None);
    let lab1_bob = Submission3::new(Some("Insufficient".to_string()), Some(3));
    let lab2_bob = Submission3::new(Some("Perfect".to_string()), Some(10));
    {

        set(math_bob.children.clone(), vec!(exam_bob.clone(),lab_bob.clone()));
        set(lab_bob.children.clone(), vec!(lab1_bob.clone(),lab2_bob.clone()));
    }
    println!("Alice");

    println!("{:?}", math_alice.grade());
    println!("{:?}", math_alice.pass());
    println!("");
    println!("Bob");

    println!("{:?}", math_bob.grade());
    println!("{:?}", math_bob.pass());
}

#[allow(dead_code)]
#[derive(Debug,Hash,Eq,PartialEq,Clone)]
struct Submission1 {
    children: Art<Vec<Submission2>>,
    answer: Option<String>,
    manual_grade: Option<i32>
}
impl Submission1 {
    fn new(answer: Option<String>, manual_grade: Option<i32>) -> Submission1 {
        Submission1 {
            children: mycell(Vec::new()),
            answer: answer,
            manual_grade: manual_grade,
        }
    }

    fn child_grade(&self) -> Option<i32> {
        let grades: Vec<i32> = force(&self.children).iter().flat_map(|x| -> Option<i32>{
            x.grade()
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
        match self.manual_grade {
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
        let g = self.grade();
        match g {
            Option::None => false,
            Some(v) => v >= 5
        }
    }
}

#[allow(dead_code)]
#[derive(Debug,Hash,Eq,PartialEq,Clone)]
struct Submission2 {
    children: Art<Vec<Submission3>>,
    answer: Option<String>,
    manual_grade: Option<i32>
}
impl Submission2 {
    fn new(answer: Option<String>, manual_grade: Option<i32>) -> Submission2 {
        Submission2 {
            children: mycell(Vec::new()),
            answer: answer,
            manual_grade: manual_grade,
        }
    }

    fn child_grade(&self) -> Option<i32> {
        let grades: Vec<i32> = force(&self.children).iter().flat_map(|x| -> Option<i32>{
            x.grade()
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
        match self.manual_grade {
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
        let g = self.grade();
        match g {
            Option::None => false,
            Some(v) => v >= 5
        }
    }
}

#[allow(dead_code)]
#[derive(Debug,Hash,Eq,PartialEq,Clone)]
struct Submission3 {
    answer: Option<String>,
    manual_grade: Option<i32>
}
impl Submission3 {
    fn new(answer: Option<String>, manual_grade: Option<i32>) -> Submission3 {
        Submission3 {
            answer: answer,
            manual_grade: manual_grade,
        }
    }

    fn grade(&self) -> Option<i32> {
        self.manual_grade
    }

    fn pass(&self) -> bool {
        let g = self.grade();
        match g {
            Option::None => false,
            Some(v) => v >= 5
        }
    }
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