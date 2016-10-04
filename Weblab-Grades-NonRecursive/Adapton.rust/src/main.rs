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
    let math_alice = Submission1::new(None, None);
    let exam_alice = Submission2::new(Some("Good".to_string()), Some(8));
    let lab_alice = Submission2::new(None, None);
    let lab1_alice = Submission3::new(Some("Perfect".to_string()), Some(10));
    let lab2_alice = Submission3::new(Some("Sufficient".to_string()), Some(6));
    {
        let mut mut_math_alice = force(&math_alice);
        mut_math_alice.children.push(exam_alice.clone());
        mut_math_alice.children.push(lab_alice.clone());
        let mut mut_lab_alice = force(&lab_alice);
        mut_lab_alice.children.push(lab1_alice.clone());
        mut_lab_alice.children.push(lab2_alice.clone());
    }
    let math_bob = Submission1::new(None, None);
    let exam_bob = Submission2::new(Some("Very Good".to_string()), Some(9));
    let lab_bob = Submission2::new(None, None);
    let lab1_bob = Submission3::new(Some("Insufficient".to_string()), Some(3));
    let lab2_bob = Submission3::new(Some("Perfect".to_string()), Some(10));
    {
        let mut mut_math_bob = force(&math_bob);
        mut_math_bob.children.push(exam_bob.clone());
        mut_math_bob.children.push(lab_bob.clone());
        let mut mut_lab_bob = force(&lab_bob);
        mut_lab_bob.children.push(lab1_bob.clone());
        mut_lab_bob.children.push(lab2_bob.clone());
    }
    println!("Alice");
    let math_alice = force(&math_alice);
    println!("{:?}", math_alice.grade());
    println!("{:?}", math_alice.pass());
    println!("");
    println!("Bob");
    let math_bob = force(&math_bob);
    println!("{:?}", math_bob.grade());
    println!("{:?}", math_bob.pass());
}

#[allow(dead_code)]
#[derive(Debug,Hash,Eq,PartialEq,Clone)]
struct Submission1 {
    children: Vec<Art<Submission2>>,
    answer: Option<String>,
    manual_grade: Option<i32>
}
impl Submission1 {
    fn new(answer: Option<String>, manual_grade: Option<i32>) -> Art<Submission1> {
        mycell(Submission1 {
            children: Vec::new(),
            answer: answer,
            manual_grade: manual_grade,
        })
    }

    fn child_grade(&self) -> Option<i32> {
        let grades: Vec<i32> = self.children.iter().flat_map(|x| -> Option<i32>{
            force(x).grade()
        }).collect();
        avg(&grades)
    }

    fn child_pass(&self) -> bool {
        let passes: Vec<bool> = self.children.iter().map(|x| -> bool{
            force(x).pass()
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
    children: Vec<Art<Submission3>>,
    answer: Option<String>,
    manual_grade: Option<i32>
}
impl Submission2 {
    fn new(answer: Option<String>, manual_grade: Option<i32>) -> Art<Submission2> {
        mycell(Submission2 {
            children: Vec::new(),
            answer: answer,
            manual_grade: manual_grade,
        })
    }

    fn child_grade(&self) -> Option<i32> {
        let grades: Vec<i32> = self.children.iter().flat_map(|x| -> Option<i32>{
            force(x).grade()
        }).collect();
        avg(&grades)
    }

    fn child_pass(&self) -> bool {
        let passes: Vec<bool> = self.children.iter().map(|x| -> bool{
            force(x).pass()
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
    fn new(answer: Option<String>, manual_grade: Option<i32>) -> Art<Submission3> {
        mycell(Submission3 {
            answer: answer,
            manual_grade: manual_grade,
        })
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